package gui.util;

import javafx.scene.control.Alert;
import model.entities.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

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

            if (fornecedor == null){
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

        for (ItemPedido i : list) {

            cliente = clienteMap.get(i.getPedido().getCliente().getId());
            Row row = sheetRelatorio.createRow(rowNumb++);
            int cellNumb = 0;
            int count = i.getQuantidade();

            if (cliente == null) {
                row = sheetRelatorio.createRow(rowNumb++);
                Cell cellPedidoCliente = row.createCell(cellNumb++);
                cellPedidoCliente.setCellValue(i.getPedido().getCliente().getNome());
                clienteMap.put(i.getPedido().getCliente().getId(), i.getPedido().getCliente());
                row = sheetRelatorio.createRow(rowNumb++);
            }

            cellNumb = 0;
            Cell cellItemQuantidade = row.createCell(cellNumb);
            cellItemQuantidade.setCellValue(i.getQuantidade() + " " + i.getProduto().getCategoria().getAbreviacao() + " " + i.getProduto().getNome());

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
}
