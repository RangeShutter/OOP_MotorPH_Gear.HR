package util;

public final class PayrollUtils {

    private PayrollUtils() {
        // Utility class; prevent instantiation
    }

    // Shared SSS contribution brackets
    private static final double[][] SSS_BRACKETS = {
        {0, 5249.99, 250},
        {5250, 5749.99, 275},
        {5750, 6249.99, 300},
        {6250, 6749.99, 325},
        {6750, 7249.99, 350},
        {7250, 7749.99, 375},
        {7750, 8249.99, 400},
        {8250, 8749.99, 425},
        {8750, 9249.99, 450},
        {9250, 9749.99, 475},
        {9750, 10249.99, 500},
        {10250, 10749.99, 525},
        {10750, 11249.99, 550},
        {11250, 11749.99, 575},
        {11750, 12249.99, 600},
        {12250, 12749.99, 625},
        {12750, 13249.99, 650},
        {13250, 13749.99, 675},
        {13750, 14249.99, 700},
        {14250, 14749.99, 725},
        {14750, 15249.99, 750},
        {15250, 15749.99, 775},
        {15750, 16249.99, 800},
        {16250, 16749.99, 825},
        {16750, 17249.99, 850},
        {17250, 17749.99, 875},
        {17750, 18249.99, 900},
        {18250, 18749.99, 925},
        {18750, 19249.99, 950},
        {19250, 19749.99, 975},
        {19750, 20249.99, 1000},
        {20250, 20749.99, 1025},
        {20750, 21249.99, 1050},
        {21250, 21749.99, 1075},
        {21750, 22249.99, 1100},
        {22250, 22749.99, 1125},
        {22750, 23249.99, 1150},
        {23250, 23749.99, 1175},
        {23750, 24249.99, 1200},
        {24250, 24749.99, 1225},
        {24750, 25249.99, 1250},
        {25250, 25749.99, 1275},
        {25750, 26249.99, 1300},
        {26250, 26749.99, 1325},
        {26750, 27249.99, 1350},
        {27250, 27749.99, 1375},
        {27750, 28249.99, 1400},
        {28250, 28749.99, 1425},
        {28750, 29249.99, 1450},
        {29250, 29749.99, 1475},
        {29750, 30249.99, 1500},
        {30250, 30749.99, 1525},
        {30750, 31249.99, 1550},
        {31250, 31749.99, 1575},
        {31750, 32249.99, 1600},
        {32250, 32749.99, 1625},
        {32750, 33249.99, 1650},
        {33250, 33749.99, 1675},
        {33750, 34249.99, 1700},
        {34250, 34749.99, 1725},
        {34750, 9999999.99, 1750}
    };

    public static double calculateSSSAmount(double baseSalary) {
        for (double[] bracket : SSS_BRACKETS) {
            if (baseSalary >= bracket[0] && baseSalary <= bracket[1]) {
                return bracket[2];
            }
        }
        return 1750;
    }

    public static double calculatePhilHealthAmount(double baseSalary) {
        double philHealthAmount = baseSalary * 0.05;
        return Math.max(500.0, Math.min(philHealthAmount, 5000.0));
    }

    public static double calculatePagIbigAmount(double baseSalary) {
        double pagIbigAmount = baseSalary * 0.02;
        return Math.min(pagIbigAmount, 200.0);
    }

    public static double calculateWithholdingTax(double baseSalary, double riceSubsidy,
                                                 double phoneAllowance, double clothingAllowance) {
        double netTaxableComp = baseSalary + phoneAllowance + riceSubsidy + clothingAllowance;
        double subtrahend;
        double percent;
        double addend;

        if (netTaxableComp <= 20833) {
            subtrahend = 0;
            percent = 0;
            addend = 0;
        } else if (netTaxableComp <= 33332) {
            subtrahend = 20833;
            percent = 0.20;
            addend = 0;
        } else if (netTaxableComp <= 66666) {
            subtrahend = 33333;
            percent = 0.25;
            addend = 2500.00;
        } else if (netTaxableComp <= 166666) {
            subtrahend = 66667;
            percent = 0.30;
            addend = 10833.33;
        } else if (netTaxableComp <= 666666) {
            subtrahend = 166667;
            percent = 0.32;
            addend = 40833.33;
        } else {
            subtrahend = 666667;
            percent = 0.35;
            addend = 200833.33;
        }

        double taxExcess = netTaxableComp - subtrahend;
        if (taxExcess < 0) {
            taxExcess = 0;
        }
        return (taxExcess * percent) + addend;
    }
}
