package island.world;

import island.entities.Plant;
import island.entities.animals.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final int x, y;
    private final ZoneType zone;
    private final Island island;

    private final List<Animal> animals = new ArrayList<>();
    private final List<Plant> plants = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Cell(int x, int y, ZoneType zone, Island island) {
        this.x = x;
        this.y = y;
        this.zone = zone;
        this.island = island;
    }

    public void addAnimal(Animal a) {
        lock.lock();
        try {
            animals.add(a);
            a.setCell(this);
        } finally {
            lock.unlock();
        }
    }

    public void removeAnimal(Animal a) {
        lock.lock();
        try {
            animals.remove(a);
        } finally {
            lock.unlock();
        }
    }

    public void addPlant(Plant p) {
        lock.lock();
        try {
            plants.add(p);
        } finally {
            lock.unlock();
        }
    }

    public void removePlant(Plant p) {
        lock.lock();
        try {
            plants.remove(p);
        } finally {
            lock.unlock();
        }
    }

    public List<Animal> animals() {
        lock.lock();
        try {
            return new ArrayList<>(animals);
        } finally {
            lock.unlock();
        }
    }

    public List<Plant> plants() {
        lock.lock();
        try {
            return new ArrayList<>(plants);
        } finally {
            lock.unlock();
        }
    }

    public ZoneType getZone() {
        return zone;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }


    public void moveAnimalRandom(Animal a, int speed) {
        if (speed <= 0) return;

        int nx = x + ThreadLocalRandom.current().nextInt(-speed, speed + 1);
        int ny = y + ThreadLocalRandom.current().nextInt(-speed, speed + 1);

        if (nx < 0 || ny < 0 || nx >= island.getWidth() || ny >= island.getHeight()) return;

        Cell target = island.getGrid()[ny][nx];
        if (target == this) return;

        this.removeAnimal(a);
        target.addAnimal(a);
    }


    public int countAnimalsOfSpecies(island.config.Species species) {
        lock.lock();
        try {
            return (int) animals.stream().filter(an -> an.getSpecies() == species).count();
        } finally {
            lock.unlock();
        }
    }
}
