package service;

import model.Employee;
import model.PayrollData;
import model.PayrollResult;
import repository.IPayrollRepository;
import util.PayrollUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Computes employee compensation; persistence delegated to repository (OOP redesign - GEAR.HR).
 * [POLYMORPHISM] Depends on IPayrollRepository so implementations can be swapped.
 */
public class PayrollProcessor {
    /** [INTERFACE] [POLYMORPHISM] Uses IPayrollRepository contract. */
    private final IPayrollRepository repository;
    private final Map<String, PayrollData> payrollData = new HashMap<>();

    public PayrollProcessor(IPayrollRepository repository) {
        this.repository = repository;
        payrollData.putAll(repository.load());
        if (payrollData.isEmpty()) {
            initializeSamplePayrollData();
            savePayrollDataToCSV();
        }
    }

    public void loadPayrollDataFromCSV() {
        payrollData.clear();
        payrollData.putAll(repository.load());
    }

    public void savePayrollDataToCSV() {
        Map<String, PayrollData> toSave = new HashMap<>();
        for (Map.Entry<String, PayrollData> e : payrollData.entrySet()) {
            PayrollData d = e.getValue();
            if (!d.isValid()) continue;
            double tax = PayrollUtils.calculateWithholdingTax(
                d.getBaseSalary(), d.getRiceSubsidy(), d.getPhoneAllowance(), d.getClothingAllowance()
            );
            PayrollData built = new PayrollData(
                d.getBaseSalary(), d.getSssAmount(), d.getPhilHealthAmount(), d.getPagIbigAmount(),
                (float) tax, d.getRiceSubsidy(), d.getPhoneAllowance(), d.getClothingAllowance()
            );
            if (built.isValid()) toSave.put(e.getKey(), built);
        }
        repository.save(toSave);
    }

    private void initializeSamplePayrollData() {
        double base1 = 35000.0, base2 = 60000.0, base3 = 45000.0;
        float rice1 = 1500.0f, phone1 = 1000.0f, cloth1 = 800.0f;
        float rice2 = 1500.0f, phone2 = 800.0f, cloth2 = 600.0f;
        float rice3 = 1500.0f, phone3 = 900.0f, cloth3 = 700.0f;
        payrollData.put("1001", new PayrollData(base1,
            PayrollUtils.calculateSSSAmount(base1), PayrollUtils.calculatePhilHealthAmount(base1), PayrollUtils.calculatePagIbigAmount(base1),
            (float) PayrollUtils.calculateWithholdingTax(base1, rice1, phone1, cloth1), rice1, phone1, cloth1));
        payrollData.put("1002", new PayrollData(base2,
            PayrollUtils.calculateSSSAmount(base2), PayrollUtils.calculatePhilHealthAmount(base2), PayrollUtils.calculatePagIbigAmount(base2),
            (float) PayrollUtils.calculateWithholdingTax(base2, rice2, phone2, cloth2), rice2, phone2, cloth2));
        payrollData.put("1003", new PayrollData(base3,
            PayrollUtils.calculateSSSAmount(base3), PayrollUtils.calculatePhilHealthAmount(base3), PayrollUtils.calculatePagIbigAmount(base3),
            (float) PayrollUtils.calculateWithholdingTax(base3, rice3, phone3, cloth3), rice3, phone3, cloth3));
    }

    private static PayrollData getDefaultPayrollData() {
        double baseSalary = 30000.0;
        float rice = 1500.0f, phone = 1000.0f, cloth = 800.0f;
        return new PayrollData(baseSalary,
            PayrollUtils.calculateSSSAmount(baseSalary), PayrollUtils.calculatePhilHealthAmount(baseSalary), PayrollUtils.calculatePagIbigAmount(baseSalary),
            (float) PayrollUtils.calculateWithholdingTax(baseSalary, rice, phone, cloth), rice, phone, cloth);
    }

    public PayrollData getPayrollData(String employeeId) {
        return payrollData.getOrDefault(employeeId, getDefaultPayrollData());
    }

    public void updatePayrollData(String employeeId, PayrollData data) {
        if (employeeId == null || data == null || !data.isValid()) return;
        payrollData.put(employeeId, data);
        savePayrollDataToCSV();
    }

    public void removePayrollData(String employeeId) {
        payrollData.remove(employeeId);
        savePayrollDataToCSV();
    }

    public PayrollResult processPayroll(Employee employee, String month) {
        if (employee == null || month == null) return null;
        PayrollData data = getPayrollData(employee.getEmployeeNumber());
        double baseSalary = data.getBaseSalary();
        double sssDeduction = data.getSSSDeduction();
        double philHealthDeduction = data.getPhilHealthDeduction();
        double pagIbigDeduction = data.getPagIbigDeduction();
        double taxDeduction = data.getTaxDeduction();
        double totalDeductions = data.calculateTotalDeductions();
        double totalAllowances = data.calculateTotalAllowances();
        double netSalary = data.calculateNetSalary();
        String name = (employee.getFirstName() + " " + employee.getLastName()).trim();
        return new PayrollResult(
            employee.getEmployeeNumber(), name, employee.getPosition(), month,
            baseSalary, sssDeduction, philHealthDeduction, pagIbigDeduction,
            taxDeduction, totalDeductions,
            data.getRiceSubsidy(), data.getPhoneAllowance(), data.getClothingAllowance(), totalAllowances,
            netSalary
        );
    }
}
