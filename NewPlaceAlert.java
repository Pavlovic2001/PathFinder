// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NewPlaceAlert extends Alert {

    private final TextField nameField = new TextField();

    public NewPlaceAlert(){
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();

        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(10);
        setTitle("Name");
        grid.addRow(0, new Label("Name of place:"), nameField);
        setHeaderText(null);
        getDialogPane().setContent(grid);
    }

    public String getName(){
        return nameField.getText();
    }
}
