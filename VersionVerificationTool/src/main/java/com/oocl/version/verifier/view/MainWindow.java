package com.oocl.version.verifier.view;

import javax.swing.*;

public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private JComboBox domainCombo;
    private JComboBox qaEnvironmentCombo;
    private JComboBox prodEnvironmentCombo;
    private JTable qaVersionResultTable;
    private JTable productionVersionAndResultTable;
    private JButton verifyButton;

    public MainWindow() {
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setVisible(true);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JComboBox getDomainCombo() {
        return domainCombo;
    }

    public void setDomainCombo(JComboBox domainCombo) {
        this.domainCombo = domainCombo;
    }

    public JComboBox getQaEnvironmentCombo() {
        return qaEnvironmentCombo;
    }

    public void setQaEnvironmentCombo(JComboBox qaEnvironmentCombo) {
        this.qaEnvironmentCombo = qaEnvironmentCombo;
    }

    public JComboBox getProdEnvironmentCombo() {
        return prodEnvironmentCombo;
    }

    public void setProdEnvironmentCombo(JComboBox prodEnvironmentCombo) {
        this.prodEnvironmentCombo = prodEnvironmentCombo;
    }

    public JTable getQaVersionResultTable() {
        return qaVersionResultTable;
    }

    public void setQaVersionResultTable(JTable qaVersionResultTable) {
        this.qaVersionResultTable = qaVersionResultTable;
    }

    public JTable getProductionVersionAndResultTable() {
        return productionVersionAndResultTable;
    }

    public void setProductionVersionAndResultTable(JTable productionVersionAndResultTable) {
        this.productionVersionAndResultTable = productionVersionAndResultTable;
    }

    public JButton getVerifyButton() {
        return verifyButton;
    }

    public void setVerifyButton(JButton verifyButton) {
        this.verifyButton = verifyButton;
    }
}
