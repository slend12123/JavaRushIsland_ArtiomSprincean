
package island.entities.animals;

import island.config.Species;
import island.world.Cell;

import java.util.List;

public class Wolf extends Predator {

    public Wolf() {
        super(Species.WOLF);
    }

    @Override
    public Animal newInstance() {
        return new Wolf();
    }
}