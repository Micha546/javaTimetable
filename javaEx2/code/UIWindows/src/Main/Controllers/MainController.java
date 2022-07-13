package Main.Controllers;

import Main.Adapters.UIAdapterFactory;
import Main.Engine.Evolution.Algorithm;
import Main.Logic.FileLoadingLogic;
import Main.Logic.FileSavingLogic;
import Main.Logic.FileType;
import Main.Logic.MainLogic;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.net.URL;

public class MainController {

    private MainLogic logic;
    private Thread logicThread;
    private AlgorithmInfoController algorithmInfoController;
    private AlgorithmOutputController algorithmOutputController;
    private AlgorithmPropertiesController algorithmPropertiesController;
    private BestSolutionController bestSolutionController;
    private final BooleanProperty isAlgorithmRunning;
    private final BooleanProperty isAlgorithmPaused;
    private final BooleanProperty isAlgorithmStopped;
    private final BooleanProperty isAlgorithmLoaded;
    private final IntegerProperty generationNumber;
    private final DoubleProperty fitnessScore;
    private final LongProperty timePassedInSeconds;
    private Boolean animationFlag;

    @FXML private BorderPane mainBoarderPane;
    @FXML private MenuBar topMenuBar;
    @FXML private MenuItem topMenuBarOptionLoadFxml;
    @FXML private MenuItem topMenuBarOptionLoadAlsf;
    @FXML private RadioMenuItem defaultCSSRadioMenuItem;
    @FXML private RadioMenuItem darkModeCSSRadioMenuItem;
    @FXML private RadioMenuItem uglyModeCSSRadioMenuItem;
    @FXML private MenuItem topMenuBarOptionRunAlgorithm;
    @FXML private MenuItem topMenuBarOptionPauseAlgorithm;
    @FXML private MenuItem topMenuBarOptionStopAlgorithm;
    @FXML private ScrollPane algorithmOutputScrollPane;
    @FXML private ScrollPane algorithmInfoScrollPane;
    @FXML private ScrollPane algorithmPropertiesScrollPane;
    @FXML private ScrollPane bestSolutionScrollPane;
    @FXML private Tab algorithmOutPutTab;
    @FXML private Tab algorithmInfoTab;
    @FXML private Tab algorithmPropertiesTab;
    @FXML private Tab bestSolutionTab;
    @FXML private Button leftSideButtonStart;
    @FXML private Button leftSideButtonPause;
    @FXML private Button leftSideButtonStop;
    @FXML private Label generationNumberLabel;
    @FXML private Label fitnessScoreLabel;
    @FXML private Label timeRunningLabel;

    protected MainLogic getLogic()
    {
        return logic;
    }

    @FXML
    public void initialize() {
        logic = new MainLogic(new UIAdapterFactory(this).create());
        logicThread = new Thread(logic, "Logic Thread");
        logicThread.setDaemon(true);
        logicThread.start();
        loadAlgorithmInfoController();
        loadAlgorithmOutputController();
        loadAlgorithmPropertiesController();
        loadBestSolutionController();
        initAlgorithmRunButtons();
        bindLabels();
        initTabAnimations();
    }

    public MainController()
    {
        isAlgorithmRunning = new SimpleBooleanProperty(false);
        isAlgorithmPaused = new SimpleBooleanProperty(false);
        isAlgorithmStopped = new SimpleBooleanProperty(false);
        isAlgorithmLoaded = new SimpleBooleanProperty(false);

        generationNumber = new SimpleIntegerProperty(0);
        fitnessScore = new SimpleDoubleProperty(0);
        timePassedInSeconds = new SimpleLongProperty(0);

        animationFlag = false;
    }

    private void initAlgorithmRunButtons()
    {
        topMenuBarOptionRunAlgorithm.disableProperty().bind(isAlgorithmPaused.not().and(isAlgorithmStopped.not()));
        topMenuBarOptionPauseAlgorithm.disableProperty().bind(isAlgorithmRunning.not());
        topMenuBarOptionStopAlgorithm.disableProperty().bind(isAlgorithmRunning.not().and(isAlgorithmPaused.not()));

        leftSideButtonStart.disableProperty().bind(topMenuBarOptionRunAlgorithm.disableProperty());
        leftSideButtonPause.disableProperty().bind(topMenuBarOptionPauseAlgorithm.disableProperty());
        leftSideButtonStop.disableProperty().bind(topMenuBarOptionStopAlgorithm.disableProperty());
    }

