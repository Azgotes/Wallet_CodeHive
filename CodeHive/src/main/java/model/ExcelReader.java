package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelReader {

    public Map<String, Double> readPrices(String excelFilePath, String sheetName) throws IOException {
        Map<String, Double> prices = new HashMap<>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IOException("Sheet with name '" + sheetName + "' does not exist in the workbook");
        }

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) { // Skip header row
                continue;
            }

            Cell nameCell = row.getCell(0);
            Cell priceCell = row.getCell(1);

            if (nameCell != null && priceCell != null && priceCell.getCellType() == CellType.NUMERIC) {
                prices.put(nameCell.getStringCellValue(), priceCell.getNumericCellValue());
            }
        }

        workbook.close();
        inputStream.close();

        return prices;
    }

    public double readPrice(String assetName, AssetType assetType, String excelFilePath) throws IOException {
        String sheetName = assetType == AssetType.CRYPTO ? "Cryptocurrencies" : "Stocks";
        Map<String, Double> prices = readPrices(excelFilePath, sheetName);

        if (prices.containsKey(assetName)) {
            return prices.get(assetName);
        } else {
            throw new IOException("Price for asset '" + assetName + "' not found in sheet '" + sheetName + "'");
        }
    }

    public Map<String, Double> readUserAssets(String username, String excelFilePath) throws IOException {
        Map<String, Double> userAssets = new HashMap<>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("/Files/Users.xlsx");
        if (sheet == null) {
            throw new IOException("Sheet 'Users' does not exist in the workbook");
        }

        for (Row row : sheet) {
            Cell usernameCell = row.getCell(0);
            if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                // Les actifs commencent à la 5ème colonne (index 4)
                for (int columnIndex = 4; columnIndex < row.getLastCellNum(); columnIndex++) {
                    Cell assetCell = row.getCell(columnIndex);
                    if (assetCell != null && assetCell.getCellType() == CellType.NUMERIC) {
                        String assetName = sheet.getRow(0).getCell(columnIndex).getStringCellValue();
                        double assetValue = assetCell.getNumericCellValue();
                        userAssets.put(assetName, assetValue);
                    }
                }
                break;
            }
        }

        workbook.close();
        inputStream.close();

        return userAssets;
    }
}
