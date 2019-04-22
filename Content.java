package Note_App;

import java.io.Serializable;

public class Content implements Serializable{
    protected String path_of_parent, name;
    protected String color;
    public String getPath_of_parent() {
        return path_of_parent;
    }
    public void setPath_of_parent(String path_of_parent) {
        this.path_of_parent = path_of_parent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Content(String name) {
        this.name = name;
        this.color = Main.current_color;
    }
}