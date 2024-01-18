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

    // Méthode pour lire les prix des actifs à partir d'un fichier Excel.
    public Map<String, Double> readPrices(String excelFilePath, String sheetName) throws IOException {
        Map<String, Double> prices = new HashMap<>();
        // Ouverture d'un flux de lecture du fichier Excel.
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        // Création d'un Workbook (classeur) à partir du fichier Excel.
        Workbook workbook = new XSSFWorkbook(inputStream);
        // Obtention de la feuille spécifiée par sheetName.
        Sheet sheet = workbook.getSheet(sheetName);
        // Vérification de l'existence de la feuille.
        if (sheet == null) {
            throw new IOException("Sheet with name '" + sheetName + "' does not exist in the workbook");
        }

        // Itération sur chaque ligne (Row) de la feuille.
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Ignorer la première ligne (en-tête).
            if (row.getRowNum() == 0) {
                continue;
            }

            // Lecture des cellules pour le nom et le prix.
            Cell nameCell = row.getCell(0);
            Cell priceCell = row.getCell(1);

            // Vérification de la validité des cellules et ajout dans la carte des prix.
            if (nameCell != null && priceCell != null && priceCell.getCellType() == CellType.NUMERIC) {
                prices.put(nameCell.getStringCellValue(), priceCell.getNumericCellValue());
            }
        }

        // Fermeture du Workbook et du flux d'entrée.
        workbook.close();
        inputStream.close();

        return prices;
    }

    // Méthode pour lire le prix d'un actif spécifique.
    public double readPrice(String assetName, AssetType assetType, String excelFilePath) throws IOException {
        // Choix de la feuille selon le type d'actif.
        String sheetName = assetType == AssetType.CRYPTO ? "Cryptocurrencies" : "Stocks";
        // Lecture des prix sur la feuille spécifiée.
        Map<String, Double> prices = readPrices(excelFilePath, sheetName);

        // Vérification de l'existence de l'actif et renvoi de son prix.
        if (prices.containsKey(assetName)) {
            return prices.get(assetName);
        } else {
            throw new IOException("Price for asset '" + assetName + "' not found in sheet '" + sheetName + "'");
        }
    }

    // Méthode pour lire les actifs d'un utilisateur à partir d'un fichier Excel.
    public Map<String, Double> readUserAssets(String username, String excelFilePath) throws IOException {
        Map<String, Double> userAssets = new HashMap<>();
        // Ouverture du fichier et création du Workbook.
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            // Accès à la feuille "Users".
            Sheet sheet = workbook.getSheet("Users");
            // Itération sur chaque ligne pour trouver l'utilisateur.
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                // Vérification du nom d'utilisateur.
                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    // Lecture des différentes balances de l'utilisateur.
                    double balanceTotal = row.getCell(4).getNumericCellValue();
                    double balanceCrypto = row.getCell(5).getNumericCellValue();
                    double balanceStocks = row.getCell(6).getNumericCellValue();
                    double balanceCash = row.getCell(7).getNumericCellValue();

                    // Ajout des balances à la carte des actifs.
                    userAssets.put("balance_total", balanceTotal);
                    userAssets.put("balance_crypto", balanceCrypto);
                    userAssets.put("balance_stocks", balanceStocks);
                    userAssets.put("balance_cash", balanceCash);

                    // Lecture des quantités d'actifs spécifiques.
                    for (int i = 8; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                            String assetKey = sheet.getRow(0).getCell(i).getStringCellValue();
                            double assetAmount = cell.getNumericCellValue();
                            userAssets.put(assetKey, assetAmount);
                        }
                    }
                    break; // Arrêt de la boucle une fois l'utilisateur trouvé.
                }
            }
        }
        return userAssets;
    }

    // Méthode pour lire une valeur numérique spécifique à partir d'un fichier Excel.
    public Double readExcelNumericValue(String username,String columHeaderValue,String excelFilePath) throws Exception {
        // Ouverture du fichier et création du Workbook.
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            // Accès à la feuille "Users".
            Sheet sheet = workbook.getSheet("Users");
            int index = -1;
            // Recherche de l'index de la colonne demandée.
            for(int i = 0; i<30;i++){
                if(sheet.getRow(0).getCell(i) == null){
                    continue;
                }
                if(sheet.getRow(0).getCell(i).getStringCellValue().equals(columHeaderValue)){
                    index = i;
                }
            }
            // Vérification de l'existence de la colonne.
            if (index==-1){
                throw(new Exception("Value not found"));
            }
            // Itération sur les lignes pour trouver l'utilisateur et la valeur demandée.
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
