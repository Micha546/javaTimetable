package Main.Engine.Evolution.TimeTableSolution.Selections;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.Selection;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum TimeTableSelections implements Selection {
    Truncation{
        @Override
        public List<Solution> doSelection(List<Solution> populationSortedByFitness, AlgorithmManager manager, Object... args) {
            TimeTableUtils.checkArgs(this.name(), new Class[] {Integer.class}, args);
            if(populationSortedByFitness.stream().anyMatch(solution -> !(solution instanceof TimeTable)))
                throw new RuntimeException(this.name() + " expects to get a list of TimeTable objects");

            int limitSize = (int)((Integer)args[0] * 0.01 * populationSortedByFitness.size());

            return populationSortedByFitness.stream()
                    .limit(limitSize)
                    .collect(Collectors.toList());
        }
    }
    ;


}
