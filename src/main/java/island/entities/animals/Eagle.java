package island.entities.animals;

import island.config.Species;
public class Eagle extends Predator {
    public Eagle(){
        super(Species.EAGLE);
    }


    @Override
    public Animal newInstance() {
        return new Eagle();
    }
}
