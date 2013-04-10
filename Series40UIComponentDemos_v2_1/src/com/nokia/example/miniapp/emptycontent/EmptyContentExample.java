/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.emptycontent;

import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.orientation.Orientation;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

public class EmptyContentExample
    extends MIDlet
    implements CommandListener {

    private final int INDICATE_NO_CONTENT = 0;
    private final int ADD_BUTTON = 1;
    private final int ADD_IN_ACTION_1 = 2;
    private final int ADD_IN_MSK = 1;
    private final String[] LIST_ITEMS = Compatibility.toLowerCaseIfFT(
        new String[]{
            "Indicate 'no content'",
            "Add button",
            Compatibility.isFullTouch() ? "Add in action #1"
            : "Add in Middle Soft Key"
        });
    private final String[] NON_TOUCH_LIST_ITEMS = {
        "Indicate 'no content'",
        "Add in Middle Soft Key"
    };
    private List list;
    private BackStack backStack;
    private NoContentView noContentView;
    private AddButtonView addButtonView;
    private AddInAction1View addInAction1View;

    /**
     * Start the app, create and show the initial List view, setup listeners and
     * enable orientation support
     */
    public void startApp() {
        list = new List(Strings.getTitle(Strings.EMPTYCONTENT),
            List.IMPLICIT, Compatibility.isNonTouch() ? NON_TOUCH_LIST_ITEMS
            : LIST_ITEMS, null);
        list.setSelectCommand(Commands.SELECT);
        list.addCommand(Commands.EXIT);
        list.addCommand(Commands.INFORMATION);
        list.setCommandListener(this);
        backStack = new BackStack(this);
        Orientation.enableOrientations();
        Display.getDisplay(this).setCurrent(list);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == Commands.SELECT) {
            // Lazily initialize and display the wanted view
            if (Compatibility.isNonTouch()) {
                switch (list.getSelectedIndex()) {
                    case INDICATE_NO_CONTENT:
                        if (noContentView == null) {
                            noContentView = new NoContentView(this);
                        }
                        backStack.forward(noContentView);
                        break;
                    case ADD_IN_MSK:
                        if (addInAction1View == null) {
                            addInAction1View = new AddInAction1View(
                                LIST_ITEMS[ADD_IN_MSK], this);
                        }
                        backStack.forward(addInAction1View);
                        break;
                }
            }
            else {
                switch (list.getSelectedIndex()) {
                    case INDICATE_NO_CONTENT:
                        if (noContentView == null) {
                            noContentView = new NoContentView(this);
                        }
                        backStack.forward(noContentView);
                        break;
                    case ADD_BUTTON:
                        if (addButtonView == null) {
                            addButtonView = new AddButtonView(
                                LIST_ITEMS[ADD_BUTTON], this);
                        }
                        backStack.forward(addButtonView);
                        break;
                    case ADD_IN_ACTION_1:
                        if (addInAction1View == null) {
                            addInAction1View = new AddInAction1View(
                                LIST_ITEMS[ADD_IN_ACTION_1], this);
                        }
                        backStack.forward(addInAction1View);
                        break;
                }
            }
        }
        else if (command == Commands.EXIT
            || command == Commands.BACK
            || command == Commands.INFORMATION_BACK) {
            backStack.back();
        }
        else if (command == Commands.INFORMATION) {
            backStack.forward(new InformationView(Strings.EMPTYCONTENT,
                this));
        }
    }
}
