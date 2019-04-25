package Note_App;
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
                if (child.name.equalsIgnoreCase(newDirName)) {
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
                if (child.name.equalsIgnoreCase(name)) {
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
                if (child.name.equalsIgnoreCase(name)) {
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
                if (child.name.equalsIgnoreCase(name)) {
                    paths.add(((Directory) child).getPath());
                    //add the path of the directory to the list of directories
                } else {
                    //searches all of the children dirs
                    paths.addAll(((Directory) child).search(name));
                }
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
}