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
import java.io.*;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

public class ExclusiveListOriginalView extends List implements CommandListener {

    private static final String RMS_NAME = "ExclusiveListOriginalExample";
    private CommandListener parentCommandListener;
    private int exclusiveSelection;
    private BackStack backStack;
    
    public ExclusiveListOriginalView(MIDlet parent, CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("Exclusive list (original)"),
              List.EXCLUSIVE);
        this.setCommandListener(this);
        this.parentCommandListener = commandListener;
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.addCommand(Commands.DONE);
        
        for (int i = 0; i < 10; i++) {
            this.append(Compatibility.toLowerCaseIfFT("list item " +
                    String.valueOf(i+1)), null);
        }
        loadState();
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
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            backStack.back();
        }
        else {
            parentCommandListener.commandAction(c, d);
        }
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
