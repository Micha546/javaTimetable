package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.*;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.CrossOvers.TimeTableCrossOvers;
import Main.Engine.Evolution.TimeTableSolution.Mutations.TimeTableMutations;
import Main.Engine.Evolution.TimeTableSolution.Rules.TimeTableRules;
import Main.Engine.Evolution.TimeTableSolution.Selections.TimeTableSelections;

import java.util.*;
import java.util.stream.Collectors;

public class DTOTimeTableManager implements InformationCarrier
{
    private final TimeTableManager timeTableManager;

    public DTOTimeTableManager(TimeTableManager timeTableManager)
    {
        this.timeTableManager = timeTableManager;
    }

    public DTOTimeTableCrossOver getCrossOver()
    {
        return new DTOTimeTableCrossOver((TimeTableCrossOvers) timeTableManager.getCrossOver(),
                timeTableManager.getCrossOverCutSize());
    }

    public List<DTOTimeTableMutation> getMutations()
    {
        List<TimeTableMutations> lst = new ArrayList<>();

        for(Mutation mutation : timeTableManager.getMutations())
            lst.add((TimeTableMutations) mutation);

        List<DTOTimeTableMutation> DTOLst = new ArrayList<>();

        for(TimeTableMutations mutation : lst)
            DTOLst.add(new DTOTimeTableMutation(
                    mutation,
                    timeTableManager.getMutationProbability(mutation),
                    Arrays.asList(timeTableManager.getMutationArguments(mutation))
            ));

        return DTOLst;
    }

    public DTOTimeTableSelection getSelection()
    {
        return new DTOTimeTableSelection(
                (TimeTableSelections) timeTableManager.getSelection(),
                Arrays.asList(timeTableManager.getSelectionArguments())
        );
    }

    public Set<DTOTimeTableRules> getRules()
    {
        Set<TimeTableRules> set = new HashSet<>();

        for(Rule rule : timeTableManager.getRules())
            set.add((TimeTableRules) rule);

        Set<DTOTimeTableRules> DTOSet = new HashSet<>();

        for(TimeTableRules rule : set)
            DTOSet.add(new DTOTimeTableRules(
                    rule,
                    timeTableManager.getRuleSeverity(rule),
                    Arrays.asList(timeTableManager.getRuleArguments(rule)),
                    timeTableManager.getHardRuleWeight()
            ));

        return DTOSet;
    }

    public List<DTOSubject> getSubjects()
    {
        return timeTableManager.getAllSubjects().stream()
                .map(DTOSubject::new)
                .collect(Collectors.toList());
    }

    public List<DTOTeacher> getTeachers()
    {
        return timeTableManager.getAllTeachers().stream()
                .map(DTOTeacher::new)
                .collect(Collectors.toList());
    }

    public List<DTOGrade> getGrades()
    {
        return timeTableManager.getAllGrades().stream()
                .map(DTOGrade::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableManager that = (DTOTimeTableManager) o;
        return Objects.equals(timeTableManager, that.timeTableManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTableManager);
    }
}
