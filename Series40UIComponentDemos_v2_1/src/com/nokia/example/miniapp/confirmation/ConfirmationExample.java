/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.confirmation;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.IDisplayer;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.orientation.Orientation;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

public class ConfirmationExample
    extends MIDlet
    implements CommandListener, IDisplayer {

    private final int BACK_FROM_FORM = 0;
    private final int DELETE_MULTIPLE = 1;
    private final String[] LIST_ITEMS = Compatibility.toLowerCaseIfFT(
        new String[]{
            "Back from form",
            "Delete multiple"
        });
    private List list;
    private int selectedIndex = 0;
    private FormConfirmationView formConfirmationView;
    private DeleteMultipleView deleteMultipleView;

    /**
     * Start the app, create and show the initial List view, setup listeners and
     * enable orientation support
     */
    public void startApp() {
        list = new List(Strings.getTitle(Strings.CONFIRMATION),
            List.IMPLICIT, LIST_ITEMS, null);
        list.setSelectCommand(Commands.SELECT);
        list.addCommand(Commands.EXIT);
        list.addCommand(Commands.INFORMATION);
        list.setCommandListener(this);
        Orientation.enableOrientations();
        Display.getDisplay(this).setCurrent(list);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command command, Displayable displayable) {
        Display d = Display.getDisplay(this);

        if (command == Commands.SELECT) {
            switch (list.getSelectedIndex()) {
                case BACK_FROM_FORM:
                    // Lazily initialize FormConfirmationView and display it
                    if (formConfirmationView == null) {
                        formConfirmationView = new FormConfirmationView();
                        formConfirmationView.setParentCommandListener(this);
                        formConfirmationView.setDisplayer(this);
                    }
                    // Set the selected index
                    formConfirmationView.setSelected(selectedIndex);
                    d.setCurrent(formConfirmationView);
                    break;
                case DELETE_MULTIPLE:
                    // Lazily initialize DeleteMultipleView and display it
                    if (deleteMultipleView == null) {
                        deleteMultipleView = new DeleteMultipleView(
                            Compatibility.toLowerCaseIfFT("Delete multiple"),
                            Display.getDisplay(this));
                        deleteMultipleView.setParentCommandListener(this);
                        deleteMultipleView.setDisplayer(this);
                    }
                    d.setCurrent(deleteMultipleView);
                    break;
            }
        }
        else if (command == Commands.EXIT) {
            notifyDestroyed();
        }
        // Coming back from child view via Back
        else if (command == Commands.BACK || command
            == Commands.INFORMATION_BACK) {
            d.setCurrent(list);
        }
        // Coming back from child view via OK; store the selected index
        else if (command == Commands.OK) {
            d.setCurrent(list);
            this.selectedIndex = formConfirmationView.getSelected();
        }
        else if (command == Commands.INFORMATION) {
            d.setCurrent(new InformationView(Strings.CONFIRMATION, this));
        }
    }

    public void setCurrent(Displayable displayable) {
        Display.getDisplay(this).setCurrent(displayable);
    }
}
