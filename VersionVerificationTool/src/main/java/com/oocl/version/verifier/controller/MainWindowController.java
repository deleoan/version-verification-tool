package com.oocl.version.verifier.controller;

import com.oocl.version.verifier.util.Client;
import com.oocl.version.verifier.util.Util;
import com.oocl.version.verifier.view.MainWindow;
import org.codehaus.jettison.json.JSONException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class MainWindowController {
    private final MainWindow mainWindow;

    private String qaVersion = "";
    private String columns[] = {};
    private String domain = "";
    private String qaEnvironment = "";
    private String nonQAEnvironment = "";

    private DefaultTableModel qaTableModel;
    private DefaultTableModel prodTableModel;


    public MainWindowController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initController();
    }

    private void initController() {
        onSelectDomain();
        onSelectQAEnvironment();
        onSelectProductionDomain();
        onVerifyButtonClicked();
    }

    private void onVerifyButtonClicked() {
        this.mainWindow.getVerifyButton().addActionListener(e -> {
            if (!domain.isEmpty() && !qaEnvironment.isEmpty() && !nonQAEnvironment.isEmpty()) {
                verifyVersions();
            } else {
                JOptionPane.showMessageDialog(null, "Please fill all required fields!");
            }
        });
    }

    private void onSelectProductionDomain() {
        this.mainWindow.getProdEnvironmentCombo().addActionListener(e -> {
            columns = new String[]{"URL", "Version", "Result"};
            prodTableModel = new DefaultTableModel(columns, 0);
            prodTableModel.fireTableDataChanged();

            JComboBox cb = (JComboBox) e.getSource();
            nonQAEnvironment = (String) cb.getSelectedItem();
            assert nonQAEnvironment != null;
            if(!nonQAEnvironment.isEmpty() && !domain.isEmpty()) {
                boolean isProduction = nonQAEnvironment.equals("PROD");
                String path = getPath(false, isProduction);
                JsonObject environmentUrlsObject = Util.getEnvironmentUrlsObject(path);
                JsonArray nonQaDomainUrls = (JsonArray) environmentUrlsObject.get(domain);

                for (JsonValue url : nonQaDomainUrls) {
                    System.out.println(url.toString());
                    try {
                        JsonString newUrl = (JsonString) url;
                        String nonQAVersion = Client.getVersion(newUrl.getString());
                        Object[] rowObject = {url.toString(), nonQAVersion};
                        prodTableModel.addRow(rowObject);
                        refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResult());
                    } catch (InterruptedException | JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void onSelectQAEnvironment() {
        this.mainWindow.getQaEnvironmentCombo().addActionListener(e -> {
            columns = new String[]{"URL", "Version"};
            qaTableModel = new DefaultTableModel(columns, 0);
            qaTableModel.fireTableDataChanged();
            JComboBox cb = (JComboBox) e.getSource();
            qaEnvironment = (String) cb.getSelectedItem();
            assert qaEnvironment != null;
            if(!qaEnvironment.isEmpty() && !domain.isEmpty()) {
                String path = getPath(true, false);
                JsonObject environmentUrlsObject = Util.getEnvironmentUrlsObject(path);
                String updatedDomain = domain.contains("SHP") ? "SHP" : domain;
                JsonArray qaDomainUrls = (JsonArray) environmentUrlsObject.get(updatedDomain);

                try {
                    JsonObject obj = (JsonObject) qaDomainUrls.get(0);
                    JsonString url = (JsonString) obj.get(qaEnvironment);

                    qaVersion = Client.getVersion(url.getString());

                    Object[] rowObject = {url.toString(), qaVersion};
                    qaTableModel.addRow(rowObject);
                    refreshTable(qaTableModel, this.mainWindow.getQaVersionResult());
                } catch (InterruptedException | JSONException e1) {
                    System.out.println("dasdasdasd");
                    e1.printStackTrace();
                }
            }
        });
    }

    private void onSelectDomain() {
        this.mainWindow.getDomainCombo().addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            domain = (String) cb.getSelectedItem();
            qaTableModel = new DefaultTableModel(columns, 0);
            refreshTable(qaTableModel, this.mainWindow.getQaVersionResult());
            prodTableModel = new DefaultTableModel(columns, 0);
            refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResult());
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
        refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResult());
    }

    private String getPath(boolean isQAEnvironment, boolean isProduction) {
        String path;
        if (isQAEnvironment) {
            path = "urls/qaDomain.json";
        } else if (isProduction) {
            path = "urls/prodDomain.json";
        } else {
            path = "urls/ppDomain.json";
        }
        return path;
    }
    private void refreshTable(DefaultTableModel tableModel, JTable tableResult) {
        tableResult.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }
}
