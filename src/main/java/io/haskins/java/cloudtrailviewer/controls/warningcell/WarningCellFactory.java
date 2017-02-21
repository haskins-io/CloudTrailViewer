package io.haskins.java.cloudtrailviewer.controls.warningcell;


import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

/**
 * Created by markhaskins on 19/02/2017.
 */
public class WarningCellFactory<E> implements Callback<TableColumn<WarningCell,String>, TableCell<WarningCell,String>> {

    private String normalStyle = ".table-cell{-fx-border-color: white; -fx-text-fill: black;-fx-border-width: 0;};",
            wrongStyle = "-fx-border-color: red; -fx-border-width: 1; -fx-text-fill: tomato;";

    private final int colIndex;

    public WarningCellFactory(int colIndex) {
        this.colIndex = colIndex;
    }

    @Override
    public TableCell<WarningCell, String> call(TableColumn<WarningCell, String> col) {
        return createTableCell(col);
    }

    private TextFieldTableCell<WarningCell, String> createTableCell(TableColumn<WarningCell, String> col) {

        TextFieldTableCell<WarningCell, String> cell = new TextFieldTableCell<WarningCell, String>(new DefaultStringConverter()) {

            @Override
            public void updateItem(String arg0, boolean empty) {

                this.setEditable( true );

                super.updateItem(arg0, empty);

                if( !empty ) {
                    this.setText( arg0 );
                    WarningCell warnableObject = (WarningCell) this.getTableRow().getItem();

                    if( warnableObject != null && warnableObject.displayWarning(colIndex) ) {
                        this.setStyle(wrongStyle);

                    } else {
                        this.setStyle(normalStyle);
                    }
                } else {
                    this.setText( null );  // clear from recycled obj
                    this.setStyle(normalStyle);
                }
            }
        };

        return cell;
    }
}