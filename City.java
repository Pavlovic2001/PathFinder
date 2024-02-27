// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class City extends Circle {

    private final String name;

    public City (String name, double x, double y){
        super(x, y, 10);
        setFill(Color.BLUE);
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
