package application;

import javax.json.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public String domain = "";
    public String qaVersion = "";
    public List<String> nonQAVersions = new ArrayList<>();

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
                    JsonArray urls = getUrls(true, false);
                    try {
                        JsonObject obj = (JsonObject) urls.get(0);
                        JsonString url = (JsonString) obj.get(qaEnvironment);
                        qaVersion = getVersion(url.toString());

                        Object[] objs = {url.toString(), qaVersion};
                        String col[] = {"URL","Version"};
                        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
                        table1.setModel(tableModel);
                        tableModel.addRow(objs);

                        tableModel.fireTableDataChanged();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        prodCombo.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String col[] = {"URL","Version"};
                DefaultTableModel tableModel = new DefaultTableModel(col, 0);

                JComboBox cb = (JComboBox) e.getSource();
                String nonQAEnvironment = (String) cb.getSelectedItem();
                if(!nonQAEnvironment.isEmpty() && !domain.isEmpty()) {
                    boolean isProduction = nonQAEnvironment == "PROD" ? true : false;
                    JsonArray urls = getUrls(false, false);
                    for (JsonValue url : urls) {
                        System.out.println(url.toString());
                        try {
                            String version = getVersion(url.toString());
                            nonQAVersions.add(version);

                            Object[] objs = {url.toString(), nonQAVersions.get(0)};
                            tableModel.addRow(objs);
                            table2.setModel(tableModel);
                            tableModel.fireTableDataChanged();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println(nonQAVersions);
                    }
                }
            }
        });
        table1.addComponentListener(new ComponentAdapter() {
        });
        verifyButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Verify button clicked!");
            }
        });
    }

    public JsonArray getUrls(boolean isQAEnvionment, boolean isProduction) {
        String path = getPath(isQAEnvionment, isProduction);

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

    private String getPath(boolean isQAEnvionment, boolean isProduction) {
        String path = "";
        if (isQAEnvionment) {
            path = "C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\version-verification-tool\\urls\\qaDomain.json";
        } else if (isProduction) {
            path = "C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\version-verification-tool\\urls\\prodDomain.json";
        } else {
            path = "C:\\Users\\Ana Katrina De Leon\\Documents\\Work\\TW\\version-verification-tool\\urls\\ppDomain.json";
        }
        return path;
    }

    public String getVersion(String domainUrl) throws IOException {
        domainUrl = "http://services.groupkt.com/state/get/IND/all";
        URL url = new URL(domainUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output = "";
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
//            System.out.println(output);
            return "121.12.1";
        }

        conn.disconnect();
        return output;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
