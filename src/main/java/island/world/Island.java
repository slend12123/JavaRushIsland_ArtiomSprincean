package island.world;

import island.config.Config;
import island.entities.Plant;
import island.entities.animals.Animal;

import island.config.Species;
import java.util.EnumMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Island {
    private final int width, height;
    private final Cell[][] grid;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(Config.SCHEDULED_POOL_SIZE);
    private final ExecutorService animalPool =
            Executors.newFixedThreadPool(Config.ANIMAL_POOL_SIZE);
    private final Random random = new Random();


    private static final int ZONE_SIZE = 6;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];

        generateZones();
        randomPopulate();
        scheduleBackgroundTasks();
    }


    private void generateZones() {
        for (int y = 0; y < height; y += ZONE_SIZE) {
            for (int x = 0; x < width; x += ZONE_SIZE) {
                ZoneType zone = ZoneType.values()[random.nextInt(ZoneType.values().length)];

                for (int dy = y; dy < Math.min(y + ZONE_SIZE, height); dy++) {
                    for (int dx = x; dx < Math.min(x + ZONE_SIZE, width); dx++) {
                        grid[dy][dx] = new Cell(dx, dy, zone, this);
                    }
                }
            }
        }
    }

    public Map<Species, Integer> countBySpecies() {
        Map<Species, Integer> counts = new EnumMap<>(Species.class);
        for (Species s : Species.values()) {
            counts.put(s, 0);
        }
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                for (var a : grid[y][x].animals()) {
                    counts.put(a.getSpecies(), counts.get(a.getSpecies()) + 1);
                }
            }
        }
        return counts;
    }

    private void randomPopulate() {

        for (int i = 0; i < Config.INITIAL_PLANTS; i++) {
            int x = random.nextInt(width), y = random.nextInt(height);
            grid[y][x].addPlant(new Plant());
        }

        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            int n = random.nextInt(Config.INITIAL_ANIMALS_MAX_PER_CELL - Config.INITIAL_ANIMALS_MIN_PER_CELL + 1)
                    + Config.INITIAL_ANIMALS_MIN_PER_CELL;
            for (int k = 0; k < n; k++) {
                Animal a = AnimalFactory.randomAnimal();
                grid[y][x].addAnimal(a);
            }
        }
    }

    private void scheduleBackgroundTasks() {
        scheduler.scheduleAtFixedRate(this::growPlants, 500, 500, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::printStats, 1, 1, TimeUnit.SECONDS);
    }

    private void growPlants() {
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            if (Math.random() < Config.PLANT_GROW_CHANCE_PER_TICK * 0.3) {
                grid[y][x].addPlant(new Plant());
            }
        }
    }

    public void simulateTurn() {
        List<Callable<Void>> jobs = new ArrayList<>();
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            Cell c = grid[y][x];
            jobs.add(() -> {
                var snapshot = new ArrayList<>(c.animals());
                for (Animal a : snapshot) {
                    if (!a.isAlive()) continue;
                    a.eat();
                    a.move();
                    a.reproduce();
                    a.endOfTickUpdate();
                }
                return null;
            });
        }
        try {
            animalPool.invokeAll(jobs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (Config.STOP_WHEN_NO_ANIMALS && totalAnimals() == 0) {
            shutdown();
        }
    }

    private void printStats() {
        System.out.printf("Stats: animals=%d, plants=%d%n", totalAnimals(), totalPlants());
    }

    public int totalAnimals() {
        int c = 0;
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++)
            c += grid[y][x].animals().size();
        return c;
    }

    public int totalPlants() {
        int c = 0;
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++)
            c += grid[y][x].plants().size();
        return c;
    }

    public void shutdown() {
        scheduler.shutdownNow();
        animalPool.shutdownNow();
    }

    public Cell[][] getGrid() { return grid; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
}