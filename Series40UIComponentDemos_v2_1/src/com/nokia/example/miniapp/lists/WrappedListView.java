/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */ 

package com.nokia.example.miniapp.lists;

import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.uihelpers.CustomList;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;


public class WrappedListView extends CustomList implements CommandListener {
    
    private static final String[] LONG_ITEMS = {
        "List item 1, Lorem ipsum dolor sit amet, consectetur adipiscing "
        + "elit. Phasellus vel neque pulvinar libero vestibulum malesua.",
        "List item 2, Lorem ipsum dolor sit amet, consectetur adipiscing "
        + "elit. Phasellus vel neque pulvinar libero vestibulum malesua.",
        "List item 3, Lorem ipsum dolor sit amet, consectetur adipiscing "
        + "elit. Phasellus vel neque pulvinar libero vestibulum malesua."
    };
    private CommandListener parentCommandListener;
    private BackStack backStack;
    
    public WrappedListView(MIDlet parent, CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("Wrapped"),
              List.IMPLICIT,
              LONG_ITEMS,
              null);
        this.setTheme(CustomList.createTheme(Display.getDisplay(parent)));        
        this.setFitPolicy(List.TEXT_WRAP_ON);
        this.setSelectCommand(Commands.LIST_SELECT);
        this.addCommand(Commands.INFORMATION);
        this.addCommand(Commands.BACK);
        this.setCommandListener(this);
        parentCommandListener = commandListener;
        backStack = new BackStack(parent);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.BACK) {
            parentCommandListener.commandAction(c, d);
        }
        else if (c == Commands.LIST_SELECT) {
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
