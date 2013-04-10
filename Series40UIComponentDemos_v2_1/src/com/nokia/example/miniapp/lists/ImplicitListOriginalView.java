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
import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.uihelpers.Compatibility;
import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class ImplicitListOriginalView extends List implements CommandListener {

    private CommandListener parentCommandListener;
    private BackStack backStack;
    
    public ImplicitListOriginalView(MIDlet parent, CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("Implicit list (original)"),
              List.IMPLICIT);
        this.setCommandListener(this);
        this.parentCommandListener = commandListener;
        this.addCommand(Commands.BACK);
        this.setSelectCommand(List.SELECT_COMMAND);
        for (int i = 0; i < 10; i++) {
            this.append(Compatibility.toLowerCaseIfFT("list item " +
                    String.valueOf(i+1)), null);
        }
        backStack = new BackStack(parent);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.BACK) {
            parentCommandListener.commandAction(c, d);
        }
        else if (c == List.SELECT_COMMAND) {
            Alert selectedAlert = new Alert(
                Compatibility.toLowerCaseIfFT("Selected"));
            selectedAlert.setString(
                    "List item " +
                    (this.getSelectedIndex() + 1) +
                    " selected.");
            selectedAlert.addCommand(Commands.ALERT_CONTINUE);
            selectedAlert.setTimeout(Alert.FOREVER);
            selectedAlert.setCommandListener(this);
            backStack.forward(selectedAlert);
        }
        else if (c == Commands.ALERT_CONTINUE) {
            // First get out from the Alert
            backStack.back();
            // Then get out of this example back to the main List view
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else {
            parentCommandListener.commandAction(c, d);
        }
    }
}
