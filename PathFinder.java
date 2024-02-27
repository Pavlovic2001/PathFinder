// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 007 
// Mihajlo Radotic mira0355
// Shaza Alkharat shal8055
// Alexander Pavlovic alpa7946
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class PathFinder extends Application {

    private BorderPane root;
    private Stage stage;
    private Pane center;
    private Button newPlaceb;
    private City cityFrom;
    private City cityTo;
    private ListGraph<City> listGraph = new ListGraph<>();
    private boolean changed;

    @Override
    public void start(Stage primaryStage){
        stage = primaryStage;
        root = new BorderPane();
        root.setMinWidth(618.0);

        VBox menuVbox = new VBox();
        root.setTop(menuVbox);

        center = new Pane();
        root.setCenter(center);
        center.setId("outputArea");

        MenuBar menu = new MenuBar();
        menu.setId("menu");
        Menu fileMenu = new Menu("File");
        fileMenu.setId("menuFile");

        MenuItem newMap = new MenuItem("New Map");
        newMap.setId("menuNewMap");
        newMap.setOnAction(new NewMapHandler());

        MenuItem open = new MenuItem("Open");
        open.setId("menuOpenFile");
        open.setOnAction(new OpenHandler());

        MenuItem save = new MenuItem("Save");
        save.setId("menuSaveFile");
        save.setOnAction(new SaveHandler());

        MenuItem saveImage = new MenuItem("Save Image");
        saveImage.setId("menuSaveImage");
        saveImage.setOnAction(new SaveImageHandler());

        MenuItem exit = new MenuItem("Exit");
        exit.setId("menuExit");
        exit.setOnAction(new ExitItemHandler());

        fileMenu.getItems().addAll(newMap, open, save, saveImage, exit);
        menu.getMenus().add(fileMenu);

        FlowPane topPane = new FlowPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(10);
        topPane.setPadding(new Insets(10));

        Button findPathb = new Button("Find Path");
        findPathb.setId("btnFindPath");
        findPathb.setOnAction(new FindPathHandler());

        Button showConnectionb = new Button("Show Connection");
        showConnectionb.setId("btnShowConnection");
        showConnectionb.setOnAction(new ShowConnectionHandler());

        newPlaceb = new Button("New Place");
        newPlaceb.setId("btnNewPlace");
        newPlaceb.setOnAction(new NewPlaceHandler());

        Button newConnectionb = new Button("New Connection");
        newConnectionb.setId("btnNewConnection");
        newConnectionb.setOnAction(new NewConnectionHandler());

        Button changeConnectionb = new Button("Change Connection");
        changeConnectionb.setId("btnChangeConnection");
        changeConnectionb.setOnAction(new ChangeConnectionWeightHandler());

        topPane.getChildren().addAll(findPathb, showConnectionb, newPlaceb, newConnectionb, changeConnectionb);

        menuVbox.getChildren().addAll(menu, topPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("PathFinder");
        primaryStage.setOnCloseRequest(new ExitHandler());
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    class ExitHandler implements EventHandler<WindowEvent>{
        @Override
        public void handle(WindowEvent event) {
            if(changed){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning!");
                alert.setHeaderText(null);
                alert.setContentText("Unsaved changes, continue anyway?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() != ButtonType.OK){
                    event.consume();
                }
            }
        }
    }

    class ExitItemHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

        }
    }

    class NewMapHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(changed){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning!");
                alert.setHeaderText(null);
                alert.setContentText("Unsaved changes, continue anyway?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() != ButtonType.OK){
                    return;
                }
            }
            listGraph = new ListGraph<>();
            center.getChildren().clear();
            Image europaImage = new Image("file:europa.gif");
            ImageView imageView = new ImageView(europaImage);
            center.getChildren().add(imageView);
            root.setCenter(center);
            cityFrom = null;
            cityTo = null;
            stage.sizeToScene();
            stage.centerOnScreen();
            changed = true;
        }
    }

    class NewPlaceHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            center.setOnMouseClicked(new NewPlaceClickHandler());
            center.setCursor(Cursor.CROSSHAIR);
            newPlaceb.setDisable(true);
        }
    }

    class NewPlaceClickHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            NewPlaceAlert alert = new NewPlaceAlert();
            Optional<ButtonType> result = alert.showAndWait();
            String name = alert.getName();
            if(result.isPresent() && result.get() != ButtonType.OK){
                newPlaceb.setDisable(false);
                center.setCursor(Cursor.DEFAULT);
                center.setOnMouseClicked(null);
                return;
            }
            City city = new City(name, x, y);
            city.setId(name);
            city.setOnMouseClicked(new CityClickHandler());
            listGraph.add(city);
            Text text = new Text(x + 5.0, y + 20.0, name);
            text.setDisable(true);
            center.getChildren().addAll(city, text);
            center.setOnMouseClicked(null);
            center.setCursor(Cursor.DEFAULT);
            newPlaceb.setDisable(false);
            changed = true;
        }
    }

    class CityClickHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            City c = (City) mouseEvent.getSource();

            if(c.getFill() == Color.RED){
                if(c == cityFrom){
                    cityFrom = null;
                } else{
                    cityTo = null;
                }
                c.setFill(Color.BLUE);
            } else {
                if(cityFrom == null){
                    cityFrom = c;
                    c.setFill(Color.RED);
                } else if (cityTo == null && c != cityFrom) {
                    cityTo = c;
                    c.setFill(Color.RED);
                }
            }
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                errorMessage("Two places must be selected!");
                return;
            }
            if(listGraph.getEdgeBetween(cityFrom, cityTo) != null){
                errorMessage("Only one connection can exist between cities!");
                return;
            }
            try{
                NewConnectionAlert alert = new NewConnectionAlert(cityFrom, cityTo);
                Optional<ButtonType> result = alert.showAndWait();
                
                if(result.isPresent() && result.get() != ButtonType.OK){
                    return;
                }
                
                String name = alert.getName();
                int weight = alert.getWeight();

                if(name.isEmpty()){
                    errorMessage("Name field can not be empty!");
                    return;
                }

                listGraph.connect(cityFrom, cityTo, name, weight);
                Connection c = new Connection(cityFrom, cityTo);
                c.setDisable(true);
                center.getChildren().add(c);
                changed = true;

            } catch (NumberFormatException e){
                errorMessage("Time field must be in digits!");
            }

        }
    }

    class ShowConnectionHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                errorMessage("Two places must be selected!");
                return;
            }
            if(listGraph.getEdgeBetween(cityFrom, cityTo) == null){
                errorMessage("There are no connections between selected cities");
                return;
            }

            Edge<City> edge = listGraph.getEdgeBetween(cityFrom, cityTo);
            ShowConnectionAlert alert = new ShowConnectionAlert(cityFrom, cityTo, edge.getName(), edge.getWeight());
            alert.showAndWait();


        }
    }

    class ChangeConnectionWeightHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                errorMessage("Two places must be selected!");
                return;
            }

            if(listGraph.getEdgeBetween(cityFrom, cityTo) == null){
                errorMessage("There are no connections between selected cities");
                return;
            }
            try{
                Edge<City> edge = listGraph.getEdgeBetween(cityFrom, cityTo);
                ChangeConnectionWeightAlert alert = new ChangeConnectionWeightAlert(cityFrom, cityTo, edge.getName());
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() != ButtonType.OK){
                    return;
                }

                listGraph.setConnectionWeight(cityFrom, cityTo, alert.getWeight());

            } catch (NumberFormatException e){
                errorMessage("Time field must be in digits!");
            }


        }
    }

    class FindPathHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                errorMessage("Two places must be selected!");
                return;
            }

            if(listGraph.getPath(cityFrom, cityTo) == null){
                FindPathAlert alert = new FindPathAlert(cityFrom, cityTo);
                alert.addText("No available path between two cities!");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK){
                    return;
                }
            }

            FindPathAlert alert = new FindPathAlert(cityFrom, cityTo);

            List<Edge<City>> list = listGraph.getPath(cityFrom, cityTo);

            int totalWeight = 0;
            for (Edge<City> edge : list) {
                alert.addText("to " + edge.getDestination().getName() + " by " + edge.getName() + " takes " + edge.getWeight());
                totalWeight += edge.getWeight();
            }

            alert.addText("Total " + totalWeight);
            alert.showAndWait();
        }
    }


    class OpenHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            try{
                if(changed){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning!");
                    alert.setHeaderText(null);
                    alert.setContentText("Unsaved changes, continue anyway?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.isPresent() && result.get() != ButtonType.OK){
                        return;
                    }
                }

                listGraph = new ListGraph<>();
                center.getChildren().clear();

                FileReader reader = new FileReader("europa.graph");
                BufferedReader in = new BufferedReader(reader);

                String line;

                line = in.readLine();
                Image europaImage = new Image(line);
                ImageView imageView = new ImageView(europaImage);
                center.getChildren().add(imageView);
                root.setCenter(center);
                stage.sizeToScene();
                stage.centerOnScreen();

                line = in.readLine();

                String[] nodeTokens = line.split(";");
                for(int i = 0; i < nodeTokens.length; i+=3){
                    String name = nodeTokens[i];
                    double x = Double.parseDouble(nodeTokens[i+1]);
                    double y = Double.parseDouble(nodeTokens[i+2]);
                    City city = new City(name, x, y);
                    city.setOnMouseClicked(new CityClickHandler());
                    city.setId(name);
                    Text text = new Text(x + 5.0, y + 20.0, name);
                    text.setDisable(true);
                    center.getChildren().addAll(city, text);
                    listGraph.add(city);
                }

                while ((line = in.readLine()) != null){
                    String[] edgeTokens = line.split(";");
                    String fromCity = edgeTokens[0];
                    String toCity = edgeTokens[1];
                    String edgeName = edgeTokens[2];
                    int weight = Integer.parseInt(edgeTokens[3]);
                    for (City city1 : listGraph.getNodes()){
                        for(City city2 : listGraph.getNodes()){
                            if(city1.getName().equals(fromCity) && city2.getName().equals(toCity)){
                                if(listGraph.getEdgeBetween(city1, city2) == null){
                                    listGraph.connect(city1, city2, edgeName, weight);
                                    Connection connection = new Connection(city1, city2);
                                    connection.setDisable(true);
                                    center.getChildren().add(connection);
                                    break;
                                }
                            }

                        }

                    }


                }
                in.close();
                reader.close();
                changed = false;
                cityFrom = null;
                cityTo = null;
            }catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Can't open file");
                alert.showAndWait();
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-Fel" + e.getMessage());
                alert.showAndWait();
            }
        }
    }


    class SaveHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            try{
                FileWriter writer = new FileWriter("europa.graph");
                PrintWriter out = new PrintWriter(writer);
                out.print("");

                out.println("file:europa.gif");

                Iterator<City> iter = listGraph.getNodes().iterator();
                while(iter.hasNext()){
                    City c = iter.next();
                    if(iter.hasNext()){
                        out.print(c.getName() + ";" + Math.floor(c.getCenterX()) + ";" + Math.floor(c.getCenterY()) + ";");
                    } else {
                        out.print(c.getName() + ";" + Math.floor(c.getCenterX()) + ";" + Math.floor(c.getCenterY()));
                    }
                }

                out.println();

                for(City cityFrom : listGraph.getNodes()) {
                    for(City cityTo : listGraph.getNodes()){
                        if(listGraph.getEdgeBetween(cityFrom, cityTo) != null){
                            Edge<City> edge = listGraph.getEdgeBetween(cityFrom, cityTo);
                            out.println(cityFrom.getName() + ";" + cityTo.getName() + ";" + edge.getName() + ";" + edge.getWeight());
                        }

                    }

                }

                out.close();
                writer.close();
                changed = false;
            }catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Can't save file");
                alert.showAndWait();
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-Fel" + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    class SaveImageHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            try{
                WritableImage image = root.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image,null);
                ImageIO.write(bufferedImage, "png", new File("capture.png"));

            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-Fel" + e.getMessage());
                alert.showAndWait();
            }
        }
    }


    private void errorMessage(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

}

