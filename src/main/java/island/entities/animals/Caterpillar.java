package island.entities.animals;

import island.config.Species;

public class Caterpillar extends Herbivore {
    public Caterpillar(){
        super(Species.CATERPILLAR);
    }

    @Override
    public Herbivore newInstance() {
        return new Caterpillar();
    }
}
