package Main.Controllers;

import Main.Logic.MainLogic;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.function.UnaryOperator;

public class AlgorithmOutputController {

    @FXML private LineChart<String, Double> fitnessGenerationLineChart;
    @FXML private CheckBox byGenerationCheckBox;
    @FXML private CheckBox byFitnessCheckBox;
    @FXML private CheckBox byTimeCheckBox;
    @FXML private Spinner<Integer> byGenerationSpinner;
    @FXML private Spinner<Integer> byFitnessSpinner;
    @FXML private Spinner<Integer> byTimeSpinner;
    @FXML private VBox byGenerationProgressBarVBox;
    @FXML private ProgressBar byGenerationProgressBar;
    @FXML private VBox byFitnessProgressBarVBox;
    @FXML private ProgressBar byFitnessProgressBar;
    @FXML private VBox byTimeProgressBarVBox;
    @FXML private ProgressBar byTimeProgressBar;

    private final XYChart.Series<String, Double> series = new XYChart.Series<>();
    private final DoubleProperty generationProgress = new SimpleDoubleProperty(0);
    private final DoubleProperty fitnessProgress = new SimpleDoubleProperty(0);
    private final DoubleProperty timeProgress = new SimpleDoubleProperty(0);

    private MainController mainController;

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
        bindCheckBoxes();
    }

    @FXML
    public void initialize() {
        bindProgressBarsWidth();
        bindProgressBarSpinnerDisabled();
        bindProgressBars();
        initSpinners();
        initLineChart();
        setChangedSpinnerValueEvents();
    }

    private void bindProgressBarsWidth()
    {
        byGenerationProgressBar.prefWidthProperty().bind(byGenerationProgressBarVBox.widthProperty().subtract(20));
        byFitnessProgressBar.prefWidthProperty().bind(byFitnessProgressBarVBox.widthProperty().subtract(20));
        byTimeProgressBar.prefWidthProperty().bind(byTimeProgressBarVBox.widthProperty().subtract(20));
    }

    private void bindProgressBarSpinnerDisabled()
    {
        byGenerationProgressBar.disableProperty().bind(byGenerationCheckBox.selectedProperty().not());
        byGenerationSpinner.disableProperty().bind(byGenerationCheckBox.selectedProperty().not());

        byFitnessProgressBar.disableProperty().bind(byFitnessCheckBox.selectedProperty().not());
        byFitnessSpinner.disableProperty().bind(byFitnessCheckBox.selectedProperty().not());

        byTimeProgressBar.disableProperty().bind(byTimeCheckBox.selectedProperty().not());
        byTimeSpinner.disableProperty().bind(byTimeCheckBox.selectedProperty().not());
    }

    private void bindProgressBars()
    {
        byGenerationProgressBar.progressProperty().bind(generationProgress);
        byFitnessProgressBar.progressProperty().bind(fitnessProgress);
        byTimeProgressBar.progressProperty().bind(timeProgress);
    }

    private void bindCheckBoxes()
    {
        byGenerationCheckBox.disableProperty().bind(mainController.isAlgorithmPausedProperty().not()
                .and(mainController.isAlgorithmStoppedProperty().not()));
        byFitnessCheckBox.disableProperty().bind(mainController.isAlgorithmPausedProperty().not()
                .and(mainController.isAlgorithmStoppedProperty().not()));
        byTimeCheckBox.disableProperty().bind(mainController.isAlgorithmPausedProperty().not()
                .and(mainController.isAlgorithmStoppedProperty().not()));
    }

    private void initSpinners()
    {
        // My code

        /*
        byGenerationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        byFitnessSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        byTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        UnaryOperator<TextFormatter.Change> filterIntegers = (change) -> {
            try{
                Integer.parseInt(change.getControlNewText());
            }
            catch (NumberFormatException ex)
            {
                if(change.getAnchor() != 0)
                {
                    change.setAnchor(change.getAnchor() - 1);
                }

                change.setText("0");
            }
            return change;
        };

        byGenerationSpinner.getEditor().setTextFormatter(new TextFormatter<>(filterIntegers));
        byFitnessSpinner.getEditor().setTextFormatter(new TextFormatter<>(filterIntegers));
        byTimeSpinner.getEditor().setTextFormatter(new TextFormatter<>(filterIntegers));
        */


        // StackOverflow magic

        SpinnerValueFactory<Integer> generationSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        SpinnerValueFactory<Integer> fitnessSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        SpinnerValueFactory<Integer> timeSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000);

        byGenerationSpinner.setValueFactory(generationSpinnerFactory);
        byFitnessSpinner.setValueFactory(fitnessSpinnerFactory);
        byTimeSpinner.setValueFactory(timeSpinnerFactory);

        TextFormatter<Integer> generationSpinnerFormatter
                = new TextFormatter<>(generationSpinnerFactory.getConverter(), generationSpinnerFactory.getValue());
        TextFormatter<Integer> fitnessSpinnerFormatter
                = new TextFormatter<>(fitnessSpinnerFactory.getConverter(), fitnessSpinnerFactory.getValue());
        TextFormatter<Integer> timeSpinnerFormatter
                = new TextFormatter<>(timeSpinnerFactory.getConverter(), timeSpinnerFactory.getValue());

        byGenerationSpinner.getEditor().setTextFormatter(generationSpinnerFormatter);
        byFitnessSpinner.getEditor().setTextFormatter(fitnessSpinnerFormatter);
        byTimeSpinner.getEditor().setTextFormatter(timeSpinnerFormatter);

        generationSpinnerFactory.valueProperty().bindBidirectional(generationSpinnerFormatter.valueProperty());
        fitnessSpinnerFactory.valueProperty().bindBidirectional(fitnessSpinnerFormatter.valueProperty());
        timeSpinnerFactory.valueProperty().bindBidirectional(timeSpinnerFormatter.valueProperty());
    }

    private void initLineChart()
    {
        fitnessGenerationLineChart.setCreateSymbols(false);
        fitnessGenerationLineChart.getData().add(series);
    }

    public void addPointToChart(Integer generationValue, Double fitnessValue)
    {
        /*if(series.getData().size() >= 15)
            series.getData().remove(0);*/

        series.getData().add(new XYChart.Data<>(Integer.toString(generationValue), fitnessValue));
    }

    public void clearChart()
    {
        series.getData().clear();
    }

    public void clearFields()
    {
        series.getData().clear();
        byGenerationCheckBox.selectedProperty().set(false);
        byFitnessCheckBox.selectedProperty().set(false);
        byTimeCheckBox.selectedProperty().set(false);

        mainController.getLogic().pushCommand(() -> {
            mainController.getLogic().commandSetStopConditionFlag(
                    MainLogic.StopConditionType.byGeneration, byGenerationCheckBox.isSelected()
            );

            mainController.getLogic().commandSetStopConditionFlag(
                    MainLogic.StopConditionType.byFitness, byGenerationCheckBox.isSelected()
            );

            mainController.getLogic().commandSetStopConditionFlag(
                    MainLogic.StopConditionType.byTime, byGenerationCheckBox.isSelected()
            );
        });

        byGenerationSpinner.getValueFactory().setValue(0);
        byFitnessSpinner.getValueFactory().setValue(0);
        byTimeSpinner.getValueFactory().setValue(0);
        generationProgress.set(0);
        fitnessProgress.set(0);
        timeProgress.set(0);
    }

    public boolean areAllCheckBoxesUnchecked()
    {
        return !byGenerationCheckBox.isSelected() && !byFitnessCheckBox.isSelected() && !byTimeCheckBox.isSelected();
    }

    @FXML
    void clickedByGeneration(ActionEvent event) {
        mainController.getLogic().pushCommand(() ->
                mainController.getLogic().commandSetStopConditionFlag(
                        MainLogic.StopConditionType.byGeneration, byGenerationCheckBox.isSelected()
                )
        );
    }

    @FXML
    void clickedByFitness(ActionEvent event) {
        mainController.getLogic().pushCommand(() ->
            mainController.getLogic().commandSetStopConditionFlag(
                    MainLogic.StopConditionType.byFitness, byFitnessCheckBox.isSelected()
            )
        );
    }

    @FXML
    void clickedByTime(ActionEvent event) {
        mainController.getLogic().pushCommand(() ->
                mainController.getLogic().commandSetStopConditionFlag(
                        MainLogic.StopConditionType.byTime, byTimeCheckBox.isSelected()
                )
        );
    }

    private void setChangedSpinnerValueEvents()
    {
        byGenerationSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
            mainController.getLogic().pushCommand(() ->
                    mainController.getLogic().commandSetStopByGenerationValue(
                            MainLogic.StopConditionType.byGeneration, newValue
                    )
            )
        );

        byFitnessSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
                mainController.getLogic().pushCommand(() ->
                        mainController.getLogic().commandSetStopByGenerationValue(
                                MainLogic.StopConditionType.byFitness, newValue
                        )
                )
        );

        byTimeSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
                mainController.getLogic().pushCommand(() ->
                        mainController.getLogic().commandSetStopByGenerationValue(
                                MainLogic.StopConditionType.byTime, newValue
                        )
                )
        );
    }

    // getters for all properties and sub controllers

    public DoubleProperty generationProgressProperty() {
        return generationProgress;
    }

    public DoubleProperty fitnessProgressProperty() {
        return fitnessProgress;
    }

    public DoubleProperty timeProgressProperty() {
        return timeProgress;
    }
}
