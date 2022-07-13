package Main.HelperClasses;

import javafx.scene.control.ListCell;


public class MutationListViewCell extends ListCell<MutationChangerListViewCell> {

    @Override
    protected void updateItem(MutationChangerListViewCell mutationChanger, boolean empty) {
        super.updateItem(mutationChanger, empty);
        setText(null);
        setGraphic(null);

        if (mutationChanger != null && !empty) {

            mutationChanger.getDeleteRowButton().setOnAction(event -> {
                getListView().getItems().remove(getItem());
                mutationChanger.isDeletedProperty().set(true);
            });
            setGraphic(mutationChanger.getHbox());
        }
    }
}
