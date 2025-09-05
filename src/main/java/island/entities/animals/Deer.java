package island.entities.animals;

import island.config.Species;
public class Deer extends Herbivore {
    public Deer(){
        super(Species.DEER);
    }

    @Override
    public Herbivore newInstance() {
        return new Deer();
    }
}
