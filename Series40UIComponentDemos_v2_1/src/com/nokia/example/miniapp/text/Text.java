/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.text;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.IDisplayer;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.VirtualKeyboardUtils;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.uihelpers.Compatibility;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import com.nokia.uihelpers.orientation.Orientation;

/**
 * Demonstrates the usage of the TextBox class
 */
public class Text
    extends MIDlet
    implements CommandListener, IDisplayer {

    private final int EDITABLE_TEXT = 0;
    private final int NON_EDITABLE_TEXT = 1;
    private final int EDIT_FLOW = 2;
    private List list;
    private ExampleTextView editableView;
    private ExampleTextView nonEditableView;
    private ExampleTextView editFlowView;
    private ExampleTextView editFlowEditView;
    private String originalString = "";
    private ExampleTextView confirmParent = null;
    private final String[] LIST_ITEMS = Compatibility.toLowerCaseIfFT(
        new String[]{
            "Editable text",
            "Non-editable text",
            "Edit flow"
        });

    /**
     * Start the app, create the initial list and display it
     */
    public void startApp() {
        list = new List(Strings.getTitle(Strings.TEXT),
            List.IMPLICIT, LIST_ITEMS, null);
        list.addCommand(Commands.EXIT);
        list.addCommand(Commands.INFORMATION);
        list.setSelectCommand(Commands.SELECT);
        list.setCommandListener(this);

        editableView = new ExampleTextView(true, this, this);
        nonEditableView = new ExampleTextView(false, this, this);
        editFlowView = new ExampleTextView(
            Compatibility.toLowerCaseIfFT("Edit flow"), false, this, this);
        editFlowView.addCommand(Commands.EDIT);
        editFlowEditView = new ExampleTextView(
            Compatibility.toLowerCaseIfFT("Edit flow"), true, this, this);

        Display.getDisplay(this).setCurrent(list);
        try {
            Class.forName("com.nokia.mid.ui.VirtualKeyboard");
            Class.forName("com.nokia.mid.ui.KeyboardVisibilityListener");

            VirtualKeyboardUtils.setVisibilityListener(
                new VirtualKeyboardUtils.KeyboardVisibilityListener() {

                    public void showNotify(int keyboardCategory) {
                    }

                    public void hideNotify(int keyboardCategory) {
                        if (Display.getDisplay(Text.this).getCurrent()
                            == editFlowEditView) {
                            editFlowView.setString(editFlowEditView.getString());
                            setCurrent(editFlowView);
                        }
                    }
                });
        }
        catch (ClassNotFoundException e) {
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.EDIT) {
            editFlowEditView.setString(editFlowView.getString());
            setCurrent(editFlowEditView);
        }
        else if (c == Commands.SELECT) {
            // Instantiate and display the example view as either
            // editable or non-editable
            switch (list.getSelectedIndex()) {
                default:
                case EDITABLE_TEXT:
                    originalString = editableView.getString();
                    setCurrent(editableView);
                    break;
                case NON_EDITABLE_TEXT:
                    setCurrent(nonEditableView);
                    break;
                case EDIT_FLOW:
                    originalString = editFlowView.getString();
                    setCurrent(editFlowView);
                    break;
            }
        }
        else if (c == Commands.DONE) {
            if (d == editFlowEditView) {
                editFlowView.setString(editFlowEditView.getString());
            }
            setCurrent(list);
        }
        else if (c == Commands.BACK) {
            if (d == nonEditableView) {
                setCurrent(list);
            }
            else if (d == editableView) {
                if (editableView.getString().equals(originalString)) {
                    setCurrent(list);
                }
                else {
                    showConfirm(editableView);
                }
            }
            else {
                if (d == editFlowEditView) {
                    editFlowView.setString(editFlowEditView.getString());
                }
                if (editFlowView.getString().equals(originalString)) {
                    setCurrent(list);
                }
                else {
                    showConfirm(editFlowView);
                }
            }
        }
        else if (c == Commands.INFORMATION) {
            setCurrent(new InformationView(Strings.TEXT, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            setCurrent(list);
        }
        else if (c == Commands.ALERT_SAVE_YES) {
            setCurrent(list);
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            confirmParent.setString(originalString);
            confirmParent = null;
            setCurrent(list);
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            setCurrent(confirmParent);
            confirmParent = null;
        }
        else if (c == Commands.EXIT) {
            notifyDestroyed();
        }
    }

    /**
     * Shows a confirmation dialog when navigated back
     */
    private void showConfirm(ExampleTextView parent) {
        confirmParent = parent;
        setCurrent(new SaveChangesAlert(this));
    }

    public void setCurrent(Displayable displayable) {
        Display.getDisplay(this).setCurrent(displayable);
    }
}
