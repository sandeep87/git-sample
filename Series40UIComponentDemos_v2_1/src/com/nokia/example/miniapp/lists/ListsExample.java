package com.nokia.example.miniapp.lists;

/*
 * Copyright © 2012 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.orientation.Orientation;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * This example demonstrates the usage of the List class
 */
public class ListsExample
    extends MIDlet
    implements CommandListener {

    private static final int IMPLICIT_LIST = 0;
    private static final int IMPLICIT_ACTION_LIST = 1;
    private static final int EXCLUSIVE_LIST = 2;
    private static final int EXCLUSIVE_CONFIRM_LIST = 3;
    private static final int MULTIPLE_LIST = 4;
    private static final int TRUNCATED_LIST = 5;
    private static final int WRAPPED_LIST = 6;
    private static final int THUMBNAILS_LIST = 7;
    private static final int FANCY_LIST = 8;
    private static final int GRID_LIST = 9;
    private static final int EXCLUSIVE_LIST_ORIGINAL = 10;
    private static final int MULTIPLE_LIST_ORIGINAL = 11;
    private static final int IMPLICIT_LIST_ORIGINAL = 12;

    private static final String[] LIST_ITEMS = {
        "Implicit",
        "Implicit + action",
        "Exclusive",
        "Exclusive + confirm",
        "Multiple",
        "Truncated",
        "Wrapped",
        "Thumbnails",
        "Fancy list",
        "Grid list",
        "Exclusive list (original)",
        "Multiple list (original)",
        "Implicit list (original)"
    };
    private static final String[] LIST_ITEMS_NON_TOUCH = {
        "Implicit",
        "Exclusive",
        "Exclusive + confirm",
        "Multiple",
        "Truncated",
        "Wrapped",
        "Thumbnails",
        "Fancy list",
        "Grid list",
        "Exclusive list (original)",
        "Multiple list (original)",
        "Implicit list (original)"
    };

    private List list;
    private BackStack backStack;
    private int listType;
    
    /**
     * Start the app, create and display the initial list view
     */
    public void startApp() {
        // for some reason SDK 2.0 cannot handle these in static initializer
        Compatibility.toLowerCaseIfFT(LIST_ITEMS);
        list =
            new List(Strings.getTitle(Strings.LISTS), List.IMPLICIT,
            Compatibility.isNonTouch() ? LIST_ITEMS_NON_TOUCH : LIST_ITEMS,
            null);
        list.setSelectCommand(Commands.SELECT);
        list.addCommand(Commands.INFORMATION);
        list.addCommand(Commands.BACK);
        list.setCommandListener(this);
        Display.getDisplay(this).setCurrent(list);

        Orientation.enableOrientations();
        backStack = new BackStack(this);

    }

    public void pauseApp() {
    }

    public void destroyApp(boolean a)
        throws MIDletStateChangeException {
    }

    /**
     * Displays the requested view
     *
     * @param index
     */
    private void showListView(int index) {
        listType = index;
        if (Compatibility.isNonTouch() && listType >= IMPLICIT_ACTION_LIST) {
            listType++;
        }
        switch (listType) {
            case IMPLICIT_LIST:
                backStack.forward(new ImplicitListView(this, this));
                break;
            case IMPLICIT_ACTION_LIST:
                backStack.forward(new ImplicitActionListView(this, this));
                break;
            case EXCLUSIVE_LIST:
                backStack.forward(new ExclusiveListView(this, this));
                break;
            case EXCLUSIVE_CONFIRM_LIST:
                backStack.forward(new ExclusiveConfirmListView(this, this));
                break;
            case MULTIPLE_LIST:
                backStack.forward(new MultipleListView(this, this));
                break;
            case TRUNCATED_LIST:
                backStack.forward(new TruncatedListView(this, this));
                break;
            case WRAPPED_LIST:
                backStack.forward(new WrappedListView(this, this));
                break;
            case THUMBNAILS_LIST:
                backStack.forward(new ThumbnailListView(this, this));
                break;
            case FANCY_LIST:
                backStack.forward(new FancyListView(this, this));
                break;
            case GRID_LIST:
                backStack.forward(new GridListView(this, this));
                break;
            case EXCLUSIVE_LIST_ORIGINAL:
                backStack.forward(new ExclusiveListOriginalView(this, this));
                break;
            case MULTIPLE_LIST_ORIGINAL:
                backStack.forward(new MultipleListOriginalView(this, this));
                break;
            case IMPLICIT_LIST_ORIGINAL:
                backStack.forward(new ImplicitListOriginalView(this, this));
                break;
        }
    }

    /**
     * Handle the commands made in this or the child views
     *
     * @param c
     * @param disp
     */
    public void commandAction(Command c, Displayable d) {
        if (c == Commands.SELECT) {
            showListView(list.getSelectedIndex());
        }
        else if (c == Commands.BACK) {
            backStack.back();
        }
        else if (c == Commands.INFORMATION) {
            backStack.forward(new InformationView(Strings.LISTS, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            backStack.back();
        }
    }
}
