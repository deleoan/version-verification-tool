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
import java.awt.event.ActionEvent;
import java.util.Vector;

public class MainWindowController {
    private static final String URL = "URL";
    private static final String VERSION = "Version";
    private static final String RESULT = "Result";
    private static final String VERSION_MATCHED = "Version Matched";
    private static final String NO_VERSION = "No Version";
    private static final String SERVER_IS_DOWN = "Server is down";
    private static final String VERSION_MISMATCHED = "Version Mismatched";
    private static final String QA_DOMAIN_JSON_PATH = "urls/qaDomain.json";
    private static final String PROD_DOMAIN_JSON_PATH = "urls/prodDomain.json";
    private static final String PP_DOMAIN_JSON_PATH = "urls/ppDomain.json";

    private final MainWindow mainWindow;

    private String qaVersion = "";
    private String columns[] = {};
    private String selectedDomain = "";
    private String selectedQaEnvironment = "";
    private String selectedNonQaEnvironment = "";

    private DefaultTableModel qaTableModel;
    private DefaultTableModel prodTableModel;


    public MainWindowController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initController();
    }

    private void initController() {
        this.mainWindow.getDomainCombo().addActionListener(this::onSelectDomain);
        this.mainWindow.getQaEnvironmentCombo().addActionListener(this::onSelectQAEnvironment);
        this.mainWindow.getProdEnvironmentCombo().addActionListener(this::onSelectProductionDomain);
        this.mainWindow.getVerifyButton().addActionListener(e -> onVerifyButtonClicked());

        /*
        TODO:
        POPULATE MODEL AND REFLECT TO UI
        CREATE TDD!!!!!
        MAKE IT EXECUTABLE
        CHANGE SELECTED ITEM WHEN QA COMBO UPDATED
        IF HAVE MORE TIME:
            CHANGE COMBO BOX TO MULTI-SELECT
            ADD PROGRESS BAR
        */
    }

    private void onSelectDomain(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        selectedDomain = (String) cb.getSelectedItem();

        qaTableModel = new DefaultTableModel(columns, 0);
        refreshTable(qaTableModel, this.mainWindow.getQaVersionResultTable());
        prodTableModel = new DefaultTableModel(columns, 0);
        refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResultTable());
    }

    private void onSelectQAEnvironment(ActionEvent e) {
        columns = new String[]{URL, VERSION};
        qaTableModel = new DefaultTableModel(columns, 0);
        prodTableModel = new DefaultTableModel(columns, 0);
        refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResultTable());

        JComboBox cb = (JComboBox) e.getSource();
        selectedQaEnvironment = (String) cb.getSelectedItem();

        assert selectedQaEnvironment != null;
        if (!selectedQaEnvironment.isEmpty() && !selectedDomain.isEmpty()) {
            String updatedDomain = selectedDomain.contains("SHP") ? "SHP" : selectedDomain;
            JsonArray qaDomainUrls = getDomainUrls(true, false, updatedDomain);

            try {
                JsonObject obj = (JsonObject) qaDomainUrls.get(0);
                JsonString url = (JsonString) obj.get(selectedQaEnvironment);

                qaVersion = Client.getVersion(url.getString());
                Object[] rowObject = {url.toString(), qaVersion};
                qaTableModel.addRow(rowObject);
                refreshTable(qaTableModel, this.mainWindow.getQaVersionResultTable());
            } catch (InterruptedException | JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void onSelectProductionDomain(ActionEvent e) {
        columns = new String[]{URL, VERSION, RESULT};
        prodTableModel = new DefaultTableModel(columns, 0);
        prodTableModel.fireTableDataChanged();

        JComboBox cb = (JComboBox) e.getSource();
        selectedNonQaEnvironment = (String) cb.getSelectedItem();
        assert selectedNonQaEnvironment != null;
        if (!selectedNonQaEnvironment.isEmpty() && !selectedDomain.isEmpty()) {
            boolean isProduction = selectedNonQaEnvironment.equals("PROD");
            JsonArray nonQaDomainUrls = getDomainUrls(false, isProduction, "");

            for (JsonValue url : nonQaDomainUrls) {
                System.out.println(url.toString());
                try {
                    JsonString newUrl = (JsonString) url;
                    String nonQAVersion = Client.getVersion(newUrl.getString());
                    Object[] rowObject = {url.toString(), nonQAVersion};
                    prodTableModel.addRow(rowObject);
                    refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResultTable());
                } catch (InterruptedException | JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private JsonArray getDomainUrls(boolean isQAEnvironment, boolean isProduction, String domain) {
        domain = domain.isEmpty() ? selectedDomain : domain;
        String path = getPath(isQAEnvironment, isProduction);
        JsonObject environmentUrlsObject = Util.getEnvironmentUrlsObject(path);
        return (JsonArray) environmentUrlsObject.get(domain);
    }

    private void onVerifyButtonClicked() {
        if (selectedDomain.isEmpty() || selectedQaEnvironment.isEmpty() || selectedNonQaEnvironment.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields!");
        } else {
            verifyVersions();
        }
    }

    private void verifyVersions() {
        for (Object record : prodTableModel.getDataVector()) {
            Vector prodRecord = ((Vector) record);
            if (prodRecord.get(1).equals(qaVersion)) {
                prodRecord.add(2, VERSION_MATCHED);
            } else if (prodRecord.get(1).equals(NO_VERSION)) {
                prodRecord.add(2, SERVER_IS_DOWN);
            } else {
                prodRecord.add(2, VERSION_MISMATCHED);
            }
        }
        refreshTable(prodTableModel, this.mainWindow.getProductionVersionAndResultTable());
    }

    private String getPath(boolean isQAEnvironment, boolean isProduction) {
        String path;
        if (isQAEnvironment) {
            path = QA_DOMAIN_JSON_PATH;
        } else {
            if (isProduction) {
                path = PROD_DOMAIN_JSON_PATH;
            } else {
                path = PP_DOMAIN_JSON_PATH;
            }
        }
        return path;
    }

    private void refreshTable(DefaultTableModel tableModel, JTable tableResult) {
        tableResult.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }
}
