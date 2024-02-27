// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import javafx.scene.shape.Line;

public class Connection extends Line {

    public Connection(City cityFrom, City cityTo){
        setStartX(cityFrom.getCenterX());
        setStartY(cityFrom.getCenterY());
        setEndX(cityTo.getCenterX());
        setEndY(cityTo.getCenterY());
        setStrokeWidth(3);
    }



}
