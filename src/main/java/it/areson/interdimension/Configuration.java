package it.areson.interdimension;

public class Configuration {

    public static final int rangeMinPortal = 2;
    public static final int rangeMaxPortal = 8;
    public static final int portalDurationSeconds = 60;

    public static final double defaultProbabilityToSpawnEveryFiveSeconds = 0.0285;

    public static final String mainWorldName = "world";

    // Formula probabilità che spawnino k portali con probabilità p: (115 k) * p^k * (1-p)^(n-k)
    public static double getProbabilityOfPortals(int nPortals, double probability) {
        double cumulative = 0;
        for (int i = 0; i <= nPortals; i++) {
            cumulative += ((double) binomial(115, i)) * Math.pow(probability, i) * Math.pow(1 - probability, 115 - i);
        }
        return 1 - cumulative;
    }

    static long binomial(int n, int k) {
        if ((n == k) || (k == 0))
            return 1;
        else
            return binomial(n - 1, k) + binomial(n - 1, k - 1);
    }

}
