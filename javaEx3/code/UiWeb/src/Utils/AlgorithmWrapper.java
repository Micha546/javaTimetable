package Utils;

import InformationHolders.AlgorithmInformationWebVersion;
import InformationHolders.TableViewClasses.RuleView;
import Main.Engine.Evolution.Algorithm;
import Main.Engine.Evolution.GenerationSolutionFitnessComplex;
import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.StopCondition;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableFactory;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.DTO.*;
import Main.Engine.Xml.AutoGenerated.ETTDescriptor;
import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AlgorithmWrapper {

    public enum Status {
        Running, Paused, Stopped
    }

    public enum AlgorithmStopCondition {
        ByGeneration, ByFitness, ByTime;
    }

    private int byGenerationValue;
    private boolean byGenerationIsActive = false;
    private StopCondition byGenerationFunction;
    private int byFitnessValue;
    private boolean byFitnessIsActive = false;
    private StopCondition byFitnessFunction;
    private int byTimeValue;
    private boolean byTimeIsActive = false;
    private StopCondition byTimeFunction;

    private final Object populationSizeKey = new Object();
    private final Object mutationKey = new Object();
    private final Object selectionKey = new Object();
    private final Object crossoverKey = new Object();
    private final Object statusKey = new Object();
    private final Object stopConditionKey = new Object();

    private final Algorithm algorithm;
    private Status status;
    private Thread thread;

    public AlgorithmWrapper(ETTDescriptor descriptor)
    {
        this.algorithm = new Algorithm(new TimeTableFactory(), new TimeTableManager(descriptor));
        this.status = Status.Paused;

        byGenerationValue = 100;
        byFitnessValue = 100;
        byTimeValue = 1;
    }

    public synchronized AlgorithmInformationWebVersion getAlgorithmInformation()
    {
        Pair<GenerationSolutionFitnessComplex, Map<InformationCarrier, Integer>> bigComplex =
                algorithm.readBestSolutionFitnessAndItsRules();

        GenerationSolutionFitnessComplex complex = bigComplex.getKey();

        Map<String, Integer> ruleNameIntegerMap = bigComplex.getValue().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ((DTOTimeTableRules) entry.getKey()).getRuleName(), Map.Entry::getValue
                ));

        return new AlgorithmInformationWebVersion(
                (DTOTimeTableManager) algorithm.readGeneralInformation(),
                (DTOTimeTable) complex.getSolution(),
                ruleNameIntegerMap,
                complex.getGeneration(),
                complex.getFitness(),
                (int) algorithm.readTimeAlgorithmIsRunning(TimeUnit.SECONDS),
                status,
                byGenerationIsActive,
                byFitnessIsActive,
                byTimeIsActive,
                byGenerationValue,
                byFitnessValue,
                byTimeValue
        );
    }

    public void addMutation(DTOTimeTableMutation dtoTimeTableMutation)
    {
        synchronized (mutationKey)
        {
            algorithm.addMutation(dtoTimeTableMutation);
        }
    }

    public void deleteMutation(DTOTimeTableMutation dtoTimeTableMutation)
    {
        synchronized (mutationKey)
        {
            algorithm.deleteMutation(dtoTimeTableMutation);
        }
    }

    public void changeSelection(DTOTimeTableSelection dtoTimeTableSelection)
    {
        synchronized (selectionKey)
        {
            algorithm.setSelection(dtoTimeTableSelection);
        }
    }

    public void changeCrossover(DTOTimeTableCrossOver dtoTimeTableCrossOver)
    {
        synchronized (crossoverKey)
        {
            algorithm.setCrossover(dtoTimeTableCrossOver);
        }
    }

    public void changePopulationSize(int newPopulationSize)
    {
        synchronized (populationSizeKey)
        {
            algorithm.setPopulationSize(newPopulationSize);
        }
    }

    public void changeStatus(Status newStatus)
    {
        synchronized (statusKey)
        {
            switch(newStatus)
            {
                case Running:
                    handleRunThread();
                    break;
                case Paused:
                    handlePauseThread();
                    break;
                case Stopped:
                    handleStopThread();
                    break;
            }
        }
    }

    public AlgorithmWrapper.Status getStatus()
    {
        synchronized (statusKey)
        {
            return status;
        }
    }

    private void handleRunThread()
    {
        if(status != Status.Running)
        {
            if(status == Status.Stopped)
                algorithm.reset();

            startAlgorithm();
        }

        this.status = Status.Running;
    }

    private void handlePauseThread()
    {
        this.status = Status.Paused;
        stopAlgorithm();
    }

    private void handleStopThread()
    {
        this.status = Status.Stopped;
        stopAlgorithm();
    }

    private void startAlgorithm()
    {
        thread = new Thread(algorithm);
        thread.start();
    }

    private void stopAlgorithm()
    {
        thread.interrupt();
    }

    public void addStopCondition(AlgorithmStopCondition stopCondition, int value)
    {
        synchronized (stopConditionKey)
        {
            switch (stopCondition)
            {
                case ByGeneration:
                    byGenerationIsActive = true;
                    byGenerationValue = value;
                    updateStopCondition(AlgorithmStopCondition.ByGeneration,
                            algorithm -> algorithm.readGeneration() >= byGenerationValue);
                    break;
                case ByFitness:
                    byFitnessIsActive = true;
                    byFitnessValue = value;
                    updateStopCondition(AlgorithmStopCondition.ByFitness,
                            algorithm -> algorithm.readBestFitness() >= byFitnessValue);
                    break;
                case ByTime:
                    byTimeIsActive = true;
                    byTimeValue = value;
                    updateStopCondition(AlgorithmStopCondition.ByTime,
                            algorithm -> algorithm.readTimeAlgorithmIsRunning(TimeUnit.MINUTES) >= byTimeValue);
                    break;
            }
        }
    }

    private void updateStopCondition(AlgorithmStopCondition stopCondition, StopCondition newFunction)
    {
        switch (stopCondition)
        {
            case ByGeneration:
                algorithm.removeStopCondition(byGenerationFunction);
                byGenerationFunction = newFunction;
                algorithm.addStopConditions(byGenerationFunction);
                break;
            case ByFitness:
                algorithm.removeStopCondition(byFitnessFunction);
                byFitnessFunction = newFunction;
                algorithm.addStopConditions(byFitnessFunction);
                break;
            case ByTime:
                algorithm.removeStopCondition(byTimeFunction);
                byTimeFunction = newFunction;
                algorithm.addStopConditions(byTimeFunction);
                break;
        }
    }

    public void removeStopCondition(AlgorithmStopCondition stopCondition)
    {
        synchronized (stopConditionKey)
        {
            switch(stopCondition)
            {
                case ByGeneration:
                    byGenerationIsActive = false;
                    algorithm.removeStopCondition(byGenerationFunction);
                    break;
                case ByFitness:
                    byFitnessIsActive = false;
                    algorithm.removeStopCondition(byFitnessFunction);
                    break;
                case ByTime:
                    byTimeIsActive = false;
                    algorithm.removeStopCondition(byTimeFunction);
                    break;
            }
        }
    }

}
