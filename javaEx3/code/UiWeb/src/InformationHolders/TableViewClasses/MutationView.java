package InformationHolders.TableViewClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;

import java.util.List;

public class MutationView {
    private final String name;
    private final Integer chance;
    private final List<Object> arguments;
    private final List<String> argumentsDescription;

    public MutationView(DTOTimeTableMutation dtoTimeTableMutation)
    {
        this.name = dtoTimeTableMutation.getMutationName();
        this.chance = dtoTimeTableMutation.getProbability();
        this.arguments = dtoTimeTableMutation.getMutationArgs();
        this.argumentsDescription = dtoTimeTableMutation.getArgsDescriptionList();
    }

    public String getName() {
        return name;
    }

    public Integer getChance() {
        return chance;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public List<String> getArgumentsDescription() {
        return argumentsDescription;
    }
}
