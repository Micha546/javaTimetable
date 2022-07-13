package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableTuple;

import java.util.Objects;

public class DTOTimeTableTuples implements InformationCarrier {
    private final TimeTableTuple tuple;

    public DTOTimeTableTuples(TimeTableTuple tuple) {
        this.tuple = tuple;
    }

    public Integer getDay()
    {
        return tuple.getDay();
    }

    public Integer getHour()
    {
        return tuple.getHour();
    }

    public DTOTeacher getTeacher()
    {
        return new DTOTeacher(tuple.getTeacher());
    }

    public DTOSubject getSubject()
    {
        return new DTOSubject(tuple.getSubject());
    }

    public DTOGrade getGrade()
    {
        return new DTOGrade(tuple.getGrade());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableTuples that = (DTOTimeTableTuples) o;
        return Objects.equals(tuple, that.tuple);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tuple);
    }
}
