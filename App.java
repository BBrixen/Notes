package Note_App;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class App extends JFrame {

    public static JTextArea input;
    public static JLabel path, output;

    public static void main(String[] args) {
//        JFrame frame = new JFrame("Notes");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setBackground(Color.black);
//
//        JTextArea input = new JTextArea(); //where the user types to input commands
//        input.setBackground(Color.BLACK);
//        input.setDisabledTextColor(Color.WHITE); //Disabled color is white
//        input.setSelectedTextColor(Color.GRAY); //selected color is gray
//        input.setSize(new Dimension(100, 20));
//        input.setEditable(true);
//
//
//        frame.pack();
//        frame.setVisible(true);
        App app = new App();
    }

    private App() {
        super("Notes");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200,200);
        setBackground(Color.black);
        setVisible(true);

        //basic variables
        input = new JTextArea();
        input.setEditable(true);
        input.setBackground(Color.WHITE);
        input.setToolTipText("Input commands");

        path = new JLabel("Notes/");
        path.setBackground(Color.BLACK);

        output = new JLabel();
        output.setBackground(Color.BLACK);

        //layout
        Container mainContainer = getContentPane();
        mainContainer.setLayout(new BorderLayout(10,10));
        mainContainer.setBackground(Color.black);

        getRootPane().setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.black));

        JPanel topPanel = new JPanel(); //this is where the current path and the user input will be
        topPanel.setBorder(new LineBorder(Color.black, 3));
        topPanel.setBackground(Color.BLACK);
        topPanel.setLayout(new FlowLayout(5));
        topPanel.add(path);
        topPanel.add(input);
        mainContainer.add(topPanel, BorderLayout.NORTH);

        JPanel outputPanel = new JPanel(); //text output, not Printing output
        outputPanel.setBorder(new LineBorder(Color.black, 3));
        outputPanel.setBackground(Color.BLACK);
        outputPanel.setLayout(new FlowLayout(5));
        outputPanel.add(output);
        mainContainer.add(outputPanel, BorderLayout.WEST);

        JPanel printPanel = new JPanel(); //user's typed notes, in form of a list of buttons or labels
        printPanel.setLayout(new GridLayout(5,1,10,10)); //rows will depend on the amount of buttons
        printPanel.setBorder(new LineBorder(Color.black, 3));
        printPanel.setBackground(Color.BLACK);
        mainContainer.add(printPanel, BorderLayout.EAST);
    }

}
