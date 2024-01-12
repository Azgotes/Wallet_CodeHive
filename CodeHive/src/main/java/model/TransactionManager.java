package model;

import java.io.IOException;

public class TransactionManager {

    private ExcelReader excelReader = new ExcelReader();

    public double getPriceFromExcel(String assetName, AssetType assetType) {
        try {
            return excelReader.readPrice(assetName, assetType, "./Files/ActionCrypto.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception properly
            return -1;
        }
    }
}
