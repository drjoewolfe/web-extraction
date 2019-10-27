package com.jwolfe.automation.client;

import javax.swing.*;
import java.awt.event.*;

public class InterventionRequestDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea labelTextArea;
    private JTextArea messagesTextArea;

    private boolean confirmBeforeAction;
    private String syncMessage;

    private Thread requesterThread;

    public void setConfirmBeforeAction(boolean confirmBeforeAction) {
        this.confirmBeforeAction = confirmBeforeAction;
    }

    public InterventionRequestDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("User Intervention Request");
        labelTextArea.setText("The current extractor has requested for user intervention. The steps and expectations are listed below. Carry out the steps & click on 'Complete Interaction' to proceed. NOTE: If you close the window, or click cancel, the extraction process will be aborted.");

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

    public InterventionRequestDialog(Thread requesterThread) {
        this();

        this.requesterThread = requesterThread;
    }

    public void show(String message) {
        syncMessage = message;
        messagesTextArea.setText(message);

        setSize(400, 400);
        setVisible(true);
    }

    private void onOK() {
        int confirmation = 0;
        if(confirmBeforeAction) {
            confirmation = JOptionPane.showConfirmDialog(this, "Confirmation assumes that you have completed all the steps / expectations listed out by this request. \n" +
                    "Are you sure you want to confirm the interaction completion ?");
        }

        if (confirmation == 0) {
            synchronized (syncMessage) {
                syncMessage.notifyAll();
            }

            dispose();
        }
    }

    private void onCancel() {
        int confirmation = 0;
        if(confirmBeforeAction) {
            confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the interaction ? \n" +
                    "The current extraction will be aborted.");
        }

        // 0=yes, 1=no, 2=cancel
        if(confirmation == 0) {
            if(requesterThread != null) {
                requesterThread.interrupt();
            }

            dispose();
        }
    }
}
