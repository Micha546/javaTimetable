package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.Rule;
import Main.Engine.Evolution.TimeTableSolution.Rules.TimeTableRules;

import java.util.List;
import java.util.Objects;

public class DTOTimeTableRules implements InformationCarrier {
    private final TimeTableRules timeTableRule;
    private final Rule.Severity severity;
    private final List<Object> argsList;
    private final Integer hardRuleWeight;

    public DTOTimeTableRules(TimeTableRules timeTableRule, Rule.Severity severity,
                             List<Object> argsList, Integer hardRuleWeight) {
        this.timeTableRule = timeTableRule;
        this.severity = severity;
        this.argsList = argsList;
        this.hardRuleWeight = hardRuleWeight;
    }

    public final String getRuleName()
    {
        return timeTableRule.name();
    }

    public final Rule.Severity getRuleSeverity() {
        return severity;
    }

    public final List<Object> getRuleArgumentsList() {
        return argsList;
    }

    public final Integer getHardRuleWeight() {
        return hardRuleWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOTimeTableRules that = (DTOTimeTableRules) o;
        return timeTableRule == that.timeTableRule && severity == that.severity && Objects.equals(argsList, that.argsList) && Objects.equals(hardRuleWeight, that.hardRuleWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTableRule, severity, argsList, hardRuleWeight);
    }
}
