package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class User {
    private static final String USERS_FILE = "./Files/users.xlsx";
    private static final SecureRandom RANDOM = new SecureRandom();
    private String username; // Champ pour stocker le nom d'utilisateur actuel

    public boolean authenticate(String username, String password) {
        try (InputStream is = new FileInputStream(USERS_FILE);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);
                Cell saltCell = row.getCell(2);

                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    String salt = saltCell.getStringCellValue();
                    String hashedPassword = hashPassword(password, Base64.getDecoder().decode(salt));

                    if (passwordCell != null && passwordCell.getStringCellValue().equals(hashedPassword)) {
                        this.username = username; // Mise à jour du nom d'utilisateur actuel
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

}
