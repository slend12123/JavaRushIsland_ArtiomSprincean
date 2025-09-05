package island.config;

import java.util.EnumMap;
import java.util.Map;

public final class SpeciesStats {
    public static final class Stat {
        public final double weight;
        public final int maxPerCell;
        public final int speed;
        public final double foodNeed;
        public Stat(double w, int m, int s, double f) {
            this.weight = w; this.maxPerCell = m; this.speed = s; this.foodNeed = f;
        }
    }

    private static final Map<Species, Stat> STATS = new EnumMap<>(Species.class);
    static {
        // вес, макс/клетка, скорость, аппетит
        STATS.put(Species.WOLF,       new Stat(50, 30, 3, 8));
        STATS.put(Species.BOA,        new Stat(15, 30, 1, 3));
        STATS.put(Species.FOX,        new Stat(8,  30, 2, 2));
        STATS.put(Species.BEAR,       new Stat(500,5,  2, 80));
        STATS.put(Species.EAGLE,      new Stat(6,  20, 3, 1));

        STATS.put(Species.HORSE,      new Stat(400,20, 4, 60));
        STATS.put(Species.DEER,       new Stat(300,20, 4, 50));
        STATS.put(Species.RABBIT,     new Stat(2,  150,2, 0.45));
        STATS.put(Species.MOUSE,      new Stat(0.05,500,1, 0.01));
        STATS.put(Species.GOAT,       new Stat(60, 140,3, 10));
        STATS.put(Species.SHEEP,      new Stat(70, 140,3, 15));
        STATS.put(Species.BOAR,       new Stat(400,50, 2, 50));
        STATS.put(Species.BUFFALO,    new Stat(700,10, 3, 100));
        STATS.put(Species.DUCK,       new Stat(1,  200,4, 0.15));
        STATS.put(Species.CATERPILLAR,new Stat(0.01,1000,0, 0));
    }

    public static Stat of(Species s) { return STATS.get(s); }
}
