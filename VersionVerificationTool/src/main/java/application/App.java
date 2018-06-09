package application;

import javax.json.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    private String domain = "";

    public App() {
        domainCombo.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                domain = (String) cb.getSelectedItem();
            }
        });
        qaCombo.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String qaEnvironment = (String) cb.getSelectedItem();
                if(!qaEnvironment.isEmpty() && !domain.isEmpty()) {
                    getUrls(qaEnvironment, true, false);
//                    TODO: Get version using url
                }
            }
        });
    }

    public List<JsonValue> getUrls(String environment, boolean isQAEnvionment, boolean isProduction) {
        String path = "";
        if (isQAEnvionment) {
            path = "C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\VV Tool Files\\qaDomain.json";
        } else if (isProduction) {
            path = "C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\VV Tool Files\\prodDomain.json";
        } else {
            path = "C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\VV Tool Files\\ppDomain.json";
        }

        File jsonInputFile = new File(path);
        InputStream is;
        try {
            is = new FileInputStream(jsonInputFile);
            JsonReader reader = Json.createReader(is);
            JsonObject empObj = reader.readObject();
            reader.close();
            JsonArray urls = (JsonArray) empObj.get(domain);
            return urls;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
