package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import storage.BankStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CreditServiceTest {

    @Test
    void testLoadDataCreatesDefaults() {
        // якщо файл пустий, то створюється два банки
        List<Bank> banks = service.listAllBanks();
        assertEquals(2, banks.size());
        verify(storage, atLeastOnce()).saveBanks(anyList());
    }

    @Test
    void testCreateOfferSuccess() {
        // створення оферу
        PersonalLoan offer = new PersonalLoan("NEW", 10, 100, 1000, 1, 12, true, true, "Test");
        service.createOffer("B1", offer);

        assertNotNull(service.findOfferById("NEW"));
    }

    @Test
    void testCreateOfferBankNotFound() {
        PersonalLoan offer = new PersonalLoan("NEW", 10, 100, 1000, 1, 12, true, true, "Test");
        service.createOffer("WRONG_ID", offer);
        assertNull(service.findOfferById("NEW"));
    }

    @Test
    void testSearchOffers() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setMaxRate(13.0);

        List<CreditOffer> results = service.searchOffers(criteria);
        assertTrue(results.size() >= 2);
    }

    @Test
    void testSearchByBankName() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setBankName("MonoBank");
        List<CreditOffer> results = service.searchOffers(criteria);
        assertEquals(2, results.size());
    }

    @Test
    void testUpdateOffer() {
        OfferUpdate update = new OfferUpdate();
        update.setInterestRate(50.0);

        boolean result = service.updateOffer("PL1", update); // PL1 існує в тестових даних
        assertTrue(result);
        assertEquals(50.0, service.findOfferById("PL1").getInterestRate());
    }

    @Test
    void testUpdateOfferNotFound() {
        assertFalse(service.updateOffer("FAKE", new OfferUpdate()));
    }

    @Test
    void testDeleteOffer() {
        assertTrue(service.deleteOffer("PL1"));
        assertNull(service.findOfferById("PL1"));
    }

    @Test
    void testDeleteOfferNotFound() {
        assertFalse(service.deleteOffer("FAKE"));
    }

    @Test
    void testBasket() {
        service.addToSelection("PL1");
        assertEquals(1, service.viewSelection().size());

        service.addToSelection("FAKE"); // Не знайде
        assertEquals(1, service.viewSelection().size());
    }

    @Mock
    private BankStorage storage;

    private CreditService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // повертаю пустий список, щоб спрацювала логіка створення 5 тестових оферів
        when(storage.loadBanks()).thenReturn(new ArrayList<>());
        service = new CreditService(storage);
    }
}