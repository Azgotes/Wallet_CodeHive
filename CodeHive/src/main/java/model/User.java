package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class User {
    private static final String USERS_FILE = "./Files/users.xlsx";
    private static final SecureRandom RANDOM = new SecureRandom();
    private String username; // Champ pour stocker le nom d'utilisateur actuel

    private Double balanceCrypto;

    private Double balanceCash;

    private Double balanceStock;

    private Map<Crypto,Double> cryptoOwned = new HashMap<>();

    private Map<Action,Double> actionOwned = new HashMap<>();


    private ExcelWriter excelWriter = new ExcelWriter();

    private ExcelReader excelReader = new ExcelReader();




    public boolean authenticate(String username, String password) {
        try (InputStream is = new FileInputStream(USERS_FILE);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);
                Cell saltCell = row.getCell(2);

                Cell cryptoCell = row.getCell(5);
                Cell stockCell = row.getCell(6);
                Cell cashCell = row.getCell(7);


                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    String salt = saltCell.getStringCellValue();
                    String hashedPassword = hashPassword(password, Base64.getDecoder().decode(salt));




                    if (passwordCell != null && passwordCell.getStringCellValue().equals(hashedPassword)) {
                        this.username = username; // Mise à jour du nom d'utilisateur actuel
                        setBalanceCrypto(cryptoCell.getNumericCellValue());
                        setBalanceStock(stockCell.getNumericCellValue());
                        setBalanceCash(cashCell.getNumericCellValue());
                        return true;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername() {
        return this.username; // Retourner le nom d'utilisateur actuel
    }


    public boolean registerUser(String username, String password, String email) {
        // Vérifie si l'utilisateur existe déjà
        if (userExists(username)) {
            return false;
        }

        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        String hashedPassword = hashPassword(password, salt);

        Workbook workbook = null;
        FileInputStream is = null;
        FileOutputStream os = null;

        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Users");
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Username");
                headerRow.createCell(1).setCellValue("Password");
                headerRow.createCell(2).setCellValue("Salt");
                headerRow.createCell(3).setCellValue("Email");
                headerRow.createCell(4).setCellValue("Balance Total");
                headerRow.createCell(5).setCellValue("Balance Crypto");
                headerRow.createCell(6).setCellValue("Balance Stocks");
                headerRow.createCell(7).setCellValue("Balance Cash");
            } else {
                is = new FileInputStream(file);
                workbook = new XSSFWorkbook(is);
            }

            Sheet sheet = workbook.getSheet("Users");
            int rowCount = sheet.getLastRowNum();
            Row row = sheet.createRow(++rowCount);

            row.createCell(0).setCellValue(username);
            row.createCell(1).setCellValue(hashedPassword);
            row.createCell(2).setCellValue(Base64.getEncoder().encodeToString(salt));
            row.createCell(3).setCellValue(email);
            row.createCell(4).setCellValue(100000.0); // Balance Total
            row.createCell(5).setCellValue(0.0); // Balance Crypto
            row.createCell(6).setCellValue(0.0); // Balance Stocks
            row.createCell(7).setCellValue(100000.0); // Balance Cash

            os = new FileOutputStream(USERS_FILE);
            workbook.write(os);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initCryptoOwned(List<Crypto> cryptoList){
        this.cryptoOwned = new HashMap<>();
        cryptoList.forEach(crypto -> {
            try {
                var value = excelReader.readExcelNumericValue(this.username,crypto.getName(),"./Files/users.xlsx");
                this.cryptoOwned.put(crypto,value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initActionOwned(List<Action> actionList){
        this.actionOwned = new HashMap<>();
        actionList.forEach(action -> {
            try {
                var value = excelReader.readExcelNumericValue(this.username,action.getSymbol(),"./Files/users.xlsx");
                this.actionOwned.put(action,value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean userExists(String username) {
        try (InputStream is = new FileInputStream(USERS_FILE);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);

                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    // Votre logique existante
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    public double getBalanceCrypto() {
        return balanceCrypto;
    }

    public Double getBalanceCash() {
        return balanceCash;
    }

    public double getBalanceStock() {
        return balanceStock;

    }

    public void getCryptoValueOwned(String cryptoName, Double cryptoValue){
        Map<String,Double> newValues = new HashMap<>();
        newValues.put(cryptoName,cryptoValue);
        try {
            this.excelWriter.updateUserAssets(this.username,newValues,"./Files/users.xlsx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBalanceCrypto(double balanceCrypto) {
        this.balanceCrypto = balanceCrypto;

        Map<String,Double> newValues = new HashMap<>();
        newValues.put("balance_cash",this.balanceCrypto);
        try {
            this.excelWriter.updateUserAssets(this.username,newValues,"./Files/users.xlsx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBalanceCash(double balanceCash) {
        this.balanceCash = balanceCash;

        Map<String,Double> newValues = new HashMap<>();
        newValues.put("balance_cash",this.balanceCash);
        try {
            this.excelWriter.updateUserAssets(this.username,newValues,"./Files/users.xlsx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void setBalanceStock(double balanceStock) {
        this.balanceStock = balanceStock;

        Map<String,Double> newValues = new HashMap<>();
        newValues.put("balance_cash",this.balanceStock);
        try {
            this.excelWriter.updateUserAssets(this.username,newValues,"./Files/users.xlsx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getBalanceTot(){
        return this.balanceCash + this.balanceCrypto + this.balanceStock;
    }

    public Double getBalanceCashAsPercentOfTot(){
        return (this.balanceCash / this.getBalanceTot()) * 100;
    }

    public Double getBalanceStockAsPercentOfTot(){
        return (this.balanceStock / this.getBalanceTot()) * 100;
    }

    public Double getBalanceCryptoAsPercentOfTot(){
        return (this.balanceCrypto / this.getBalanceTot()) * 100;
    }

    public Map<Crypto, Double> getCryptoOwned() {
        return cryptoOwned;
    }

    public void setCryptoOwned(Map<Crypto, Double> cryptoOwned) {
        this.cryptoOwned = cryptoOwned;
    }

    public Map<Action, Double> getActionOwned() {
        return actionOwned;
    }

    public void setActionOwned(Map<Action, Double> actionOwned) {
        this.actionOwned = actionOwned;
    }
}
