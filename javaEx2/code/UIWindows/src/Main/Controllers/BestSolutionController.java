package Main.Controllers;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOGrade;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTeacher;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTable;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableTuples;
import Main.HelperClasses.BestTimeTableComplex;
import Main.HelperClasses.GeneralAlgorithmInformation;
import Main.HelperClasses.GridPaneTableFactory;
import Main.HelperClasses.RuleEnforcementComplex;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BestSolutionController {

    private enum ShowType{
        Raw, Teacher, Grade
    }

    @FXML private GridPane fatherGrid;
    @FXML private Label generationNumberOfBestSolutionLabel;
    @FXML private ComboBox<ShowType> showTypeComboBox;
    @FXML private TableView<RuleEnforcementComplex> rulesToEnforcementTable;
    @FXML private TableColumn<RuleEnforcementComplex, String> ruleNameColumn;
    @FXML private TableColumn<RuleEnforcementComplex, Integer> percentOfEnforcementColumn;
    @FXML private ScrollPane dataShowScrollPane;
    @FXML private Label viewTypeLabel;
    @FXML private Label teacherOrGradeComboBoxLabel;
    @FXML private HBox hBoxForComboBox;
    @FXML private Button getBestSolutionButton;
    @FXML private HBox bestSolutionPickerHBox;

    private MainController mainController;
    private final StringProperty generationNumberOfBestSolution = new SimpleStringProperty("0");
    private final ListView<String> rawListView = new ListView<>();
    private final ComboBox<String> teachersNameComboBox = new ComboBox<>();
    private final ComboBox<String> gradeNameComboBox = new ComboBox<>();
    private Map<String, GridPane> teacherNameToTable;
    private Map<String, GridPane> gradeNameToTable;
    private final List<BestTimeTableComplex> allGenerationsList = new LinkedList<>();
    private final Spinner<Integer> generationPickerSpinner = new Spinner<>();
    private Integer offSet = 1;
    private final ChangeListener<Integer> pickGenerationListener;

    public BestSolutionController()
    {
        this.pickGenerationListener = (observable, oldValue, newValue) -> {
            int index = newValue - offSet;
            if((index >= 0) && (index < allGenerationsList.size()))
                writeBestSolution(allGenerationsList.get(index));
        };
    }

    public void pushNewGenerationToList(BestTimeTableComplex bestTimeTableComplex)
    {
        allGenerationsList.add(bestTimeTableComplex);
    }

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
        fatherGrid.disableProperty().bind(
                mainController.isAlgorithmRunningProperty().not().and(mainController.isAlgorithmPausedProperty().not())
        );
        bindSolutionPicker();
    }

    @FXML
    public void initialize() {
        bindLabels();
        initComboBox();
        initRuleTable();
        initGenerationPickerComboBox();
    }

    private void bindLabels()
    {
        generationNumberOfBestSolutionLabel.textProperty().bind(generationNumberOfBestSolution);
        viewTypeLabel.textProperty().bind(
                Bindings.concat("View by ", showTypeComboBox.getSelectionModel().selectedItemProperty().asString()));
    }

    private void initComboBox()
    {
        showTypeComboBox.getItems().addAll(ShowType.values());

        showTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue)
            {
                case Raw:
                    handleRawView();
                    break;
                case Teacher:
                    handleTeacherView();
                    break;
                case Grade:
                    handleGradeView();
                    break;
            }
        });

        showTypeComboBox.getSelectionModel().selectFirst();

        teachersNameComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            setDataShowScrollPaneContent();
        });

        gradeNameComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            setDataShowScrollPaneContent();
        });
    }

    private void handleRawView()
    {
        teacherOrGradeComboBoxLabel.textProperty().set("");
        hBoxForComboBox.getChildren().clear();
        setDataShowScrollPaneContent();
    }

    private void handleTeacherView()
    {
        teacherOrGradeComboBoxLabel.textProperty().set("Teacher to view:");
        hBoxForComboBox.getChildren().clear();
        hBoxForComboBox.getChildren().add(teachersNameComboBox);
        setDataShowScrollPaneContent();
    }

    private void handleGradeView()
    {
        teacherOrGradeComboBoxLabel.textProperty().set("Grade to view:");
        hBoxForComboBox.getChildren().clear();
        hBoxForComboBox.getChildren().add(gradeNameComboBox);
        setDataShowScrollPaneContent();
    }

    private void initRuleTable()
    {
        ruleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        percentOfEnforcementColumn.setCellValueFactory(new PropertyValueFactory<>("enforcement"));
    }

    private void initGenerationPickerComboBox()
    {
        generationPickerSpinner.prefWidthProperty().set(135.0);
        generationPickerSpinner.setEditable(true);
    }

    private void bindSolutionPicker()
    {
        mainController.isAlgorithmRunningProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                bestSolutionPickerHBox.getChildren().clear();
                bestSolutionPickerHBox.getChildren().add(getBestSolutionButton);
            }
        });

        mainController.isAlgorithmPausedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                bestSolutionPickerHBox.getChildren().clear();
                bestSolutionPickerHBox.getChildren().add(generationPickerSpinner);
                setUpGenerationPickerSpinner(offSet, allGenerationsList.size() + offSet);
                generationPickerSpinner.getValueFactory().setValue(allGenerationsList.size() + offSet - 1);
            }
        });
    }

    private void setDataShowScrollPaneContent()
    {
        dataShowScrollPane.setContent(null);

        switch(showTypeComboBox.getSelectionModel().getSelectedItem())
        {
            case Raw:
                dataShowScrollPane.setContent(rawListView);
                break;
            case Teacher:
                if(teacherNameToTable == null)
                    dataShowScrollPane.setContent(null);
                else
                    dataShowScrollPane.setContent(teacherNameToTable.get(teachersNameComboBox.getValue()));
                break;
            case Grade:
                if(gradeNameToTable == null)
                    dataShowScrollPane.setContent(null);
                else
                    dataShowScrollPane.setContent(gradeNameToTable.get(gradeNameComboBox.getValue()));
                break;
        }
    }

    public void onLoad(GeneralAlgorithmInformation information)
    {
        clearFields(information.getGenerationNumber());
        setUpTeacherGradeComboBoxes(information);
        setUpGenerationPickerSpinner(information.getGenerationNumber(), information.getGenerationNumber());
    }

    public void onRunAfterStop()
    {
        generationNumberOfBestSolution.set("1");
        rawListView.getItems().clear();
        dataShowScrollPane.setContent(null);
        rulesToEnforcementTable.getItems().clear();
        teacherNameToTable = null;
        gradeNameToTable = null;
        allGenerationsList.clear();
    }

    private void clearFields(Integer firstGenerationNumber)
    {
        generationNumberOfBestSolution.set(firstGenerationNumber.toString());
        rawListView.getItems().clear();
        teachersNameComboBox.getItems().clear();
        gradeNameComboBox.getItems().clear();
        dataShowScrollPane.setContent(null);
        rulesToEnforcementTable.getItems().clear();
        teacherNameToTable = null;
        gradeNameToTable = null;
        allGenerationsList.clear();
    }

    private void setUpTeacherGradeComboBoxes(GeneralAlgorithmInformation information)
    {
        teachersNameComboBox.getItems().addAll(
                information.getAllDtoTeachers().stream()
                        .map(DTOTeacher::getName)
                        .collect(Collectors.toList())
        );
        teachersNameComboBox.getSelectionModel().selectFirst();

        gradeNameComboBox.getItems().addAll(
                information.getAllDtoGrades().stream()
                        .map(DTOGrade::getName)
                        .collect(Collectors.toList())
        );
        gradeNameComboBox.getSelectionModel().selectFirst();
    }

    private void setUpGenerationPickerSpinner(Integer firstGeneration, Integer lastGeneration)
    {
        generationPickerSpinner.valueProperty().removeListener(pickGenerationListener);

        offSet = firstGeneration;

        SpinnerValueFactory<Integer> generationPickerSpinnerFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(firstGeneration, lastGeneration);

        generationPickerSpinner.setValueFactory(generationPickerSpinnerFactory);

        TextFormatter<Integer> generationPickerSpinnerFormatter
                = new TextFormatter<>(generationPickerSpinnerFactory.getConverter(), generationPickerSpinnerFactory.getValue());

        generationPickerSpinner.getEditor().setTextFormatter(generationPickerSpinnerFormatter);

        generationPickerSpinnerFactory.valueProperty().bindBidirectional(generationPickerSpinnerFormatter.valueProperty());

        generationPickerSpinner.valueProperty().addListener(pickGenerationListener);
    }

    public void writeBestSolution(BestTimeTableComplex bestTimeTableComplex)
    {
        generationNumberOfBestSolution.set(bestTimeTableComplex.getGeneration().toString());

        writeRulesInformation(bestTimeTableComplex.getRuleEnforcementComplexes());
        writeRawInformation(bestTimeTableComplex.getTimeTable());

        teacherNameToTable = teachersNameComboBox.getItems().stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> getTeacherTable(bestTimeTableComplex.getTimeTable(), name))
                );

        gradeNameToTable = gradeNameComboBox.getItems().stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> getGradeTable(bestTimeTableComplex.getTimeTable(), name))
                );

        setDataShowScrollPaneContent();
    }

    private void writeRulesInformation(List<RuleEnforcementComplex> ruleEnforcementComplexList)
    {
        rulesToEnforcementTable.getItems().clear();
        rulesToEnforcementTable.getItems().addAll(ruleEnforcementComplexList);
    }

    private void writeRawInformation(DTOTimeTable bestSolution)
    {
        rawListView.getItems().clear();
        rawListView.getItems().addAll(
                bestSolution.getTuples().stream()
                .sorted((tuple1, tuple2) -> {
                    if(!tuple1.getDay().equals(tuple2.getDay()))
                        return tuple1.getDay() - tuple2.getDay();
                    else if(!tuple1.getHour().equals(tuple2.getHour()))
                        return tuple1.getHour() - tuple2.getHour();
                    else if(!tuple1.getGrade().equals(tuple2.getGrade()))
                        return tuple1.getGrade().getId() - tuple2.getGrade().getId();
                    else
                        return tuple1.getTeacher().getId() - tuple2.getTeacher().getId();
                })
                .map(tuple ->
                        String.format("< Day: %d, Hour: %d, Teacher: %s, Grade: %s, Subject: %s >",
                                tuple.getDay(),
                                tuple.getHour(),
                                tuple.getTeacher().getName(),
                                tuple.getGrade().getName(),
                                tuple.getSubject().getName()).toString())
                .collect(Collectors.toList())
        );
    }

    private GridPane getTeacherTable(DTOTimeTable bestSolution, String teacherName)
    {
        if(bestSolution != null)
        {
            GridPaneTableFactory factory = new GridPaneTableFactory(bestSolution.getDays(), bestSolution.getHours());
            Set<DTOTimeTableTuples> tuplesOfTeacher = bestSolution.getTuples().stream()
                    .filter(tuple -> tuple.getTeacher().getName().equals(teacherName))
                    .collect(Collectors.toSet());

            for(int i = 1; i <= bestSolution.getDays(); ++i)
            {
                for(int j = 1; j <= bestSolution.getHours(); ++j)
                {
                    int day = i;
                    int hour = j;
                    factory.setCellTuples(tuplesOfTeacher.stream()
                                    .filter(tuple -> tuple.getDay() == day && tuple.getHour() == hour)
                                    .collect(Collectors.toSet()),
                            hour, day, true);
                }
            }

            return factory.create();
        }
        else
            return null;
    }

    private GridPane getGradeTable(DTOTimeTable bestSolution, String gradeName)
    {
        if(bestSolution != null)
        {
            GridPaneTableFactory factory = new GridPaneTableFactory(bestSolution.getDays(), bestSolution.getHours());
            Set<DTOTimeTableTuples> tuplesOfGrade = bestSolution.getTuples().stream()
                    .filter(tuple -> tuple.getGrade().getName().equals(gradeName))
                    .collect(Collectors.toSet());

            for(int i = 1; i <= bestSolution.getDays(); ++i)
            {
                for(int j = 1; j <= bestSolution.getHours(); ++j)
                {
                    int day = i;
                    int hour = j;
                    factory.setCellTuples(tuplesOfGrade.stream()
                                    .filter(tuple -> tuple.getDay() == day && tuple.getHour() == hour)
                                    .collect(Collectors.toSet()),
                            hour, day, false);
                }
            }

            return factory.create();
        }
        else
            return null;
    }

    @FXML
    void getBestSolution(ActionEvent event) {
        writeBestSolution(allGenerationsList.get(allGenerationsList.size() - offSet));
        //dataShowScrollPane.setContent(null);
    }

    // getters for all properties and sub controllers

    public StringProperty generationNumberOfBestSolutionProperty() {
        return generationNumberOfBestSolution;
    }
}
