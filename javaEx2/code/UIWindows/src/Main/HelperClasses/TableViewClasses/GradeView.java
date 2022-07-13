package Main.HelperClasses.TableViewClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOGrade;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOSubject;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GradeView {

    private final Integer ID;
    private final String name;
    private final List<Pair<SubjectView, Integer>> subjectsToHours;

    public GradeView(DTOGrade dtoGrade, Set<DTOSubject> allDtoSubjects)
    {
        this.ID = dtoGrade.getId();
        this.name = dtoGrade.getName();
        this.subjectsToHours = allDtoSubjects.stream()
                .filter(dtoSubject -> dtoGrade.getSubjectIdToWeeklyHours().containsKey(dtoSubject.getId()))
                .map(dtoSubject -> new Pair<>(
                        new SubjectView(dtoSubject),
                        dtoGrade.getSubjectIdToWeeklyHours().get(dtoSubject.getId())))
                .collect(Collectors.toList());
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public List<SubjectView> getSubjects()
    {
        return subjectsToHours.stream()
                .sorted(Comparator.comparingInt(pair -> pair.getKey().getID()))
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }

    public List<Integer> getSubjectsHours()
    {
        return subjectsToHours.stream()
                .sorted(Comparator.comparingInt(pair -> pair.getKey().getID()))
                .map(Pair::getValue)
                .collect(Collectors.toList());
    }
}
