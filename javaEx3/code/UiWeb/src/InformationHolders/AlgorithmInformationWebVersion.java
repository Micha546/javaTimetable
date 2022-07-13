package InformationHolders;

import InformationHolders.TableViewClasses.*;
import Main.Engine.Evolution.TimeTableSolution.DTO.*;
import Utils.AlgorithmWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlgorithmInformationWebVersion {
    private final AlgorithmWrapper.Status status;
    private final Integer population;
    private final List<SubjectView> subjects;
    private final List<TeacherView> teachers;
    private final List<GradeView> grades;
    private final List<MutationView> mutations;
    private final List<RuleView> rules;
    private final DTOTimeTableCrossOver crossOver;
    private final DTOTimeTableSelection selection;
    private final DTOTimeTable bestSolution;
    private final Map<String, Integer> ruleNameToEnforcementMap;
    private final Integer generation;
    private final Double fitness;
    private final Integer timeRunningInSeconds;
    private final Boolean stopByGeneration;
    private final Boolean stopByFitness;
    private final Boolean stopByTime;
    private final Integer byGenerationStopValue;
    private final Integer byFitnessStopValue;
    private final Integer byTimeStopValue;

    public AlgorithmInformationWebVersion(
            DTOTimeTableManager manager,
            DTOTimeTable bestSolution,
            Map<String, Integer> ruleNameToEnforcementMap,
            Integer generation,
            Double fitness,
            Integer timeRunningInSeconds,
            AlgorithmWrapper.Status status,
            Boolean stopByGeneration,
            Boolean stopByFitness,
            Boolean stopByTime,
            Integer byGenerationStopValue,
            Integer byFitnessStopValue,
            Integer byTimeStopValue
    )
    {
        this.status = status;
        this.population = manager.getPopulationSize();
        this.subjects = manager.getSubjects().stream()
                .map(SubjectView::new)
                .collect(Collectors.toList());
        this.teachers = manager.getTeachers().stream()
                .map(dto -> new TeacherView(dto, new HashSet<>(manager.getSubjects())))
                .collect(Collectors.toList());
        this.grades = manager.getGrades().stream()
                .map(dtoGrade -> new GradeView(dtoGrade, new HashSet<>(manager.getSubjects())))
                .collect(Collectors.toList());
        this.mutations = manager.getMutations().stream()
                .map(MutationView::new)
                .collect(Collectors.toList());
        this.rules = manager.getRules().stream()
                .map(RuleView::new)
                .collect(Collectors.toList());
        this.crossOver = manager.getCrossOver();
        this.selection = manager.getSelection();
        this.bestSolution = bestSolution;
        this.ruleNameToEnforcementMap = ruleNameToEnforcementMap;
        this.generation = generation;
        this.fitness = fitness;
        this.timeRunningInSeconds = timeRunningInSeconds;
        this.stopByGeneration = stopByGeneration;
        this.stopByFitness = stopByFitness;
        this.stopByTime = stopByTime;
        this.byGenerationStopValue = byGenerationStopValue;
        this.byFitnessStopValue = byFitnessStopValue;
        this.byTimeStopValue = byTimeStopValue;
    }

    public AlgorithmWrapper.Status getStatus() {
        return status;
    }

    public Integer getPopulation() {
        return population;
    }

    public List<TeacherView> getTeachers() {
        return teachers;
    }

    public List<GradeView> getGrades() {
        return grades;
    }

    public List<SubjectView> getSubjects() {
        return subjects;
    }

    public List<MutationView> getMutations() {
        return mutations;
    }

    public List<RuleView> getRules() {
        return rules;
    }

    public DTOTimeTableCrossOver getCrossOver() {
        return crossOver;
    }

    public DTOTimeTableSelection getSelection() {
        return selection;
    }

    public DTOTimeTable getBestSolution() {
        return bestSolution;
    }

    public Map<String, Integer> getRuleNameToEnforcementMap() {
        return ruleNameToEnforcementMap;
    }

    public Integer getGeneration() {
        return generation;
    }

    public Double getFitness() {
        return fitness;
    }

    public Integer getTimeRunningInSeconds() {
        return timeRunningInSeconds;
    }

    public Boolean getStopByGeneration() {
        return stopByGeneration;
    }

    public Boolean getStopByFitness() {
        return stopByFitness;
    }

    public Boolean getStopByTime() {
        return stopByTime;
    }

    public Integer getByGenerationStopValue() {
        return byGenerationStopValue;
    }

    public Integer getByFitnessStopValue() {
        return byFitnessStopValue;
    }

    public Integer getByTimeStopValue() {
        return byTimeStopValue;
    }
}
