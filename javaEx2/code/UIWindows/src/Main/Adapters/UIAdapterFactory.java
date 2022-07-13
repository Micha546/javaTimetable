package Main.Adapters;

import Main.Controllers.MainController;
import Main.Engine.Evolution.GenerationSolutionFitnessComplex;
import Main.HelperClasses.BestTimeTableComplex;
import Main.HelperClasses.GeneralAlgorithmInformation;
import Main.HelperClasses.GenerationFitnessTimeComplex;
import Main.HelperClasses.StopConditionProgressComplex;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class UIAdapterFactory {

    MainController controller;

    public UIAdapterFactory(MainController controller)
    {
        this.controller = controller;
    }

    public UIAdapter create()
    {
        return new UIAdapter(
                createOnGenerationChange(),
                createOnAlgorithmStart(),
                createOnAlgorithmPause(),
                createOnAlgorithmStop(),
                createOnAlgorithmLoad(),
                createOnStopConditionChange(),
                createOnAlgorithmPropertyChange(),
                createOnBestSolutionGet(),
                createOnGenerationFitnessTimeUpdate()
        );
    }

    private Consumer<GenerationSolutionFitnessComplex> createOnGenerationChange()
    {
        return complex -> {
            controller.getAlgorithmInfoController().generationLabelTextProperty().set(complex.getGeneration().toString());

            if(complex.getGeneration() == 1 || complex.getGeneration() % 20 == 0)
                controller.getAlgorithmOutputController().addPointToChart(complex.getGeneration(), complex.getFitness());
        };
    }

    private Runnable createOnAlgorithmStart()
    {
        return () -> {
            if(controller.isAlgorithmStoppedProperty().get())
            {
                controller.getAlgorithmOutputController().clearChart();
                controller.getBestSolutionController().onRunAfterStop();
            }

            controller.isAlgorithmRunningProperty().set(true);
            controller.isAlgorithmPausedProperty().set(false);
            controller.isAlgorithmStoppedProperty().set(false);
            controller.isAlgorithmLoadedProperty().set(true);
        };
    }

    private Runnable createOnAlgorithmPause()
    {
        return () -> {
            controller.isAlgorithmRunningProperty().set(false);
            controller.isAlgorithmPausedProperty().set(true);
            controller.isAlgorithmStoppedProperty().set(false);
            controller.isAlgorithmLoadedProperty().set(true);
        };
    }

    private Runnable createOnAlgorithmStop()
    {
        return () -> {
            controller.isAlgorithmRunningProperty().set(false);
            controller.isAlgorithmPausedProperty().set(false);
            controller.isAlgorithmStoppedProperty().set(true);
            controller.isAlgorithmLoadedProperty().set(true);
        };
    }

    private Consumer<GeneralAlgorithmInformation> createOnAlgorithmLoad()
    {
        return (information -> {
            controller.getAlgorithmInfoController().writeInformation(information);
            controller.getAlgorithmOutputController().clearFields();
            controller.getAlgorithmPropertiesController().writeInformation(information);
            controller.getBestSolutionController().onLoad(information);
        });
    }

    private Consumer<StopConditionProgressComplex> createOnStopConditionChange()
    {
        return complex -> {

            double newValue = complex.getNewProgressValue();

            switch (complex.getStopConditionType())
            {
                case byGeneration:
                    if(controller.getAlgorithmOutputController().generationProgressProperty().get() != newValue)
                        controller.getAlgorithmOutputController().generationProgressProperty().set(newValue);
                    break;
                case byFitness:
                    if(controller.getAlgorithmOutputController().fitnessProgressProperty().get() != newValue)
                        controller.getAlgorithmOutputController().fitnessProgressProperty().set(newValue);
                    break;
                case byTime:
                    if(controller.getAlgorithmOutputController().timeProgressProperty().get() != newValue)
                        controller.getAlgorithmOutputController().timeProgressProperty().set(newValue);
                    break;
            }
        };
    }

    private Consumer<GeneralAlgorithmInformation> createOnAlgorithmPropertyChange()
    {
        return information -> controller.getAlgorithmInfoController().writeInformation(information);
    }

    private Consumer<BestTimeTableComplex> createOnBestSolutionGet()
    {
        return bestSolution -> controller.getBestSolutionController().pushNewGenerationToList(bestSolution);
    }

    private Consumer<GenerationFitnessTimeComplex> createOnGenerationFitnessTimeUpdate()
    {
        return complex -> {
            controller.generationNumberProperty().set(complex.getGeneration());
            controller.fitnessScoreProperty().set(complex.getFitness());
            controller.timePassedInSecondsProperty().set(complex.getTimeRunning(TimeUnit.SECONDS));
        };
    }
}
