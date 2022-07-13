package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.CrossOvers.TimeTableCrossOvers;

import java.util.Objects;

public class DTOTimeTableCrossOver implements InformationCarrier {
    private final TimeTableCrossOvers crossOver;
    private final Integer cutSize;

    public DTOTimeTableCrossOver(TimeTableCrossOvers crossOver, Integer cutSize) {
        this.crossOver = crossOver;
        this.cutSize = cutSize;
    }

    public final String getCrossOverName()
    {
        return crossOver.name();
    }

    public final Integer getCutSize()
    {
        return cutSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableCrossOver that = (DTOTimeTableCrossOver) o;
        return crossOver == that.crossOver && Objects.equals(cutSize, that.cutSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crossOver, cutSize);
    }
}
