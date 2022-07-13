package Main.Logic;

import Main.Adapters.UIAdapter;
import Main.Engine.Evolution.Algorithm;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableCrossOver;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableSelection;
import Main.HelperClasses.BestTimeTableComplex;
import Main.HelperClasses.GeneralAlgorithmInformation;
import Main.HelperClasses.GenerationFitnessTimeComplex;
import Main.HelperClasses.StopConditionProgressComplex;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainLogic implements Runnable {

    private enum AlgorithmStatus {
        Running, Paused, Stopped, NotLoaded
    }

    public enum StopConditionType {
        byGeneration, byFitness, byTime
    }

    private ArrayList<Runnable> commands;
    private final UIAdapter adapter;
    private Algorithm algorithm;
    private AlgorithmStatus algorithmStatus;
    private Boolean resetFlag;

    private boolean stopByGenerationFlag;
    private int stopByGenerationValue;
    private boolean stopByFitnessFlag;
    private int stopByFitnessValue;
    private boolean stopByTimeFlag;
    private long stopByTimeValue;

    public MainLogic(UIAdapter adapter)
    {
        this.commands = new ArrayList<>();
        this.adapter = adapter;
        this.algorithm = null;
        this.algorithmStatus = AlgorithmStatus.NotLoaded;
        this.resetFlag = false;

        this.stopByGenerationFlag = false;
        this.stopByGenerationValue = 0;
        this.stopByFitnessFlag = false;
        this.stopByFitnessValue = 0;
        this.stopByTimeFlag = false;
        this.stopByTimeValue = 0;
    }

    public synchronized void pushCommand(Runnable command)
    {
        commands.add(command);
    }

    private synchronized ArrayList<Runnable> getCommands()
    {
        ArrayList<Runnable> newCommands = commands;
        commands = new ArrayList<>();
        return newCommands;
    }

    public void commandSetAlgorithm(Algorithm algorithm)
    {
        this.algorithm = algorithm;
        algorithm.WriteEveryXGenerationsToMap(1);

        resetFlag = false;
        algorithmStatus = AlgorithmStatus.Paused;
        adapter.handleAlgorithmPause();

        adapter.handleAlgorithmLoad(new GeneralAlgorithmInformation(
                algorithm.readGeneralInformation(),
                algorithm.readGeneration(),
                algorithm.readPopulationSize()));

        updateGenerationFitnessTime();
        updateBestSolution();
    }

    public void commandStartAlgorithm()
    {
        algorithmStatus = AlgorithmStatus.Running;
        adapter.handleAlgorithmStart();
    }

    public void commandPauseAlgorithm()
    {
        algorithmStatus = AlgorithmStatus.Paused;
        adapter.handleAlgorithmPause();
    }

    public void commandStopAlgorithm()
    {
        resetFlag = true;
        algorithmStatus = AlgorithmStatus.Stopped;
        adapter.handleAlgorithmStop();
    }

    public void commandSetStopConditionFlag(StopConditionType stopConditionType, boolean newFlag)
    {
        switch (stopConditionType)
        {
            case byGeneration:
                stopByGenerationFlag = newFlag;
                break;
            case byFitness:
                stopByFitnessFlag = newFlag;
                break;
            case byTime:
                stopByTimeFlag = newFlag;
                break;
        }
    }

    public void commandSetStopByGenerationValue(StopConditionType stopConditionType, int value)
    {
        switch (stopConditionType)
        {
            case byGeneration:
                stopByGenerationValue = value;
                break;
            case byFitness:
                stopByFitnessValue = value;
                break;
            case byTime:
                stopByTimeValue = value;
                break;
        }
    }

    public void commandSetSelection(DTOTimeTableSelection dtoTimeTableSelection)
    {
        algorithm.setSelection(dtoTimeTableSelection);
        adapter.handleAlgorithmPropertyChange(new GeneralAlgorithmInformation(
                algorithm.readGeneralInformation(),
                algorithm.readGeneration(),
                algorithm.readPopulationSize()));
    }

    public void commandSetCrossover(DTOTimeTableCrossOver dtoTimeTableCrossOver)
    {
        algorithm.setCrossover(dtoTimeTableCrossOver);
        adapter.handleAlgorithmPropertyChange(new GeneralAlgorithmInformation(
                algorithm.readGeneralInformation(),
                algorithm.readGeneration(),
                algorithm.readPopulationSize()));
    }

    public void commandAddMutation(DTOTimeTableMutation mutation)
    {
        algorithm.addMutation(mutation);
        adapter.handleAlgorithmPropertyChange(new GeneralAlgorithmInformation(
                algorithm.readGeneralInformation(),
                algorithm.readGeneration(),
                algorithm.readPopulationSize()));
    }

    public void commandDeleteMutation(DTOTimeTableMutation mutation)
    {
        algorithm.deleteMutation(mutation);
        adapter.handleAlgorithmPropertyChange(new GeneralAlgorithmInformation(
                algorithm.readGeneralInformation(),
                algorithm.readGeneration(),
                algorithm.readPopulationSize()));
    }

    public void commandSetMutationProbability(DTOTimeTableMutation mutation, Integer newProbability)
    {
        algorithm.changeMutationProbability(mutation, newProbability);
        adapter.handleAlgorithmPropertyChange(new GeneralAlgorithmInformation(
                algorithm.readGeneralInformation(),
                algorithm.readGeneration(),
                algorithm.readPopulationSize()));
    }

    public void commandSave(String savePath)
    {
        if(algorithmStatus != AlgorithmStatus.NotLoaded)
        {
            try{
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(savePath));

                out.writeObject(algorithm);
                out.flush();
                out.close();
            }
            catch (Exception ignored) {}
        }
    }

    @Override
    public void run() {

        while(true)
        {
            if(algorithmStatus == AlgorithmStatus.Running && reachedStopCondition() && !resetFlag)
                commandPauseAlgorithm();

            switch(algorithmStatus)
            {
                case Running:
                    if(resetFlag)
                    {
                        algorithm.reset();
                        resetFlag = false;
                        updateBestSolution();
                    }

                    runOneGenerationAndUpdateUI();
                    break;
                case Paused:
                case Stopped:
                case NotLoaded:
                    sleepForAWhile(100);
                    break;
            }

            executeCommands();
        }
    }

    private void updateStopConditions()
    {
        if(stopByGenerationFlag)
        {
            adapter.handleGenerationStopConditionChange(stopByGenerationValue == 0
                    ? new StopConditionProgressComplex(StopConditionType.byGeneration, 1d)
                    : new StopConditionProgressComplex(StopConditionType.byGeneration,
                    (double) algorithm.readGeneration() / (double) stopByGenerationValue));
        }

        if(stopByFitnessFlag)
        {
            adapter.handleGenerationStopConditionChange(stopByFitnessValue == 0
                    ? new StopConditionProgressComplex(StopConditionType.byFitness, 1d)
                    : new StopConditionProgressComplex(StopConditionType.byFitness,
                    algorithm.readBestFitness() / (double) stopByFitnessValue));
        }

        if(stopByTimeFlag)
        {
            adapter.handleGenerationStopConditionChange(stopByTimeValue == 0
                    ? new StopConditionProgressComplex(StopConditionType.byTime, 1d)
                    : new StopConditionProgressComplex(StopConditionType.byTime,
                    algorithm.readTimeAlgorithmIsRunning(TimeUnit.SECONDS) / (double) (stopByTimeValue*60)));
        }
    }

    private void updateGenerationFitnessTime()
    {
        adapter.handleOnGenerationFitnessTimeUpdate(new GenerationFitnessTimeComplex(
                algorithm.readGeneration(),
                algorithm.readBestFitness(),
                algorithm.readTimeAlgorithmIsRunning(TimeUnit.MILLISECONDS)
        ));
    }

    private void updateBestSolution()
    {
        adapter.handleOnBestSolutionGet(new BestTimeTableComplex(algorithm.readBestSolutionFitnessAndItsRules()));
    }

    private boolean reachedStopCondition()
    {
        boolean stopRunning = false;

        if(stopByGenerationFlag && algorithm.readGeneration() >= stopByGenerationValue)
            stopRunning = true;

        if(stopByFitnessFlag && algorithm.readBestFitness() >= stopByFitnessValue)
            stopRunning = true;

        if(stopByTimeFlag && algorithm.readTimeAlgorithmIsRunning(TimeUnit.MINUTES) >= stopByTimeValue)
            stopRunning = true;

        return stopRunning;
    }

    private void runOneGenerationAndUpdateUI()
    {
        algorithm.executeOneGeneration();
        algorithm.readGenerationsSaved().forEach(adapter::handleGenerationChange);
        updateStopConditions();
        updateGenerationFitnessTime();
        updateBestSolution();
    }

    private void executeCommands()
    {
        List<Runnable> newCommands = getCommands();

        for(Runnable command : newCommands)
        {
            command.run();
        }
    }


    private void sleepForAWhile(long millisToSleep)
    {
        try{
            Thread.sleep(millisToSleep);
        }
        catch (InterruptedException ignored) {}
    }

}
