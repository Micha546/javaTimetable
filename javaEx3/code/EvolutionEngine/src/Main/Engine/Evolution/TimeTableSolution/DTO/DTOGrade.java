package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Classes.Grade;

import java.util.Map;
import java.util.Objects;

public class DTOGrade implements InformationCarrier {
    private final Grade grade;

    public DTOGrade(Grade grade) {
        this.grade = grade;
    }

    public final Integer getId()
    {
        return grade.getId();
    }

    public final String getName()
    {
        return grade.getName();
    }

    public final Map<Integer, Integer> getSubjectIdToWeeklyHours()
    {
        return grade.getSubjectIdToWeeklyHours();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOGrade dtoGrade = (DTOGrade) o;
        return Objects.equals(grade, dtoGrade.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade);
    }
}
