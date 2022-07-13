package Main.Engine.Evolution.TimeTableSolution.Mutations;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.Mutation;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TimeTableMutationWrapper implements Mutation {

    private TimeTableMutations timeTableMutation;
    private Integer probability;
    private Object[] mutationArguments;

    public TimeTableMutationWrapper(TimeTableMutations timeTableMutation
            , Integer probability, Object[] mutationArguments)
    {
        this.timeTableMutation = timeTableMutation;
        this.probability = probability;
        this.mutationArguments = mutationArguments;
    }

    @Override
    public void doMutation(List<Solution> offSpringPopulation, AlgorithmManager manager, Object... args) {
        if(offSpringPopulation.stream().anyMatch(solution -> !(solution instanceof TimeTable)))
            throw new RuntimeException(this.getClass().getName() + " expects to get a list of TimeTable objects");
        TimeTableUtils.assertManagerIsTimeTableManager(this.getClass().getName(), manager);

        TimeTableManager timeTableManager = (TimeTableManager) manager;

        Random rand = new Random();

        offSpringPopulation.forEach(offSpring -> {
            if(rand.nextInt(100) <= probability)
                timeTableMutation.mutate(offSpring, timeTableManager, args);
        });
    }

    public TimeTableMutations getTimeTableMutation() {
        return timeTableMutation;
    }

    public void setTimeTableMutation(TimeTableMutations timeTableMutation) {
        this.timeTableMutation = timeTableMutation;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }

    public Object[] getMutationArguments() {
        return mutationArguments;
    }

    public void setMutationArguments(Object[] mutationArguments) {
        this.mutationArguments = mutationArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableMutationWrapper that = (TimeTableMutationWrapper) o;
        return timeTableMutation == that.timeTableMutation && Objects.equals(probability, that.probability) && Arrays.equals(mutationArguments, that.mutationArguments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(timeTableMutation, probability);
        result = 31 * result + Arrays.hashCode(mutationArguments);
        return result;
    }
}
