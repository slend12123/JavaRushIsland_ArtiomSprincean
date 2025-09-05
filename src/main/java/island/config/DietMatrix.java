package island.config;

import java.util.EnumMap;
import java.util.Map;


public final class DietMatrix {
    private static final Map<Species, Map<Species, Integer>> M = new EnumMap<>(Species.class);

    static {
        // Удобный конструктор карт
        M.put(Species.WOLF, map(
                p(Species.HORSE,10), p(Species.DEER,15), p(Species.RABBIT,60), p(Species.MOUSE,80),
                p(Species.GOAT,60), p(Species.SHEEP,70), p(Species.BOAR,15), p(Species.BUFFALO,10),
                p(Species.DUCK,40)
        ));
        M.put(Species.BOA, map(
                p(Species.FOX,15), p(Species.RABBIT,20), p(Species.MOUSE,40), p(Species.DUCK,10)
        ));
        M.put(Species.FOX, map(
                p(Species.RABBIT,70), p(Species.MOUSE,90), p(Species.DUCK,60), p(Species.CATERPILLAR,40)
        ));
        M.put(Species.BEAR, map(
                p(Species.BOA,80), p(Species.HORSE,40), p(Species.DEER,80), p(Species.RABBIT,80),
                p(Species.MOUSE,90), p(Species.GOAT,70), p(Species.SHEEP,70), p(Species.BOAR,50),
                p(Species.BUFFALO,20), p(Species.DUCK,10)
        ));
        M.put(Species.EAGLE, map(
                p(Species.FOX,10), p(Species.RABBIT,90), p(Species.MOUSE,90), p(Species.DUCK,80)
        ));

        M.put(Species.DUCK, map(
                p(Species.CATERPILLAR,90)
        ));

    }

    public static int chance(Species eater, Species victim) {
        Map<Species, Integer> sub = M.get(eater);
        if (sub == null) return 0;
        return sub.getOrDefault(victim, 0);
    }

    private static Map<Species,Integer> map(Map.Entry<Species,Integer>... pairs) {
        Map<Species,Integer> m = new EnumMap<>(Species.class);
        for (var e : pairs) m.put(e.getKey(), e.getValue());
        return m;
    }
    private static Map.Entry<Species,Integer> p(Species s, int v) {
        return Map.entry(s, v);
    }
}
