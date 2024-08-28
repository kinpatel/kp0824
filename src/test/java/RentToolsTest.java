import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RentToolsTest {

    private RentTools rentTools;

    @BeforeEach
    void setup() {
        String[][] toolsToBeAdded = new String[][]{
                {"CHNS", "Chainsaw", "Stihl"},
                {"LADW", "Ladder", "Werner"},
                {"JAKD", "Jackhammer", "DeWalt"},
                {"JAKR", "Jackhammer", "Ridgid"}};

        rentTools = new RentTools(toolsToBeAdded);
    }

    @Test
    void test_1() {
        try {
            rentTools.checkout("JAKR", LocalDate.of(2015,9,3), 5, 101);
        } catch (Exception e) {
            assertEquals("DiscountRate needs to be between 0-100", e.getMessage());
            return;
        }
        fail("Exception is expected");
    }

    @Test
    void test_2() throws Exception {
        RentalAgreement rentalAgreement = rentTools.checkout("LADW", LocalDate.of(2020,7,2), 3, 10);
        assertEquals("LADW", rentalAgreement.getToolCode());
        assertEquals("Ladder", rentalAgreement.getToolType());
        assertEquals("Werner", rentalAgreement.getToolBrand());
        assertEquals(3, rentalAgreement.getRentalDays());
        assertEquals(LocalDate.of(2020,7,2), rentalAgreement.getCheckOutDate());
        assertEquals(LocalDate.of(2020,7,5), rentalAgreement.getDueDate());
        assertEquals(1.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(2, rentalAgreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(3.98), rentalAgreement.getPreDiscountCharge());
        assertEquals(10, rentalAgreement.getDiscountPercent());
        assertEquals(new BigDecimal("0.40"), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.58), rentalAgreement.getFinalCharge());
    }

    @Test
    void test_3() throws Exception {
        RentalAgreement rentalAgreement = rentTools.checkout("CHNS", LocalDate.of(2015,7,2), 5, 25);
        assertEquals("CHNS", rentalAgreement.getToolCode());
        assertEquals("Chainsaw", rentalAgreement.getToolType());
        assertEquals("Stihl", rentalAgreement.getToolBrand());
        assertEquals(5, rentalAgreement.getRentalDays());
        assertEquals(LocalDate.of(2015,7,2), rentalAgreement.getCheckOutDate());
        assertEquals(LocalDate.of(2015,7,7), rentalAgreement.getDueDate());
        assertEquals(1.49, rentalAgreement.getDailyRentalCharge());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(4.47), rentalAgreement.getPreDiscountCharge());
        assertEquals(25, rentalAgreement.getDiscountPercent());
        assertEquals(BigDecimal.valueOf(1.12), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.35), rentalAgreement.getFinalCharge());
    }

    @Test
    void test_4() throws Exception {
        RentalAgreement rentalAgreement = rentTools.checkout("JAKD", LocalDate.of(2015,9,3), 6, 0);
        assertEquals("JAKD", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("DeWalt", rentalAgreement.getToolBrand());
        assertEquals(6, rentalAgreement.getRentalDays());
        assertEquals(LocalDate.of(2015,9,3), rentalAgreement.getCheckOutDate());
        assertEquals(LocalDate.of(2015,9,9), rentalAgreement.getDueDate());
        assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountPercent());
        assertEquals(new BigDecimal("0.00"), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getFinalCharge());
    }

    @Test
    void test_5() throws Exception {
        RentalAgreement rentalAgreement = rentTools.checkout("JAKR", LocalDate.of(2015,7,2), 9, 0);
        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());
        assertEquals(9, rentalAgreement.getRentalDays());
        assertEquals(LocalDate.of(2015,7,2), rentalAgreement.getCheckOutDate());
        assertEquals(LocalDate.of(2015,7,11), rentalAgreement.getDueDate());
        assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(5, rentalAgreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountPercent());
        assertEquals(new BigDecimal("0.00"), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.getFinalCharge());
    }

    @Test
    void test_6() throws Exception {
        RentalAgreement rentalAgreement = rentTools.checkout("JAKR", LocalDate.of(2020,7,2), 4, 50);
        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());
        assertEquals(4, rentalAgreement.getRentalDays());
        assertEquals(LocalDate.of(2020,7,2), rentalAgreement.getCheckOutDate());
        assertEquals(LocalDate.of(2020,7,6), rentalAgreement.getDueDate());
        assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(1, rentalAgreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getPreDiscountCharge());
        assertEquals(50, rentalAgreement.getDiscountPercent());
        assertEquals(new BigDecimal("1.50"), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(1.49), rentalAgreement.getFinalCharge());
    }

}