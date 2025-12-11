package service;

import model.CreditOffer;
import model.OfferUpdate;
import model.PersonalLoan;
import model.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CommandsTest {

    @Mock
    private CreditService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOfferCommand() {
        Command cmd = new CreateOfferCommand(service);
        cmd.execute("B1 NEW_OFFER");

        ArgumentCaptor<CreditOffer> captor = ArgumentCaptor.forClass(CreditOffer.class);
        verify(service).createOffer(eq("B1"), captor.capture());

        assertEquals("NEW_OFFER", captor.getValue().getId());
        assertTrue(captor.getValue() instanceof PersonalLoan);

        // перевірка на помилку, що замало аргументів
        cmd.execute("B1");
        verify(service, times(1)).createOffer(any(), any());
    }

    @Test
    void testDeleteOfferCommand() {
        Command cmd = new DeleteOfferCommand(service);
        when(service.deleteOffer("OFF1")).thenReturn(true);

        cmd.execute("OFF1");
        verify(service).deleteOffer("OFF1");

        // перевірка на пусте введення
        cmd.execute("");
        verify(service, times(1)).deleteOffer(any());
    }

    @Test
    void testDeleteOfferCommandElse() {
        Command cmd = new DeleteOfferCommand(service);
        when(service.deleteOffer("OFF1")).thenReturn(false);

        cmd.execute("OFF1");
        verify(service).deleteOffer("OFF1");

        // перевірка на пусте введення
        cmd.execute("");
        verify(service, times(1)).deleteOffer(any());
    }

    @Test
    void testUpdateOfferCommand() {
        Command cmd = new UpdateOfferCommand(service);
        when(service.updateOffer(anyString(), any())).thenReturn(true);

        cmd.execute("OFF1 rate=12.5 minAmount=1000");

        ArgumentCaptor<OfferUpdate> captor = ArgumentCaptor.forClass(OfferUpdate.class);
        verify(service).updateOffer(eq("OFF1"), captor.capture());

        assertEquals(12.5, captor.getValue().getInterestRate());
        assertEquals(1000.0, captor.getValue().getMinAmount());
    }

    @Test
    void testSearchOffersCommand() {
        Command cmd = new SearchOffersCommand(service);
        cmd.execute("rate=10 bank=Privat");

        ArgumentCaptor<SearchCriteria> captor = ArgumentCaptor.forClass(SearchCriteria.class);
        verify(service).searchOffers(captor.capture());

        assertEquals(10.0, captor.getValue().getMaxRate());
        assertEquals("Privat", captor.getValue().getBankName());
    }

    @Test
    void testCalculatePaymentCommand() {
        Command cmd = new CalculatePaymentCommand(service);
        CreditOffer mockOffer = mock(CreditOffer.class);
        when(service.findOfferById("OFF1")).thenReturn(mockOffer);
        when(mockOffer.calculatePayment(anyDouble(), anyInt())).thenReturn(100.0);

        cmd.execute("OFF1 5000 12");

        verify(mockOffer).calculatePayment(5000.0, 12);
    }

    @Test
    void testSimpleCommands() {
        // list command
        new ListBanksCommand(service).execute("");
        verify(service).listAllBanks();

        // load command
        new LoadDataCommand(service).execute("");
        verify(service).loadData();

        // save command
        new SaveDataCommand(service).execute("");
        verify(service).saveData();

        // select command
        new SelectOfferCommand(service).execute("OFF1");
        verify(service).addToSelection("OFF1");

        // view command
        new ViewSelectionCommand(service).execute("");
        verify(service).viewSelection();
    }
}