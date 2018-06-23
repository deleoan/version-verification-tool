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
    private JLabel qaVersionsLabel;
    private JLabel verificationResultLabel;
    private JLabel titleLabel;

    public MainWindow() {
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setVisible(true);
        setTitle("Version Verification Tool");
    }

    public JComboBox getDomainCombo() {
        return domainCombo;
    }

    public JComboBox getQaEnvironmentCombo() {
        return qaEnvironmentCombo;
    }

    public JComboBox getProdEnvironmentCombo() {
        return prodEnvironmentCombo;
    }

    public JTable getQaVersionResultTable() {
        return qaVersionResultTable;
    }

    public JTable getProductionVersionAndResultTable() {
        return productionVersionAndResultTable;
    }

    public JButton getVerifyButton() {
        return verifyButton;
    }

}
