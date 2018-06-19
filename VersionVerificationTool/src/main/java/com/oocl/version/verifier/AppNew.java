package com.oocl.version.verifier;

import com.oocl.version.verifier.controller.MainWindowController;
import com.oocl.version.verifier.view.MainWindow;

public class AppNew {

    public static void main(String[] args) {

        MainWindowController mainWindowController = new MainWindowController(new MainWindow());
        System.out.println("Started!");
    }
}
