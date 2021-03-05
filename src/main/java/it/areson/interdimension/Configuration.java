package it.areson.interdimension;

import java.math.BigInteger;

public class Configuration {

    public static final int rangeMinPortal = 4;
    public static final int rangeMaxPortal = 10;
    public static final int portalDurationSeconds = 10;

    public static final double defaultProbabilityToSpawnEveryFiveSeconds = 0.0285;

    public static final String mainWorldName = "world";

    public static double getProbabilityOfPortals(int nPortals, double probability) {
        return ((factorial(60)).divide(factorial(60).multiply(factorial(60 - nPortals)))).longValue() * (Math.pow(probability, nPortals) * Math.pow(1 - probability, 60 - nPortals));
    }

    private static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++)
            result = result.multiply(BigInteger.valueOf(i));
        return result;
    }

    // Formula per probabilità: (60!/n!(n-60)!) * p^60-p^n = pc

}
