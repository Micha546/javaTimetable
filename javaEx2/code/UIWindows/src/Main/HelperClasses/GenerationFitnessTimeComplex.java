package Main.HelperClasses;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GenerationFitnessTimeComplex {
    private final Integer generation;
    private final Double fitness;
    private final Long timeRunningInMillis;

    public GenerationFitnessTimeComplex(Integer generation, Double fitness, Long timeRunningInMillis) {
        this.generation = generation;
        this.fitness = fitness;
        this.timeRunningInMillis = timeRunningInMillis;
    }

    public Integer getGeneration() {
        return generation;
    }

    public Double getFitness() {
        return fitness;
    }

    public Long getTimeRunning(TimeUnit timeUnit) {
        return timeUnit.convert(timeRunningInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerationFitnessTimeComplex that = (GenerationFitnessTimeComplex) o;
        return Objects.equals(generation, that.generation) && Objects.equals(fitness, that.fitness) && Objects.equals(timeRunningInMillis, that.timeRunningInMillis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generation, fitness, timeRunningInMillis);
    }
}
