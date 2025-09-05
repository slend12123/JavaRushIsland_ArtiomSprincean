package island.entities.animals;

import island.config.Species;
public class Bear extends Predator {
    public Bear(){
        super(Species.BEAR);
    }

    @Override
    public Animal newInstance() {
        return new Bear();
    }
}
