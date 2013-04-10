/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.emptycontent;

import com.nokia.example.miniapp.utils.Commands;
import javax.microedition.lcdui.*;

public class AddButtonView
    extends Form
    implements ItemCommandListener, CommandListener {

    private final Command ADD_COMMAND = new Command("Select", Command.SCREEN, 1);
    private final String CONTENT_TEXT =
        "Lorem ipsum dolor sit amet, "
        + "consectetuer adipiscing elit, sed diam nonummy nibh euismod "
        + "tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut "
        + "wisi enim ad minim veniam, quis nostrud exerci tation "
        + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.";
    private StringItem contentStringItem;
    private CommandListener parentCommandListener;
    private boolean contentAdded = false;

    public AddButtonView(String title, CommandListener parentCommandListener) {
        super(title);

        // Create a button to allow the addition of content
        StringItem buttonStringItem = new StringItem("",
            "ADD CONTENT",
            StringItem.BUTTON);
        // StringItem.BUTTON's act as buttons if they have their 
        // default command set
        buttonStringItem.setDefaultCommand(ADD_COMMAND);
        buttonStringItem.setItemCommandListener(this);

        this.append(buttonStringItem);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(this);
        this.parentCommandListener = parentCommandListener;
    }

    private void addContent() {
        // Replace Form contents with the StringItem
        if (!contentAdded) {
            this.deleteAll();
            contentAdded = true;
        }
        contentStringItem = new StringItem("",
            CONTENT_TEXT,
            StringItem.PLAIN);
        this.append(contentStringItem);
    }

    public void commandAction(Command c, Item item) {
        if (c == ADD_COMMAND) {
            addContent();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == ADD_COMMAND) {
            addContent();
        }
        else {
            parentCommandListener.commandAction(c, d);
        }
    }
}
