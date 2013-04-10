/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.form;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import javax.microedition.lcdui.*;

/**
 * Demonstrates the various ways a StringItem can appear
 */
public class StringItemView
    extends FormExampleView {

    public StringItemView(CommandListener commandListener, String title) {
        super(title, commandListener);
        removeCommand(Commands.OK);

        // When a command is attached to BUTTON / HYPERLINK 
        // StringItem, the appearance of the StringItem becomes
        // a button or a hyperlink
        Command selectCommand = new Command("Select", Command.SCREEN, 1);

        // Basic StringItem, looks and acts like static text
        StringItem stringItem = new StringItem(Compatibility.toLowerCaseIfFT(
            "Text with title"),
            Compatibility.toLowerCaseIfFT("Lorem")
            + " ipsum dolor sit amet, consectetur adlipisicing elit, "
            + "sed to eiusmod tempor");
        this.append(stringItem);

        // A StringItem that acts as a button
        stringItem = new StringItem(Compatibility.toLowerCaseIfFT(
            "Labeled button"), "ACTION",
            StringItem.BUTTON);
        stringItem.addCommand(selectCommand);
        this.append(stringItem);

        // A button StringItem without label
        stringItem = new StringItem("", "ACTION", StringItem.BUTTON);
        stringItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE
            | Item.LAYOUT_NEWLINE_AFTER);
        stringItem.addCommand(selectCommand);
        this.append(stringItem);

        // A hyperlink StringItem without a label
        stringItem = new StringItem("", "standard hyperlink",
            StringItem.HYPERLINK);
        stringItem.setLayout(Item.LAYOUT_EXPAND);
        stringItem.addCommand(selectCommand);
        this.append(stringItem);

        // A StringItem that acts as a hyperlink
        stringItem = new StringItem(Compatibility.toLowerCaseIfFT(
            "Labeled hyperlink"), "hyperlink",
            StringItem.HYPERLINK);
        stringItem.addCommand(selectCommand);
        this.append(stringItem);
    }

    protected void storeCurrentValues() {
        // No need to implement, no changeable values
    }

    protected void cancelChanges() {
        // No need to implement, no changeable values
    }

    protected boolean confirmChanges() {
        // No need to confirm changes.
        return false;
    }
}