package gui.util;

import javafx.scene.control.Alert;
import model.entities.Fornecedor;
import model.entities.Relatorio;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportExcel {
    private static String fileName = null;

    public static void createExcel(List<Relatorio> list, String name){

        fileName = "D:/Documentos/" + name+".xls";

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetRelatorio = workbook.createSheet("Relatorio");

        int rowNumb = 0;

        for (Relatorio r : list){
            Row row = sheetRelatorio.createRow(rowNumb++);
            int cellNumb = 0;
            Cell cellProdutoNome = row.createCell(cellNumb++);
            cellProdutoNome.setCellValue(r.getNome());
            Cell cellProdutoQuantidade = row.createCell(cellNumb++);
            cellProdutoQuantidade.setCellValue(r.getQuantidade());
            Cell cellProdutoFornecedor = row.createCell(cellNumb++);
            cellProdutoFornecedor.setCellValue(r.getFornecedor());
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(ExportExcel.fileName));
            workbook.write(out);
            out.close();
            Alerts.showAlert("Relatório Exportado", null, "Relatório exportado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
