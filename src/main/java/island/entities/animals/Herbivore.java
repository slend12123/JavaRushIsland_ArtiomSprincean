package island.entities.animals;

import island.config.DietMatrix;
import island.config.Species;
import island.entities.Plant;
import island.world.AnimalFactory;

import java.util.Iterator;

public abstract class Herbivore extends Animal {
    protected Herbivore(Species species) { super(species); }

    @Override
    public void eat() {
        if (cell == null || !alive) return;

        // 1) травоядные едят растения
        cell.lock();
        try {
            Iterator<Plant> pit = cell.plants().iterator();
            while (pit.hasNext() && foodEaten < foodNeed) {
                pit.next();
                pit.remove();
                foodEaten += 1.0; // одно растение ~ 1 кг
            }
        } finally {
            cell.unlock();
        }

        // 2) Особый случай: утка ест гусеницу
        if (species == Species.DUCK && foodEaten < foodNeed) {
            cell.lock();
            try {
                var it = cell.animals().iterator();
                while (it.hasNext() && foodEaten < foodNeed) {
                    Animal a = it.next();
                    if (a.isAlive() && a.species == Species.CATERPILLAR) {
                        int chance = DietMatrix.chance(Species.DUCK, Species.CATERPILLAR);
                        if (tryChance(chance)) {
                            it.remove();
                            a.alive = false;
                            foodEaten += a.weight;
                        }
                    }
                }
            } finally { cell.unlock(); }
        }
    }

    @Override
    public void move() {
        if (cell == null || !alive || speed == 0) return;
        var dst = cell.getRandomReachable(speed, this);
        if (dst == cell) return;
        cell.transferAnimalTo(this, dst);
    }

    @Override
    public void reproduce() {
        if (cell == null || !alive) return;
        int same = cell.countSpecies(species);
        if (same >= 2 && same < maxPerCell) {
            Animal child = AnimalFactory.newAnimal(species);
            cell.addAnimal(child);
        }
    }
}
