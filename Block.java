package Note_App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serializable;

public class Block extends Content implements Serializable {

    //variables
    private String text;
    private Directory parent;
    private int id;

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        //this prints the block
        if (this.color.equalsIgnoreCase(Main.ANSI_RESET)) {
            this.color = parent.color;
        }
        return this.color + id + ". " + text + Main.ANSI_RESET;
    }

    public Block(String text, Directory parent, int id) {
        //constructor for this object, inits variables
        super("");
        this.parent = parent;
        this.text = text;
        this.id = id;
        path_of_parent = parent.getPath();
    }

    public void editText() {
        Stage window = new Stage();
        window.setTitle("Edit Text");
        window.initModality(Modality.APPLICATION_MODAL);//makes all other windows not interactable until this window closes
        window.setMinWidth(250);

        VBox layout = new VBox(10);
        layout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        TextArea text = new TextArea(this.text);
        text.setEditable(true);
        text.setPrefWidth(500);
        text.setPrefHeight(300);
        text.setWrapText(true);

        window.setOnCloseRequest(e -> {
            this.setText(text.getText());
            window.close();
        });

        layout.getChildren().addAll(text);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 1000, 500, Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("layout.css").toExternalForm());
        window.setScene(scene);
        window.setMaxHeight(500);
        window.setMaxWidth(1000);
        window.showAndWait();
    }
}