    private void bindLabels()
    {
        generationNumberLabel.textProperty().bind(generationNumber.asString());
        fitnessScoreLabel.textProperty().bind(fitnessScore.asString("%.2f"));

        timePassedInSeconds.addListener((observable, oldValue, newValue) ->
                timeRunningLabel.setText(
                String.format("%02d:%02d:%02d",
                        ((Long) newValue / (60 * 60)),
                        (((Long) newValue / 60) % 60),
                        ((Long) newValue % 60))));

        timePassedInSeconds.set(timePassedInSeconds.get() - 1);     // weird listener behavior
        timePassedInSeconds.set(timePassedInSeconds.get() + 1);
    }

    private void initTabAnimations()
    {
        algorithmOutPutTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(animationFlag)
                doFadeAnimationWhenSelectingTab(algorithmOutputScrollPane);
        });

        algorithmInfoTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(animationFlag)
                doFadeAnimationWhenSelectingTab(algorithmInfoScrollPane);
        });

        algorithmPropertiesTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(animationFlag)
                doFadeAnimationWhenSelectingTab(algorithmPropertiesScrollPane);
        });

        bestSolutionTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(animationFlag)
                doFadeAnimationWhenSelectingTab(bestSolutionScrollPane);
        });
    }

    private void loadAlgorithmInfoController()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = this.getClass().getResource("/resources/FXML/algorithmInfo.fxml");
            fxmlLoader.setLocation(url);

            algorithmInfoScrollPane.setContent(fxmlLoader.load(url.openStream()));
            algorithmInfoController = fxmlLoader.getController();
            algorithmInfoController.setMainController(this);
        }
        catch (Exception ex) {
            throw new RuntimeException("Something went wrong when loading algorithmInfo.fxml");
        }
    }

    private void loadAlgorithmOutputController()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = this.getClass().getResource("/resources/FXML/algorithmOutput.fxml");
            fxmlLoader.setLocation(url);

            algorithmOutputScrollPane.setContent(fxmlLoader.load(url.openStream()));
            algorithmOutputController = fxmlLoader.getController();
            algorithmOutputController.setMainController(this);
        }
        catch (Exception ex) {
            throw new RuntimeException("Something went wrong when loading algorithmOutput.fxml");
        }
    }

    private void loadAlgorithmPropertiesController()
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = this.getClass().getResource("/resources/FXML/algorithmProperties.fxml");
            fxmlLoader.setLocation(url);

            algorithmPropertiesScrollPane.setContent(fxmlLoader.load(url.openStream()));
            algorithmPropertiesController = fxmlLoader.getController();
            algorithmPropertiesController.setMainController(this);
        }
        catch (Exception ex) {
            throw new RuntimeException("Something went wrong when loading algorithmProperties.fxml");
        }
    }

    private void loadBestSolutionController()
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = this.getClass().getResource("/resources/FXML/bestSolution.fxml");
            fxmlLoader.setLocation(url);

            bestSolutionScrollPane.setContent(fxmlLoader.load(url.openStream()));
            bestSolutionController = fxmlLoader.getController();
            bestSolutionController.setMainController(this);
        }
        catch (Exception ex) {
            throw new RuntimeException("Something went wrong when loading bestSolution.fxml");
        }
    }

    private void doFadeAnimationWhenSelectingTab(ScrollPane scrollPane)
    {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), scrollPane.getContent());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void spinButtonAnimation(Button button)
    {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1500), button);
        rotateTransition.setFromAngle(0.0);
        rotateTransition.setToAngle(360.0);
        rotateTransition.play();
    }

    @FXML
    void loadAlsf(ActionEvent event) {
        Algorithm loadedAlgorithm = FileLoadingLogic.handleFileLoading(topMenuBar, FileType.alsfFile,
                isAlgorithmRunning.get() || isAlgorithmPaused.get());

        if(loadedAlgorithm != null)
            logic.pushCommand(() -> logic.commandSetAlgorithm(loadedAlgorithm));
    }

    @FXML
    void loadXml(ActionEvent event) {
        Algorithm loadedAlgorithm = FileLoadingLogic.handleFileLoading(topMenuBar, FileType.xmlFile,
                isAlgorithmRunning.get() || isAlgorithmPaused.get());

        if(loadedAlgorithm != null)
            logic.pushCommand(() -> logic.commandSetAlgorithm(loadedAlgorithm));
    }

    @FXML
    void saveAlsf(ActionEvent event) {
        String absolutePath = FileSavingLogic.handleFileSaving(topMenuBar, isAlgorithmLoaded.get());

        if(absolutePath != null)
            logic.pushCommand(() -> logic.commandSave(absolutePath));
    }

    @FXML
    void runAlgorithm(ActionEvent event) {
        if(algorithmOutputController.areAllCheckBoxesUnchecked())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You have to choose at least 1 stop condition");
            alert.show();
        }
        else
            logic.pushCommand(() -> logic.commandStartAlgorithm());
    }

    @FXML
    void pauseAlgorithm(ActionEvent event) {
        logic.pushCommand(() -> logic.commandPauseAlgorithm());
    }

    @FXML
    void stopAlgorithm(ActionEvent event) {
        logic.pushCommand(() -> logic.commandStopAlgorithm());
    }

    @FXML
    void runAlgorithmBigButton(ActionEvent event) {
        if(animationFlag)
            spinButtonAnimation(leftSideButtonStart);

        runAlgorithm(event);
    }

    @FXML
    void pauseAlgorithmBigButton(ActionEvent event) {
        if(animationFlag)
            spinButtonAnimation(leftSideButtonPause);

        pauseAlgorithm(event);
    }

    @FXML
    void stopAlgorithmBigButton(ActionEvent event) {
        if(animationFlag)
            spinButtonAnimation(leftSideButtonStop);

        stopAlgorithm(event);
    }

    @FXML
    void setDefaultCSS(ActionEvent event) {
        mainBoarderPane.getStylesheets().clear();
        darkModeCSSRadioMenuItem.selectedProperty().set(false);
        uglyModeCSSRadioMenuItem.selectedProperty().set(false);
        mainBoarderPane.getStylesheets().add("/resources/CSS/test.css");
    }

    @FXML
    void setDarkModeCSS(ActionEvent event) {
        mainBoarderPane.getStylesheets().clear();
        defaultCSSRadioMenuItem.selectedProperty().set(false);
        uglyModeCSSRadioMenuItem.selectedProperty().set(false);
        mainBoarderPane.getStylesheets().add("/resources/CSS/DarkMode.css");
    }

    @FXML
    void setUglyModeCSS(ActionEvent event) {
        mainBoarderPane.getStylesheets().clear();
        defaultCSSRadioMenuItem.selectedProperty().set(false);
        darkModeCSSRadioMenuItem.selectedProperty().set(false);
        mainBoarderPane.getStylesheets().add("/resources/CSS/UglyMode.css");
    }

    @FXML
    void turnAnimationsOn(ActionEvent event) {
        animationFlag = true;
    }

    @FXML
    void turnAnimationsOff(ActionEvent event) {
        animationFlag = false;
    }

    // getters for all properties and sub controllers

    public AlgorithmInfoController getAlgorithmInfoController() {
        return algorithmInfoController;
    }

    public AlgorithmOutputController getAlgorithmOutputController() {
        return algorithmOutputController;
    }

    public AlgorithmPropertiesController getAlgorithmPropertiesController() {
        return algorithmPropertiesController;
    }

    public BestSolutionController getBestSolutionController() {
        return bestSolutionController;
    }

    public BooleanProperty isAlgorithmRunningProperty() {
        return isAlgorithmRunning;
    }

    public BooleanProperty isAlgorithmPausedProperty() {
        return isAlgorithmPaused;
    }

    public BooleanProperty isAlgorithmStoppedProperty() {
        return isAlgorithmStopped;
    }

    public BooleanProperty isAlgorithmLoadedProperty() {
        return isAlgorithmLoaded;
    }

    public IntegerProperty generationNumberProperty() {
        return generationNumber;
    }

    public DoubleProperty fitnessScoreProperty() {
        return fitnessScore;
    }

    public LongProperty timePassedInSecondsProperty() {
        return timePassedInSeconds;
    }
}
