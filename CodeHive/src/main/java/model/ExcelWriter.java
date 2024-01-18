package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.io.FileInputStream;

public class ExcelWriter {

    // Méthode pour écrire les prix des cryptomonnaies et des actions dans un fichier Excel.
    public void writeDataToExcel(Map<String, Double> cryptoPrices, Map<String, Double> stockPrices, String excelFilePath) throws IOException {
        // Création d'un nouveau classeur Excel.
        Workbook workbook = new XSSFWorkbook();

        // Création et remplissage de la feuille pour les cryptomonnaies.
        Sheet cryptoSheet = workbook.createSheet("Cryptocurrencies");
        createHeaderRow(cryptoSheet, "Cryptocurrency", "Price (USD)");
        fillData(cryptoSheet, cryptoPrices);

        // Création et remplissage de la feuille pour les actions.
        Sheet stockSheet = workbook.createSheet("Stocks");
        createHeaderRow(stockSheet, "Stock Symbol", "Price (USD)");
        fillData(stockSheet, stockPrices);

        // Vérification de l'existence du fichier et des répertoires, création si nécessaire.
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

        // Écriture du Workbook dans le fichier Excel.
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } finally {
            workbook.close(); // Fermeture du Workbook pour libérer les ressources.
        }
    }

    // Méthode pour créer une ligne d'en-tête dans une feuille.
    private void createHeaderRow(Sheet sheet, String header1, String header2) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue(header1);
        headerRow.createCell(1).setCellValue(header2);
    }

    // Méthode pour remplir les données dans une feuille.
    private void fillData(Sheet sheet, Map<String, Double> data) {
        int rowNum = 1;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }
    }

    // Méthode pour mettre à jour les actifs d'un utilisateur dans un fichier Excel.
    public void updateUserAssets(String username, Map<String, Double> assets, String excelFilePath) throws IOException {
        // Ouverture du fichier Excel et recherche de la ligne de l'utilisateur.
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int userRowNum = findUserRow(sheet, username);

            if (userRowNum == -1) {
                throw new IOException("User '" + username + "' not found in the workbook");
            }

            Row userRow = sheet.getRow(userRowNum);
            // Mise à jour ou création de cellules pour chaque actif.
            for (Map.Entry<String, Double> entry : assets.entrySet()) {
                String assetKey = entry.getKey();
                double assetValue = entry.getValue();

                int column = findColumnIndex(sheet, assetKey);
                if (column == -1) {
                    column = userRow.getLastCellNum() >= 0 ? userRow.getLastCellNum() : 0;
                    sheet.getRow(0).createCell(column).setCellValue(assetKey);
                }
                Cell cell = userRow.getCell(column);
                if (cell == null || cell.getCellType() != CellType.NUMERIC) {
                    cell = userRow.createCell(column, CellType.NUMERIC);
                }
                cell.setCellValue(assetValue);
            }

            // Écriture des modifications dans le fichier Excel.
            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }
        } // Le bloc try-with-resources fermera automatiquement les ressources.
    }

    // Méthode pour trouver le numéro de ligne d'un utilisateur dans la feuille.
    private int findUserRow(Sheet sheet, String username) {
        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell != null && cell.getCellType() == CellType.STRING && username.equals(cell.getStringCellValue())) {
                return row.getRowNum();
            }
        }
        return -1;
    }

    // Méthode pour trouver l'indice de colonne d'un en-tête spécifique.
    private int findColumnIndex(Sheet sheet, String header) {
        Row headerRow = sheet.getRow(0);
        for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
            Cell cell = headerRow.getCell(columnIndex);
            if (cell != null && cell.getCellType() == CellType.STRING && header.equalsIgnoreCase(cell.getStringCellValue())) {
                return columnIndex;
            }
        }
        return -1;
    }

}
