/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.multipleitems;

import com.nokia.uihelpers.CustomList;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.IDisplayer;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.example.miniapp.utils.Strings;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class PositiveSelectionView
    extends CustomList
    implements CommandListener {

    private static final int MAX_ITEMS = 10;
    private IDisplayer displayer;
    private CommandListener parentCommandListener;
    private int selectedCount = 0;
    private boolean[] savedSelection = new boolean[MAX_ITEMS];

    public PositiveSelectionView(Display display) {
        super(Compatibility.toLowerCaseIfFT("Positive selection"),
            List.MULTIPLE);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(this);
        setTheme(CustomList.createTheme(display));

        for (int i = 0; i < MAX_ITEMS; i++) {
            append(Compatibility.toLowerCaseIfFT("List item ")
                + String.valueOf(i + 1), null);
        }

        this.setSelectCommand(Commands.SELECT);
        refreshCommands();
    }

    public void setParentCommandListener(CommandListener listener) {
        parentCommandListener = listener;
    }

    public void setDisplayer(IDisplayer displayer) {
        this.displayer = displayer;
    }

    public void setSelectedFlags(boolean[] selectedArray)
        throws NullPointerException {
        super.setSelectedFlags(selectedArray);
        savedSelection = selectedArray;
        updateSelection();
    }

    private void updateSelection() {
        boolean[] selection = new boolean[this.size()];
        getSelectedFlags(selection);
        selectedCount = 0;
        for (int i = 0; i < selection.length; i++) {
            if (selection[i]) {
                selectedCount++;
            }
        }
        refreshCommands();
    }

    private void refreshCommands() {
        if (selectedCount > 0) {
            if (hasPointerEvents()) {
                if (Compatibility.supportsIconCommands()) {
                    removeCommand(Commands.OK_INACTIVE_ICON);
                }
                addCommand(Commands.OK_ACTIVE_ICON);
            }
            else {
                addCommand(Commands.OK_SCREEN);
            }
        }
        else {
            if (hasPointerEvents()) {
                removeCommand(Commands.OK_ACTIVE_ICON);
                if (Compatibility.supportsIconCommands()) {
                    addCommand(Commands.OK_INACTIVE_ICON);
                }
            }
            else {
                removeCommand(Commands.OK_SCREEN);
            }
        }
    }

    private void confirmBack() {
        displayer.setCurrent(new SaveChangesAlert(this));
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.SELECT) {
            updateSelection();
        }
        else if (c == Commands.BACK) {
            if (hasChanges()) {
                confirmBack();
            }
            else {
                parentCommandListener.commandAction(Commands.BACK, this);
            }
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            // Trigger the back command on the parent listener
            parentCommandListener.commandAction(Commands.BACK, this);
        }
        else if (c == Commands.OK_ACTIVE_ICON
            || c == Commands.OK_SCREEN
            || c == Commands.ALERT_SAVE_YES) {
            // Trigger the back command on the parent listener
            parentCommandListener.commandAction(Commands.OK, this);
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            displayer.setCurrent(this);
        }
        else if (c == Commands.INFORMATION) {
            // Show the information view
            displayer.setCurrent(new InformationView(
                Strings.MULTIPLEITEMS, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            displayer.setCurrent(this);
        }
    }

    private boolean hasChanges() {
        boolean[] selection = new boolean[this.size()];
        getSelectedFlags(selection);
        for (int i = 0; i < selection.length; i++) {
            if (savedSelection[i] != selection[i]) {
                return true;
            }
        }
        return false;
    }
}
