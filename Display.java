package Note_App;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import static Note_App.Main.ANSI_RESET;
import static Note_App.Main.handleInput;
public class Display extends Application {

    /**
     * Text file parsing (save and load data from txt files, not serialized objects)
     *      make it so that edits to txt files update the data of the notes prgm
     *
     * Make the display side a list of buttons and texts
     *      a button is a dir and if you click on it, the dir opens
     *      the texts are blocks and clicking on them will edit them
     *      have a + button in all dirs to make a new dir or block
     */

    private static Stage window;
    private static Scene scene1;
    private static HBox main_layout;
    private static VBox main_vbox, path_vbox, input_vbox, output_vbox, print_vbox;
    private static TextArea input, output, path, print;
    private static String current_color;
    private static boolean isPrint = true;
    private int input_height = 10, input_width = 260;
    private static final String filename = "data", txtFilename = "Notes.txt";
    private static int autoComplete_i = -1, commandComplete_i = -1;
    private static String typing = "";
    public static ArrayList<String> commands = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        current_color = ANSI_RESET;
        Main.current_color = current_color;
        Directory.main_dir = (Directory) DataManager.loadData(filename);
        Directory.current_dir = Directory.main_dir;

        window = primaryStage;//this is the main window
        window.setTitle("Notes");
        window.setOnCloseRequest(e -> {
            e.consume();
            save();
            Platform.exit();
        });

        path = new TextArea(removeChars(Directory.current_dir.path + "/")); //where the path is displayed for the user
        path.setEditable(false);
        path_vbox = new VBox();
        path_vbox.setPrefSize(input_width, 10);
        path_vbox.getChildren().addAll(path);

        input = new TextArea(); //user input
        input.setMinWidth(input_width);
        input.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    //this updates vairables to their ground states and runs handleInput()
                    commands.remove(commands.size()-1);
                    commands.add(input.getText());
                    commands.add("");
                    autoComplete_i = -1;
                    commandComplete_i = -1;
                    typing = "";

