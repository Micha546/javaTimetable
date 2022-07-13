package Main.HelperClasses.TableViewClasses;

import Main.Engine.Evolution.Rule;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableRules;

public class RuleView {

    private final String name;
    private final Rule.Severity severity;

    public RuleView(DTOTimeTableRules dtoTimeTableRule)
    {
        this.name = dtoTimeTableRule.getRuleName();
        this.severity = dtoTimeTableRule.getRuleSeverity();
    }

    public String getName() {
        return name;
    }

    public Rule.Severity getSeverity() {
        return severity;
    }
}
