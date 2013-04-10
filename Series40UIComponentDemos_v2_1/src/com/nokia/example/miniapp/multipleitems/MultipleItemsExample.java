/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.multipleitems;

import com.nokia.example.miniapp.confirmation.DeleteMultipleView;
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

public class MultipleItemsExample
    extends MIDlet
    implements CommandListener, IDisplayer {

    private final int POSITIVE_SELECTION = 0;
    private final int DELETE = 1;
    private final String[] LIST_ITEMS =
        Compatibility.toLowerCaseIfFT(new String[]{
            "Positive selection",
            "Delete"
        });
    private List list;
    private PositiveSelectionView positiveSelectionView;
    private DeleteMultipleView deleteMultipleView;
    private boolean[] positiveSelectionFlags;

    /**
     * Start the app, create and show the initial List view, setup listeners and
     * enable orientation support
     */
    public void startApp() {
        list = new List(Strings.getTitle(Strings.MULTIPLEITEMS), List.IMPLICIT,
            LIST_ITEMS, null);
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
        if (command == Commands.SELECT) {
            switch (list.getSelectedIndex()) {
                case POSITIVE_SELECTION:
                    // Lazily initialize PositiveSelectionView and display it
                    if (positiveSelectionView == null) {
                        positiveSelectionView =
                            new PositiveSelectionView(Display.getDisplay(this));
                        positiveSelectionView.setParentCommandListener(this);
                        positiveSelectionView.setDisplayer(this);
                        positiveSelectionFlags =
                            new boolean[positiveSelectionView.size()];
                    }
                    positiveSelectionView.setSelectedFlags(
                        positiveSelectionFlags);
                    setCurrent(positiveSelectionView);
                    break;
                case DELETE:
                    // Lazily initialize DeleteMultipleView and display it
                    if (deleteMultipleView == null) {
                        deleteMultipleView = new DeleteMultipleView(
                            Compatibility.toLowerCaseIfFT("Delete"),
                            Display.getDisplay(this));
                        deleteMultipleView.setParentCommandListener(this);
                        deleteMultipleView.setDisplayer(this);
                        deleteMultipleView.setInformationIndex(
                            Strings.MULTIPLEITEMS);
                    }
                    setCurrent(deleteMultipleView);
                    break;
            }
        }
        else if (command == Commands.EXIT) {
            notifyDestroyed();
        }
        // Coming back from child view via Back
        else if (command == Commands.BACK || command
            == Commands.INFORMATION_BACK) {
            setCurrent(list);
        }
        // Coming back from child view via OK; store the selected index
        else if (command == Commands.OK) {
            if (displayable == positiveSelectionView) {
                positiveSelectionView.getSelectedFlags(positiveSelectionFlags);
            }
            setCurrent(list);
        }
        else if (command == Commands.INFORMATION) {
            setCurrent(
                new InformationView(Strings.MULTIPLEITEMS, this));
        }
    }

    public void setCurrent(Displayable displayable) {
        Display.getDisplay(this).setCurrent(displayable);
    }
}
