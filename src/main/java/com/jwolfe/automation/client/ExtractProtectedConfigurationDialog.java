package com.jwolfe.automation.client;

import com.jwolfe.automation.configuration.YamlConfigLoader;
import com.jwolfe.automation.types.AutomationConfiguration;

import javax.swing.*;
import java.awt.event.*;

public class ExtractProtectedConfigurationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordTextField;
    private JLabel configFilePathLabel;

    private String configFilePath;
    private boolean extractionSucceeded;
    private AutomationConfiguration configuration;

    public boolean isExtractionSucceeded() {
        return extractionSucceeded;
    }

    public AutomationConfiguration getConfiguration() {
        return configuration;
    }

    public ExtractProtectedConfigurationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Protected Configuration");
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

    public void show(String configFilePath) {
        this.configFilePath = configFilePath;


        configFilePathLabel.setText(configFilePath);
        setSize(683, 177);
        setVisible(true);
    }

    private void onOK() {
        String password = String.valueOf(passwordTextField.getPassword());
        try {
            configuration = (new YamlConfigLoader()).getConfigurationFromFile(configFilePath, password);
        } catch (Exception e) {
            // Failed to extract configuration
            JOptionPane.showMessageDialog(this, "Could not unlock the configuration with the specified password.\n" +
                            "This could be because the password is incorrect. This could also be caused if the configuration is corrupt.\n" +
                            "Try again with the correct password if appropriate.");
            extractionSucceeded = false;
            return;
        }

        extractionSucceeded = true;
        dispose();
    }

    private void onCancel() {
        extractionSucceeded = false;
        dispose();
    }
}
