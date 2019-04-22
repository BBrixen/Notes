package Note_App;

import java.io.Serializable;
import java.util.Scanner;

public class Block extends Content implements Serializable{

    private String text;
    private Directory parent;
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        if (this.color.equalsIgnoreCase(Main.ANSI_RESET)) {
            this.color = parent.color;
        }
        return this.color + id + ". " + text + Main.ANSI_RESET;
    }

    public Block(String text, Directory parent, int id) {
        super("");
        this.parent = parent;
        this.text = text;
        this.id = id;
        path_of_parent = parent.getPath();
    }

    public void editText() {
        System.out.println("Editing block with path "  + this.path_of_parent + "\nText: " + this.text);
        this.text = (new Scanner(System.in)).nextLine();
    }
}