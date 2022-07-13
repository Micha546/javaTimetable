package Main.Engine.Evolution;

import java.util.Objects;

public class GenerationSolutionFitnessComplex implements InformationCarrier {
    private final Integer generation;
    private final InformationCarrier solution;
    private final Double fitness;

    public GenerationSolutionFitnessComplex(Integer generation, InformationCarrier solution, Double fitness) {
        this.generation = generation;
        this.solution = solution;
        this.fitness = fitness;
    }

    public Integer getGeneration() {
        return generation;
    }

    public InformationCarrier getSolution() {
        return solution;
    }

    public Double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerationSolutionFitnessComplex that = (GenerationSolutionFitnessComplex) o;
        return Objects.equals(generation, that.generation) && Objects.equals(solution, that.solution) && Objects.equals(fitness, that.fitness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generation, solution, fitness);
    }
}
