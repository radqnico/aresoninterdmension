package it.areson.interdimension;

public class Configuration {

    public static final int rangeMinPortal = 4;
    public static final int rangeMaxPortal = 10;
    public static final int portalDurationSeconds = 10;

    public static final double defaultProbabilityToSpawnEveryFiveSeconds = 0.0285;

    public static final String mainWorldName = "world";

    public static double getProbabilityOfPortals(int nPortals, double probability) {
        double cumulative = 0;
        for (int i = 0; i <= nPortals; i++) {
            cumulative += ((double) binomi(115, i)) * Math.pow(probability, i) * Math.pow(1 - probability, 115 - i);
        }
        return 1 - cumulative;
    }

    static long binomi(int n, int k) {
        if ((n == k) || (k == 0))
            return 1;
        else
            return binomi(n - 1, k) + binomi(n - 1, k - 1);
    }

    // Formula per probabilità: (60!/n!(n-60)!) * p^60-p^n = pc

}
