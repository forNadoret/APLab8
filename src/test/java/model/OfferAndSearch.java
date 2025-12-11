package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfferAndSearch {

    @Test
    void testOfferUpdate() {
        OfferUpdate update = new OfferUpdate();

        // перевіряю сетери
        update.setInterestRate(10.5);
        update.setMinAmount(100.0);
        update.setMaxAmount(200.0);
        update.setMinTermMonths(1);
        update.setMaxTermMonths(12);
        update.setAllowsEarlyRepayment(true);
        update.setAllowsLineIncrease(false);

        // перевіряю гетери
        assertEquals(10.5, update.getInterestRate());
        assertEquals(100.0, update.getMinAmount());
        assertEquals(200.0, update.getMaxAmount());
        assertEquals(1, update.getMinTermMonths());
        assertEquals(12, update.getMaxTermMonths());
        assertTrue(update.getAllowsEarlyRepayment());
        assertFalse(update.getAllowsLineIncrease());
    }

    @Test
    void testSearchCriteria() {
        SearchCriteria criteria = new SearchCriteria();

        // перевіряю сетери
        criteria.setBankName("Mono");
        criteria.setMaxRate(15.0);
        criteria.setMinAmount(5000.0);
        criteria.setAllowsEarlyRepayment(true);
        criteria.setAllowsLineIncrease(false);

        // перевіряю гетери
        assertEquals("Mono", criteria.getBankName());
        assertEquals(15.0, criteria.getMaxRate());
        assertEquals(5000.0, criteria.getMinAmount());
        assertTrue(criteria.getAllowsEarlyRepayment());
        assertFalse(criteria.getAllowsLineIncrease());
    }
}