                    event.consume();
                    update(input.getText());
                } else if (event.getCode().equals(KeyCode.TAB)) {
                    //updates variables and runs autocomplete
                    autoComplete_i++;
                    event.consume();
                    typing = autoComplete(typing);
                } else if (event.getCode().equals(KeyCode.UP)) {
                    if (commandComplete_i == -1) {
                        commandComplete_i = commands.size();
                        if(commands.size() > 1) commandComplete_i--;
                    }
                    commandComplete_i--;
                    input.setText(commands.get(commandComplete_i%commands.size()));
                } else if(event.getCode().equals(KeyCode.DOWN)) {
                    commandComplete_i++;
                    input.setText(commands.get(commandComplete_i%commands.size()));
                } else if (event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.DELETE)) {
                    typing = "";
                }
            }
        });
        input_vbox = new VBox();
        input_vbox.setPrefSize(input_width, input_height);
        input_vbox.setMaxHeight(5);
        input_vbox.getChildren().addAll(input);

        //output is the area that gives errors in code
        output = new TextArea();
        output.setPrefHeight(1000);
        output.setEditable(false);
        output_vbox = new VBox();
        output_vbox.setPrefWidth(input_width);
        output_vbox.getChildren().addAll(output);

        //this holds the path, input and output
        main_vbox = new VBox();
        main_vbox.setPrefSize(input_width, 1000);
        main_vbox.getChildren().addAll(path_vbox, input_vbox, output_vbox);

        //this shows the notes that the user has taken
        print = new TextArea(removeChars(Directory.current_dir.print()));
        print.setEditable(false);
        print_vbox = new VBox();
        print_vbox.setPrefSize(3000-input_width, 1000);
        print_vbox.getChildren().addAll(print);

        //this is the overall main scene display, it is horizantally oriented
        main_layout = new HBox(20);
        main_layout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        main_layout.getChildren().addAll(main_vbox, print_vbox);

        scene1 = new Scene(main_layout, 600, 600, Color.DARKGRAY);
        scene1.getStylesheets().add(getClass().getResource("layout.css").toExternalForm());
        window.setScene(scene1);
        window.setMaximized(true);
        window.show();

        commands.add(""); //so there is no OoB error
    }

    public static void update(String command) {
        //this whole thing changes the gui when the ures inputs a command, so that they can see their changes.
        //runs each time the user inputs new commands
        command = Main.removeSpacesAround(command.replace(System.getProperty("line.separator"), "").replace("\t", ""));
        if (command.equalsIgnoreCase("ter prgm")) {
            save();
            Platform.exit();
        }
        //basic commands
        if (command.startsWith("print all")) {
            isPrint = false;
        } else if (command.startsWith("print")) {
            isPrint = true;
        }  else if (command.startsWith("clear")) {
            output.setText("");
        } else {
            //more complex inputs
            String text = removeChars(handleInput(command));
            if (text.length() > 0) {
                text = "\n" + text;
            }
            output.setText(output.getText() + text);
        }
        if (isPrint) {
            print.setText(removeChars(Directory.current_dir.print()));
        } else {
            print.setText(removeChars(Directory.current_dir.printall("")));
        }
        input.setText("");
        input.setText(input.getText().replace(System.getProperty("line.separator"), "").replace("\t", ""));
        path.setText(Directory.current_dir.getPath() + "/");
    }

    public static String removeChars(String returned_val) {
        //removes chars that dont work in gui
        returned_val = returned_val.replace("\u001B[0m", "");
        returned_val = returned_val.replace("\u001B[34m", "");
        returned_val = returned_val.replace("\u001B[32m", "");
        returned_val = returned_val.replace("\u001B[33m", "");
        returned_val = returned_val.replace("\u001B[4m", "");
        returned_val = returned_val.replace("\u001B[31m", "");
        returned_val = returned_val.replace("\u001B[35m", "");
        returned_val = returned_val.replace("\u001B[36m", "");
        return returned_val;
    }

    public static void save() {
        //saves data in serialized object and txt file
        DataManager.saveData(Directory.main_dir, filename);
        TextFile.clearText(txtFilename);
        TextFile.addLine(txtFilename, removeChars(Directory.main_dir.printall("")));
        TextFile.deleteLine(txtFilename, 0);
        TextFile.deleteLine(txtFilename, 0);
    }

    //algorithm for apcsp create project
    public static String autoComplete(String typing) {
        String[] words = input.getText().split(" "); //breaks the input into parts so it can be parsed
        if (typing.equalsIgnoreCase("")) {
            typing = words[words.length - 1]; //gets the last word that the user was typing
        }

        String newTyping = completeChildren(words, typing); //sub algorithm 1, this searches through every child in the curent directory and returns a string of what the user is typing
        //it also replaces what the user is typing with the specific index of the list of the possible child directories
        if (newTyping!=null) return newTyping;

        if (Directory.current_dir.name.startsWith(typing)) {
            //auto completes the current directiory name
            words[words.length - 1] = Directory.current_dir.name;
            String newText = reCreate(words, " "); //sub algorithm 2. This turns a list into a string, it is the reverse procedure of .splitBy(@param text);
            input.setText(newText.replace(System.getProperty("line.separator"), "").replace("\t", ""));
        }
        return typing;
    }

    public static String completeChildren(String[] words, String typing) {
        ArrayList<String> all_names = allNames(typing); //finds all children directories of the current directory
        if(all_names.size() == 0) {
            return null;
        }
        words[words.length-1] = all_names.get(autoComplete_i % all_names.size()).replace(" ", "_"); //grabs element i in list of possibilities

        String newText = reCreate(words, " "); //sub algorithm 2. This turns a list into a string, it is the reverse procedure of .splitBy(@param text);

        input.setText(newText.replace(System.getProperty("line.separator"), "").replace("\t", ""));
        //replaces the text with the newText
        return typing;
    }

    public static String reCreate(String[] words, String split_by) {
        //takes an array of strings are combines them by splitting them up with @param split_by
        String original = "";
        for (int i = 0; i < words.length -1; i++) {
            //starts combining each words
            original += words[i] + split_by;
        }
        //once the words are combined into one, it returns the new word
        return original + words[words.length - 1];
    }

    public static ArrayList<String> allNames(String text) {
        //searches through all of the children in the current directory and finds all of the ones that have a name staring with @param text
        ArrayList<String> directories = new ArrayList<>();
        for (Content child : Directory.current_dir.getChildren()) {
            if(startsWithIgnoreCase(child.name, text) && !child.name.equalsIgnoreCase("") && !text.equalsIgnoreCase("")) {
                directories.add(child.name);
            }
        }
        return directories;
    }


    public static boolean startsWithIgnoreCase(String word, String seq) {
        //fixes error for capitalization
        return word.toLowerCase().startsWith(seq.toLowerCase());
    }
}