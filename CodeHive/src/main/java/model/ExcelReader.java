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
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet("Users");
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    // Lecture et affichage des balances pour le débogage
                    double balanceTotal = row.getCell(4).getNumericCellValue();
                    double balanceCrypto = row.getCell(5).getNumericCellValue();
                    double balanceStocks = row.getCell(6).getNumericCellValue();
                    double balanceCash = row.getCell(7).getNumericCellValue();

                    System.out.println("Balance total read: " + balanceTotal);
                    System.out.println("Balance crypto read: " + balanceCrypto);
                    System.out.println("Balance stocks read: " + balanceStocks);
                    System.out.println("Balance cash read: " + balanceCash);

                    userAssets.put("balance_total", balanceTotal);
                    userAssets.put("balance_crypto", balanceCrypto);
                    userAssets.put("balance_stocks", balanceStocks);
                    userAssets.put("balance_cash", balanceCash);

                    // Lecture des quantités d'actifs spécifiques pour le débogage
                    for (int i = 8; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                            String assetKey = sheet.getRow(0).getCell(i).getStringCellValue();
                            double assetAmount = cell.getNumericCellValue();
                            System.out.println("Asset read: " + assetKey + " Amount: " + assetAmount);
                            userAssets.put(assetKey, assetAmount);
                        }
                    }
                    break;
                }
            }
        }
        return userAssets;
    }

    public Double readExcelNumericValue(String username,String columHeaderValue,String excelFilePath) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet("Users");
            int index = -1;
            for(int i = 0; i<30;i++){
                if(sheet.getRow(0).getCell(i) == null){
                    continue;
                }
                if(sheet.getRow(0).getCell(i).getStringCellValue().equals(columHeaderValue)){
                    index = i;
                }
            }
            if (index==-1){
                throw(new Exception("Value not found"));
            }
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    if(row.getCell(index)==null){
                        return 0d;
                    }
                    double value = row.getCell(index).getNumericCellValue();
                    return value;
                }
            }

        }
        throw(new Exception("Value not found"));
    }



}
