package com.golden.task;

public enum Priority {
    HIGH(30),
    MEDIUM(20),
    LOW(10);

    private final int weight;

    Priority(int weight){
        this.weight = weight;
    }

    public int getWeight(){
        return this.weight;
    }
}