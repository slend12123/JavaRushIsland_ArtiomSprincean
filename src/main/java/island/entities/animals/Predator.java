package island.entities.animals;

import island.config.DietMatrix;
import island.config.Species;
import island.world.Cell;
import island.world.AnimalFactory;

import java.util.Iterator;

public abstract class Predator extends Animal {
    protected Predator(Species species) { super(species); }

    @Override
    public void eat() {
        if (cell == null || !alive) return;
        cell.lock();
        try {
            Iterator<Animal> it = cell.animals().iterator();
            while (it.hasNext() && foodEaten < foodNeed) {
                Animal victim = it.next();
                if (victim == this || !victim.isAlive()) continue;
                int chance = DietMatrix.chance(this.species, victim.species);
                if (chance > 0 && tryChance(chance)) {
                    // съели
                    it.remove();
                    victim.alive = false;
                    foodEaten += victim.weight;
                }
            }
        } finally {
            cell.unlock();
        }
    }

    @Override
    public void move() {
        if (cell == null || !alive || speed == 0) return;
        Cell dst = cell.getRandomReachable(speed, this);
        if (dst == cell) return;
        // перенос
        cell.transferAnimalTo(this, dst);
    }

    @Override
    public void reproduce() {
        if (cell == null || !alive) return;
        // если есть пара и не переполнено
        int same = cell.countSpecies(species);
        if (same >= 2 && same < maxPerCell) {
            Animal child = AnimalFactory.newAnimal(species);
            cell.addAnimal(child);
        }
    }
}
