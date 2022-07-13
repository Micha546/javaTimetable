package Main.HelperClasses;

import Main.Engine.Evolution.GenerationSolutionFitnessComplex;
import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTable;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableRules;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BestTimeTableComplex {
    private final DTOTimeTable timeTable;
    private final Integer generation;
    private final Double fitness;
    private final List<RuleEnforcementComplex> ruleEnforcementComplexes;

    public BestTimeTableComplex(Pair<GenerationSolutionFitnessComplex, Map<InformationCarrier, Integer>> complexPair)
    {
        if(!(complexPair.getKey().getSolution() instanceof DTOTimeTable))
            throw new RuntimeException(this.getClass().getName() + " expected to get TimeTable but got " +
                    complexPair.getKey().getSolution().getClass().getName() + " instead");

        if(complexPair.getValue().keySet().stream()
                .anyMatch(informationCarrier -> !(informationCarrier instanceof DTOTimeTableRules)))
            throw new RuntimeException(this.getClass().getName() + " expected to get DTOTimeTableRules but got" +
                    " something else!");

        this.timeTable =  (DTOTimeTable) complexPair.getKey().getSolution();
        this.generation = complexPair.getKey().getGeneration();
        this.fitness = complexPair.getKey().getFitness();

        Map<DTOTimeTableRules, Integer> timeTableRulesEnforcementMap =
                complexPair.getValue().entrySet().stream().collect(Collectors.toMap(
                        entry -> (DTOTimeTableRules) entry.getKey(),
                        Map.Entry::getValue
                ));

        this.ruleEnforcementComplexes = timeTableRulesEnforcementMap.entrySet().stream()
                .map(entry -> new RuleEnforcementComplex(entry.getKey().getRuleName(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public DTOTimeTable getTimeTable() {
        return timeTable;
    }

    public Integer getGeneration() {
        return generation;
    }

    public Double getFitness() {
        return fitness;
    }

    public List<RuleEnforcementComplex> getRuleEnforcementComplexes() {
        return ruleEnforcementComplexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestTimeTableComplex that = (BestTimeTableComplex) o;
        return Objects.equals(timeTable, that.timeTable) && Objects.equals(generation, that.generation) && Objects.equals(fitness, that.fitness) && Objects.equals(ruleEnforcementComplexes, that.ruleEnforcementComplexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTable, generation, fitness, ruleEnforcementComplexes);
    }
}
