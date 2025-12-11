package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SubMenusTest {

    @Test
    void testSubMenusInitialization() {
        CreditService service = Mockito.mock(CreditService.class);

        SubMenuOffer menuOffer = new SubMenuOffer(service);
        menuOffer.getDesc();

        SubMenuBasket menuBasket = new SubMenuBasket(service);
        menuBasket.getDesc();

        SubMenuStorage menuStorage = new SubMenuStorage(service);
        menuStorage.getDesc();
    }
}