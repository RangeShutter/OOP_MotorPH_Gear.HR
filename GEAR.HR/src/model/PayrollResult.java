package model;

/**
 * DTO for payroll computation result (OOP redesign - GEAR.HR).
 * [INHERITANCE] Child class extends AbstractEntity.
 * Carries computed breakdown for PayrollReport to format.
 */
public class PayrollResult extends AbstractEntity {
    private final String employeeName;
    private final String position;
    private final String month;
    private final double baseSalary;
    private final double sssDeduction;
    private final double philHealthDeduction;
    private final double pagIbigDeduction;
    private final double taxDeduction;
    private final double totalDeductions;
    private final double riceSubsidy;
    private final double phoneAllowance;
    private final double clothingAllowance;
    private final double totalAllowances;
    private final double netSalary;

    /** [INHERITANCE] Calls super(employeeId) to set AbstractEntity.entityId. */
    public PayrollResult(String employeeId, String employeeName, String position, String month,
                         double baseSalary, double sssDeduction, double philHealthDeduction, double pagIbigDeduction,
                         double taxDeduction, double totalDeductions,
                         double riceSubsidy, double phoneAllowance, double clothingAllowance, double totalAllowances,
                         double netSalary) {
        super(employeeId != null ? employeeId : "");
        this.employeeName = employeeName != null ? employeeName : "";
        this.position = position != null ? position : "";
        this.month = month != null ? month : "";
        this.baseSalary = baseSalary;
        this.sssDeduction = sssDeduction;
        this.philHealthDeduction = philHealthDeduction;
        this.pagIbigDeduction = pagIbigDeduction;
        this.taxDeduction = taxDeduction;
        this.totalDeductions = totalDeductions;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.totalAllowances = totalAllowances;
        this.netSalary = netSalary;
    }

    /** [INTERFACE] Identifiable: entity id is employeeId. */
    public String getEmployeeId() { return getId(); }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getMonth() { return month; }
    public double getBaseSalary() { return baseSalary; }
    public double getSssDeduction() { return sssDeduction; }
    public double getPhilHealthDeduction() { return philHealthDeduction; }
    public double getPagIbigDeduction() { return pagIbigDeduction; }
    public double getTaxDeduction() { return taxDeduction; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getTotalAllowances() { return totalAllowances; }
    public double getNetSalary() { return netSalary; }

    /** [INTERFACE] Implements Validatable.isValid. [INHERITANCE] Overrides AbstractEntity.isValid. */
    @Override
    public boolean isValid() {
        return baseSalary >= 0 && sssDeduction >= 0 && philHealthDeduction >= 0 && pagIbigDeduction >= 0
                && taxDeduction >= 0 && totalDeductions >= 0 && riceSubsidy >= 0 && phoneAllowance >= 0
                && clothingAllowance >= 0 && totalAllowances >= 0 && netSalary >= 0;
    }

    /** [POLYMORPHISM - Overloading] Returns formatted string for a field (e.g. "baseSalary") using default "%.2f". */
    public String getFormattedAmount(String field) {
        return getFormattedAmount(field, "%.2f");
    }

    /** [POLYMORPHISM - Overloading] Same method name, different parameter: custom format pattern. */
    public String getFormattedAmount(String field, String format) {
        if (field == null || format == null) return "";
        double value;
        switch (field) {
            case "baseSalary": value = baseSalary; break;
            case "sssDeduction": value = sssDeduction; break;
            case "philHealthDeduction": value = philHealthDeduction; break;
            case "pagIbigDeduction": value = pagIbigDeduction; break;
            case "taxDeduction": value = taxDeduction; break;
            case "totalDeductions": value = totalDeductions; break;
            case "riceSubsidy": value = riceSubsidy; break;
            case "phoneAllowance": value = phoneAllowance; break;
            case "clothingAllowance": value = clothingAllowance; break;
            case "totalAllowances": value = totalAllowances; break;
            case "netSalary": value = netSalary; break;
            default: return "";
        }
        try {
            return String.format(format, value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }
}
