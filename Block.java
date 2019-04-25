package Note_App;

import java.io.Serializable;
import java.util.Scanner;

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
        //to change the text of the block
        System.out.println("Editing block with path "  + this.path_of_parent + "\nText: " + this.text);
        this.text = (new Scanner(System.in)).nextLine();
    }
}