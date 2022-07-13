package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Classes.Teacher;

import java.util.Objects;
import java.util.Set;

public class DTOTeacher implements InformationCarrier {
    private final Teacher teacher;

    public DTOTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public final Integer getId()
    {
        return teacher.getId();
    }

    public final String getName()
    {
        return teacher.getName();
    }

    public final Set<Integer> getTaughtSubjectIds()
    {
        return teacher.getSubjectIds();
    }

    public final Integer getWorkingHours()
    {
        return teacher.getWorkingHours();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTeacher that = (DTOTeacher) o;
        return Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacher);
    }
}
