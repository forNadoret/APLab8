package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoansTest {

    @Test
    void testPersonalLoanAndMath() {
        // перевірка конструктора
        PersonalLoan loan = new PersonalLoan("PL1", 12.0, 1000, 50000, 6, 24, true, false, "Iphone");

        assertEquals("PL1", loan.getId());
        assertEquals("Iphone", loan.getPurpose());

        // перевірка математики
        double payment = loan.calculatePayment(10000, 12);
        assertEquals(888.49, payment, 0.01);

        // перевірка toString
        String str = loan.toString();
        assertTrue(str.contains("PersonalLoan"));
        assertTrue(str.contains("PL1"));
        assertTrue(str.contains("Iphone"));
    }

    @Test
    void testCarLoan() {
        CarLoan loan = new CarLoan("CL1", 10.0, 5000, 20000, 12, 60, true, true, true);

        // перевірка особливого поля
        assertTrue(loan.isRequiresKASKO());

        // перевірка toString
        String str = loan.toString();
        assertTrue(str.contains("CarLoan"));
        assertTrue(str.contains("KASKO"));
    }

    @Test
    void testMortgageLoan() {
        MortgageLoan loan = new MortgageLoan("ML1", 8.0, 50000, 100000, 120, 360, false, false, "House");

        // перевірка особливого поля
        assertEquals("House", loan.getPropertyType());

        // перевірка toString
        String str = loan.toString();
        assertTrue(str.contains("MortgageLoan"));
        assertTrue(str.contains("House"));
    }

    // перевірка сетерів
    @Test
    void testSetters() {
        PersonalLoan loan = new PersonalLoan("TEMP", 0, 0, 0, 0, 0, false, false, "Temp");

        loan.setInterestRate(15.5);
        loan.setMinAmount(100.0);
        loan.setMaxAmount(500.0);
        loan.setMinTermMonths(5);
        loan.setMaxTermMonths(10);
        loan.setAllowsEarlyRepayment(true);
        loan.setAllowsLineIncrease(true);

        assertEquals(15.5, loan.getInterestRate());
        assertEquals(100.0, loan.getMinAmount());
        assertEquals(500.0, loan.getMaxAmount());
        assertEquals(5, loan.getMinTermMonths());
        assertEquals(10, loan.getMaxTermMonths());
        assertTrue(loan.isAllowsEarlyRepayment());
        assertTrue(loan.isAllowsLineIncrease());
    }
}