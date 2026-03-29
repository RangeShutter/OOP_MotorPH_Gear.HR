# GEAR.HR

**Important:** In this repository, **`GEAR.HR` is the project root folder**. Open/run the project from the `GEAR.HR/` directory (not from a parent folder) so relative paths like `csv/...` and `Logo/...` resolve correctly.

GEAR.HR is a desktop HR system built in **Java Swing** for managing **employees**, **attendance**, **leave requests**, and **payroll**.  
It loads and saves data from CSV files under `csv/` and shows different navigation and permissions based on the user’s role (`RoleGroup`).

## How to run

- Main entry point: [`src/ui/Main.java`](src/ui/Main.java)  
- Run `Main`: splash screen → login.

## Data files (CSV)

| File | Purpose |
|------|---------|
| `csv/user_credentials.csv` | Login: userId, password, role, email |
| `csv/employees.csv` | Employee directory |
| `csv/attendance_records.csv` | Attendance rows |
| `csv/leave_requests.csv` | Leave requests |
| `csv/payroll_records.csv` | Per-employee payroll (base salary, deductions, allowances) |

If a CSV is missing, the app usually starts with empty data for that module.

### Payroll CSV and `EmployeeData.md`

- Seed payroll rows for MotorPH-style employees **10001–10034** are aligned with [`EmployeeData.md`](EmployeeData.md) (base salary and allowances); **SSS, PhilHealth, Pag-IBIG, and withholding tax** follow [`src/util/PayrollUtils.java`](src/util/PayrollUtils.java).
- Employees **10035–10038** (in `employees.csv` but not in `EmployeeData.md`) have **no row** in `payroll_records.csv` until payroll is edited in the app (defaults apply in memory).
- To **regenerate** `payroll_records.csv` from the same rules, compile and run [`src/tools/GeneratePayrollRecordsCsv.java`](src/tools/GeneratePayrollRecordsCsv.java) from the project root (see class Javadoc).

## Role groups

Roles come from `csv/user_credentials.csv` and are mapped in [`src/service/RoleGroup.java`](src/service/RoleGroup.java).

### Role strings per group

- **HR:** `HR Manager`, `HR Team Leader`, `HR Rank and File`
- **Payroll:** `Payroll Manager`, `Payroll Team Leader`, `Payroll Rank and File`, `Account Team Leader`, `Account Rank and File`
- **IT/Admin:** `IT`, `IT Operations and Systems`
- **Normal employee:** any other role string

### Sidebar / modules

**Normal employee**

- My Attendance  
- **My Profile & Payroll** (opens profile + salary tabs and **Personal Payroll** read-only table)  
- My Leave  

**HR** (Personal Account collapsible + Directives)

- Personal: My Attendance, My Profile & Payroll, My Leave  
- Directives: Attendance Management, Employee Profile, Leave Management  

**Payroll**

- Personal: same as above  
- Directives: Payroll Management, View Attendance, View Leave Requests  

**IT/Admin**

- Personal: same as above  
- Directives: Attendance Management, Employee Profile & Payroll Management, Leave Management  

## Feature notes

### Employee Profile / Payroll Management (`EmployeeProfile`)

- **HR, Payroll, IT/Admin:** two tabs — **Employee Directory** and **Employee Payroll Data** (table from employees + payroll CSV).
- **Column sorting:** click a header to sort; one column at a time; numbers vs text handled by [`src/util/TableColumnSortUtil.java`](src/util/TableColumnSortUtil.java).
- **Edit payroll (Employee Payroll Data tab + employee detail):**
  - **IT/Admin** and **Payroll:** view and edit  
  - **HR:** view only (no Edit Payroll on payroll tab; read-only employee detail for payroll footer)
- **Normal employee:** outer tabs **My Profile** (personal info + salary computation) and **Personal Payroll** (read-only payroll row).

### Attendance

- **HR** and **IT/Admin:** **Delete** removes the **selected** attendance row (after confirm); **Clear** still clears all records (HR/IT only).  
- **Payroll** and normal employees: no delete/clear all (as implemented in [`src/ui/AttendanceScreen.java`](src/ui/AttendanceScreen.java)).

## Testing different roles

1. Edit `csv/user_credentials.csv` — set **role** to an exact string from the lists above.  
2. Save, restart app or log out/in with that userId.

Login is loaded by [`src/service/AuthenticationService.java`](src/service/AuthenticationService.java) (with optional fallback if a root-level `user_credentials.csv` exists).
