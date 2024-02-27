// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import java.util.Objects;

public class Edge<T> {

    private final T destination;
    private final String name;
    private int weight;

    public Edge(T destination, String name, int weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);
        if(Double.isNaN(weight)){
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }

    public T getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int newWeight){
        if(newWeight < 0){
            throw new IllegalArgumentException();
        }
        weight = newWeight;
    }


    public String getName() {
        return name;
    }

    public boolean equals(Object other) {
        if (other instanceof Edge edge) {
            return Objects.equals(name, edge.name) &&
                    Objects.equals(destination, edge.destination);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name, destination);
    }

    public String toString() {
        return " to " +
                destination +
                " by " + name +
                " takes " + weight;
    }

}