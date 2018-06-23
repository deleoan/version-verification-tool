package com.oocl.version.verifier.controller;

import com.oocl.version.verifier.model.Environment;
import com.oocl.version.verifier.model.EnvironmentsPojo;
import com.oocl.version.verifier.model.Modules;
import com.oocl.version.verifier.util.Client;
import com.oocl.version.verifier.view.MainWindow;
import org.codehaus.jettison.json.JSONException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.oocl.version.verifier.util.Util.getEnvironmentUrlsObject;

public class MainWindowController {
    private static final String URL = "URL";
    private static final String VERSION = "Version";
    private static final String RESULT = "Result";
    private static final String VERSION_MATCHED = "Version Matched";
    private static final String NO_VERSION = "No Version";
    private static final String SERVER_IS_DOWN = "Server is down";
    private static final String VERSION_MISMATCHED = "Version Mismatched";

    private final MainWindow mainWindow;

    private String qaVersion = "";
    private String[] columns = {};
    private String selectedDomain = "";
    private String selectedQaEnvironment = "";
    private String selectedNonQaEnvironment = "";

    private DefaultTableModel qaTableModel;
    private DefaultTableModel nonQATableModel;


    private EnvironmentsPojo environmentsPojo = getEnvironmentUrlsObject();
    private List<Environment>  qaEnvironment = environmentsPojo.getEnvironment().stream().filter(environment -> environment.getEnvName().contains("QA")).collect(Collectors.toList());
    private List<Environment> nonQAEnvironment = environmentsPojo.getEnvironment().stream().filter(environment -> !environment.getEnvName().contains("QA")).collect(Collectors.toList());

    public MainWindowController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initController();

