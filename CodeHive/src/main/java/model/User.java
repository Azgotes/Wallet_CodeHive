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

    public boolean authenticate(String username, String password) {
        try (InputStream is = new FileInputStream(USERS_FILE);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);
                Cell saltCell = row.getCell(2);
                if (usernameCell.getStringCellValue().equals(username)) {
                    String salt = saltCell.getStringCellValue();
                    String hashedPassword = hashPassword(password, Base64.getDecoder().decode(salt));
                    if (passwordCell.getStringCellValue().equals(hashedPassword)) {
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

    public boolean registerUser(String username, String password) {
        if (userExists(username)) {
            return false;
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        String hashedPassword = hashPassword(password, salt);

        try (InputStream is = new FileInputStream(USERS_FILE);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            Row row = sheet.createRow(++rowCount);

            Cell cellUsername = row.createCell(0);
            cellUsername.setCellValue(username);

            Cell cellPassword = row.createCell(1);
            cellPassword.setCellValue(hashedPassword);

            Cell cellSalt = row.createCell(2);
            cellSalt.setCellValue(Base64.getEncoder().encodeToString(salt));

            FileOutputStream os = new FileOutputStream(USERS_FILE);
            workbook.write(os);
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
                if (row.getCell(0).getStringCellValue().equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
