package Main.Controllers;

import Main.Engine.Evolution.TimeTableSolution.CrossOvers.TimeTableCrossOvers;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableCrossOver;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableSelection;
import Main.Engine.Evolution.TimeTableSolution.Mutations.TimeTableMutations;
import Main.Engine.Evolution.TimeTableSolution.Selections.TimeTableSelections;
import Main.HelperClasses.GeneralAlgorithmInformation;
import Main.HelperClasses.MutationChangerListViewCell;
import Main.HelperClasses.MutationListViewCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmPropertiesController {

    private enum SelectionTypes{
        Truncation, RouletteWheel, Tournament
    }

    private enum CrossoverTypes{
        DayTimeOriented, AspectOriented
    }

    private enum MutationTypes{
        Flipping, Sizer
    }

    @FXML private GridPane fatherGridPane;
    @FXML private ComboBox<SelectionTypes> selectionComboBox;
    @FXML private ComboBox<CrossoverTypes> crossoverComboBox;
    @FXML private Label selectionArgNameLabel1;
    @FXML private Spinner<Integer> elitismSpinner;
    @FXML private Spinner<Integer> cutSizeSpinner;
    @FXML private Label crossoverArgNameLabel1;
    @FXML private HBox selectionArg1HBox;
    @FXML private HBox crossoverArg1HBox;
    @FXML private ComboBox<MutationTypes> addMutationComboBox;
    @FXML private VBox newMutationArgsLabelVBox;
    @FXML private VBox newMutationArgsComponentsVBox;
    @FXML private Button addMutationButton;
    @FXML private ListView<MutationChangerListViewCell> mutationListView;

    private MainController mainController;
    private final Spinner<Integer> truncationSpinner = new Spinner<>();
    private final Spinner<Double> tournamentSpinner = new Spinner<>();
    private final ComboBox<String> aspectOrientedComboBox = new ComboBox<>();

    private final Label maxTuplesLabelForNewFlipping = new Label("Max Tuples:");
    private final Spinner<Integer> maxTupleSpinnerForNewFlipping = new Spinner<>();
    private final Label componentLabelForNewFlipping = new Label("Component:");
    private final ComboBox<Character> componentComboBoxForNewFlipping = new ComboBox<>();
    private final Label totalTuplesLabelForNewSizer = new Label("Total Tuples:");
    private final Spinner<Integer> totalTuplesSpinnerForNewSizer = new Spinner<>();


    public void writeInformation(GeneralAlgorithmInformation information)
    {
        DTOTimeTableSelection selection = information.getDtoSelection();
        DTOTimeTableCrossOver crossover = information.getDtoCrossOver();
        List<DTOTimeTableMutation> mutations = information.getAllDtoMutations();

        setupElitismSpinner(information.getPopulationSize());
        writeSelection(selection);
        writeCrossover(crossover);
        writeMutations(mutations);
    }

    private void writeSelection(DTOTimeTableSelection selection)
    {
        elitismSpinner.getValueFactory().setValue(selection.getElitism());

        switch (selection.getSelectionName())
        {
            case "Truncation":
                truncationSpinner.getValueFactory().setValue((int) selection.getSelectionArgumentsList().get(0));
                onSelectionTruncation();        // explicit call to default selection in combobox so will write information for sure
                selectionComboBox.valueProperty().set(SelectionTypes.Truncation);
                break;
            case "RouletteWheel":
                selectionComboBox.valueProperty().set(SelectionTypes.RouletteWheel);
                break;
            case "Tournament":
                tournamentSpinner.getValueFactory().setValue((double) selection.getSelectionArgumentsList().get(0));
                selectionComboBox.valueProperty().set(SelectionTypes.Tournament);
        }
    }

    private void writeCrossover(DTOTimeTableCrossOver crossover)
    {
        cutSizeSpinner.getValueFactory().setValue(crossover.getCutSize());

        switch (crossover.getCrossOverName())
        {
            case "DayTimeOriented":
                crossoverComboBox.valueProperty().set(CrossoverTypes.DayTimeOriented);
                onCrossoverDayTimeOriented();       // explicit call to default selection in combobox so will write information for sure
                break;
            case "AspectOriented":
                aspectOrientedComboBox.valueProperty().set((String) crossover.getArguments().get(0));
                crossoverComboBox.valueProperty().set(CrossoverTypes.AspectOriented);
                break;
        }
    }

    private void writeMutations(List<DTOTimeTableMutation> mutations)
    {
        mutationListView.getItems().clear();
        mutations.forEach(this::addMutationToList);
    }

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
        initElitismSpinner();
        bindGridToPause();
    }

    private void addMutationToList(DTOTimeTableMutation dtoTimeTableMutation)
    {
        MutationChangerListViewCell changer = new MutationChangerListViewCell(dtoTimeTableMutation);
        mutationListView.getItems().add(changer);

        changer.chanceProperty().addListener((observable, oldValue, newValue) -> {
            mainController.getLogic().pushCommand(() ->
                    mainController.getLogic().commandSetMutationProbability(
                            new DTOTimeTableMutation(
                                    TimeTableMutations.valueOf(changer.getName()),
                                    oldValue.intValue(),
                                    Arrays.asList(changer.getMutationArguments().toArray())),
                            newValue.intValue()
                    ));
        });

        changer.isDeletedProperty().addListener((observable, oldValue, newValue) -> {
            mainController.getLogic().pushCommand(() ->
                    mainController.getLogic().commandDeleteMutation(
                    new DTOTimeTableMutation(
                            TimeTableMutations.valueOf(changer.getName()),
                            changer.getChance(),
                            Arrays.asList(changer.getMutationArguments().toArray()))
                    ));
        });
    }

    @FXML
    public void initialize() {
        initComboBoxes();
        initTruncationSpinner();
        initTournamentSpinner();
        initCutSizeSpinner();
        initAspectOrientedComboBox();
        initNewMutationComponents();
        initMutationListView();
    }

    private void bindGridToPause()
    {
        fatherGridPane.disableProperty().bind(mainController.isAlgorithmPausedProperty().not()
                .and(mainController.isAlgorithmStoppedProperty().not()));
    }

    private void initComboBoxes()
    {
        selectionComboBox.getItems().addAll(SelectionTypes.values());
        crossoverComboBox.getItems().addAll(CrossoverTypes.values());
        addMutationComboBox.getItems().addAll(MutationTypes.values());
        selectionComboBox.getSelectionModel().selectFirst();
        crossoverComboBox.getSelectionModel().selectFirst();

        selectionComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue)
            {
                case Truncation:
                    onSelectionTruncation();
                    break;
                case RouletteWheel:
                    onSelectionRouletteWheel();
                    break;
                case Tournament:
                    onSelectionTournament();
                    break;
            }

            updateLogicSelectionChange(newValue);
        });

        crossoverComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue)
            {
                case DayTimeOriented:
                    onCrossoverDayTimeOriented();
                    break;
                case AspectOriented:
                    onCrossoverAspectOriented();
                    break;
            }

            updateLogicCrossoverChange(newValue);
        });

        addMutationComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch(newValue)
            {
                case Flipping:
                    setupNewFlipping();
                    break;
                case Sizer:
                    setupNewSizer();
                    break;
            }
        });

        addMutationComboBox.getSelectionModel().selectFirst();
    }

    private void initAspectOrientedComboBox()
    {
        aspectOrientedComboBox.getItems().add("CLASS");
        aspectOrientedComboBox.getItems().add("TEACHER");
        aspectOrientedComboBox.getSelectionModel().selectFirst();

        aspectOrientedComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateLogicCrossoverChange(crossoverComboBox.getValue());
        });
    }

    private void initNewMutationComponents()
    {
        componentComboBoxForNewFlipping.getItems().addAll('D', 'H', 'C', 'T', 'S');
        componentComboBoxForNewFlipping.getSelectionModel().selectFirst();

        SpinnerValueFactory<Integer> maxTuplesSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        SpinnerValueFactory<Integer> totalTuplesSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        maxTupleSpinnerForNewFlipping.editableProperty().set(true);
        totalTuplesSpinnerForNewSizer.editableProperty().set(true);

        maxTupleSpinnerForNewFlipping.setValueFactory(maxTuplesSpinnerFactory);
        totalTuplesSpinnerForNewSizer.setValueFactory(totalTuplesSpinnerFactory);

        TextFormatter<Integer> maxTuplesSpinnerFormatter
                = new TextFormatter<>(maxTuplesSpinnerFactory.getConverter(), maxTuplesSpinnerFactory.getValue());
        TextFormatter<Integer> totalTuplesSpinnerFormatter
                = new TextFormatter<>(totalTuplesSpinnerFactory.getConverter(), totalTuplesSpinnerFactory.getValue());

        maxTupleSpinnerForNewFlipping.getEditor().setTextFormatter(maxTuplesSpinnerFormatter);
        totalTuplesSpinnerForNewSizer.getEditor().setTextFormatter(totalTuplesSpinnerFormatter);

        maxTuplesSpinnerFactory.valueProperty().bindBidirectional(maxTuplesSpinnerFormatter.valueProperty());
        totalTuplesSpinnerFactory.valueProperty().bindBidirectional(totalTuplesSpinnerFormatter.valueProperty());

        maxTuplesLabelForNewFlipping.getStylesheets().add("/resources/CSS/AlgorithmPropertiesStyle.css");
        maxTupleSpinnerForNewFlipping.getStylesheets().add("/resources/CSS/AlgorithmPropertiesStyle.css");
        componentLabelForNewFlipping.getStylesheets().add("/resources/CSS/AlgorithmPropertiesStyle.css");
        componentComboBoxForNewFlipping.getStylesheets().add("/resources/CSS/AlgorithmPropertiesStyle.css");
        maxTuplesLabelForNewFlipping.getStyleClass().add("new-mutation-max-tuples-label");
        maxTupleSpinnerForNewFlipping.getStyleClass().add("new-mutation-max-tuples-Spinner");
        componentLabelForNewFlipping.getStyleClass().add("new-mutation-component-label");
        componentComboBoxForNewFlipping.getStyleClass().add("new-mutation-max-tuples-combo-box");
    }

    private void initMutationListView()
    {
        mutationListView.setCellFactory(param -> new MutationListViewCell());
    }

    private void initTruncationSpinner()
    {
        truncationSpinner.editableProperty().set(true);

        SpinnerValueFactory<Integer> truncationSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);

        truncationSpinner.setValueFactory(truncationSpinnerFactory);

        TextFormatter<Integer> truncationSpinnerFormatter
                = new TextFormatter<>(truncationSpinnerFactory.getConverter(), truncationSpinnerFactory.getValue());

        truncationSpinner.getEditor().setTextFormatter(truncationSpinnerFormatter);

        truncationSpinnerFactory.valueProperty().bindBidirectional(truncationSpinnerFormatter.valueProperty());

        truncationSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
                updateLogicSelectionChange(selectionComboBox.getValue()));
    }

    private void initTournamentSpinner()
    {
        tournamentSpinner.editableProperty().set(true);

        SpinnerValueFactory<Double> tournamentSpinnerFactory
                = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0, 0.1);

        tournamentSpinner.setValueFactory(tournamentSpinnerFactory);

        TextFormatter<Double> tournamentSpinnerFormatter
                = new TextFormatter<>(tournamentSpinnerFactory.getConverter(), tournamentSpinnerFactory.getValue());

        tournamentSpinner.getEditor().setTextFormatter(tournamentSpinnerFormatter);

        tournamentSpinnerFactory.valueProperty().bindBidirectional(tournamentSpinnerFormatter.valueProperty());

        tournamentSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
                updateLogicSelectionChange(selectionComboBox.getValue()));
    }

    private void initCutSizeSpinner()
    {
        SpinnerValueFactory<Integer> cutSizeSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);

        cutSizeSpinner.setValueFactory(cutSizeSpinnerFactory);

        TextFormatter<Integer> cutSizeSpinnerFormatter
                = new TextFormatter<>(cutSizeSpinnerFactory.getConverter(), cutSizeSpinnerFactory.getValue());

        cutSizeSpinner.getEditor().setTextFormatter(cutSizeSpinnerFormatter);

        cutSizeSpinnerFactory.valueProperty().bindBidirectional(cutSizeSpinnerFormatter.valueProperty());

        cutSizeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateLogicCrossoverChange(crossoverComboBox.getValue());
        });
    }

    private void initElitismSpinner()
    {
        setupElitismSpinner(0);
        elitismSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
                updateLogicSelectionChange(selectionComboBox.getValue()));
    }

    public void setupElitismSpinner(int maxValue)
    {
        elitismSpinner.setValueFactory(null);
        elitismSpinner.getEditor().setTextFormatter(null);

        SpinnerValueFactory<Integer> elitismSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxValue);

        elitismSpinner.setValueFactory(elitismSpinnerFactory);

        TextFormatter<Integer> elitismSpinnerFormatter
                = new TextFormatter<>(elitismSpinnerFactory.getConverter(), elitismSpinnerFactory.getValue());

        elitismSpinner.getEditor().setTextFormatter(elitismSpinnerFormatter);

        elitismSpinnerFactory.valueProperty().bindBidirectional(elitismSpinnerFormatter.valueProperty());
    }

    private void setupNewFlipping()
    {
        newMutationArgsLabelVBox.getChildren().clear();
        newMutationArgsComponentsVBox.getChildren().clear();

        newMutationArgsLabelVBox.getChildren().add(maxTuplesLabelForNewFlipping);
        newMutationArgsLabelVBox.getChildren().add(componentLabelForNewFlipping);
        newMutationArgsComponentsVBox.getChildren().add(maxTupleSpinnerForNewFlipping);
        newMutationArgsComponentsVBox.getChildren().add(componentComboBoxForNewFlipping);
    }

    private void setupNewSizer()
    {
        newMutationArgsLabelVBox.getChildren().clear();
        newMutationArgsComponentsVBox.getChildren().clear();

        newMutationArgsLabelVBox.getChildren().add(totalTuplesLabelForNewSizer);
        newMutationArgsComponentsVBox.getChildren().add(totalTuplesSpinnerForNewSizer);
    }

    private void onSelectionTruncation()
    {
        selectionArgNameLabel1.setVisible(true);
        selectionArgNameLabel1.setText("Top Percent:");
        selectionArg1HBox.getChildren().clear();
        selectionArg1HBox.getChildren().add(truncationSpinner);
    }

    private void onSelectionRouletteWheel()
    {
        selectionArgNameLabel1.setVisible(false);
        selectionArgNameLabel1.setText("");
        selectionArg1HBox.getChildren().clear();
    }

    private void onSelectionTournament()
    {
        selectionArgNameLabel1.setVisible(true);
        selectionArgNameLabel1.setText("Pte:");
        selectionArg1HBox.getChildren().clear();
        selectionArg1HBox.getChildren().add(tournamentSpinner);
    }

    private void onCrossoverDayTimeOriented()
    {
        crossoverArgNameLabel1.setVisible(false);
        crossoverArgNameLabel1.setText("");
        crossoverArg1HBox.getChildren().clear();
    }

    private void onCrossoverAspectOriented()
    {
        crossoverArgNameLabel1.setVisible(true);
        crossoverArgNameLabel1.setText("Orientation:");
        crossoverArg1HBox.getChildren().clear();
        crossoverArg1HBox.getChildren().add(aspectOrientedComboBox);
    }

    private void updateLogicSelectionChange(SelectionTypes type)
    {
        int elitismSize = elitismSpinner.getValue();
        List<Object> selectionArgs = new ArrayList<>();

        switch(type)
        {
            case Truncation:
                mainController.getLogic().pushCommand(() -> {
                    selectionArgs.add(truncationSpinner.getValue());
                    mainController.getLogic().commandSetSelection(
                            new DTOTimeTableSelection(
                                    TimeTableSelections.Truncation,
                                    elitismSize,
                                    selectionArgs
                            )
                    );
                });
                break;
            case RouletteWheel:
                mainController.getLogic().pushCommand(() -> {
                    mainController.getLogic().commandSetSelection(
                            new DTOTimeTableSelection(
                                    TimeTableSelections.RouletteWheel,
                                    elitismSize,
                                    selectionArgs
                            )
                    );
                });
                break;
            case Tournament:
                mainController.getLogic().pushCommand(() -> {
                    selectionArgs.add(tournamentSpinner.getValue());
                    mainController.getLogic().commandSetSelection(
                            new DTOTimeTableSelection(
                                    TimeTableSelections.Tournament,
                                    elitismSize,
                                    selectionArgs
                            )
                    );
                });
                break;
        }
    }

    private void updateLogicCrossoverChange(CrossoverTypes type)
    {
        int cutSize = cutSizeSpinner.getValue();
        List<Object> crossoverArgs = new ArrayList<>();

        switch(type)
        {
            case DayTimeOriented:
                mainController.getLogic().pushCommand(() -> {
                    mainController.getLogic().commandSetCrossover(
                            new DTOTimeTableCrossOver(
                                    TimeTableCrossOvers.DayTimeOriented,
                                    cutSize,
                                    crossoverArgs
                            )
                    );
                });
                break;
            case AspectOriented:
                crossoverArgs.add(aspectOrientedComboBox.valueProperty().get());
                mainController.getLogic().pushCommand(() -> {
                    mainController.getLogic().commandSetCrossover(
                            new DTOTimeTableCrossOver(
                                    TimeTableCrossOvers.AspectOriented,
                                    cutSize,
                                    crossoverArgs
                            )
                    );
                });
                break;
        }
    }

    private DTOTimeTableMutation createNewFlipping()
    {
        int maxTuples = maxTupleSpinnerForNewFlipping.getValue();
        int probability = 10;
        switch(componentComboBoxForNewFlipping.valueProperty().get())
        {
            case 'D':
                return new DTOTimeTableMutation(TimeTableMutations.FlippingDay, probability,
                        new ArrayList<Object>() {{ add(maxTuples); add('D'); }});
            case 'H':
                return new DTOTimeTableMutation(TimeTableMutations.FlippingHour, probability,
                        new ArrayList<Object>() {{ add(maxTuples); add('H'); }});
            case 'C':
                return new DTOTimeTableMutation(TimeTableMutations.FlippingGrade, probability,
                        new ArrayList<Object>() {{ add(maxTuples); add('C'); }});
            case 'T':
                return new DTOTimeTableMutation(TimeTableMutations.FlippingTeacher, probability,
                        new ArrayList<Object>() {{ add(maxTuples); add('T'); }});
            case 'S':
                return new DTOTimeTableMutation(TimeTableMutations.FlippingSubject, probability,
                        new ArrayList<Object>() {{ add(maxTuples); add('S'); }});
            default:
                throw new RuntimeException("Got bad input from componentComboBoxForNewFlipping");
        }
    }

    private DTOTimeTableMutation createNewSizer()
    {
        int probability = 10;
        int totalTuples = totalTuplesSpinnerForNewSizer.getValue();
        return new DTOTimeTableMutation(
                TimeTableMutations.Sizer, probability, new ArrayList<Object>() {{ add(totalTuples); }}
        );
    }

    @FXML
    void addMutation(ActionEvent event) {
        DTOTimeTableMutation newMutationDto;
        switch (addMutationComboBox.valueProperty().get())
        {
            case Flipping:
                newMutationDto = createNewFlipping();
                break;
            case Sizer:
                newMutationDto = createNewSizer();
                break;
            default:
                throw new RuntimeException("Weird Error");
        }

        addMutationToList(newMutationDto);

        mainController.getLogic().pushCommand(() -> {
            mainController.getLogic().commandAddMutation(newMutationDto);
        });
    }
}
