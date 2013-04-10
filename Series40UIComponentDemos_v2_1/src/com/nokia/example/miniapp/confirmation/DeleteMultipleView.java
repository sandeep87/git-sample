/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.confirmation;

import com.nokia.uihelpers.CustomList;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.IDisplayer;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class DeleteMultipleView
    extends CustomList
    implements CommandListener {

    private static final int MAX_ITEMS = 20;
    private IDisplayer displayer;
    private CommandListener parentCommandListener;
    private int selectedCount = 0;
    private int informationIndex = Strings.CONFIRMATION;

    public DeleteMultipleView(String title, Display display) {
        super(title, List.MULTIPLE);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(this);
        CustomList.Theme theme = CustomList.createTheme(display);
        theme.setToggleMultipleImageSelected("/element_close.png");
        setTheme(theme);

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

    public void setInformationIndex(int informationIndex) {
        this.informationIndex = informationIndex;
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
                    removeCommand(Commands.DELETE_INACTIVE_ICON);
                }
                addCommand(Commands.DELETE_ACTIVE_ICON);
            }
            // Nontouch device
            else {
                addCommand(Commands.DELETE_SCREEN);
            }
        }
        else {
            if (hasPointerEvents()) {
                removeCommand(Commands.DELETE_ACTIVE_ICON);
                if (Compatibility.supportsIconCommands()) {
                    addCommand(Commands.DELETE_INACTIVE_ICON);
                }
            }
            // Nontouch device
            else {
                removeCommand(Commands.DELETE_SCREEN);
            }
        }
    }

    private void deleteSelected() {
        boolean[] selection = new boolean[this.size()];
        getSelectedFlags(selection);
        selectedCount = 0;
        for (int i = selection.length - 1; i >= 0; i--) {
            if (selection[i]) {
                delete(i);
            }
        }
        refreshCommands();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.SELECT) {
            updateSelection();
        }
        else if (c == Commands.DELETE_ACTIVE_ICON
            || c == Commands.DELETE_SCREEN) {
            Alert alert = createDeleteDialog();
            alert.addCommand(Commands.ALERT_OK);
            alert.addCommand(Commands.ALERT_CANCEL);
            alert.setCommandListener(this);
            displayer.setCurrent(alert);
        }
        else if (c == Commands.ALERT_OK) {
            deleteSelected();
            // Trigger the back command on the parent listener
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else if (c == Commands.ALERT_CANCEL) {
            displayer.setCurrent(this);
        }
        else if (c == Commands.BACK) {
            if (selectedCount > 0) {
                Alert alert = createDeleteDialog();
                alert.addCommand(Commands.ALERT_SAVE_YES);
                alert.addCommand(Commands.ALERT_SAVE_NO);
                alert.addCommand(Commands.ALERT_SAVE_BACK);
                alert.setCommandListener(this);
                displayer.setCurrent(alert);
            }
            else {
                // Trigger the back command on the parent listener
                parentCommandListener.commandAction(Commands.BACK, d);
            }
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            setSelectedFlags(new boolean[size()]);
            updateSelection();
            // Trigger the back command on the parent listener
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else if (c == Commands.ALERT_SAVE_YES) {
            deleteSelected();
            // Trigger the back command on the parent listener
            parentCommandListener.commandAction(Commands.BACK, d);
        }
        else if(c == Commands.ALERT_SAVE_BACK) {
            displayer.setCurrent(this);
        }
        else if (c == Commands.INFORMATION) {
            // Show the information view
            displayer.setCurrent(new InformationView(
                informationIndex, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            displayer.setCurrent(this);
        }
    }

    private Alert createDeleteDialog() {
        return new Alert(Compatibility.toLowerCaseIfFT("Delete"),
            "Delete " + selectedCount + " item"
            + (selectedCount == 1 ? "" : "s") + "?",
            null,
            AlertType.CONFIRMATION);
    }
}
