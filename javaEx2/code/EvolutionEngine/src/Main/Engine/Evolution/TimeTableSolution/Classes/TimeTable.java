package Main.Engine.Evolution.TimeTableSolution.Classes;

import Main.Engine.Evolution.*;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTable;

import java.util.Set;
import java.util.stream.Collectors;

public class TimeTable implements Solution {

    private final Integer days;
    private final Integer hours;
    private Set<TimeTableTuple> tuples;

    protected TimeTable(Integer days, Integer hours, Set<TimeTableTuple> tuples) {
        this.days = days;
        this.hours = hours;
        this.tuples = tuples;
    }

    public Integer getDaysLimit()
    {
        return days;
    }

    public Integer getHoursLimit()
    {
        return hours;
    }

    public Set<TimeTableTuple> getAllTuplesOfDayHour(Integer day, Integer hour)
    {
        return tuples.stream().filter(tuple -> tuple.getDay().equals(day) && tuple.getHour().equals(hour))
                .collect(Collectors.toSet());
    }

    public Set<TimeTableTuple> getTuples() {
        return tuples;
    }

    @Override
    public double calculateFitness(AlgorithmManager manager)
    {
        int hardRuleNumber = (int) manager.getRules().stream()
                .filter(rule -> manager.getRuleSeverity(rule) == Rule.Severity.Hard)
                .count();
        int softRuleNumber = (int) manager.getRules().stream()
                .filter(rule -> manager.getRuleSeverity(rule) == Rule.Severity.Soft)
                .count();

        double sumOfEnforcementForHardRules = manager.getRules().stream()
                .filter(rule -> manager.getRuleSeverity(rule) == Rule.Severity.Hard)
                .mapToDouble(rule -> rule.getPercentOfEnforcement(this, manager , manager.getRuleArguments(rule)))
                .sum();

        double sumOfEnforcementForSoftRules = manager.getRules().stream()
                .filter(rule -> manager.getRuleSeverity(rule) == Rule.Severity.Soft)
                .mapToDouble(rule -> rule.getPercentOfEnforcement(this, manager , manager.getRuleArguments(rule)))
                .sum();

        if(hardRuleNumber == 0)
            return (sumOfEnforcementForSoftRules / (double) softRuleNumber) * 100;
        else if(softRuleNumber == 0)
            return (sumOfEnforcementForHardRules / (double) hardRuleNumber) * 100;
        else
            return ((sumOfEnforcementForHardRules / (double) hardRuleNumber) * manager.getHardRuleWeight() +
                    (sumOfEnforcementForSoftRules / (double) softRuleNumber) * (100 - manager.getHardRuleWeight()));
    }

    @Override
    public InformationCarrier getDTOSolution() {
        return new DTOTimeTable(this);
    }
}
