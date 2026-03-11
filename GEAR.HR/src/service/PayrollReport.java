package service;

import model.PayrollResult;

/**
 * Formats payroll computation results for display (OOP redesign - GEAR.HR).
 */
public class PayrollReport {

    public static String format(PayrollResult r) {
        if (r == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("SALARY COMPUTATION FOR ").append(r.getMonth().toUpperCase()).append("\n");
        sb.append("=====================================\n\n");
        sb.append("Employee: ").append(r.getEmployeeName()).append("\n");
        sb.append("Employee ID: ").append(r.getEmployeeId()).append("\n");
        sb.append("Position: ").append(r.getPosition()).append("\n\n");
        sb.append("GROSS SALARY:\n");
        sb.append("Base Salary: ₱").append(String.format("%,.2f", r.getBaseSalary())).append("\n\n");
        sb.append("DEDUCTIONS:\n");
        sb.append("SSS: ₱").append(String.format("%,.2f", r.getSssDeduction())).append("\n");
        sb.append("PhilHealth: ₱").append(String.format("%,.2f", r.getPhilHealthDeduction())).append("\n");
        sb.append("Pag-IBIG: ₱").append(String.format("%,.2f", r.getPagIbigDeduction())).append("\n");
        sb.append("Withholding Tax: ₱").append(String.format("%,.2f", r.getTaxDeduction())).append("\n");
        sb.append("Total Deductions: ₱").append(String.format("%,.2f", r.getTotalDeductions())).append("\n\n");
        sb.append("ALLOWANCES:\n");
        sb.append("Rice Subsidy: ₱").append(String.format("%,.2f", r.getRiceSubsidy())).append("\n");
        sb.append("Phone Allowance: ₱").append(String.format("%,.2f", r.getPhoneAllowance())).append("\n");
        sb.append("Clothing Allowance: ₱").append(String.format("%,.2f", r.getClothingAllowance())).append("\n");
        sb.append("Total Allowances: ₱").append(String.format("%,.2f", r.getTotalAllowances())).append("\n\n");
        sb.append("NET SALARY: ₱").append(String.format("%,.2f", r.getNetSalary())).append("\n");
        return sb.toString();
    }
}
