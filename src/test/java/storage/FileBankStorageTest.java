package storage;

import model.Bank;
import model.PersonalLoan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBankStorageTest {

    // тимчасова папка
    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadSuccess() {
        // шлях до файлу
        String filePath = tempDir.resolve("test_banks.dat").toString();
        FileBankStorage storage = new FileBankStorage(filePath);

        // дані для тесту
        List<Bank> banks = new ArrayList<>();
        Bank bank = new Bank("B1", "TestBank");
        bank.addOffer(new PersonalLoan("PL1", 10, 100, 1000, 1, 12, true, true, "Test"));
        banks.add(bank);

        storage.saveBanks(banks);

        // перевірка чи файл створився
        assertTrue(Files.exists(Path.of(filePath)), "File should exist after save");

        List<Bank> loadedBanks = storage.loadBanks();

        // перевіряю дані
        assertNotNull(loadedBanks);
        assertEquals(1, loadedBanks.size());
        assertEquals("B1", loadedBanks.get(0).getId());
        assertEquals("TestBank", loadedBanks.get(0).getName());
        assertEquals(1, loadedBanks.get(0).getOffers().size());
    }

    @Test
    void testLoadWhenFileDoesNotExist() {
        // шлях до файлу, якого не існує
        String nonExistentPath = tempDir.resolve("ghost.dat").toString();
        FileBankStorage storage = new FileBankStorage(nonExistentPath);

        List<Bank> result = storage.loadBanks();

        // перевірка чи завантажився пустий список банків
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}