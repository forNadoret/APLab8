package storage;

import model.Bank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBankStorage implements BankStorage {
    private static final Logger logger = LoggerFactory.getLogger(FileBankStorage.class);

    private String filePath;

    public FileBankStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Bank> loadBanks() {
        File file = new File(filePath);
        if (!file.exists())
        {
            logger.warn("File {} not found. Returning empty list.", filePath);
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            List<Bank> data = (List<Bank>) ois.readObject();
            logger.debug("Successfully loaded data from '{}'", filePath);
            return data;

        } catch (IOException | ClassNotFoundException e) {
            logger.error("CRITICAL: Failed to load banks from file '{}'. Data might be corrupted.", filePath, e);
            System.err.println("Error loading banks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveBanks(List<Bank> banks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(banks);
            logger.info("Data successfully saved to '{}'.", filePath);
            System.out.println("Data saved.");
        } catch (IOException e) {
            logger.error("CRITICAL: Failed to save data to file '{}'.", filePath, e);
            System.err.println("Error saving banks: " + e.getMessage());
        }
    }
}