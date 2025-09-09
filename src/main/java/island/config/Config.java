package island.config;

public final class Config {
    private Config() {}


    public static final int WIDTH  = 100;
    public static final int HEIGHT = 50;


    public static final int TICK_MILLIS = 500;


    public static final int INITIAL_PLANTS = 800;
    public static final int INITIAL_ANIMALS_MIN_PER_CELL = 0;
    public static final int INITIAL_ANIMALS_MAX_PER_CELL = 3;


    public static final int PLANT_MAX_PER_CELL = 200;
    public static final double PLANT_GROW_CHANCE_PER_TICK = 0.15;


    public static final int SCHEDULED_POOL_SIZE = 3; // растения, животные, статистика
    public static final int ANIMAL_POOL_SIZE = Runtime.getRuntime().availableProcessors();


    public static final boolean STOP_WHEN_NO_ANIMALS = true;
}
