package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAndSelectionTest {

    @Test
    void testBankOperations() {
        Bank bank = new Bank("B1", "Privat");

        // перевірка геттерів
        assertEquals("B1", bank.getId());
        assertEquals("Privat", bank.getName());
        assertNotNull(bank.getOffers());
        assertTrue(bank.getOffers().isEmpty());

        PersonalLoan loan = new PersonalLoan("OFF1", 10, 100, 1000, 1, 12, true, true, "Test");

        // перевірка додавання оферу
        assertTrue(bank.addOffer(loan));
        assertEquals(1, bank.getOffers().size());

        // перевірка кількості оферів через toString
        assertTrue(bank.toString().contains("offersCount=1"));

        // перевірка на видалення неіснуючого оферу
        assertFalse(bank.removeOffer("FAKE_ID"));
        assertEquals(1, bank.getOffers().size());

        // перевірка на видалення існуючого оферу
        assertTrue(bank.removeOffer("OFF1"));
        assertTrue(bank.getOffers().isEmpty());
    }

    @Test
    void testSelectionOperations() {
        Selection selection = new Selection();
        assertNotNull(selection.getSelectedOffers());

        PersonalLoan loan = new PersonalLoan("SEL1", 10, 100, 1000, 1, 12, true, true, "Test");

        // перевірка на додавання до кошику
        selection.addOffer(loan);
        assertEquals(1, selection.getSelectedOffers().size());

        // перевірка на видалення неіснуючого оферу з кошика
        assertFalse(selection.removeOffer("WRONG_ID"));

        // 3. перевірка на видалення існуючного оферу з кошика
        assertTrue(selection.removeOffer("SEL1"));
        assertTrue(selection.getSelectedOffers().isEmpty());
    }
}