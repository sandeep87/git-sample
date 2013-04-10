/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */ 

package com.nokia.example.miniapp.lists;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.RMSUtils;
import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.uihelpers.Compatibility;
import com.nokia.uihelpers.CustomList;
import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class ExclusiveConfirmListView extends CustomList implements CommandListener {
    
    private static final String RMS_NAME = "ExclusiveConfirmListExample";
    private final int MAX_ITEMS = 10;
    private int exclusiveSelection;
    private CommandListener parentCommandListener;
    private BackStack backStack;
    
    public ExclusiveConfirmListView(MIDlet parent, CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("Exclusive"), List.EXCLUSIVE);
        this.setTheme(CustomList.createTheme(Display.getDisplay(parent)));
        
        for (int i = 0; i < MAX_ITEMS; i++) {
            this.append(Compatibility.toLowerCaseIfFT("List item ")
                + String.valueOf(i + 1), null);
        }
        
        this.setFitPolicy(List.TEXT_WRAP_DEFAULT);
        this.addCommand(Commands.DONE);
        this.addCommand(Commands.INFORMATION);
        this.addCommand(Commands.BACK);
        this.setCommandListener(this);
        loadState();
        parentCommandListener = commandListener;
        backStack = new BackStack(parent);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.BACK) {
            if (hasChanges()) {
                showSaveChanges();
            } else {
                parentCommandListener.commandAction(c, d);
            }
        }
        else if (c == Commands.DONE || c == Commands.ALERT_SAVE_YES) {
            saveState();
            showSelected();
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            backStack.back();
        }
        else if (c == Commands.ALERT_CONTINUE) {
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else {
            parentCommandListener.commandAction(c, d);
        }
    }
    
    private void showSelected() {
        Alert selectedAlert = new Alert(
            Compatibility.toLowerCaseIfFT("Selected"));
        selectedAlert.setString("List item " +
                (this.getSelectedIndex() + 1) +
                " selected.");
        selectedAlert.addCommand(Commands.ALERT_CONTINUE);
        selectedAlert.setTimeout(Alert.FOREVER);
        selectedAlert.setCommandListener(this);
        backStack.forward(selectedAlert);
    }
    
    private void showSaveChanges() {
        backStack.forward(new SaveChangesAlert(this));
    }
    
    private boolean hasChanges() {
        return (this.getSelectedIndex() != exclusiveSelection);
    }
    
    private void saveState() {
        ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);
            dout.writeInt(this.getSelectedIndex());
            RMSUtils.save(RMS_NAME, bout.toByteArray());
        }
        catch (IOException e) {
        }
        finally {
            try {
                if (bout != null) {
                    bout.close();
                }
            }
            catch (IOException e) {
            }
        }
    }
    
    private void loadState() {
        byte[] data = RMSUtils.load(RMS_NAME);
        if (data != null) {
            try {
                DataInputStream din =
                    new DataInputStream(new ByteArrayInputStream(data));
                exclusiveSelection = din.readInt();
                this.setSelectedIndex(exclusiveSelection, true);
            }
            catch (IOException e) {
            }
        }
        else {
            this.setSelectedIndex(0, true);
        }
    }
}
