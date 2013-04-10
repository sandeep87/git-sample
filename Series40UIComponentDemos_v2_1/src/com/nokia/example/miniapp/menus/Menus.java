/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */ 

package com.nokia.example.miniapp.menus;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.Compatibility;
import com.nokia.uihelpers.orientation.Orientation;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Menus extends MIDlet implements CommandListener {
    
    private final int OPTIONS = 0;
    private final int NO_OPTIONS = 1;
    private final String[] LIST_ITEMS = Compatibility.toLowerCaseIfFT(
        new String[] {
            "Options",
            "No options"
        }
    );
    private List list;
    private OptionsView optionsView;
    private NoOptionsView noOptionsView;
    private BackStack backStack;
    
    public void startApp() {
        list = new List(Strings.getTitle(Strings.MENUS),
            List.IMPLICIT, LIST_ITEMS, null);
        list.addCommand(Commands.BACK);
        list.addCommand(Commands.INFORMATION);
        list.setSelectCommand(List.SELECT_COMMAND);
        list.setCommandListener(this);
        Orientation.enableOrientations();
        backStack = new BackStack(this);
        backStack.forward(list);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == List.SELECT_COMMAND && d == list) {
            switch (list.getSelectedIndex()) {
                case OPTIONS:
                    if (optionsView == null) {
                        optionsView = new OptionsView(this);
                    }
                    backStack.forward(optionsView);
                    break;
                case NO_OPTIONS:
                    if (noOptionsView == null) {
                        noOptionsView = new NoOptionsView(this);
                    }
                    backStack.forward(noOptionsView);
                    break;
            }
        } else if (c == Commands.BACK || c == Commands.INFORMATION_BACK) {
            backStack.back();
        } else if (c == Commands.INFORMATION) {
            backStack.forward(new InformationView(Strings.MENUS, this));
        } else if (c != List.SELECT_COMMAND) {
            // Display an alert for the selected menu option
            Alert alert = new Alert("Action");
            alert.setString("You selected " + c.getLabel());
            alert.setTimeout(3000);
            alert.setType(AlertType.CONFIRMATION);
            Display.getDisplay(this).setCurrent(alert);
        }
    }
}
