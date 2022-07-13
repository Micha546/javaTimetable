package InformationHolders.TableViewClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOSubject;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTeacher;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherView {

    private final Integer ID;
    private final String name;
    private final List<SubjectView> subjects;

    public TeacherView(DTOTeacher dtoTeacher, Set<DTOSubject> allDtoSubjects)
    {
        this.ID = dtoTeacher.getId();
        this.name = dtoTeacher.getName();
        this.subjects = allDtoSubjects.stream()
                .filter(dtoSubject -> dtoTeacher.getTaughtSubjectIds().contains(dtoSubject.getId()))
                .map(SubjectView::new)
                .sorted(Comparator.comparingInt(SubjectView::getID))
                .collect(Collectors.toList());
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public List<SubjectView> getSubjects() {
        return subjects;
    }
}
