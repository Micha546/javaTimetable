package Main.Engine.Evolution.TimeTableSolution.Selections;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.Selection;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;

import java.util.*;
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
    },
    RouletteWheel{
        @Override
        public List<Solution> doSelection(List<Solution> populationSortedByFitness, AlgorithmManager manager, Object... args) {
            TimeTableUtils.checkArgs(this.name(), new Class[] {}, args);

            TreeMap<Integer, Solution> weightToSolution = new TreeMap<>();
            int weightSum = 0;

            for(Solution solution : populationSortedByFitness)
            {
                weightSum += (int) solution.calculateFitness(manager);
                weightToSolution.put(weightSum, solution);
            }

            Random rand = new Random();
            List<Solution> chosenParents = new ArrayList<>();

            for(int i = 0; i < populationSortedByFitness.size(); ++i)
            {
                chosenParents.add(weightToSolution.higherEntry(rand.nextInt(weightSum)).getValue());
            }

            return chosenParents;
        }
    },
    Tournament {
        @Override
        public List<Solution> doSelection(List<Solution> populationSortedByFitness, AlgorithmManager manager, Object... args) {
            TimeTableUtils.checkArgs(this.name(), new Class[] {Double.class}, args);

            Double pte = (Double) args[0];

            Map<Solution, Double> solutionFitnessMap = populationSortedByFitness.stream().collect(Collectors.toMap(
                    solution -> solution,
                    solution -> solution.calculateFitness(manager)
            ));

            Random rand = new Random();
            List<Solution> chosenParents = new ArrayList<>();

            for(int i = 0; i < populationSortedByFitness.size(); ++i)
            {
                Solution contestant1 = populationSortedByFitness.get(rand.nextInt(populationSortedByFitness.size()));
                Solution contestant2 = populationSortedByFitness.get(rand.nextInt(populationSortedByFitness.size()));
                Solution winner;

                double randomPteValue = rand.nextDouble();

                if(randomPteValue >= pte)
                {
                    winner = solutionFitnessMap.get(contestant1) > solutionFitnessMap.get(contestant2)
                            ? contestant1 : contestant2;
                }
                else
                {
                    winner = solutionFitnessMap.get(contestant1) < solutionFitnessMap.get(contestant2)
                            ? contestant1 : contestant2;
                }

                chosenParents.add(winner);
            }

            return chosenParents;
        }
    };
}
