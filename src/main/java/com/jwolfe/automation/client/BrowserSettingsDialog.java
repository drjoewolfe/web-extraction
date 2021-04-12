package com.jwolfe.automation.client;

import com.jwolfe.automation.types.AutomationConfiguration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class BrowserSettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField chromeDriverLocationTextField;
    private JTextField chromeUserDataDirectoryTextField;
    private JTextField firefoxDriverLocationTextField;
    private JCheckBox chromeStartMaximizedCheckBox;
    private JCheckBox chromeDisableExtensionsCheckBox;
    private JCheckBox chromeDisableNotificationsCheckBox;
    private JCheckBox chromeDisablePopUpBlockingCheckBox;
    private JCheckBox chromeEnableCredentialsServiceCheckBox;
    private JCheckBox chromeEnablePasswordManagerCheckBox;
    private JCheckBox chromeEnableW3CCheckBox;
    private JCheckBox chromeNoSandboxCheckBox;
    private JTextField chromeImplicitTimeoutTextField;
    private JCheckBox firefoxStartMaximizedCheckBox;
    private JCheckBox firefoxDisableNotificationsCheckBox;

    private AutomationConfiguration config;
    private Consumer<AutomationConfiguration> callback;

    public AutomationConfiguration getConfig() {
        return config;
    }

    public void setConfig(AutomationConfiguration config) {
        this.config = config;
    }

    public BrowserSettingsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
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
            public void actionPerformed(final ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void show(final AutomationConfiguration config) {
        this.show(config, null);
    }

    public void show(final AutomationConfiguration config, final Consumer<AutomationConfiguration> callback) {
        this.callback = callback;

        setSize(500, 500);
        setConfig(config);
        updateFieldsFromConfig();

        setVisible(true);
    }

    private void updateFieldsFromConfig() {
        updateChromeFieldsFromConfig();
        updateFirefoxFieldsFromConfig();
    }

    private void updateChromeFieldsFromConfig() {
        var browserDriverOptions = config.getBrowserDriverOptions();

        chromeDriverLocationTextField.setText(browserDriverOptions.getChromeDriverLocation());
        chromeUserDataDirectoryTextField.setText(browserDriverOptions.getChromeUserDataDirectory());

        chromeStartMaximizedCheckBox.setSelected(browserDriverOptions.isChromeStartMaximized());
        chromeDisableExtensionsCheckBox.setSelected(browserDriverOptions.isChromeDisableExtensions());
        chromeDisableNotificationsCheckBox.setSelected(browserDriverOptions.isChromeDisableNotifications());
        chromeDisablePopUpBlockingCheckBox.setSelected(browserDriverOptions.isChromeDisablePopupBlocking());
        chromeEnableCredentialsServiceCheckBox.setSelected(browserDriverOptions.isChromeCredentialsServiceEnabled());
        chromeEnablePasswordManagerCheckBox.setSelected(browserDriverOptions.isChromePasswordManagerEnabled());
        chromeEnableW3CCheckBox.setSelected(browserDriverOptions.isChromeW3CEnabled());
        chromeNoSandboxCheckBox.setSelected(browserDriverOptions.isChromeNoSandbox());

        chromeImplicitTimeoutTextField.setText(String.valueOf(browserDriverOptions.getChromeImplicitTimeout()));
    }

    private void updateFirefoxFieldsFromConfig() {
        var browserDriverOptions = config.getBrowserDriverOptions();

        firefoxDriverLocationTextField.setText(browserDriverOptions.getFirefoxDriverLocation());

        firefoxStartMaximizedCheckBox.setSelected(browserDriverOptions.isFirefoxStartMaximized());
        firefoxDisableNotificationsCheckBox.setSelected(browserDriverOptions.isFirefoxDisableNotifications());
    }

    private void updateConfigFromFields() {
        updateChromeConfigFromFields();
        updateFirefoxConfigFromFields();
    }

    private void updateChromeConfigFromFields() {
        var browserDriverOptions = config.getBrowserDriverOptions();

        browserDriverOptions.setChromeDriverLocation(chromeDriverLocationTextField.getText());
        browserDriverOptions.setChromeUserDataDirectory(chromeUserDataDirectoryTextField.getText());

        browserDriverOptions.setChromeStartMaximized(chromeStartMaximizedCheckBox.isSelected());
        browserDriverOptions.setChromeDisableExtensions(chromeDisableExtensionsCheckBox.isSelected());
        browserDriverOptions.setChromeDisableNotifications(chromeDisableNotificationsCheckBox.isSelected());
        browserDriverOptions.setChromeDisablePopupBlocking(chromeDisablePopUpBlockingCheckBox.isSelected());
        browserDriverOptions.setChromeCredentialsServiceEnabled(chromeEnableCredentialsServiceCheckBox.isSelected());
        browserDriverOptions.setChromePasswordManagerEnabled(chromeEnablePasswordManagerCheckBox.isSelected());
        browserDriverOptions.setChromeW3CEnabled(chromeEnableW3CCheckBox.isSelected());
        browserDriverOptions.setChromeNoSandbox(chromeNoSandboxCheckBox.isSelected());

        browserDriverOptions.setChromeImplicitTimeout(Integer.valueOf(chromeImplicitTimeoutTextField.getText()));
    }

    private void updateFirefoxConfigFromFields() {
        var browserDriverOptions = config.getBrowserDriverOptions();

        browserDriverOptions.setFirefoxDriverLocation(firefoxDriverLocationTextField.getText());

        firefoxStartMaximizedCheckBox.setEnabled(browserDriverOptions.isFirefoxStartMaximized());
        firefoxDisableNotificationsCheckBox.setEnabled(browserDriverOptions.isFirefoxDisableNotifications());

        browserDriverOptions.setFirefoxStartMaximized(firefoxStartMaximizedCheckBox.isSelected());
        browserDriverOptions.setFirefoxDisableNotifications(firefoxDisableNotificationsCheckBox.isSelected());
    }

    private void onOK() {
        updateConfigFromFields();
        if(callback != null) {
            callback.accept(this.config);
        }

        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
