package island.entities.animals;

import island.config.Species;
public class Buffalo extends Herbivore {
    public Buffalo(){
        super(Species.BUFFALO);
    }

    @Override
    public Herbivore newInstance() {
        return new Buffalo();
    }
}
