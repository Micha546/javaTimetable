package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Selections.TimeTableSelections;

import java.util.List;
import java.util.Objects;

public class DTOTimeTableSelection implements InformationCarrier {
    private final TimeTableSelections timeTableSelection;
    private final List<Object> argsList;

    public DTOTimeTableSelection(TimeTableSelections timeTableSelection, List<Object> argsList) {
        this.timeTableSelection = timeTableSelection;
        this.argsList = argsList;
    }

    public final String getSelectionName()
    {
        return timeTableSelection.name();
    }

    public final List<Object> getSelectionArgumentsList() {
        return argsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableSelection that = (DTOTimeTableSelection) o;
        return timeTableSelection == that.timeTableSelection && Objects.equals(argsList, that.argsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTableSelection, argsList);
    }
}
