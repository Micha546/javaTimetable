package Main.Engine.Evolution.TimeTableSolution;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.Rule;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.Rules.TimeTableRules;

import java.util.*;

public class TimeTableUtils {
    public static List<TimeTable> castListToTimeTable(List<Solution> solutionList)
    {
        List<TimeTable> timeTables = new ArrayList<>();

        for(Solution solution : solutionList)
            timeTables.add((TimeTable) solution);

        return timeTables;
    }

    public static Map<TimeTableRules, Rule.Severity> castMapToTimeTableRules(Map<Rule, Rule.Severity> rules)
    {
        Map<TimeTableRules, Rule.Severity> newMap = new HashMap<>();

        for(Map.Entry<Rule, Rule.Severity> entry : rules.entrySet())
            newMap.put((TimeTableRules) entry.getKey(), entry.getValue());

        return newMap;
    }

    public static void assertSolutionIsTimeTable(Solution solution, String callerClassName)
    {
        if(!(solution instanceof TimeTable))
            throw new RuntimeException(callerClassName +
                    "expects to get a TimeTable but got " + solution.getClass().getName());
    }

    public static void checkArgs(String name, Class<?>[] classes, Object... args)
    {
        if(classes.length != args.length)
            throw new RuntimeException(name + " expects to get " + classes.length + " additional arguments but got "
                    + args.length + " additional arguments");

        for(int i = 0; i < args.length; ++i)
            if(args[i].getClass() != classes[i])
                throw new RuntimeException(name + " expected to get " + classes[i].getName()
                        + " as the " + i + "th argument but got " + args[i].getClass() + " instead");
    }

    public static void assertManagerIsTimeTableManager(String callerName, AlgorithmManager manager)
    {
        if(!(manager instanceof TimeTableManager))
            throw new RuntimeException(callerName + " expected to get TimeTableManager but got "
                    + manager.getClass().getName());
    }

}
