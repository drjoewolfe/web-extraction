package com.jwolfe.automation.client;

import com.jwolfe.automation.types.AutomationConfiguration;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
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
        if (callback != null) {
            callback.accept(this.config);
        }

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Save");
        panel2.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Chrome Driver Location:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 5, 0, 0);
        panel3.add(label1, gbc);
        chromeDriverLocationTextField = new JTextField();
        chromeDriverLocationTextField.setColumns(0);
        chromeDriverLocationTextField.setText("/home/apput/Desktop/dev/jw/jars/chromedriver");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 0, 0);
        panel3.add(chromeDriverLocationTextField, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("User Data Directory:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 5, 0, 0);
        panel3.add(label2, gbc);
        chromeUserDataDirectoryTextField = new JTextField();
        chromeUserDataDirectoryTextField.setText("/home/apput/.config/google-chrome/");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 0, 0);
        panel3.add(chromeUserDataDirectoryTextField, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Google Chrome Options");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label3, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 18, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Firefox Options");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel3.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Firefox Driver Location:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 5, 0, 0);
        panel3.add(label5, gbc);
        firefoxDriverLocationTextField = new JTextField();
        firefoxDriverLocationTextField.setColumns(0);
        firefoxDriverLocationTextField.setText("/home/apput/Desktop/dev/jw/jars/geckodriver");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 0, 0);
        panel3.add(firefoxDriverLocationTextField, gbc);
        chromeStartMaximizedCheckBox = new JCheckBox();
        chromeStartMaximizedCheckBox.setText("Start Maximized");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeStartMaximizedCheckBox, gbc);
        chromeDisableExtensionsCheckBox = new JCheckBox();
        chromeDisableExtensionsCheckBox.setText("Disable Extensions");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeDisableExtensionsCheckBox, gbc);
        chromeDisableNotificationsCheckBox = new JCheckBox();
        chromeDisableNotificationsCheckBox.setText("Disable Notifications");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeDisableNotificationsCheckBox, gbc);
        chromeDisablePopUpBlockingCheckBox = new JCheckBox();
        chromeDisablePopUpBlockingCheckBox.setText("Disable Pop-up Blocking");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeDisablePopUpBlockingCheckBox, gbc);
        chromeEnableCredentialsServiceCheckBox = new JCheckBox();
        chromeEnableCredentialsServiceCheckBox.setText("Enable Credentials Service");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeEnableCredentialsServiceCheckBox, gbc);
        chromeEnablePasswordManagerCheckBox = new JCheckBox();
        chromeEnablePasswordManagerCheckBox.setText("Enable Password Manager");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeEnablePasswordManagerCheckBox, gbc);
        chromeEnableW3CCheckBox = new JCheckBox();
        chromeEnableW3CCheckBox.setText("Enable W3C");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeEnableW3CCheckBox, gbc);
        chromeNoSandboxCheckBox = new JCheckBox();
        chromeNoSandboxCheckBox.setText("No Sandbox");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chromeNoSandboxCheckBox, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Implicit Timeout (seconds): ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 5, 0, 0);
        panel3.add(label6, gbc);
        chromeImplicitTimeoutTextField = new JTextField();
        chromeImplicitTimeoutTextField.setText("20");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 0, 0);
        panel3.add(chromeImplicitTimeoutTextField, gbc);
        firefoxStartMaximizedCheckBox = new JCheckBox();
        firefoxStartMaximizedCheckBox.setText("Start Maximized");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(firefoxStartMaximizedCheckBox, gbc);
        firefoxDisableNotificationsCheckBox = new JCheckBox();
        firefoxDisableNotificationsCheckBox.setText("Disable Notifications");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(firefoxDisableNotificationsCheckBox, gbc);
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
