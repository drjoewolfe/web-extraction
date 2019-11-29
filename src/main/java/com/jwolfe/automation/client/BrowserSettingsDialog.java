package com.jwolfe.automation.client;

import com.jwolfe.automation.types.*;

import javax.swing.*;
import java.awt.event.*;

public class BrowserSettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField chromeDriverLocationTextField;
    private JTextField chromeUserDataDirectoryTextField;
    private JTextField firefoxDriverLocationTextField;

    public BrowserSettingsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void show(AutomationConfiguration config) {
        setSize(500, 400);
        updateConfigurationFields(config);

        setVisible(true);
    }

    private void updateConfigurationFields(AutomationConfiguration config) {
        updateChromeConfigurationFields(config);
        updateFirefoxConfigurationFields(config);
    }

    private void updateChromeConfigurationFields(AutomationConfiguration config) {
        var browserDriverOptions = config.getBrowserDriverOptions();

        chromeDriverLocationTextField.setText(browserDriverOptions.getChromeDriverLocation());
        chromeUserDataDirectoryTextField.setText(browserDriverOptions.getChromeUserDataDirectory());
    }

    private void updateFirefoxConfigurationFields(AutomationConfiguration config) {
        var browserDriverOptions = config.getBrowserDriverOptions();

        firefoxDriverLocationTextField.setText(browserDriverOptions.getFirefoxDriverLocation());
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
