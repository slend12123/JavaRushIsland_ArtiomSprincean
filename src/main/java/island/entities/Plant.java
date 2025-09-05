package island.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class Plant {
    private static final AtomicInteger idGen = new AtomicInteger();
    private final int id;
    private boolean alive = true;

    public Plant() {
        this.id = idGen.incrementAndGet();
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    @Override
    public String toString() {
        return "Plant#" + id;
    }}