        populateDomainComboBox();
        populateQAComboBox();
        populateNonQAComboBox();
    }

    private void populateDomainComboBox() {
        List<Modules> nonQAModules = qaEnvironment.get(0).getModules();
        List<String> domainNameList = new ArrayList<>();
        for (Modules module : nonQAModules) {
            domainNameList.add(module.getModule());
        }
        String[] domainNames = domainNameList.toArray(new String[0]);
        JComboBox domainCombo = this.mainWindow.getDomainCombo();
        setComboBoxModel(domainCombo, domainNames, "Select Domain");
    }

    private void populateNonQAComboBox() {
        List<String> nonQAEnvironmentNameList = nonQAEnvironment.stream().map(Environment::getEnvName).collect(Collectors.toList());
        String[] nonQAEnvironmentName = nonQAEnvironmentNameList.toArray(new String[0]);
        JComboBox nonQAEnvironmentCombo = this.mainWindow.getProdEnvironmentCombo();
        setComboBoxModel(nonQAEnvironmentCombo, nonQAEnvironmentName, "Select Non QA Environment");
    }

    private void populateQAComboBox() {
        List<String> qaEnvironmentNameList = qaEnvironment.stream().map(Environment::getEnvName).collect(Collectors.toList());
        String[] qaEnvironmentName = qaEnvironmentNameList.toArray(new String[0]);
        JComboBox qaEnvironmentCombo = this.mainWindow.getQaEnvironmentCombo();
        setComboBoxModel(qaEnvironmentCombo, qaEnvironmentName, "Select QA Environment");
    }

    private void setComboBoxModel(JComboBox comboBox, String[] environmentName, String defaultSelectedItem) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(environmentName);
        model.setSelectedItem(defaultSelectedItem);
        comboBox.setModel(model);
    }

    private void initController() {
        this.mainWindow.getDomainCombo().addActionListener(this::onSelectDomain);
        this.mainWindow.getQaEnvironmentCombo().addActionListener(this::onSelectQAEnvironment);
        this.mainWindow.getProdEnvironmentCombo().addActionListener(this::onSelectProductionDomain);
        this.mainWindow.getVerifyButton().addActionListener(e -> onVerifyButtonClicked());
    }

    private void onSelectDomain(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        selectedDomain = (String) cb.getSelectedItem();


        qaTableModel = new DefaultTableModel(columns, 0);
        refreshTable(qaTableModel, this.mainWindow.getQaVersionResultTable());
        nonQATableModel = new DefaultTableModel(columns, 0);
        refreshTable(nonQATableModel, this.mainWindow.getProductionVersionAndResultTable());
    }

    private void onSelectQAEnvironment(ActionEvent e) {
        columns = new String[]{URL, VERSION};
        qaTableModel = new DefaultTableModel(columns, 0);
        nonQATableModel = new DefaultTableModel(columns, 0);
        refreshTable(nonQATableModel, this.mainWindow.getProductionVersionAndResultTable());

        JComboBox cb = (JComboBox) e.getSource();
        selectedQaEnvironment = (String) cb.getSelectedItem();

        if (selectedQaEnvironment != null && !selectedQaEnvironment.isEmpty() && !selectedDomain.isEmpty()) {
            String url = getDomainUrls(true).get(0);
            try {
                qaVersion = Client.getVersion(url);
                Object[] rowObject = {url, qaVersion};
                qaTableModel.addRow(rowObject);
                refreshTable(qaTableModel, this.mainWindow.getQaVersionResultTable());
            } catch (InterruptedException | JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void onSelectProductionDomain(ActionEvent e) {
        columns = new String[]{URL, VERSION, RESULT};
        nonQATableModel = new DefaultTableModel(columns, 0);
        nonQATableModel.fireTableDataChanged();

        JComboBox cb = (JComboBox) e.getSource();
        selectedNonQaEnvironment = (String) cb.getSelectedItem();
        if (selectedNonQaEnvironment != null && !selectedNonQaEnvironment.isEmpty() && !selectedDomain.isEmpty()) {
            List<String> nonQaDomainUrls = getDomainUrls(false);
            for (String url : nonQaDomainUrls) {
                try {
                    String nonQAVersion = Client.getVersion(url);
                    Object[] rowObject = {url, nonQAVersion};
                    nonQATableModel.addRow(rowObject);
                    refreshTable(nonQATableModel, this.mainWindow.getProductionVersionAndResultTable());
                } catch (InterruptedException | JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private List<String> getDomainUrls(boolean isQAEnvironment) {
        String domain = selectedDomain.contains("SHP") ? "SHP" : selectedDomain;
        domain = domain.isEmpty() ? selectedDomain : domain;
        String finalDomain = domain;
        List<Environment> environmentModules;

        if (isQAEnvironment) {
            environmentModules = qaEnvironment.stream().filter(environment -> environment.getEnvName().equals(selectedQaEnvironment)).collect(Collectors.toList());
        } else {
            environmentModules = nonQAEnvironment.stream().filter(environment -> environment.getEnvName().equals(selectedNonQaEnvironment)).collect(Collectors.toList());
        }

        return environmentModules.get(0).getModules().stream().filter(mod -> mod.getModule().equals(finalDomain)).collect(Collectors.toList()).get(0).getLinks();
    }

    private void onVerifyButtonClicked() {
        if (selectedDomain.isEmpty() || selectedQaEnvironment.isEmpty() || selectedNonQaEnvironment.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields!");
        } else {
            verifyVersions();
        }
    }

    private void verifyVersions() {
        for (Object record : nonQATableModel.getDataVector()) {
            Vector prodRecord = ((Vector) record);
            if (prodRecord.get(1).equals(qaVersion)) {
                prodRecord.add(2, VERSION_MATCHED);
            } else if (prodRecord.get(1).equals(NO_VERSION)) {
                prodRecord.add(2, SERVER_IS_DOWN);
            } else {
                prodRecord.add(2, VERSION_MISMATCHED);
            }
        }
        refreshTable(nonQATableModel, this.mainWindow.getProductionVersionAndResultTable());
    }

    private void refreshTable(DefaultTableModel tableModel, JTable tableResult) {
        tableResult.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }
}
