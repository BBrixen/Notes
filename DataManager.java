package Note_App;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class DataManager {
    public static boolean saveData(Object data, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            //this makes a new file output and object output to write the data
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(data); //writes the data to the object it is saving to
            out.close();
            return true; //finishes the method returning true for a successful save
        } catch (Exception ex) {
            return false; //unsuccessful save
        }
    }
    public static Object loadData(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            //makes a new file input and object input to read the data
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            Object data = (Object) in.readObject(); //reads the object data and makes an object out of that
            in.close();
            return data; //returns the data
        } catch (Exception ex) {
            System.out.println("Error loading data");
            return new Directory("Notes"); //makes a new data type for when the data fails to load
        }
    }
}