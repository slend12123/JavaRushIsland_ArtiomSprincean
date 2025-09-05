package island.entities.animals;

import island.config.Species;
public class Sheep extends Herbivore {
    public Sheep(){ super(Species.SHEEP);
    }

    @Override
    public Animal newInstance() {
        return new Sheep();
    }
}
