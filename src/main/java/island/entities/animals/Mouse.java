package island.entities.animals;

import island.config.Species;
public class Mouse extends Herbivore {
    public Mouse(){
        super(Species.MOUSE);
    }

    @Override
    public Herbivore newInstance() {
        return new Mouse();
    }
}
