package island.world;

import island.config.Config;
import island.config.Species;
import island.config.SpeciesStats;
import island.entities.Plant;
import island.entities.animals.Animal;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final int x, y;
    private final ZoneType zone;
    private final Island island;

    private final ReentrantLock lock = new ReentrantLock();

    private final CopyOnWriteArrayList<Animal> animals = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Plant> plants  = new CopyOnWriteArrayList<>();

    public Cell(int x, int y, ZoneType zone, Island island) {
        this.x = x; this.y = y; this.zone = zone; this.island = island;
    }

    public ZoneType getZone() { return zone; }
    public int getX(){ return x; }
    public int getY(){ return y; }

    public List<Animal> animals(){ return animals; }
    public List<Plant> plants(){ return plants; }

    public void lock(){ lock.lock(); }
    public void unlock(){ lock.unlock(); }

    public void addAnimal(Animal a) {
        lock();
        try {
            long same = animals.stream().filter(an -> an.getSpecies()==a.getSpecies()).count();
            if (same < SpeciesStats.of(a.getSpecies()).maxPerCell) {
                animals.add(a);
                a.setCell(this);
            }
        } finally { unlock(); }
    }

    public void removeAnimal(Animal a) {
        animals.remove(a);
    }

    public void addPlant(Plant p) {
        lock();
        try {
            if (plants.size() < Config.PLANT_MAX_PER_CELL) plants.add(p);
        } finally { unlock(); }
    }

    public int countSpecies(Species s) {
        int c = 0;
        for (var a: animals) if (a.getSpecies()==s && a.isAlive()) c++;
        return c;
    }

    public Cell getRandomReachable(int speed, Animal who) {
        if (speed <= 0) return this;
        int dx = ThreadLocalRandom.current().nextInt(-speed, speed+1);
        int dy = ThreadLocalRandom.current().nextInt(-speed, speed+1);
        int nx = Math.max(0, Math.min(island.getWidth()-1, x + dx));
        int ny = Math.max(0, Math.min(island.getHeight()-1, y + dy));
        return island.getGrid()[ny][nx];
    }

    public void transferAnimalTo(Animal a, Cell dst) {
        if (dst == this) return;
        Cell first = this.hashCode() < dst.hashCode() ? this : dst;
        Cell second = this.hashCode() < dst.hashCode() ? dst : this;

        first.lock(); second.lock();
        try {
            if (!animals.remove(a)) return;
            long same = dst.animals.stream().filter(an -> an.getSpecies()==a.getSpecies()).count();
            if (same < SpeciesStats.of(a.getSpecies()).maxPerCell) {
                dst.animals.add(a);
                a.setCell(dst);
            } else {
                // вернуть обратно, если в целевой переполнено
                animals.add(a);
                a.setCell(this);
            }
        } finally {
            second.unlock(); first.unlock();
        }
    }
}
