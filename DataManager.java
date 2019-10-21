package Note_App;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class DataManager {

    public static boolean saveData(Object data, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            //this makes a new file output and object output to write the data
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(data); //writes the data to the object it is saving to
            out.close();
            return true; //finishes the method returning true for a successful save
        } catch (Exception ex) {
            return false; //unsuccessful save
        }
    }
    public static Object loadData(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            //makes a new file input and object input to read the data
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            Object data = (Object) in.readObject(); //reads the object data and makes an object out of that
            in.close();
            return data; //returns the data
        } catch (Exception ex) {
            if (confirmReload()) {
                return new Directory("Notes"); //makes a new data type for when the data fails to load
            } else {
                //doesnt make new data
                throw new IllegalArgumentException("No data to load");//this should be file not found exception, but im bad and it only works woth illegal argumant exception
            }
        }
    }

    static boolean reload;
    public static boolean confirmReload() {
        Stage window = new Stage();
        window.setTitle("Error loading data");
        window.initModality(Modality.APPLICATION_MODAL);//makes all other windows not interactable until this window closes
        window.setMinWidth(250);
        window.setOnCloseRequest(e -> {
            reload=false;
            window.close();
        });

        Text message = new Text("There was a problem loading the data.\n" +
                "You can attempt to reload the data by restarting the application,\n" +
                "or you can initialize new data, which results in deleting your old data.\n" +
                "It is recommended to attempt to reload, and if this fails,\n" +
                "save the data onto your computer (make a copy of Notes.txt and move it elsewhere)\n" +
                "and initialize new data.\n" +
                "You can recover your data by pasting it into the textfile named Notes.txt");

        Button create = new Button("Create new data");
        Button save = new Button("Save data");

        create.setOnAction(e -> {
            reload = true;
            window.close();
        });
        save.setOnAction(e ->{
            reload = false;
            window.close();
        });

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(create, save);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(message, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return reload;
    }
}