package island.entities.animals;

import island.config.Species;
public class Goat extends Herbivore {
    public Goat(){
        super(Species.GOAT);
    }

    @Override
    public Herbivore newInstance() {
        return new Goat();
    }
}
