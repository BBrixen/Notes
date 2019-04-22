package Note_App;
import java.io.Serializable;
import java.util.ArrayList;
public class Directory extends Content implements Serializable {

    public static Directory current_dir, main_dir;
    public String path;
    private Directory parent;
    private ArrayList<Content> children = new ArrayList<>();

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
        super(name);
        this.name = name;
        this.parent = parent;
        this.path = parent.getPath() + "/" + name;
        path_of_parent = parent.getPath();
    }
    public Directory(String name) {
        super(name);
        this.parent = null;
        this.path = name;
        path_of_parent = null;
    }

    public String ls() {
        String names = "";
        if (children.size() > 0) {
            int num = children.size();
            for (Content child : children) {
                if (!child.name.equalsIgnoreCase("")) {
                    names += child.name + "\n";
                    num --;
                }
            }
            names += "";
            if (num == 1) {
                names += num + " text"; //think of a better name for what to call blocks
            } else if (num > 1) {
                names += num + " texts"; //think of a better name for what to call blocks
            }
            return names;
        }
        return "No children";
    }

    public String cd() {
        if (this.parent != null) {
            current_dir = this.parent;
            return"";
        } else {
            return Main.ANSI_RED + "Current directory has no parent" + Main.ANSI_RESET;
        }
    }
    public String cd(String newDirName) {
        if (newDirName.equalsIgnoreCase("..")) {
            cd();
            return "";
        }
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase(newDirName)) {
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
        int id = 1;
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase("")) {
                    id ++;
                }
            }
        }
        children.add(new Block(text, this, id));
    }
    public String mkdir(String name) {
        if (name.equalsIgnoreCase("") || name.contains("/") || name.equalsIgnoreCase(this.name) || name.equalsIgnoreCase("notes")) {
            return Main.ANSI_RED + "Invalid name" + Main.ANSI_RESET;
        }
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase(name)) {
                    return Main.ANSI_RED + "Directory with the same path already exists" + Main.ANSI_RESET;
                }
            }
        }
        children.add(new Directory(name, this));
        return "";
    }

    public String remdir(String name) {
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase(name)) {
                    if (((Directory) child).children.size() > 0) {
                        return Main.ANSI_RED + "There are files in the directory" + Main.ANSI_RESET;
                    } else {
                        children.remove(child);
                    }
                    return"";
                }
            }
            return Main.ANSI_RED + "That directory does not exist" + Main.ANSI_RESET;
        } else {
            return Main.ANSI_RED + "Current directory has no children"+ Main.ANSI_RESET;
        }
    }
    public String remdirForced(String name) {
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase(name)) {
                    children.remove(child);
                    child = null; //this is needed
                    return "";
                }
            }
            return "";
        } else {
            return Main.ANSI_RED + "Current directory has no children" + Main.ANSI_RESET;
        }
    }

    public ArrayList<String> search(String name) {
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
    }

    public String print() {
        if (children.size() > 0) {
            String message = "";
            for (Content child : children) {
                if (child.name.equalsIgnoreCase("")) {
                    message += child.toString() + "\n";
                } else {
                    message += "    " + child.color + child.name + Main.ANSI_RESET + "\n";
                }
            }
            return "\n" + this.name + "\n" + message;
        }
        return "\n" + this.name;
    }
    public String printall(String indent) {
        String message = "";
        message += "\n" + indent + this.color + Main.ANSI_UNDERLINE + this.name +":"+ Main.ANSI_RESET;
        if (children.size() > 0) {
            for (Content child : children) {
                if (child.name.equalsIgnoreCase("")) {
                    message += "\n" + indent + "    " + child.toString();
                } else {
                    message += indent + ((Directory)child).printall(indent + "    ");
                }
            }
        }
        return message;
    }
}