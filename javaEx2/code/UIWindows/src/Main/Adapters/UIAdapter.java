package Main.Adapters;

import Main.Engine.Evolution.GenerationSolutionFitnessComplex;
import Main.HelperClasses.BestTimeTableComplex;
import Main.HelperClasses.GeneralAlgorithmInformation;
import Main.HelperClasses.GenerationFitnessTimeComplex;
import Main.HelperClasses.StopConditionProgressComplex;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private final Consumer<GenerationSolutionFitnessComplex> onGenerationChange;
    private final Runnable onAlgorithmStart;
    private final Runnable onAlgorithmPause;
    private final Runnable onAlgorithmStop;
    private final Consumer<GeneralAlgorithmInformation> onAlgorithmLoad;
    private final Consumer<StopConditionProgressComplex> onStopConditionChange;
    private final Consumer<GeneralAlgorithmInformation> onAlgorithmPropertyChange;
    private final Consumer<BestTimeTableComplex> onBestSolutionGet;
    private final Consumer<GenerationFitnessTimeComplex> onGenerationFitnessTimeUpdate;

    protected UIAdapter(Consumer<GenerationSolutionFitnessComplex> onGenerationChange,
                        Runnable onAlgorithmStart,
                        Runnable onAlgorithmPause,
                        Runnable onAlgorithmStop,
                        Consumer<GeneralAlgorithmInformation> onAlgorithmLoad,
                        Consumer<StopConditionProgressComplex> onStopConditionChange,
                        Consumer<GeneralAlgorithmInformation> onAlgorithmPropertyChange,
                        Consumer<BestTimeTableComplex> onBestSolutionGet,
                        Consumer<GenerationFitnessTimeComplex> onGenerationFitnessTimeUpdate)
    {
        this.onGenerationChange = onGenerationChange;
        this.onAlgorithmStart = onAlgorithmStart;
        this.onAlgorithmPause = onAlgorithmPause;
        this.onAlgorithmStop = onAlgorithmStop;
        this.onAlgorithmLoad = onAlgorithmLoad;
        this.onStopConditionChange = onStopConditionChange;
        this.onAlgorithmPropertyChange = onAlgorithmPropertyChange;
        this.onBestSolutionGet = onBestSolutionGet;
        this.onGenerationFitnessTimeUpdate = onGenerationFitnessTimeUpdate;
    }

    public void handleGenerationChange(GenerationSolutionFitnessComplex complex)
    {
        Platform.runLater(() -> onGenerationChange.accept(complex));
    }

    public void handleAlgorithmStart()
    {
        Platform.runLater(onAlgorithmStart);
    }

    public void handleAlgorithmPause()
    {
        Platform.runLater(onAlgorithmPause);
    }

    public void handleAlgorithmStop()
    {
        Platform.runLater(onAlgorithmStop);
    }

    public void handleAlgorithmLoad(GeneralAlgorithmInformation information)
    {
        Platform.runLater(() -> onAlgorithmLoad.accept(information));
    }

    public void handleGenerationStopConditionChange(StopConditionProgressComplex complex)
    {
        Platform.runLater(() -> onStopConditionChange.accept(complex));
    }

    public void handleAlgorithmPropertyChange(GeneralAlgorithmInformation information)
    {
        Platform.runLater(() -> onAlgorithmPropertyChange.accept(information));
    }

    public void handleOnBestSolutionGet(BestTimeTableComplex bestTimeTableComplex)
    {
        Platform.runLater(() -> onBestSolutionGet.accept(bestTimeTableComplex));
    }

    public void handleOnGenerationFitnessTimeUpdate(GenerationFitnessTimeComplex complex)
    {
        Platform.runLater(() -> onGenerationFitnessTimeUpdate.accept(complex));
    }
}
