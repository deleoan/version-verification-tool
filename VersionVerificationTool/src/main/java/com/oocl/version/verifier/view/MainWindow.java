package com.oocl.version.verifier.view;

import javax.swing.*;

public class MainWindow extends JFrame {

    private JPanel panelMain;
    private JComboBox domainCombo;
    private JComboBox qaEnvironmentCombo;
    private JComboBox prodEnvironmentCombo;
    private JTable qaVersionResult;
    private JTable productionVersionAndResult;
    private JButton verifyButton;

    public MainWindow() {
        setContentPane(panelMain);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500,400);
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public void setPanelMain(JPanel panelMain) {
        this.panelMain = panelMain;
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

    public JTable getQaVersionResult() {
        return qaVersionResult;
    }

    public void setQaVersionResult(JTable qaVersionResult) {
        this.qaVersionResult = qaVersionResult;
    }

    public JTable getProductionVersionAndResult() {
        return productionVersionAndResult;
    }

    public void setProductionVersionAndResult(JTable productionVersionAndResult) {
        this.productionVersionAndResult = productionVersionAndResult;
    }

    public JButton getVerifyButton() {
        return verifyButton;
    }

    public void setVerifyButton(JButton verifyButton) {
        this.verifyButton = verifyButton;
    }
}
