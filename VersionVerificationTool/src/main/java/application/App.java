package application;

import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    private JPanel panelMain;
    private JComboBox qaCombo;
    private JComboBox prodCombo;
    private JComboBox domainCombo;
    private JLabel qaVersionsLabel;
    private JTable table1;
    private JButton verifyButton;
    private JLabel verificationResultLabel;
    private JTable table2;
    private JLabel titleLabel;

    private List<String> qaUrls = new ArrayList<>();
    private List<String> prodUrls = new ArrayList<>();

    public App() {
        qaCombo.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println(e.getItem());
//                getUrls("", e.getItem().toSring());
            }
        });
        domainCombo.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                getUrls(e.getItem().toString(), "");
            }
        });
    }

    public void getUrls(String domain, String qaEnv) {
        try {
            com.google.gson.stream.JsonReader reader = new JsonReader(new FileReader("C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\VV Tool Files\\ppDomain.json"));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                System.out.println(name);
                if (name.equals(domain)) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        System.out.println(reader.nextString());
                    }
                    reader.endArray();
                }
            }
            reader.endObject();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
