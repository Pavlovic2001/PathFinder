// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class FindPathAlert extends Alert {

    private final TextArea text = new TextArea();

    public FindPathAlert(City cityFrom, City cityTo){
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(10);
        setTitle("Message");
        setHeaderText("The path from " + cityFrom.getName() + " to " + cityTo.getName() + ":");
        text.setEditable(false);
        grid.addRow(0, text);
        getDialogPane().setContent(grid);
        getButtonTypes().remove(ButtonType.CANCEL);
    }

    public void addText(String s){
        text.appendText(s + "\n");
    }
}
