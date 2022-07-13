package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Classes.Grade;
import Main.Engine.Evolution.TimeTableSolution.Classes.Teacher;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DTOTimeTable implements InformationCarrier {
    private final TimeTable timeTable;

    public DTOTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public Set<DTOTimeTableTuples> getTuples()
    {
        return timeTable.getTuples().stream()
                .map(DTOTimeTableTuples::new)
                .collect(Collectors.toSet());
    }

    public Set<DTOTimeTableTuples> getTuplesByTeacher(Teacher teacher)
    {
        return getTuplesByTeacher(new DTOTeacher(teacher));
    }

    public Set<DTOTimeTableTuples> getTuplesByTeacher(DTOTeacher dtoTeacher)
    {
        return timeTable.getTuples().stream()
                .filter(tuple -> tuple.getTeacher().getId().equals(dtoTeacher.getId()))
                .map(DTOTimeTableTuples::new)
                .collect(Collectors.toSet());
    }

    public Set<DTOTimeTableTuples> getTuplesByGrade(Grade grade)
    {
        return getTuplesByGrade(new DTOGrade(grade));
    }

    public Set<DTOTimeTableTuples> getTuplesByGrade(DTOGrade dtoGrade)
    {
        return timeTable.getTuples().stream()
                .filter(tuple -> tuple.getGrade().getId().equals(dtoGrade.getId()))
                .map(DTOTimeTableTuples::new)
                .collect(Collectors.toSet());
    }

    public Integer getDays()
    {
        return timeTable.getDaysLimit();
    }

    public Integer getHours()
    {
        return timeTable.getHoursLimit();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTable that = (DTOTimeTable) o;
        return Objects.equals(timeTable, that.timeTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTable);
    }
}
