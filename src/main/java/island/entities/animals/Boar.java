package island.entities.animals;

import island.config.Species;
public class Boar extends Herbivore {
    public Boar(){
        super(Species.BOAR);
    }

    @Override
    public Herbivore newInstance() {
        return new Boar();
    }
}
