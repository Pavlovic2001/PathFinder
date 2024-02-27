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

public class NewConnectionAlert extends Alert {

    private final TextField nameField = new TextField();
    private final TextField weightField = new TextField();

    public NewConnectionAlert(City cityFrom, City cityTo){
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(10);
        setTitle("Connection");
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Time:"), weightField);
        setHeaderText("Connection from " + cityFrom.getName() + " to " + cityTo.getName());
        getDialogPane().setContent(grid);
    }

    public String getName(){
        return nameField.getText();
    }

    public int getWeight(){
        return Integer.parseInt(weightField.getText());
    }

}
