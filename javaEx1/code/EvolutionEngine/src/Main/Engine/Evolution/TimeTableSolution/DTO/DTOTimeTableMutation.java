package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Mutations.TimeTableMutations;

import java.util.List;
import java.util.Objects;


public class DTOTimeTableMutation implements InformationCarrier {

    private final TimeTableMutations timeTableMutation;
    private final Integer probability;
    private final List <Object> argsList;

    public DTOTimeTableMutation(TimeTableMutations timeTableMutation, Integer probability, List<Object> argsList) {
        this.timeTableMutation = timeTableMutation;
        this.probability = probability;
        this.argsList = argsList;
    }

    public final String getMutationName()
    {
        return timeTableMutation.name();
    }

    public final Integer getProbability()
    {
        return probability;
    }

    public final List<Object> getMutationArgs()
    {
        return argsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableMutation that = (DTOTimeTableMutation) o;
        return timeTableMutation == that.timeTableMutation && Objects.equals(probability, that.probability) && Objects.equals(argsList, that.argsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTableMutation, probability, argsList);
    }
}
