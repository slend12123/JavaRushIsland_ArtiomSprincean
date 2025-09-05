package island.entities.animals;

import island.config.Species;
public class Horse extends Herbivore {
    public Horse(){
        super(Species.HORSE);
    }

    @Override
    public Herbivore newInstance() {
        return new Horse();
    }
}
