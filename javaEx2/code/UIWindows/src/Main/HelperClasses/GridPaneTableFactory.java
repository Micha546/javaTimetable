package Main.HelperClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableTuples;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Comparator;
import java.util.Set;
import java.util.function.DoubleSupplier;

public class GridPaneTableFactory {

    private final GridPane table;

    public GridPaneTableFactory(int days, int hours)
    {
        this.table = new GridPane();

        table.setMaxHeight(Region.USE_COMPUTED_SIZE);
        table.setMaxWidth(Region.USE_COMPUTED_SIZE);
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);
        table.setPrefWidth(Region.USE_COMPUTED_SIZE);
        table.setMinHeight(Region.USE_PREF_SIZE);
        table.setMinWidth(Region.USE_PREF_SIZE);

        for(int i = 0; i <= days; ++i)
        {
            table.addColumn(i);
            table.getColumnConstraints().add(new ColumnConstraints());
        }

        for(int i = 0; i <= hours; ++i)
        {
            table.addRow(i);
            table.getRowConstraints().add(new RowConstraints());
        }


        for(int i = 1; i <= days; ++i)
        {
            HBox hBox = new HBox();
            Label dayNumber = new Label(String.valueOf(i));
            hBox.getChildren().add(dayNumber);
            hBox.alignmentProperty().set(Pos.CENTER);
            HBox.setMargin(dayNumber, new Insets(5,0,5,0));
            table.add(hBox, i, 0);
        }

        for(int i = 1; i <= hours; ++i)
        {
            HBox hBox = new HBox();
            Label hourNumber = new Label(String.valueOf(i));
            hBox.getChildren().add(hourNumber);
            hBox.alignmentProperty().set(Pos.CENTER);
            HBox.setMargin(hourNumber, new Insets(0,5,0,5));
            table.add(hBox, 0, i);
        }

        table.setGridLinesVisible(true);
    }

    public void setCellTuples(Set<DTOTimeTableTuples> tuplesOfCell, int hour, int day, boolean byTeacher)
    {
        VBox vBox = new VBox();

        tuplesOfCell.forEach(tuple -> {
            Label gradeOrTeacherLabel = new Label(byTeacher
                    ? "Grade: " + tuple.getGrade().getName() + " (ID = " + tuple.getGrade().getId() + ")"
                    : "Teacher: " + tuple.getTeacher().getName() + " (ID = " + tuple.getTeacher().getId() + ")");
            Label subjectLabel = new Label("Subject: " + tuple.getSubject().getName()
                    + " (ID = " + tuple.getSubject().getId() + ")");

            gradeOrTeacherLabel.autosize();
            subjectLabel.autosize();


            vBox.getChildren().addAll(gradeOrTeacherLabel, subjectLabel);
            VBox.setMargin(gradeOrTeacherLabel, new Insets(5,5,0,5));
            VBox.setMargin(subjectLabel, new Insets(0,5,5,5));

            VBox.setVgrow(gradeOrTeacherLabel, Priority.ALWAYS);
            VBox.setVgrow(subjectLabel, Priority.ALWAYS);

            /*vBox.setMinWidth(Math.max(
                            vBox.getMinWidth(),
                            Math.max(gradeOrTeacherLabel.getPrefWidth(), subjectLabel.getPrefWidth())
                    )
            );*/
        });

        vBox.alignmentProperty().set(Pos.CENTER_LEFT);
        table.add(vBox, day, hour);
        GridPane.setVgrow(vBox, Priority.ALWAYS);
        GridPane.setHgrow(vBox, Priority.ALWAYS);
    }

    public GridPane create()
    {
        double minWidth = table.getColumnConstraints().stream()
                .mapToDouble(ColumnConstraints::getPrefWidth)
                .max()
                .orElse(0);

        table.getColumnConstraints().forEach(constraint -> constraint.setMinWidth(minWidth));

        return table;
    }
}
