package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.CrossOvers.TimeTableCrossOvers;

import java.util.List;
import java.util.Objects;

public class DTOTimeTableCrossOver implements InformationCarrier {
    private final TimeTableCrossOvers crossOver;
    private final Integer cutSize;
    private final List<Object> args;

    public DTOTimeTableCrossOver(TimeTableCrossOvers crossOver, Integer cutSize, List<Object> arguments) {
        this.crossOver = crossOver;
        this.cutSize = cutSize;
        this.args = arguments;
    }

    public final String getCrossOverName()
    {
        return crossOver.name();
    }

    public final Integer getCutSize()
    {
        return cutSize;
    }

    public final List<Object> getArguments()
    {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableCrossOver that = (DTOTimeTableCrossOver) o;
        return crossOver == that.crossOver && Objects.equals(cutSize, that.cutSize) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crossOver, cutSize, args);
    }
}
