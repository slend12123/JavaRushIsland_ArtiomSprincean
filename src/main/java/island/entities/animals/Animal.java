package island.entities.animals;

import island.config.Species;
import island.config.SpeciesStats;
import island.world.Cell;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal {
    protected final Species species;
    protected final double weight;
    protected final int maxPerCell;
    protected final int speed;
    protected final double foodNeed;

    protected double foodEaten = 0.0;
    protected boolean alive = true;
    protected Cell cell;

    protected Animal(Species species) {
        this.species = species;
        var st = SpeciesStats.of(species);
        this.weight = st.weight;
        this.maxPerCell = st.maxPerCell;
        this.speed = st.speed;
        this.foodNeed = st.foodNeed;
    }

    public Species getSpecies() { return species; }
    public boolean isAlive() { return alive; }
    public void setCell(Cell c) { this.cell = c; }

    public abstract void eat();
    public abstract void move();
    public abstract void reproduce();
    public abstract Animal newInstance();

    protected boolean tryChance(int percent) {
        if (percent <= 0) return false;
        return ThreadLocalRandom.current().nextInt(100) < percent;
    }

    protected void die() {
        alive = false;
        if (cell != null) cell.removeAnimal(this);
    }

    /** Голод: умирает, если в конце тика не наелся хотя бы на 10% нужды 2 тика подряд (упрощённо: 1 тик) */
    public void endOfTickUpdate() {
        if (foodEaten < 0.1 * foodNeed) {
            die();
        }
        foodEaten = 0.0;
    }
}
