package island.entities.animals;

import island.config.Species;

import java.util.Optional;

public abstract class Predator extends Animal {
    protected Predator(Species species) {
        super(species);
    }

    @Override
    public void eat() {
        if (cell == null || !isAlive()) return;
        Optional<Animal> prey = cell.animals().stream()
                .filter(a -> a instanceof Herbivore && a.isAlive())
                .findAny();
        if (prey.isPresent()) {
            prey.get().die();
            foodEaten += foodNeed;
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
        if (same >= 2 && tryChance(5)) {
            Animal baby = newInstance();
            cell.addAnimal(baby);
        }
    }
}