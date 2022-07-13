package Main.Engine.Evolution;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Algorithm implements Runnable, Serializable {

    private AlgorithmManager manager;
    private SolutionFactory factory;
    private List<Solution> solutionList;
    private Integer generation;
    private List<StopCondition> stopConditions;
    private GenerationSolutionFitnessComplex bestSolutionComplex;
    private Map<InformationCarrier, Integer> ruleToPercentOfBestSolution;
    private Boolean keepTrackOfBestSolution;
    private Integer generationGapBetweenTracks;
    private ArrayList<GenerationSolutionFitnessComplex> bestSolutionComplexArray;
    private ArrayList<AlgorithmLog> logsArray;
    private Boolean writeLogs;
    private Integer generationJumpForLogs;
    private Boolean reachedStopCondition;
    private StopWatch stopWatch;


    public Algorithm(SolutionFactory factory, AlgorithmManager manager)
    {
        this.manager = manager;
        this.factory = factory;
        this.solutionList = new ArrayList<>();
        this.generation = 1;
        this.stopConditions = new ArrayList<>();

        for(int i = 0; i < manager.getPopulationSize(); ++i)
            solutionList.add(factory.createRandom(manager));

        this.bestSolutionComplex =
                new GenerationSolutionFitnessComplex(
                        generation,
                        solutionList.get(0).getDTOSolution(),
                        solutionList.get(0).calculateFitness(manager)
                );
        this.ruleToPercentOfBestSolution = new HashMap<>();
        this.keepTrackOfBestSolution = false;
        this.generationGapBetweenTracks = 0;
        this.bestSolutionComplexArray = new ArrayList<>();
        this.reachedStopCondition = false;
        this.stopWatch = new StopWatch();

        this.logsArray = new ArrayList<>();
        this.writeLogs = false;
        this.generationJumpForLogs = 0;

        sortSolutionsFast();

        updateBestSolutionFitnessAndRuleMap();
    }

    public synchronized void reset()
    {
        this.stopWatch.stopAndReset();
        this.generation = 1;
        this.solutionList = new ArrayList<>();

        for(int i = 0; i < manager.getPopulationSize(); ++i)
            solutionList.add(factory.createRandom(manager));

        sortSolutionsFast();

        updateBestSolutionFitnessAndRuleMap();
    }

    private synchronized void updateBestSolutionFitnessAndRuleMap()
    {
        Solution bestSolution = solutionList.get(0);                    // assume already sorted by fitness
        bestSolutionComplex = new GenerationSolutionFitnessComplex(
                generation,
                bestSolution.getDTOSolution(),
                bestSolution.calculateFitness(manager));
        manager.getRules().forEach(rule ->
                ruleToPercentOfBestSolution.put(manager.getDTORule(rule),
                        (int)(rule.getPercentOfEnforcement(bestSolution, manager,
                                manager.getRuleArguments(rule)) * 100)));

        if(keepTrackOfBestSolution && generation % generationGapBetweenTracks == 0)
        {
            bestSolutionComplexArray.add(bestSolutionComplex);
        }

        if(writeLogs && generation % generationJumpForLogs == 0)
        {
            logsArray.add(new AlgorithmLog(bestSolutionComplex.getGeneration(), bestSolutionComplex.getFitness()));
        }
    }

    public synchronized void WriteEveryXGenerationsToMap(int generationGap)
    {
        keepTrackOfBestSolution = true;
        generationGapBetweenTracks = generationGap;
    }

    public synchronized void stopWriteEveryXGenerationsToMap()
    {
        keepTrackOfBestSolution = false;
        bestSolutionComplexArray = new ArrayList<>();
    }

    public synchronized void WriteLogsEveryXGenerations(int generationGap)
    {
        writeLogs = true;
        generationJumpForLogs = generationGap;
    }

    public synchronized ArrayList<AlgorithmLog> readLogs()
    {
        return logsArray;
    }

    public synchronized boolean readIsKeepingLogs()
    {
        return writeLogs;
    }

    public synchronized ArrayList<GenerationSolutionFitnessComplex> readGenerationsSaved()
    {
        ArrayList<GenerationSolutionFitnessComplex> lst = new ArrayList<>(bestSolutionComplexArray);
        bestSolutionComplexArray = new ArrayList<>();
        return lst;
    }

    public synchronized void addStopConditions(List<StopCondition> stopConditions)
    {
        this.stopConditions.addAll(stopConditions);
    }

    public synchronized void addStopConditions(StopCondition stopCondition)
    {
        this.stopConditions.add(stopCondition);
    }

    public synchronized void removeStopCondition(StopCondition stopCondition)
    {
        this.stopConditions.remove(stopCondition);
    }

    public synchronized void clearStopConditions()
    {
        this.stopConditions.clear();
    }

    public synchronized final GenerationSolutionFitnessComplex readBestSolutionFitness()
    {
        return bestSolutionComplex;
    }

    public synchronized final Map<InformationCarrier, Integer> readRulesToEnforcementForBestSolution()
    {
        return ruleToPercentOfBestSolution;
    }

    public synchronized Pair<GenerationSolutionFitnessComplex, Map<InformationCarrier, Integer>>
            readBestSolutionFitnessAndItsRules()
    {
        return new Pair<>(bestSolutionComplex, ruleToPercentOfBestSolution);
    }

    public synchronized double readBestFitness()
    {
        return bestSolutionComplex.getFitness();
    }

    public synchronized InformationCarrier readGeneralInformation()
    {
        return manager.getDTOManager();
    }

    public synchronized final Integer readPopulationSize()
    {
        return manager.getPopulationSize();
    }

    public synchronized void setPopulationSize(int newPopulationSize)
    {
        manager.setPopulationSize(newPopulationSize);
    }

    public synchronized final Integer readGeneration()
    {
        return generation;
    }

    public synchronized final Boolean readGotToStopCondition()
    {
        return reachedStopCondition;
    }

    public synchronized final Boolean readHasAtLeastOneStopCondition()
    {
        return stopConditions.size() > 0;
    }

    public synchronized final long readTimeAlgorithmIsRunning(TimeUnit timeUnitToRead)
    {
        return stopWatch.getCurrentRead(timeUnitToRead);
    }

    public void setSelection(InformationCarrier dtoSelection)
    {
        manager.setSelection(dtoSelection);
    }

    public void setCrossover(InformationCarrier dtoCrossover)
    {
        manager.setCrossOver(dtoCrossover);
    }

    public void addMutation(InformationCarrier dtoMutation) {
        manager.addMutation(dtoMutation);
    }

    public void deleteMutation(InformationCarrier dtoMutation)
    {
        manager.deleteMutation(dtoMutation);
    }

    public void changeMutationProbability(InformationCarrier dtoMutation, Integer newProbability)
    {
        manager.changeMutationProbability(dtoMutation, newProbability);
    }

    private void sortSolutionsFast()
    {
        Map<Solution, Double> solutionToFitness = solutionList.stream()
                .collect(Collectors.toMap(
                        solution -> solution,
                        solution -> solution.calculateFitness(manager))
                );

        solutionList.sort(Comparator.comparingDouble(solutionToFitness::get));
        Collections.reverse(solutionList);
    }

    private void updateReachedStopCondition()
    {
        if(stopConditions.stream().anyMatch(stopCondition -> stopCondition.isConditionApplying(this)))
            reachedStopCondition = true;
    }

    public void run()
    {
        if(stopConditions.size() == 0)
            throw new RuntimeException(Algorithm.class.getName() + " has to have at least 1 stop condition to run");

        stopWatch.start();

        while(stopConditions.stream().noneMatch(stopCondition -> stopCondition.isConditionApplying(this)))
        {
            runOneGeneration();
            if(Thread.currentThread().isInterrupted())
            {
                break;
            }
        }

        stopWatch.stop();

        updateReachedStopCondition();
    }

    private void runOneGeneration()
    {
        // Solutions are sorted by fitness when you get here

        List<Solution> elitismSolutions = solutionList.stream()
                .limit(manager.getElitism())
                .map(solution -> factory.copy(solution))
                .collect(Collectors.toList());

        solutionList = manager.getSelection().doSelection(solutionList, manager, manager.getSelectionArguments());

        List<Solution> offSpringList = manager.getCrossOver()
                .doCrossOver(solutionList, manager.getPopulationSize() - manager.getElitism(), factory,
                        manager.getCrossOverCutSize(), manager.getCrossOverArguments());

        manager.getMutations()
                .forEach(mutation ->
                        mutation.doMutation(offSpringList, manager, manager.getMutationArguments(mutation)));
        solutionList = offSpringList;

        solutionList.addAll(elitismSolutions);

        this.generation += 1;

        sortSolutionsFast();

        updateBestSolutionFitnessAndRuleMap();
    }

    public void executeOneGeneration()
    {
        stopWatch.start();

        runOneGeneration();

        stopWatch.stop();

        updateReachedStopCondition();
    }
}
