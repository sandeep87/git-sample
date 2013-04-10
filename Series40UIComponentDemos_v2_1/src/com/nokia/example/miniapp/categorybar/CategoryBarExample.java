/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.categorybar;

import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.CategoryBarUtils;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.orientation.Orientation;
import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.ElementListener;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class CategoryBarExample
    extends MIDlet
    implements CommandListener, CategoryBarUtils.ElementListener {

    private final int FOUR_CATEGORIES = 0;
    private final int SIX_CATEGORIES = 1;
    private final int FIFTEEN_CATEGORIES = 2;
    private final String[] LIST_ITEMS = {
        "4 categories",
        "6 categories",
        "15 categories"
    };
    private List viewList;
    private CategoryBar categoryBar;
    private CategoryBarView categoryBarView;
    private CategoryGridView categoryGridView;
    private BackStack backStack;

    /**
     * Start the app, create and show the initial List view,
     * setup listeners and enable orientation support
     */
    public void startApp() {
        viewList = new List(Strings.getTitle(Strings.CATEGORYBAR),
            List.IMPLICIT, LIST_ITEMS, null);
        viewList.setCommandListener(this);
        viewList.setSelectCommand(Commands.SELECT);
        viewList.addCommand(Commands.EXIT);
        viewList.addCommand(Commands.INFORMATION);

        Orientation.enableOrientations();
        backStack = new BackStack(this);
        Display.getDisplay(this).setCurrent(viewList);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.SELECT) {
            // Initialize and display the requested demo view
            displayView(viewList.getSelectedIndex());
        }
        else if (c == Commands.BACK || c == Commands.EXIT) {
            backStack.back();
        }
        else if (c == Commands.INFORMATION) {
            if (categoryBar != null) {
                categoryBar.setVisibility(false);
            }
            backStack.forward(new InformationView(Strings.CATEGORYBAR,
                this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            if (categoryBar != null) {
                categoryBar.setVisibility(true);
            }
            backStack.back();
        }
        else if (c == Commands.LIST_SELECT) {
            int index = categoryGridView.getSelectedIndex();
            Form form = new Form(CategoryGridView.NAMES[index]);
            form.append(CategoryGridView.NAMES[index] + " screen");
            form.addCommand(Commands.BACK);
            form.addCommand(Commands.INFORMATION);
            form.setCommandListener(this);
            backStack.forward(form);
        }
    }

    /**
     * Handles CategoryBar events, tells the currently visible CategoryBarView
     * to switch view to whatever item is tapped
     * @param categoryBar
     * @param selectedIndex 
     */
    public void notifyElementSelected(CategoryBar categoryBar, int selectedIndex) {
        switch (selectedIndex) {
            case ElementListener.BACK:
                categoryBar.setVisibility(false);
                backStack.back();
                break;
            default:
                categoryBarView.setActive(selectedIndex);
                break;
        }
    }

    private void displayView(int index) {
        switch (index) {
            case FOUR_CATEGORIES:
                createCategoryView(4);
                break;
            case SIX_CATEGORIES:
                createCategoryView(6);
                break;
            case FIFTEEN_CATEGORIES:
                createCategoryView(15);
                break;
            default:
                break;
        }
    }

    /**
     * Generates and displays the CategoryBarView with the requested
     * amount of items
     * @param amountOfCategories 
     */
    private void createCategoryView(int amountOfCategories) {
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");
            Class.forName("com.nokia.mid.ui.ElementListener");
            Class.forName("com.nokia.mid.ui.IconCommand");
            categoryBarView = new CategoryBarView(amountOfCategories);
            categoryBarView.setCommandListener(this);
            categoryBarView.setActive(0);

            categoryBar = categoryBarView.createCategoryBar();
            CategoryBarUtils.setListener(categoryBar, this);
            categoryBar.setVisibility(true);

            Display d = Display.getDisplay(this);
            categoryBar.setHighlightColour(d.getColor(
                Display.COLOR_HIGHLIGHTED_BACKGROUND));

            // Navigate to the created view
            backStack.forward(categoryBarView);
        }
        catch (ClassNotFoundException e) {
            categoryGridView = new CategoryGridView(amountOfCategories);
            categoryGridView.setCommandListener(this);
            CategoryGridView.Theme theme = CategoryGridView.createTheme(
                Display.getDisplay(this));
            theme.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
                Font.SIZE_SMALL));
            categoryGridView.setTheme(theme);
            backStack.forward(categoryGridView);
        }
    }
}
