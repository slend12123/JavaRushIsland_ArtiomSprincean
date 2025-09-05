package island.entities.animals;

import island.config.Species;
public class Fox extends Predator {
    public Fox(){
        super(Species.FOX);
    }

    @Override
    public Animal newInstance() {
        return new Fox();
    }
}
