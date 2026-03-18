# GEAR.HR

**Important:** In this repository, **`GEAR.HR` is the project root folder**. Open/run the project from the `GEAR.HR/` directory (not from `OOP_MotorPH_Gear.HR/`) so relative paths like `csv/...` resolve correctly.

GEAR.HR is a simple desktop HR system built in Java Swing for managing **Employees**, **Attendance**, **Leave Requests**, and **Payroll**.  
It loads and saves data using CSV files under the `csv/` folder and shows different modules depending on the user’s role.

## How to run

- Main entry point: `src/ui/Main.java`  
  Running `Main` shows a splash screen, then opens the Login screen.

## Data files (CSV)

These are the main CSV files used by the app:

- `csv/user_credentials.csv`: login credentials (userId, password, role, email)
- `csv/employees.csv`: employee directory data
- `csv/attendance_records.csv`: attendance records
- `csv/leave_requests.csv`: leave requests
- `csv/payroll_records.csv`: payroll records per employee

Note: If a CSV file is missing, the app typically loads an empty list/map and still opens.

## Role groups (Role-based access)

GEAR.HR maps the **role string** from `csv/user_credentials.csv` into a role group using `RoleGroup.fromRole(role)` (`src/service/RoleGroup.java`).

### Role strings per group

- **HR**
  - `HR Manager`
  - `HR Team Leader`
  - `HR Rank and File`

- **Payroll**
  - `Payroll Manager`
  - `Payroll Team Leader`
  - `Payroll Rank and File`
  - `Account Team Leader`
  - `Account Rank and File`

- **IT/Admin**
  - `IT`
  - `IT Operations and Systems`

- **Normal Employee**
  - Any other role string not listed above

### What each role can access (sidebar modules)

These menu options are shown from `src/ui/Main.java`:

- **Normal Employee**
  - My Attendance
  - My Profile
  - My Payroll
  - My Leave

- **HR**
  - Attendance Management
  - Employee Profile
  - Leave Management

- **Payroll**
  - Payroll Management
  - View Attendance
  - View Leave Requests

- **IT/Admin**
  - Attendance Management
  - Employee Profile & Payroll Management
  - Leave Management

## Testing roles (external QA “work around”)

If you need to test different role experiences without changing code, you can switch a user’s role in the credentials CSV.

1. Open `csv/user_credentials.csv`
2. Pick an existing row (userId)
3. Change the **role** column to one of the role strings listed above (must match spelling exactly)
4. Save the file
5. Log out and log back in (or restart the app) using that userId

### Important notes for testers

- The app uses **exact role strings** to map into HR/Payroll/IT/Admin; spelling/case should match what’s listed.
- Login credentials are loaded by `src/service/AuthenticationService.java` from `csv/user_credentials.csv` (fallback: `user_credentials.csv` in project root if present).

