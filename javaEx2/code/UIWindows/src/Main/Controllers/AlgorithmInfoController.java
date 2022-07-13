package Main.Controllers;

import Main.HelperClasses.TableViewClasses.*;
import Main.Engine.Evolution.Rule;
import Main.HelperClasses.GeneralAlgorithmInformation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmInfoController {

    @FXML private Label generationDisplay;
    @FXML private Label populationSizeDisplay;
    @FXML private Label selectionNameDisplay;
    @FXML private Label crossOverNameDisplay;
    @FXML private Label crossOverCutSizeDisplay;
    @FXML private Label elitismSizeDisplay;
    @FXML private Label hardRuleWeightDisplay;
    @FXML private Label softRuleWeightDisplay;
    @FXML private TableView<SubjectView> subjectTable;
    @FXML private TableColumn<SubjectView, Integer> subjectTableColID;
    @FXML private TableColumn<SubjectView, String> subjectTableColName;
    @FXML private TableView<TeacherView> teacherTable;
    @FXML private TableColumn<TeacherView, Integer> teacherTableColID;
    @FXML private TableColumn<TeacherView, String> teacherTableColName;
    @FXML private TableColumn<TeacherView, List<SubjectView>> teacherTableColSubjectsTeaching;
    @FXML private TableView<GradeView> gradeTable;
    @FXML private TableColumn<GradeView, Integer> gradeTableColID;
    @FXML private TableColumn<GradeView, String> gradeTableColName;
    @FXML private TableColumn<GradeView, List<SubjectView>> gradeTableColSubjects;
    @FXML private TableColumn<GradeView, List<Integer>> gradeTableColHours;
    @FXML private TableView<RuleView> ruleTable;
    @FXML private TableColumn<RuleView, String> ruleTableColName;
    @FXML private TableColumn<RuleView, Rule.Severity> ruleTableColSeverity;
    @FXML private TableView<MutationView> mutationTable;
    @FXML private TableColumn<MutationView, String> mutationTableColName;
    @FXML private TableColumn<MutationView, Integer> mutationTableColChance;
    @FXML private TableColumn<MutationView, List<String>> mutationTableColArguments;

    private final StringProperty generationLabelText = new SimpleStringProperty("0");
    private final StringProperty populationSizeLabelText = new SimpleStringProperty("0");
    private final StringProperty selectionNameLabelText = new SimpleStringProperty("");
    private final StringProperty crossOverNameLabelText = new SimpleStringProperty("");
    private final StringProperty crossOverCutSizeLabelText = new SimpleStringProperty("0");
    private final StringProperty elitismSizeLabelText = new SimpleStringProperty("0");
    private final StringProperty hardRuleWeightLabelText = new SimpleStringProperty("0");
    private final StringProperty softRuleWeightLabelText = new SimpleStringProperty("0");

    private MainController mainController;

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        generationDisplay.textProperty().bind(generationLabelText);
        populationSizeDisplay.textProperty().bind(populationSizeLabelText);
        selectionNameDisplay.textProperty().bind(selectionNameLabelText);
        crossOverNameDisplay.textProperty().bind(crossOverNameLabelText);
        crossOverCutSizeDisplay.textProperty().bind(crossOverCutSizeLabelText);
        elitismSizeDisplay.textProperty().bind(elitismSizeLabelText);
        hardRuleWeightDisplay.textProperty().bind(hardRuleWeightLabelText);
        softRuleWeightDisplay.textProperty().bind(softRuleWeightLabelText);

        initSubjectTable();
        initTeacherTable();
        initGradeTable();
        initRuleTable();
        initMutationTable();
    }

    private void initSubjectTable()
    {
        subjectTableColID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        subjectTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void initTeacherTable()
    {
        teacherTableColID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        teacherTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherTableColSubjectsTeaching.setCellValueFactory(new PropertyValueFactory<>("subjects"));

        teacherTableColSubjectsTeaching.setCellFactory(col -> new TableCell<TeacherView, List<SubjectView>>() {
            @Override
            public void updateItem(List<SubjectView> subjectsTaught, boolean empty) {
                super.updateItem(subjectsTaught, empty);
                if(empty)
                    setText(null);
                else
                {
                    setText(subjectsTaught.stream()
                            .map(subjectView -> subjectView.getName() + " (ID = " + subjectView.getID() + ")")
                            .collect(Collectors.joining(System.lineSeparator())));
                }
            }
        });
    }

    private void initGradeTable()
    {
        gradeTableColID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        gradeTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        gradeTableColSubjects.setCellValueFactory(new PropertyValueFactory<>("subjects"));
        gradeTableColHours.setCellValueFactory(new PropertyValueFactory<>("subjectsHours"));

        gradeTableColSubjects.setCellFactory(col -> new TableCell<GradeView, List<SubjectView>>() {
            @Override
            public void updateItem(List<SubjectView> subjectsLearned, boolean empty) {
                super.updateItem(subjectsLearned, empty);
                if(empty)
                    setText(null);
                else
                {
                    setText(subjectsLearned.stream()
                            .map(subjectView -> subjectView.getName() + " (ID = " + subjectView.getID() + ")")
                            .collect(Collectors.joining(System.lineSeparator())));
                }
            }
        });

        gradeTableColHours.setCellFactory(col -> new TableCell<GradeView, List<Integer>>() {
            @Override
            public void updateItem(List<Integer> subjectsHours, boolean empty) {
                super.updateItem(subjectsHours, empty);
                if(empty)
                    setText(null);
                else
                {
                    setText(subjectsHours.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(System.lineSeparator())));
                }
            }
        });
    }

    private void initRuleTable()
    {
        ruleTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ruleTableColSeverity.setCellValueFactory(new PropertyValueFactory<>("severity"));
    }

    private void initMutationTable()
    {
        mutationTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        mutationTableColChance.setCellValueFactory(new PropertyValueFactory<>("chance"));
        mutationTableColArguments.setCellValueFactory(new PropertyValueFactory<>("arguments"));

        mutationTableColChance.setCellFactory(col -> new TableCell<MutationView, Integer>() {
            @Override
            public void updateItem(Integer chance, boolean empty) {
                super.updateItem(chance, empty);
                setText(empty ? null : (chance + "%"));
            }
        });

        mutationTableColArguments.setCellFactory(col -> new TableCell<MutationView, List<String>>() {
            @Override
            public void updateItem(List<String> arguments, boolean empty) {
                super.updateItem(arguments, empty);
                if(empty)
                    setText(null);
                else
                    setText(arguments.stream().collect(Collectors.joining(System.lineSeparator())));
            }
        });
    }


    public void writeInformation(GeneralAlgorithmInformation information)
    {
        clearTables();

        writeLabels(information);

        writeSubjectTable(information);
        writeTeacherTable(information);
        writeGradeTable(information);
        writeRuleTable(information);
        writeMutationTable(information);
    }

    private void clearTables()
    {
        subjectTable.getItems().clear();
        teacherTable.getItems().clear();
        gradeTable.getItems().clear();
        ruleTable.getItems().clear();
        mutationTable.getItems().clear();
    }

    private void writeLabels(GeneralAlgorithmInformation information)
    {
        generationLabelText.set(information.getGenerationNumber().toString());
        populationSizeLabelText.set(information.getPopulationSize().toString());
        selectionNameLabelText.set(information.getDtoSelection().getSelectionName());
        crossOverNameLabelText.set(information.getDtoCrossOver().getCrossOverName());
        crossOverCutSizeLabelText.set(information.getDtoCrossOver().getCutSize().toString());
        elitismSizeLabelText.set(information.getDtoSelection().getElitism().toString());

        boolean noRules = information.getAllDtoRules().size() == 0;
        Integer hardWeight = noRules ? 0 : information.getAllDtoRules().get(0).getHardRuleWeight();
        Integer softWeight = noRules ? 0 : (100 - information.getAllDtoRules().get(0).getHardRuleWeight());

        hardRuleWeightLabelText.set(hardWeight.toString());
        softRuleWeightLabelText.set(softWeight.toString());
    }

    private void writeSubjectTable(GeneralAlgorithmInformation information)
    {
        subjectTable.getItems().addAll(information.getAllDtoSubjects().stream()
                .map(SubjectView::new)
                .collect(Collectors.toList()));
    }

    private void writeTeacherTable(GeneralAlgorithmInformation information)
    {
        teacherTable.getItems().addAll(information.getAllDtoTeachers().stream()
                .map(dtoTeacher -> new TeacherView(dtoTeacher, new HashSet<>(information.getAllDtoSubjects())))
                .collect(Collectors.toList()));
    }

    private void writeGradeTable(GeneralAlgorithmInformation information)
    {
        gradeTable.getItems().addAll(information.getAllDtoGrades().stream()
                .map(dtoGrade -> new GradeView(dtoGrade, new HashSet<>(information.getAllDtoSubjects())))
                .collect(Collectors.toList()));
    }

    private void writeRuleTable(GeneralAlgorithmInformation information)
    {
        ruleTable.getItems().addAll(information.getAllDtoRules().stream()
                .map(RuleView::new)
                .collect(Collectors.toList()));
    }

    private void writeMutationTable(GeneralAlgorithmInformation information)
    {
        mutationTable.getItems().addAll(information.getAllDtoMutations().stream()
                .map(MutationView::new)
                .collect(Collectors.toList()));
    }

    // getters for all properties and sub controllers

    public StringProperty generationLabelTextProperty() {
        return generationLabelText;
    }

    public StringProperty populationSizeLabelTextProperty() {
        return populationSizeLabelText;
    }

    public StringProperty selectionNameLabelTextProperty() {
        return selectionNameLabelText;
    }

    public StringProperty crossOverNameLabelTextProperty() {
        return crossOverNameLabelText;
    }

    public StringProperty crossOverCutSizeLabelTextProperty() {
        return crossOverCutSizeLabelText;
    }

    public StringProperty elitismSizeLabelTextProperty() {
        return elitismSizeLabelText;
    }
}
