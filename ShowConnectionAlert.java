// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ShowConnectionAlert extends Alert {

    public ShowConnectionAlert(City cityFrom, City cityTo, String name, int weight ){
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();

        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(10);
        setTitle("Connection");
        TextField nameField = new TextField();
        grid.addRow(0, new Label("Name:"), nameField);
        TextField weightField = new TextField();
        grid.addRow(1, new Label("Time:"), weightField);
        nameField.setText(name);
        weightField.setText(String.valueOf(weight));
        setHeaderText("Connection from " + cityFrom.getName() + " to " + cityTo.getName());
        getDialogPane().setContent(grid);
        getButtonTypes().remove(ButtonType.CANCEL);
        nameField.setEditable(false);
        weightField.setEditable(false);
    }

}
