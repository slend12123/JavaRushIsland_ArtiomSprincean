package island.entities.animals;

import island.config.Species;
public class Boa extends Predator {
    public Boa(){
        super(Species.BOA);
    }

    @Override
    public Animal newInstance() {
        return new Boa();
    }
}
