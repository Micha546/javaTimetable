package Main.Engine.Evolution;

import java.util.Objects;

public class AlgorithmLog implements InformationCarrier {
    private final Integer generation;
    private final Double fitness;

    public AlgorithmLog(Integer generation, Double fitness) {
        this.generation = generation;
        this.fitness = fitness;
    }

    public Integer getGeneration() {
        return generation;
    }

    public Double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmLog that = (AlgorithmLog) o;
        return Objects.equals(generation, that.generation) && Objects.equals(fitness, that.fitness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generation, fitness);
    }
}
