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
}
