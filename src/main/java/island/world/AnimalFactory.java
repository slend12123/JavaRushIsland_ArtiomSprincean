package island.world;

import island.config.Species;
import island.entities.animals.*;

import java.util.concurrent.ThreadLocalRandom;

public final class AnimalFactory {
    private AnimalFactory(){}

    public static Animal newAnimal(Species s) {
        return switch (s) {
            case WOLF -> new Wolf();
            case BOA -> new Boa();
            case FOX -> new Fox();
            case BEAR -> new Bear();
            case EAGLE -> new Eagle();
            case HORSE -> new Horse();
            case DEER -> new Deer();
            case RABBIT -> new Rabbit();
            case MOUSE -> new Mouse();
            case GOAT -> new Goat();
            case SHEEP -> new Sheep();
            case BOAR -> new Boar();
            case BUFFALO -> new Buffalo();
            case DUCK -> new Duck();
            case CATERPILLAR -> new Caterpillar();
        };
    }

    public static Animal randomAnimal() {
        Species[] vals = Species.values();
        return newAnimal(vals[ThreadLocalRandom.current().nextInt(vals.length)]);
    }
}
