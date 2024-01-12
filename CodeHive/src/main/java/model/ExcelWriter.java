package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ExcelWriter {

    public void writeDataToExcel(Map<String, Double> cryptoPrices, Map<String, Double> stockPrices, String excelFilePath) throws IOException {
        // Création du classeur Excel
        Workbook workbook = new XSSFWorkbook();

        // Création de la feuille pour les cryptomonnaies
        Sheet cryptoSheet = workbook.createSheet("Cryptocurrencies");
        createHeaderRow(cryptoSheet, "Cryptocurrency", "Price (USD)");
        fillData(cryptoSheet, cryptoPrices);

        // Création de la feuille pour les actions
        Sheet stockSheet = workbook.createSheet("Stocks");
        createHeaderRow(stockSheet, "Stock Symbol", "Price (USD)");
        fillData(stockSheet, stockPrices);

        // Vérification et création du fichier et des répertoires si nécessaire
        File file = new File(excelFilePath);
        if (!file.exists()) {
            File parentDirectory = file.getParentFile();
            if (!parentDirectory.exists() && !parentDirectory.mkdirs()) {
                throw new IOException("Could not create directory " + parentDirectory);
            }
            if (!file.createNewFile()) {
                throw new IOException("Could not create file " + file);
            }
        }

        // Écriture des données dans le fichier Excel
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } finally {
            workbook.close(); // Assurez-vous de fermer le classeur pour libérer les ressources système
        }
    }

    private void createHeaderRow(Sheet sheet, String header1, String header2) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue(header1);
        headerRow.createCell(1).setCellValue(header2);
    }

    private void fillData(Sheet sheet, Map<String, Double> data) {
        int rowNum = 1;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }
    }
}
