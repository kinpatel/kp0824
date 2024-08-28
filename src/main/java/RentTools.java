import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class RentTools {

    private final Map<String, Tool> tools;

    public RentTools(String[][] toolsToBeAdded) {
        this.tools = new HashMap<>();
        for(String[] toolToBeAdded : toolsToBeAdded) {
            Tool tool = new Tool(toolToBeAdded[0], ToolType.valueOf(toolToBeAdded[1].toUpperCase()), toolToBeAdded[2]);
            tools.put(toolToBeAdded[0], tool);
        }
    }

    public RentalAgreement checkout(String toolCode, LocalDate checkoutDate, int rentalDays, int discountPercent) throws Exception {
        validateRequest(rentalDays, discountPercent);
        if(tools.containsKey(toolCode)) {
            Tool tool = tools.get(toolCode);
            int chargeDays = getChargeDaysCount(toolCode, rentalDays, checkoutDate);
            BigDecimal preDiscountCharge = BigDecimal.valueOf(chargeDays * tool.getToolType().getDailyCharge()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

            RentalAgreement rentalAgreement = new RentalAgreement();
            rentalAgreement.setToolCode(toolCode);
            rentalAgreement.setToolType(tool.getToolType().getDescription());
            rentalAgreement.setToolBrand(tool.getBrand());
            rentalAgreement.setRentalDays(rentalDays);
            rentalAgreement.setCheckOutDate(checkoutDate);
            rentalAgreement.setDueDate(checkoutDate.plusDays(rentalDays));
            rentalAgreement.setDailyRentalCharge(tool.getToolType().getDailyCharge());
            rentalAgreement.setChargeDays(chargeDays);
            rentalAgreement.setPreDiscountCharge(preDiscountCharge);
            rentalAgreement.setDiscountPercent(discountPercent);
            rentalAgreement.setDiscountAmount(discountAmount);
            rentalAgreement.setFinalCharge(finalCharge);
            return rentalAgreement;
        }
        return new RentalAgreement();
    }

    private int getChargeDaysCount(String toolCode, int rentalDays, LocalDate checkoutDate) {
        LocalDate endDate = checkoutDate.plusDays(rentalDays);
        LocalDate fourthJulyHoliday = getFourthJulyHoliday(checkoutDate);
        LocalDate labourDay = getLabourDay(checkoutDate);

        int weekEndCount = 0, holidayCount = 0, chargeDays = 0;

        for(LocalDate date = checkoutDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) weekEndCount++;
            if(date.equals(fourthJulyHoliday) || date.equals(labourDay)) holidayCount++;
        }

        ToolType toolType = tools.get(toolCode).getToolType();

        if(toolType.isHolidayCharge()) {
            chargeDays += holidayCount;
        }
        if(toolType.isWeekendCharge()) {
            chargeDays += weekEndCount;
        }
        if(toolType.isWeekdayCharge()) {
            chargeDays += rentalDays-weekEndCount-holidayCount;
        }

        return chargeDays;
    }

    private LocalDate getLabourDay(LocalDate checkoutDate) {
        LocalDate labourDay = LocalDate.of(checkoutDate.getYear(), 9, 1);
        // Find the first Monday of September
        return labourDay.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    private LocalDate getFourthJulyHoliday(LocalDate checkoutDate) {
        LocalDate fourthJuly = LocalDate.of(checkoutDate.getYear(), 7, 4);
        DayOfWeek dayOfWeek = fourthJuly.getDayOfWeek();
        if(dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            return fourthJuly.minusDays(1);
        } else if(dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            return fourthJuly.plusDays(1);
        }
        return fourthJuly;
    }

    private void validateRequest(int rentalDays, int discountPercent) throws ValidationException {
        if(rentalDays <= 0) {
            throw new ValidationException("RentalDays needs to be greater than 0");
        } else if (discountPercent < 0 || discountPercent > 100) {
            throw new ValidationException("DiscountRate needs to be between 0-100");
        }
    }

    public static void main(String[] args) throws Exception {

        String[][] toolsToBeAdded = new String[][]{
                {"CHNS", "Chainsaw", "Stihl"},
                {"LADW", "Ladder", "Werner"},
                {"JAKD", "Jackhammer", "DeWalt"},
                {"JAKR", "Jackhammer", "Ridgid"}};

        RentTools rentTools = new RentTools(toolsToBeAdded);
        rentTools.checkout("JAKD",  LocalDate.of(2015, 9, 3),6, 20);
        System.out.println(rentTools.tools);

    }
}
