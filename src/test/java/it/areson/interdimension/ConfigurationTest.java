package it.areson.interdimension;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class ConfigurationTest extends TestCase {

    private static BigDecimal factorial(int n) {
        BigDecimal result = BigDecimal.ONE;
        for (int i = 2; i <= n; i++)
            result = result.multiply(BigDecimal.valueOf(i));
        return result;
    }

    static long binomi(int n, int k) {
        if ((n == k) || (k == 0))
            return 1;
        else
            return binomi(n - 1, k) + binomi(n - 1, k - 1);
    }

    public void testGetProbabilityOfPortals() {
        int nPortals = 2;
        double probability = 0.02;
        System.out.println(((double) binomi(115, nPortals)) * Math.pow(probability, nPortals) * Math.pow(1 - probability, 115 - nPortals));
    }
}