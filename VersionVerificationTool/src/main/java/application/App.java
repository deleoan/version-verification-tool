package application;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.json.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class App {
    private JPanel panelMain;
    private JComboBox qaEnvironmentCombo;
    private JComboBox prodEnvironmentCombo;
    private JComboBox domainCombo;
    private JLabel qaVersionsLabel;
    private JTable qaVersionResult;
    private JLabel verificationResultLabel;
    private JTable productionVersionAndResult;
    private JLabel titleLabel;
    private JButton verifyButton;

    private String qaVersion = "";
    private String columns[] = {};
    public String domain = "";
    private String qaEnvironment = "";
    private String nonQAEnvironment = "";

    private DefaultTableModel qaTableModel;
    private DefaultTableModel prodTableModel;

    public App() {
        domainCombo.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            domain = (String) cb.getSelectedItem();
            qaTableModel = new DefaultTableModel(columns, 0);
            refreshTable(qaTableModel, qaVersionResult);
            prodTableModel = new DefaultTableModel(columns, 0);
            refreshTable(prodTableModel, productionVersionAndResult);
        });
        qaEnvironmentCombo.addActionListener(e -> {
            columns = new String[]{"URL", "Version"};
            qaTableModel = new DefaultTableModel(columns, 0);
            qaTableModel.fireTableDataChanged();
            JComboBox cb = (JComboBox) e.getSource();
            qaEnvironment = (String) cb.getSelectedItem();
            assert qaEnvironment != null;
            if(!qaEnvironment.isEmpty() && !domain.isEmpty()) {
                JsonArray urls = getUrls(true, false);
                try {
                    JsonObject obj = (JsonObject) urls.get(0);
                    JsonString url = (JsonString) obj.get(qaEnvironment);
                    qaVersion = getVersion(url.toString());

                    Object[] rowObject = {url.toString(), qaVersion};
                    qaTableModel.addRow(rowObject);
                    refreshTable(qaTableModel, qaVersionResult);
                } catch (InterruptedException | IOException | JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
        selectProductionDomain();
        verifyButton.addActionListener(e -> {
            if (!domain.isEmpty() && !qaEnvironment.isEmpty() && !nonQAEnvironment.isEmpty()) {
                verifyVersions();
            } else {
                JOptionPane.showMessageDialog(null, "Please fill all required fields!");
            }
        });
    }

    private void selectProductionDomain() {
        prodEnvironmentCombo.addActionListener(e -> {
            columns = new String[]{"URL", "Version", "Result"};
            prodTableModel = new DefaultTableModel(columns, 0);
            prodTableModel.fireTableDataChanged();

            JComboBox cb = (JComboBox) e.getSource();
            nonQAEnvironment = (String) cb.getSelectedItem();
            assert nonQAEnvironment != null;
            if(!nonQAEnvironment.isEmpty() && !domain.isEmpty()) {
                boolean isProduction = nonQAEnvironment.equals("PROD");
                JsonArray urls = getUrls(false, isProduction);
                for (JsonValue url : urls) {
                    System.out.println(url.toString());
                    try {
                        String nonQAVersion = getVersion(url.toString());
                        Object[] rowObject = {url.toString(), nonQAVersion};
                        prodTableModel.addRow(rowObject);
                        refreshTable(prodTableModel, productionVersionAndResult);
                    } catch (IOException | InterruptedException | JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void verifyVersions() {
        for (Object record : prodTableModel.getDataVector()) {
            Vector prodRecord = ((Vector) record);
            if (prodRecord.get(1).equals(qaVersion)) {
                prodRecord.add(2, "Version Matched");
            } else if (prodRecord.get(1).equals("No Version")) {
                prodRecord.add(2, "Server is down");
            } else {
                prodRecord.add(2, "Version Mismatched");
            }
        }
        refreshTable(prodTableModel, productionVersionAndResult);
    }

    private void refreshTable(DefaultTableModel tableModel, JTable tableResult) {
        tableResult.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    public JsonArray getUrls(boolean isQAEnvironment, boolean isProduction) {
        String path = getPath(isQAEnvironment, isProduction);

        File jsonInputFile = new File(path);
        InputStream is;
        try {
            is = new FileInputStream(jsonInputFile);
            JsonReader reader = Json.createReader(is);
            JsonObject empObj = reader.readObject();
            reader.close();
            if (isQAEnvironment) {
                String updateSHPDomain = domain.contains("SHP") ? "SHP" : domain;
                return (JsonArray) empObj.get(updateSHPDomain);
            }
            return (JsonArray) empObj.get(domain);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPath(boolean isQAEnvironment, boolean isProduction) {
        String path;
        if (isQAEnvironment) {
            path = "D:\\My documents\\SHPBKG\\TW Workshop\\TW\\repo\\version-verification-tool\\urls\\qaDomain.json";
        } else if (isProduction) {
            path = "D:\\My documents\\SHPBKG\\TW Workshop\\TW\\repo\\version-verification-tool\\urls\\prodDomain.json";
        } else {
            path = "D:\\My documents\\SHPBKG\\TW Workshop\\TW\\repo\\version-verification-tool\\urls\\ppDomain.json";
        }
        return path;
    }

    private String getVersion(String domainUrl) throws IOException, InterruptedException, JSONException {
        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        Future<Response> whenResponse = asyncHttpClient.prepareGet(domainUrl).execute();
        Response response = null;
        String version;
        try {
            response = whenResponse.get();
            JSONObject jsonObj = new JSONObject(response.getResponseBody());
            version = (String) jsonObj.get("version");
        } catch (ExecutionException e) {
            version = "No Version";
        }
        return version;

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
