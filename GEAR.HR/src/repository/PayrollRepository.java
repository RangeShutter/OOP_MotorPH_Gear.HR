package repository;

import model.PayrollData;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * CSV persistence for payroll data (employeeId -> PayrollData). Load/save only.
 * Expects map values to already include withholding tax when saving.
 * [INTERFACE] Implements IPayrollRepository.
 * [POLYMORPHISM] Can be used as IPayrollRepository by callers.
 */
public class PayrollRepository implements IPayrollRepository {
    private static final String FILE = "csv/payroll_records.csv";
    private static final String HEADER = "EmployeeID,BaseSalary,SSSAmount,PhilHealthAmount,PagIBIGAmount,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance";

    /** [INTERFACE] Implements IPayrollRepository.load. */
    @Override
    public Map<String, PayrollData> load() {
        Map<String, PayrollData> map = new HashMap<>();
        if (!Files.exists(Paths.get(FILE))) return map;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 9) {
                    String id = data[0].trim();
                    double base = Double.parseDouble(data[1]);
                    double sss = Double.parseDouble(data[2]);
                    double phil = Double.parseDouble(data[3]);
                    double pag = Double.parseDouble(data[4]);
                    float tax = Float.parseFloat(data[5]);
                    float rice = Float.parseFloat(data[6]);
                    float phone = Float.parseFloat(data[7]);
                    float cloth = Float.parseFloat(data[8]);
                    map.put(id, new PayrollData(base, sss, phil, pag, tax, rice, phone, cloth));
                }
            }
        } catch (IOException | NumberFormatException e) {
            // return empty
        }
        return map;
    }

    /** [INTERFACE] Implements IPayrollRepository.save. */
    @Override
    public void save(Map<String, PayrollData> data) {
        if (data == null) return;
        try (PrintWriter w = new PrintWriter(new FileWriter(FILE))) {
            w.println(HEADER);
            for (Map.Entry<String, PayrollData> e : data.entrySet()) {
                PayrollData d = e.getValue();
                w.println(String.join(",",
                    e.getKey(),
                    String.valueOf(d.getBaseSalary()),
                    String.valueOf(d.getSssAmount()),
                    String.valueOf(d.getPhilHealthAmount()),
                    String.valueOf(d.getPagIbigAmount()),
                    String.format("%.2f", d.getTaxDeduction()),
                    String.valueOf(d.getRiceSubsidy()),
                    String.valueOf(d.getPhoneAllowance()),
                    String.valueOf(d.getClothingAllowance())
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
