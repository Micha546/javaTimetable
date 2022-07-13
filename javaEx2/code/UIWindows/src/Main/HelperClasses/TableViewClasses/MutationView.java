package Main.HelperClasses.TableViewClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;

import java.util.List;
import java.util.Objects;

public class MutationView {
    private final String name;
    private final Integer chance;
    private final List<String> arguments;

    public MutationView(DTOTimeTableMutation dtoTimeTableMutation)
    {
        this.name = dtoTimeTableMutation.getMutationName();
        this.chance = dtoTimeTableMutation.getProbability();
        this.arguments = dtoTimeTableMutation.getArgsDescriptionList();
    }

    public String getName() {
        return name;
    }

    public Integer getChance() {
        return chance;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
