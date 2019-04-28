package Note_App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TextFile {

    public static void addText(String data, String fileName) {
        //this adds text to the end of the file
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        try {
            //makes new file scanner
            Scanner scan = new Scanner(new File(fileName));

            //makes a new list
            ArrayList<String> words = readFromFile(fileName);
            //stores old data to be used again
            words.add(data);

            //rewrites file
            writeToFile(words, fileName);
        } catch (FileNotFoundException e) {
            //errors for file not existing
            System.out.println("No file found. Creating One");
            ArrayList<String> start = new ArrayList<>();
            start.add(data);
            writeToFile(start, fileName);
        }
    }

    public static void clearText(String fileName) {
        //removes all text from the file
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        ArrayList<String> clear = new ArrayList<>();
        clear.add("");
        writeToFile(clear, fileName);
    }

    public static void writeToFile(ArrayList<String> lines, String fileName) {
        //writes the list to the file. each element is a new line.
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        try {
            PrintWriter outputStream = new PrintWriter(fileName);

            for (String text : lines) {
                //adds a line for each element
                outputStream.println(text);
//                System.out.println("Added " + text + " to " + fileName);

                outputStream.flush();
            }

            outputStream.close(); //this stops you from being able
            // to write to the file later
            //if you want to keep writing to the file
            //use outputStream.flush(); instead

        } catch (FileNotFoundException e) {
            //no file named @param fileName
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFromFile(String fileName) {
        //turn file into a list with each element as a line
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        Scanner scan;
        try {
            //scans file
            scan = new Scanner(new File(fileName));
            ArrayList<String> words = new ArrayList<>();

            //while the file has a line
            while (scan.hasNext()) {
                words.add(scan.next() + " ");
            }
            return words;

        } catch (FileNotFoundException e) {
            if (!fileName.equalsIgnoreCase("secretTalks")) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String readLine(String fileName, int line) {
        //reads only the specific line input
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        Scanner scan;
        try {
            scan = new Scanner(new File(fileName));
            ArrayList<String> lines = new ArrayList<>();

            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            if (line < lines.size()) {
                //gets the specified line
                return lines.get(line);
            } else {
                System.out.println("\n\n\nERROR AT TextFile LINE 107\n\n\n");
            }

        } catch (FileNotFoundException e) {
            if (!fileName.equalsIgnoreCase("secretTalks")) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static int numberOfLines(String fileName) {
        //return how many lines the file has
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        Scanner scan;
        try {
            System.out.println(fileName);
            scan = new Scanner(new File(fileName));
            ArrayList<String> lines = new ArrayList<>();

            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            return lines.size();

        } catch (FileNotFoundException e) {
            if (!fileName.equalsIgnoreCase("secretTalks")) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public static void deleteLine(String fileName, int line) {
        //removes specific line
        ArrayList<String> lines = allLines(fileName);
        lines.remove(line);
        saveLines(fileName, lines);
    }

    public static ArrayList<String> allLines(String fileName) {
        //gets a list with every line
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        Scanner scan;
        try {
            scan = new Scanner(new File(fileName));
            ArrayList<String> lines = new ArrayList<>();

            while (scan.hasNextLine()) {
                //adds items to the list
                lines.add(scan.nextLine());
            }
            return lines;

        } catch (FileNotFoundException e) {
            if (!fileName.equalsIgnoreCase("secretTalks")) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveLines(String fileName, ArrayList<String> lines) {
        //saves the new data into the text file
        if (!fileName.startsWith(Main.txtFilename)) {
            fileName = Main.txtFilename + fileName;
        }

        try {
            Scanner scan = new Scanner(new File(fileName));
            writeToFile(lines, fileName);
        } catch (FileNotFoundException e) {
            System.out.println("No file found. Creating One");
            ArrayList<String> start = new ArrayList<>();
            writeToFile(start, fileName);
        }
    }

    public static void addLine(String fileName, String line) {
        //adds a line to the text of the file
        ArrayList<String> lines = allLines(fileName);
        lines.add(line);
        saveLines(fileName, lines);
    }

    public static boolean fileExists(String fileName) {
        //returns true if the file does exist
        try {
            Scanner scan = new Scanner(new File(fileName));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}