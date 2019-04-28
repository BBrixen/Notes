package Note_App;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {

    //main variables for the program
    public static String current_color;
    public static final String ANSI_RESET = "\u001B[0m", ANSI_RED = "\u001B[31m", ANSI_GREEN = "\u001B[32m", ANSI_YELLOW = "\u001B[33m",
            ANSI_BLUE = "\u001B[34m", ANSI_PURPLE = "\u001B[35m", ANSI_CYAN = "\u001B[36m", ANSI_UNDERLINE = "\u001B[4m";
    public static final String filename = "data", txtFilename = "Notes.txt";

    public static void main(String[] args) {
        current_color = ANSI_RESET;

//        Directory.main_dir = new Directory("Notes");
        Directory.main_dir = (Directory) DataManager.loadData(filename);

        Directory.current_dir = Directory.main_dir;

        //takes user input and handles it
        String input = "";
        while (!input.equalsIgnoreCase("TER PRGM")) {
            System.out.print(Directory.current_dir.getPath() + "/    ");
            input = (new Scanner(System.in)).nextLine();
            System.out.print(handleInput(input));
        }
        //saves data when program is over
        Display.save();
    }

    public static String handleInput(String raw_input) {
        //this takes all possible inputs and runs the needed method
        //uses .startswith to find matches
        String returned_str = "";
        raw_input = removeSpacesAround(raw_input);
        String input = raw_input;

        if (input.startsWith("help")) {
            returned_str = "\ncd [name] : changes directory to [name]" +
                    "\n\ncd.. : changes directory to parent" +
                    "\n\nremdir [name] : removes [name] if it has no chilren" +
                    "\n\nforce remdir [name] : removes [name]" +
                    "\n\nmkdir [name] : makes a new directory named [name]" +
                    "\n\nprint all : displays every dir and their children" +
                    "\n\nprint : displays only the current directory's children" +
                    "\n\nfind in all [name] : searches everything for [name]" +
                    "\n\nfind [name] : searches the current directory for [name]" +
                    "\n\ngo [path] : changes current directory to [path]" +
                    "\n\ndel [number] : removes the text with id [number] from the current directory" +
                    "\n\nclear : clears the output";
        } else if (input.startsWith("+")) {
            Directory.current_dir.mkblock(removeSpacesAround(input.substring(1)));
            Display.save();
        } else {
            input = input.toLowerCase().replace(" ", "_");
            if (input.startsWith("ls")) {
                returned_str = Directory.current_dir.ls();
            } else if (input.startsWith("cd..")) {
                returned_str = Directory.current_dir.cd();
            } else if (input.startsWith("cd")) {
                returned_str = Directory.current_dir.cd(removeSpacesAround(raw_input.substring(2)));
            } else if (input.startsWith("remdir")) {
                returned_str = Directory.current_dir.remdir(removeSpacesAround(raw_input.substring(6)));
                Display.save();
            }  else if (input.startsWith("mkdir")) {
                returned_str = Directory.current_dir.mkdir(removeSpacesAround(raw_input.substring(5)));
                Display.save();
            }  else if (input.startsWith("force_remdir")) {
                returned_str = Directory.current_dir.remdirForced(removeSpacesAround(raw_input.substring(12)));
                Display.save();
            }  else if (input.startsWith("print_all")) {
                returned_str = Directory.current_dir.printall("") + "\n";
            }  else if (input.startsWith("print")) {
                returned_str = Directory.current_dir.print() + "\n";
            }  else if (input.startsWith("find_in_all")) {
                System.out.println();
                returned_str = "\n" + find(removeSpacesAround(raw_input.substring(11)), Directory.main_dir) + "\n";
            }  else if (input.startsWith("find")) {
                returned_str = "\n" + find(removeSpacesAround(raw_input.substring(4)), Directory.current_dir) + "\n";
            }  else if (input.startsWith("go")) {
                parsePath(removeSpacesAround(input.substring(2)));
            }  else if (input.startsWith("del")|| input.startsWith("edit")) {
                String text = input.substring(3);
                if (input.startsWith("edit")) {
                    text = text.substring(1);
                }
                text = removeSpacesAround(text);

                try {
                    if (Directory.current_dir.getChildren().size() > 0) {
                        int number = Integer.parseInt(text);

                        for (Content child : Directory.current_dir.getChildren()) {
                            if (child.name.equalsIgnoreCase("")) {
                                if (((Block)child).getId() == number) {
                                    if (input.startsWith("del")) {
                                        Directory.current_dir.getChildren().remove(child);
                                        break;
                                    } else {
                                        ((Block) child).editText();
                                    }
                                }
                            }
                        }

                        if (input.startsWith("del")) {
                            for (Content child : Directory.current_dir.getChildren()) {
                                if (child.name.equalsIgnoreCase("")) {
                                    if (((Block) child).getId() > number) {
                                        ((Block) child).setId(((Block) child).getId() - 1);
                                    }
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Display.save();
            }  else if (input.startsWith("path")) {
                returned_str = Directory.current_dir.getPath();
            } else if (input.startsWith("clear")) {
                for (int i = 0; i < 50; i++) {
                    System.out.println();
                }
            } else if (input.startsWith("colors")) {
                returned_str = ANSI_RED + "Red, " + ANSI_GREEN + "Green, "+ ANSI_BLUE +"Blue," + ANSI_YELLOW + "Yellow,"+ANSI_PURPLE+"Purple,"+ANSI_CYAN+"Cyan,"+ANSI_RESET+"Gray/White (use the first 3 letters)";
            } else if (input.startsWith("col")) {
                current_color = findColor(removeSpacesAround(input.substring(3)));
            } else if (input.startsWith("coldir")) {
                Directory.current_dir.color = findColor(removeSpacesAround(input.substring(6)));
                Display.save();
            } else if (input.startsWith("mkcolor")) {
                input = removeSpacesAround(input.substring(7));
                if (input.startsWith("dir")) {
                    String name = removeSpacesAround(input.substring(3, input.length()-3));
                    if (name.equalsIgnoreCase("")) {
                        return"";
                    }
                    String col = input.substring(input.length()-3);
                    for (Content child : Directory.current_dir.getChildren()) {
                        if (child.name.equalsIgnoreCase(name)) {
                            child.color = findColor(col);
                        }
                    }
                } else {
                    try {
                        int x = Integer.parseInt(input.substring(0, 1));
                        if (input.length() < 3) {
                            return"";
                        }
                        String col = input.substring(input.length() - 3);
                        for (Content child : Directory.current_dir.getChildren()) {
                            if (child.name.equalsIgnoreCase("")) {
                                if (((Block) child).getId() == x) {
                                    child.color = findColor(col);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                Display.save();
            }
        }
        return returned_str;
    }

    public static String findColor(String input) {
        //changes the color based on user input
        //this is not used in the Display.java, this was only used in older versions of the app
        String color = current_color;
        if (input.equalsIgnoreCase("red")) {
            color = Main.ANSI_RED;
        } else if (input.equalsIgnoreCase("gre")) {
            color = Main.ANSI_GREEN;
        } else if (input.equalsIgnoreCase("blu")) {
            color = Main.ANSI_BLUE;
        } else if (input.equalsIgnoreCase("yel")) {
            color = Main.ANSI_YELLOW;
        } else if (input.equalsIgnoreCase("pur")) {
            color = Main.ANSI_PURPLE;
        } else if (input.equalsIgnoreCase("cya")) {
            color = Main.ANSI_CYAN;
        } else if (input.equalsIgnoreCase("res") || input.equalsIgnoreCase("gra") || input.equalsIgnoreCase("whi")) {
            color = Main.ANSI_RESET;
        }
        return color;
    }
    public static void parsePath(String path) {
        //this takes the user to the path that they input
        path = removeSlashesAround(path.toLowerCase());
        if (path.startsWith("notes")) {
            path = path.substring(5);
            parse(path, Directory.main_dir);
        } else {
            int length = path.split("/")[0].length();
            path = path.substring(length);
            parse(path, Directory.current_dir);
        }
    }
    public static void parse(String path, Directory dir) {
        //changes the dir for a path, and it keeps the old path name to itar again
        path = removeSlashesAround(path);
        int length = path.split("/")[0].length();
        if (length == 0) {
            Directory.current_dir = dir;
        }
        String name = path.substring(0, length);
        path = removeSlashesAround(path.substring(length));

        for (Content child : dir.getChildren()) {
            if (child.name.equalsIgnoreCase(name)) {
                parse(path, (Directory)child);
            }
        }
    }
    public static String find(String name, Directory dir) {
        String paths_list = "";
        //finds all directory paths with the same name, and combines them
        ArrayList<String> paths = dir.search(name);
        if (paths.size() > 0) {
            for (String path : paths) {
                if (dir == Directory.main_dir) {
                    paths_list += path + "\n";
                } else {
                    paths_list += path.replace(dir.getPath_of_parent(), "") + "\n";
                }
            }
            return paths_list;
        } else {
            return "No directories found\n";
        }
    }

    public static String removeSpacesAround(String text) {
        //removes spaces at the beginning and end of the message
        while (text.startsWith("_") || text.startsWith(" ")) {
            text = text.substring(1);
        }
        while (text.endsWith("_") || text.endsWith(" ")) {
            text = text.substring(0, text.length()-1);
        }
        return text;
    }
    public static String removeSlashesAround(String text) {
        //removes slashes at the beginning and end of the message
        while (text.startsWith("/")) {
            text = text.substring(1);
        }
        while (text.endsWith("/")) {
            text = text.substring(0, text.length()-1);
        }
        return text;
    }
}