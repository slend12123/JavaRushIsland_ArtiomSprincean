package island.entities.animals;

import island.config.Species;
import java.util.Optional;

public abstract class Herbivore extends Animal {
    protected Herbivore(Species species) {
        super(species);
    }

    @Override
    public void eat() {
        if (cell == null || !isAlive()) return;

        boolean ate = false;

        // 1. сначала пробуем растения
        if (!cell.plants().isEmpty()) {
            cell.plants().remove(0);
            foodEaten += foodNeed;
            ate = true;
        }

        // 2. особые случаи: утка и кабан могут есть животных
        if (!ate) {
            if (species == Species.DUCK) {
                Optional<Animal> worm = cell.animals().stream()
                        .filter(a -> a.getSpecies() == Species.CATERPILLAR && a.isAlive())
                        .findAny();
                worm.ifPresent(animal -> {
                    animal.die();
                    foodEaten += foodNeed;
                });
            }
            if (species == Species.BOAR) {
                Optional<Animal> mouse = cell.animals().stream()
                        .filter(a -> a.getSpecies() == Species.MOUSE && a.isAlive())
                        .findAny();
                mouse.ifPresent(animal -> {
                    animal.die();
                    foodEaten += foodNeed;
                });
            }
        }
    }

    @Override
    public void move() {
        if (cell == null || !isAlive()) return;
        cell.moveAnimalRandom(this, speed);
    }

    @Override
    public void reproduce() {
        if (cell == null || !isAlive()) return;
        long same = cell.animals().stream()
                .filter(a -> a.getSpecies() == this.species && a.isAlive())
                .count();
        if (same >= 2 && tryChance(15)) {
            Animal baby = newInstance();
            cell.addAnimal(baby);
        }
    }
}