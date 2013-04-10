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
 * Demonstrates the usage of the Spacer class
 */
public class SpacerView
    extends FormExampleView {

    private final int SPACER_HEIGHT = 100;

    public SpacerView(CommandListener commandListener, String title) {
        super(title, commandListener);
        removeCommand(Commands.OK);

        // When a command is attached to BUTTON / HYPERLINK 
        // StringItem, the appearance of the StringItem becomes
        // a button or a hyperlink
        Command selectCommand = new Command("Select", Command.SCREEN, 1);

        // Create a Button StringItem
        StringItem stringItem = new StringItem(Compatibility.toLowerCaseIfFT(
            "Spacer between buttons"),
            "ACTION", StringItem.BUTTON);
        stringItem.addCommand(selectCommand);
        this.append(stringItem);

        // Create a spacer with the width of the screen, height of 100 pixels
        Spacer spacer = new Spacer(this.getWidth(), SPACER_HEIGHT);
        this.append(spacer);

        // Create another Button StringItem
        stringItem = new StringItem("", "ACTION", StringItem.BUTTON);
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