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

public class MultipleListView extends CustomList implements CommandListener {
    
    private static final String RMS_NAME = "MultipleListExample";
    private final int MAX_ITEMS = 10;
    private boolean[] selections;
    private CommandListener parentCommandListener;
    private BackStack backStack;
    
    public MultipleListView(MIDlet parent, CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("Multiple"), List.MULTIPLE);
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
        boolean[] s = new boolean[this.size()];
        this.getSelectedFlags(s);
        String text = "List items ";
        boolean anySelected = false;
        for (int i = 0; i < s.length; i++) {
            if (s[i]) {
                text += (i + 1) + ", ";
                anySelected = true;
            }
        }
        if (anySelected) {
            text = text.substring(0, text.length() - 2);
            text += " selected.";
        }
        else {
            text = "No list items selected.";
        }
        
        Alert selectedAlert = new Alert(
            Compatibility.toLowerCaseIfFT("Selected"));
        selectedAlert.setString(text);
        selectedAlert.addCommand(Commands.ALERT_CONTINUE);
        selectedAlert.setTimeout(Alert.FOREVER);
        selectedAlert.setCommandListener(this);
        backStack.forward(selectedAlert);
    }
    
    private void showSaveChanges() {
        backStack.forward(new SaveChangesAlert(this));
    }
    
    private boolean hasChanges() {
        boolean[] s = new boolean[this.size()];
        this.getSelectedFlags(s);
        if (selections == null && s.length > 0) {
            // Pressed back with 1 or more items selected
            // but there was no previous information on selected items
            return true;
        }
        else {
            for (int i = 0; i < s.length; i++) {
                if (selections[i] != s[i]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void saveState() {
        boolean[] s = new boolean[this.size()];
        this.getSelectedFlags(s);
        ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);
            dout.writeInt(s.length);
            for (int i = 0, size = s.length; i < size; i++) {
                dout.writeBoolean(s[i]);
            }
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
                int length = din.readInt();
                selections = new boolean[length];
                for (int i = 0; i < length; i++) {
                    selections[i] = din.readBoolean();
                }
                this.setSelectedFlags(selections);
            }
            catch (IOException e) {
            }
        }
    }
}
