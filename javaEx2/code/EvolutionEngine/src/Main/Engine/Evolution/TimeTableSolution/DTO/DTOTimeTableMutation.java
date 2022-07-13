package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Mutations.TimeTableMutationWrapper;
import Main.Engine.Evolution.TimeTableSolution.Mutations.TimeTableMutations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class DTOTimeTableMutation implements InformationCarrier {

    private final TimeTableMutationWrapper wrapper;
    private final List<String> argsDescriptionList;

    public DTOTimeTableMutation(TimeTableMutations timeTableMutation, Integer probability, List<Object> argsList)
    {
        this.wrapper = new TimeTableMutationWrapper(timeTableMutation, probability, argsList.toArray());
        this.argsDescriptionList = getDescriptionAccordingToMutationType(timeTableMutation, argsList);
    }

    public DTOTimeTableMutation(TimeTableMutationWrapper wrapper)
    {
        this.wrapper = wrapper;
        this.argsDescriptionList = getDescriptionAccordingToMutationType(
                wrapper.getTimeTableMutation(),
                Arrays.asList(wrapper.getMutationArguments())
        );
    }

    private List<String> getDescriptionAccordingToMutationType (TimeTableMutations timeTableMutation
            , List<Object> argsList)
    {
        List<String> newDescriptionList = new ArrayList<>();

        switch(timeTableMutation)
        {
            case FlippingSubject:
            case FlippingTeacher:
            case FlippingGrade:
            case FlippingHour:
            case FlippingDay:
                newDescriptionList.add("MaxTuples = " + (int) argsList.get(0));
                newDescriptionList.add("Component = " + (char) argsList.get(1));
                break;
            case Sizer:
                newDescriptionList.add("Total Tuples = " + (int) argsList.get(0));
                break;
        }

        return newDescriptionList;
    }

    public final String getMutationName()
    {
        return wrapper.getTimeTableMutation().name();
    }

    public final Integer getProbability()
    {
        return wrapper.getProbability();
    }

    public final List<Object> getMutationArgs()
    {
        return Arrays.asList(wrapper.getMutationArguments());
    }

    public final List<String> getArgsDescriptionList() {return argsDescriptionList;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableMutation that = (DTOTimeTableMutation) o;
        return Objects.equals(wrapper, that.wrapper) && Objects.equals(argsDescriptionList, that.argsDescriptionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrapper, argsDescriptionList);
    }
}
