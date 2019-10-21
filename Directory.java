package Note_App;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.Serializable;
import java.util.ArrayList;
public class Directory extends Content implements Serializable {

    //variables
    public static Directory current_dir, main_dir;
    public String path;
    private Directory parent;
    private ArrayList<Content> children = new ArrayList<>();

    //getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public ArrayList<Content> getChildren() {
        return children;
    }

    public Directory(String name, Directory parent) {
        //constructor for this object
        //this is for the children dirs
        super(name);
        this.name = name;
        this.parent = parent;
        this.path = parent.getPath() + "/" + name; //updates the path
        path_of_parent = parent.getPath();
    }
    public Directory(String name) {
        //constructor for this object
        //this is for the main dir
        super(name);
        this.parent = null;
        this.path = name;
        path_of_parent = null;
    }

    public String ls() {
        //this lists all the children dirs in this parent
        String names = "";
        if (children.size() > 0) {
            int num = children.size(); //this keeps track of the number of blocks in the dir
            for (Content child : children) {
                if (!child.name.equalsIgnoreCase("")) {
                    //makes sure not to count blocks
                    names += child.name + "\n";
                    num --;
                }
            }
            names += num + " text(s)"; //this shows the number of blocks
            return names;
        }
        return "No children";
    }

    public String cd() {
        //this goes to the parent dir
        if (this.parent != null) {
            //ensures no @nullPointer errors
            current_dir = this.parent;
            return"";
        } else {
            return Main.ANSI_RED + "Current directory has no parent" + Main.ANSI_RESET;
        }
    }
    public String cd(String newDirName) {
        //this changes the current directory to some dir in this parent dir named @param newDirName
        if (newDirName.equalsIgnoreCase("..")) {
            //this cds to the parent dir
            cd();
            return "";
        }
        if (children.size() > 0) {
            for (Content child : children) {
                if (equals(child.name, newDirName)) {
                    //finds the one matching the name and changes to it
                    current_dir = (Directory) child;
                    return "";
                }
            }
            return Main.ANSI_RED + "Directory does not exist" + Main.ANSI_RESET;
        } else {
            return Main.ANSI_RED + "Current directory has no children" + Main.ANSI_RESET;
        }
    }

    public void mkblock(String text) {
        //adds a block of text for the notes
        int id = 1;
        if (children.size() > 0) {
            //this for loop makes the correct id for the block
            for (Content child : children) {
                if (child.name.equalsIgnoreCase("")) {
                    id ++;
                }
            }
        }
        children.add(new Block(text, this, id));
    }
    public String mkdir(String name) {
        //makes a new directory with this as its parent
        if (name.equalsIgnoreCase("") || name.contains("/") || name.equalsIgnoreCase(this.name) || name.equalsIgnoreCase("notes")) {
            //this is to ensure no invalid or reused names
            return Main.ANSI_RED + "Invalid name" + Main.ANSI_RESET;
        }
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase(name)) {
                    //ensures that no directories exist with the same path
                    return Main.ANSI_RED + "Directory with the same path already exists" + Main.ANSI_RESET;
                }
            }
        }
        children.add(new Directory(name, this));
        return "";
    }

    public String remdir(String name) {
        //this will try to delete the dir named @param dir, but will stop if it has children
        if (children.size() > 0) {
            for (Content child : children) {
                if (equals(child.name, name)) {
                    if (((Directory) child).children.size() > 0) {
                        //does not romeve the child because it has children
                        return Main.ANSI_RED + "There are files in the directory" + Main.ANSI_RESET;
                    } else {
                        //removes the child, since it has no children
                        children.remove(child);
                    }
                    return"";
                }
            }
            //this occurs if the directory does not exist
            return Main.ANSI_RED + "That directory does not exist" + Main.ANSI_RESET;
        } else {
            //it cannot itar the dir
            return Main.ANSI_RED + "Current directory has no children"+ Main.ANSI_RESET;
        }
    }
    public String remdirForced(String name) {
        //this will delete the dir named @param name, and it will ignore if the dir has children
        if (children.size() > 0) {
            for (Content child : children) {
                if (equals(child.name, name)) {
                    children.remove(child);
                    child = null; //this is needed, idk why its needed, but for some reason it is
                    return "";
                }
            }
            return "";
        } else {
            return Main.ANSI_RED + "Current directory has no children" + Main.ANSI_RESET;
        }
    }

    public ArrayList<String> search(String name) {
        //this searches the this dir for any dirs names @param name
        ArrayList<String> paths = new ArrayList<>();

        for (Content child : this.getChildren()) { //searches through every child to find all childern with @param name
            if (!child.name.equalsIgnoreCase("")) {
                if (equals(child.name, name)) {
                    paths.add(((Directory) child).getPath());
                    //add the path of the directory to the list of directories
                }
                paths.addAll(((Directory) child).search(name));
            }
        }
        return paths;
        //this returns a list of paths with the name @param name
    }

    public String print() {
        //prints only the children which are dirs and the blocks in the children
        if (children.size() > 0) {
            String message = "";
            for (Content child : children) {
                if (child.name.equalsIgnoreCase("")) {
                    //adds the entire text for a block
                    message += child.toString() + "\n";
                } else {
                    //only takes the name of the directory
                    message += "    " + child.color + child.name + Main.ANSI_RESET + "\n";
                }
            }
            return "\n" + this.name + "\n" + message;
        }
        return "\n" + this.name;
    }
    public String printall(String indent) {
        //this prints the entire content of the dir, and all of its children
        String message = "";
        message += "\n" + indent + this.color + Main.ANSI_UNDERLINE + this.name +":"+ Main.ANSI_RESET;

        //the @param indent is used to indent the text of child directories, so that it structures the notes nicely

        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase("")) {
                    //prints the entire text in a block
                    message += "\n" + indent + "    " + child.toString();
                } else {
                    //runs an itarative method to print all of the content in this child
                    message += indent + ((Directory)child).printall(indent + "    ");
                }
            }
        }
        return message;
    }

    public VBox compileVBox(boolean first) {
        //this is used to compile the VBox for print_all
        VBox vBox = new VBox();
        if (!first) vBox.setPadding(new Insets(0,0,0,25));

        Button name_button = new Button(this.name);
        name_button.setOnAction(e -> {
            Directory.current_dir = this;
            App.update("");
        });
        vBox.getChildren().addAll(name_button);

        for (Content content : this.getChildren()) {
            if (content.name.equalsIgnoreCase("")) {
                TextArea textArea = new TextArea(((Block) content).getId() + ". " + ((Block) content).getText());
                textArea.setEditable(false);
                textArea.setMaxHeight(8);
                vBox.getChildren().addAll(textArea);
            } else {
                vBox.getChildren().addAll(((Directory) content).compileVBox(false));
            }
        }
        return vBox;
    }

    public void add() {
        Stage window = new Stage();
        window.setTitle("Edit Text");
        window.initModality(Modality.APPLICATION_MODAL);//makes all other windows not interactable until this window closes
        window.setMinWidth(250);

        VBox layout = new VBox(10);
        layout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        TextArea text = new TextArea();
        text.setEditable(true);
        text.setPrefWidth(500);
        text.setPrefHeight(200);
        text.setWrapText(true);

        CheckBox dir_checkbox = new CheckBox("Directory");

        Button create = new Button("Create");
        Button cancel = new Button("Cancel");
        create.setOnAction(e -> {
            windowClose(dir_checkbox, window, text);
        });
        cancel.setOnAction(e -> window.close());
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(create, cancel);

        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER) && event.isShiftDown()) {
                    windowClose(dir_checkbox, window, text);
                }
            }
        });

        window.setOnCloseRequest(e -> {
            window.close();
        });

        layout.getChildren().addAll(text, dir_checkbox, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 300, Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("layout.css").toExternalForm());
        window.setScene(scene);
        window.setMaxHeight(300);
        window.setMaxWidth(500);
        window.showAndWait();
    }

    public void windowClose(CheckBox dir_checkbox, Stage window, TextArea text) {
        if (dir_checkbox.isSelected()) {
            String inputText = text.getText().replace(System.getProperty("line.separator"), " ");
            App.update("mkdir " + inputText);
        } else {
            App.update("+ " + text.getText());
        }
        window.close();
    }

    public boolean equals(String one, String two) {
        return one.replace(" ", "_").equalsIgnoreCase(two.replace(" ", "_"));
    }

    @Override
    public String toString() {
        return name;
    }
}