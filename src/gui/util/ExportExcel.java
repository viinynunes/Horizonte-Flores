package gui.util;

import javafx.scene.control.Alert;
import model.entities.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExportExcel {
    private static String fileName = null;

    public static void createExcelRelatorio(List<Relatorio> list, String name) {

        fileName = "D:/Documentos/" + name + ".xls";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetRelatorio = workbook.createSheet("Relatorio");

        int rowNumb = 0;

        Map<String, String> fornecedorMap = new HashMap<>();


        for (Relatorio r : list) {

            String fornecedor = r.getFornecedor();

            Row row = sheetRelatorio.createRow(rowNumb++);

            int cellNumb = 0;

            fornecedor = fornecedorMap.get(r.getFornecedor());

            if (fornecedor == null) {
                Cell cellProdutoFornecedor = row.createCell(cellNumb++);
                cellProdutoFornecedor.setCellValue(r.getFornecedor());
                row = sheetRelatorio.createRow(rowNumb++);
                fornecedorMap.put(r.getFornecedor(), fornecedor);
            }

            cellNumb = 0;
            Cell cellProdutoNome = row.createCell(cellNumb++);
            cellProdutoNome.setCellValue(r.getNome());
            Cell cellProdutoQuantidade = row.createCell(cellNumb++);
            cellProdutoQuantidade.setCellValue(r.getQuantidade());

        }

        try {
            FileOutputStream out = new FileOutputStream(new File(ExportExcel.fileName));
            workbook.write(out);
            out.close();
            Alerts.showAlert("Relatório Exportado", null, "Relatório exportado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExcelPedido(Set<ItemPedido> list, String name) {

        fileName = "D:/Documentos/Pedido " + name + ".xls";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetRelatorio = workbook.createSheet("Relatorio");


        Map<Integer, Cliente> clienteMap = new HashMap<>();
        Cliente cliente;

        int rowNumb = 0;
        int cellAux = 0;
        int iterator = 0;
        int maxRow = 5;
        int cellNumb;

        for (ItemPedido i : list) {
/*
            if (iterator == maxRow){
                System.out.println("mais que 5");
                rowNumb = 0;
                cellAux += 2;
            }
 */

            cliente = clienteMap.get(i.getPedido().getCliente().getId());
            Row row = sheetRelatorio.createRow(rowNumb++);

            cellNumb = cellAux;

            if (cliente == null) {
                Cell space = row.createCell(rowNumb++);
                space.setCellValue("ESPACO VAZIO");
                row = sheetRelatorio.createRow(rowNumb++);
                Cell cellPedidoCliente = row.createCell(cellNumb);
                cellPedidoCliente.setCellValue(i.getPedido().getCliente().getNome());
                clienteMap.put(i.getPedido().getCliente().getId(), i.getPedido().getCliente());
                row = sheetRelatorio.createRow(rowNumb++);
            }

            cellNumb = cellAux;
            Cell cellItemQuantidade = row.createCell(cellNumb);
            cellItemQuantidade.setCellValue(i.getQuantidade() + " " + i.getProduto().getCategoria().getAbreviacao() + " " + i.getProduto().getNome());
            iterator++;
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(ExportExcel.fileName));
            workbook.write(out);
            out.close();
            Alerts.showAlert("Relatório Exportado", null, "Relatório exportado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExcelPedido2(Set<ItemPedido> list, String name) {

        fileName = "D:/Documentos/Pedido " + name + ".xls";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetRelatorio = workbook.createSheet("Relatorio");

        HSSFCellStyle styleBorderThin = createBorderThin(workbook);
        HSSFCellStyle styleCellCenterBorderThin = createAlignCenterBorderThin(workbook);



        Map<Integer, Cliente> clienteMap = new HashMap<>();
        Cliente cliente;
        Row row = null;
        
        int rowNumb = 0;
        int cellAux = 0;
        int maxRow = 60;
        int cellNumb;
        int aux = 0;
        
        // cria todas as linhas da pagina com um numero maximo de linhas
        while (aux < maxRow){
            row = sheetRelatorio.createRow(rowNumb++);
            aux++;
        }

        rowNumb = 0;

        //percorre toda a lista de itens do pedido
        for (ItemPedido i : list) {

            //checa se o numero de linhas foi atingido, caso sim, volta para a primeira linha e aumenta a posicao da celula
            if (rowNumb == maxRow){
                System.out.println("mais que o maximo de linhas");
                rowNumb = 0;
                row = sheetRelatorio.getRow(0);
                rowNumb = 0;
                cellAux += 2;
            }

            row = sheetRelatorio.getRow(rowNumb);

            cliente = clienteMap.get(i.getPedido().getCliente().getId());

            cellNumb = cellAux;

            if (cliente == null) {
                Cell space = row.createCell(cellNumb);
                space.setCellValue("         ");
                rowNumb++;
                row = sheetRelatorio.getRow(rowNumb);
                Cell cellPedidoCliente = row.createCell(cellNumb);
                cellPedidoCliente.setCellStyle(styleCellCenterBorderThin);
                cellPedidoCliente.setCellValue(i.getPedido().getCliente().getNome());
                clienteMap.put(i.getPedido().getCliente().getId(), i.getPedido().getCliente());
                rowNumb++;
                row = sheetRelatorio.getRow(rowNumb);
            }

            cellNumb = cellAux;
            Cell cellItemQuantidade = row.createCell(cellNumb);
            cellItemQuantidade.setCellStyle(styleBorderThin);
            cellItemQuantidade.setCellValue(i.getQuantidade() + " " + i.getProduto().getCategoria().getAbreviacao() + " " + i.getProduto().getNome());
            rowNumb++;
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(ExportExcel.fileName));
            workbook.write(out);
            out.close();
            Alerts.showAlert("Relatório Exportado", null, "Relatório exportado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            list.clear();
        }
    }

    public static void createExcelByEstabelecimento(List<Sobra> list, String name) {

        fileName = "D:/Documentos/Resumo " + name + ".xls";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetRelatorio = workbook.createSheet("Relatorio");

        HSSFCellStyle styleBorderThin = createBorderThin(workbook);
        HSSFCellStyle styleCellCenterBorderThin = createAlignCenterBorderThin(workbook);

        Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
        Fornecedor fornecedor;

        int rowNumb = 0;

        for (Sobra sobra : list) {

            fornecedor = fornecedorMap.get(sobra.getProduto().getFornecedor().getId());
            Row row = sheetRelatorio.createRow(rowNumb++);
            int cellNumb = 0;

            if (fornecedor == null) {
                row = sheetRelatorio.createRow(rowNumb++);
                Cell cellFornecedorNome = row.createCell(cellNumb);
                cellFornecedorNome.setCellStyle(styleCellCenterBorderThin);
                cellFornecedorNome.setCellValue(sobra.getProduto().getFornecedor().getNome());
                row = sheetRelatorio.createRow(rowNumb++);
                fornecedorMap.put(sobra.getProduto().getFornecedor().getId(), sobra.getProduto().getFornecedor());
            }

            cellNumb = 0;
            Cell cellProdutoNome = row.createCell(cellNumb++);
            cellProdutoNome.setCellValue(sobra.getProduto().getNome());
            Cell cellLocalFornecedor = row.createCell(cellNumb++);
            cellLocalFornecedor.setCellValue(sobra.getProduto().getFornecedor().getNome());
            Cell cellTotalPedido = row.createCell(cellNumb++);
            cellTotalPedido.setCellValue(sobra.getProduto().getCategoria().getAbreviacao() + "   " + sobra.getTotalPedido());
            Cell cellTotalPedidoAtualizado = row.createCell(cellNumb++);
            cellTotalPedidoAtualizado.setCellValue(sobra.getProduto().getCategoria().getAbreviacao() + "   " + sobra.getTotalPedidoAtualizado());
            Cell cellTotalSobra = row.createCell(cellNumb++);
            cellTotalSobra.setCellValue(sobra.getSobra());

        }

        try {
            FileOutputStream out = new FileOutputStream(new File(ExportExcel.fileName));
            workbook.write(out);
            out.close();
            Alerts.showAlert("Relatório Exportado", null, "Relatório exportado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HSSFCellStyle createBorderThin(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private static HSSFCellStyle createAlignCenterBorderThin(HSSFWorkbook workbook){
        HSSFCellStyle style = workbook.createCellStyle();

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

}
