package island.entities.animals;

import island.config.Species;
public class Rabbit extends Herbivore {
    public Rabbit(){
        super(Species.RABBIT);
    }

    @Override
    public Herbivore newInstance() {
        return new Rabbit();
    }
}
