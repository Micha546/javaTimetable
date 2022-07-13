package Main.HelperClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

public class MutationChangerListViewCell {

    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty chance = new SimpleIntegerProperty();
    private final List<Object> mutationArguments = new ArrayList<>();
    private final List<String> argumentsAsStringList = new ArrayList<>();
    private final BooleanProperty isDeleted = new SimpleBooleanProperty(false);

    HBox hbox = new HBox();
    Label mutationNameLabel = new Label("");
    Pane separatorPane1 = new Pane();
    HBox probabilityHBox = new HBox();
    Label sliderNameLabel = new Label("Chance:");
    Slider mutationProbabilitySlider = new Slider();
    Label sliderValueLabel = new Label();
    Pane separatorPane2 = new Pane();
    HBox argumentsHBox = new HBox();
    Label arguments = new Label();
    Pane separatorPane3 = new Pane();
    Button deleteRowButton = new Button("Del");

    public MutationChangerListViewCell(DTOTimeTableMutation dtoTimeTableMutation)
    {
        this.name.set(dtoTimeTableMutation.getMutationName());
        this.chance.set(dtoTimeTableMutation.getProbability());
        this.mutationArguments.addAll(dtoTimeTableMutation.getMutationArgs());
        this.argumentsAsStringList.addAll(dtoTimeTableMutation.getArgsDescriptionList());

        setUpCellStructure();
    }

    private void setUpCellStructure()
    {
        setUpMutationName();

        setUpProbabilityHBox();

        setUpArgumentsHBox();

        setUpMainHBox();
    }

    private void setUpMutationName()
    {
        mutationNameLabel.setMinWidth(120);
        mutationNameLabel.textProperty().bind(name);
    }

    private void setUpProbabilityHBox()
    {
        probabilityHBox.getChildren().addAll(
                sliderNameLabel,
                mutationProbabilitySlider,
                sliderValueLabel);

        mutationProbabilitySlider.adjustValue(chance.get());

        mutationProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mutationProbabilitySlider.setValue(Math.round(newValue.doubleValue()));
        });

        sliderValueLabel.textProperty().bind(mutationProbabilitySlider.valueProperty().asString("%.0f"));
        chance.bind(mutationProbabilitySlider.valueProperty());

        probabilityHBox.setMinWidth(225);
    }

    private void setUpArgumentsHBox()
    {
        argumentsHBox.getChildren().add(arguments);

        arguments.textProperty().set(String.join(", ", argumentsAsStringList));

        argumentsHBox.setMinWidth(225);
    }

    private void setUpMainHBox()
    {
        hbox.getChildren().addAll(
                mutationNameLabel,
                separatorPane1,
                probabilityHBox,
                separatorPane2,
                argumentsHBox,
                separatorPane3,
                deleteRowButton);

        HBox.setHgrow(separatorPane1, Priority.ALWAYS);
        HBox.setHgrow(separatorPane2, Priority.ALWAYS);
        HBox.setHgrow(separatorPane3, Priority.ALWAYS);
        HBox.setMargin(mutationNameLabel, new Insets(5, 0, 0, 0));
        HBox.setMargin(sliderNameLabel, new Insets(5, 4, 0, 0));
        HBox.setMargin(mutationProbabilitySlider, new Insets(6, 0, 0, 0));
        HBox.setMargin(sliderValueLabel, new Insets(5, 0, 0, 4));
        HBox.setMargin(arguments, new Insets(5, 5, 0, 0));
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getChance() {
        return chance.get();
    }

    public IntegerProperty chanceProperty() {
        return chance;
    }

    public List<Object> getMutationArguments() {
        return mutationArguments;
    }

    public List<String> getArgumentsAsStringList() {
        return argumentsAsStringList;
    }

    public boolean isIsDeleted() {
        return isDeleted.get();
    }

    public BooleanProperty isDeletedProperty() {
        return isDeleted;
    }

    public HBox getHbox() {
        return hbox;
    }

    public Label getMutationNameLabel() {
        return mutationNameLabel;
    }

    public Pane getSeparatorPane1() {
        return separatorPane1;
    }

    public HBox getProbabilityHBox() {
        return probabilityHBox;
    }

    public Label getSliderNameLabel() {
        return sliderNameLabel;
    }

    public Slider getMutationProbabilitySlider() {
        return mutationProbabilitySlider;
    }

    public Label getSliderValueLabel() {
        return sliderValueLabel;
    }

    public Pane getSeparatorPane2() {
        return separatorPane2;
    }

    public HBox getArgumentsHBox() {
        return argumentsHBox;
    }

    public Label getArguments() {
        return arguments;
    }

    public Pane getSeparatorPane3() {
        return separatorPane3;
    }

    public Button getDeleteRowButton() {
        return deleteRowButton;
    }
}
