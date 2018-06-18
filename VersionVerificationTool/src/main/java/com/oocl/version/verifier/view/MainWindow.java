package com.oocl.version.verifier.view;

import javax.swing.*;

public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private JComboBox moduleCombobox;
    private JComboBox environmentComboBox;
    private JTable sourceTable;
    private JTable targetTable;

    public MainWindow() {
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setSize(400,400);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JComboBox getModuleCombobox() {
        return moduleCombobox;
    }

    public void setModuleCombobox(JComboBox moduleCombobox) {
        this.moduleCombobox = moduleCombobox;
    }

    public JComboBox getEnvironmentComboBox() {
        return environmentComboBox;
    }

    public void setEnvironmentComboBox(JComboBox environmentComboBox) {
        this.environmentComboBox = environmentComboBox;
    }

    public JTable getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(JTable sourceTable) {
        this.sourceTable = sourceTable;
    }

    public JTable getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(JTable targetTable) {
        this.targetTable = targetTable;
    }
}
