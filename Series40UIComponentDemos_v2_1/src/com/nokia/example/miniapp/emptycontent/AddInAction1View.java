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
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.ImageLoader;
import javax.microedition.lcdui.*;

public class AddInAction1View
    extends Form
    implements CommandListener {

    private final String CONTENT_TEXT =
        "Lorem ipsum dolor sit amet, "
        + "consectetuer adipiscing elit, sed diam nonummy nibh euismod "
        + "tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut "
        + "wisi enim ad minim veniam, quis nostrud exerci tation "
        + "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo "
        + "consequat.";
    private final String HELP_TEXT = "no content\n\npress "
        + (Compatibility.isFullTouch() ? "+" : "Add") + " to add content";
    private Command addCommand;
    private CommandListener parentCommandListener;
    private StringItem contentStringItem;
    private final StringItem helpStringItem;
    private boolean contentAdded = false;
    private Image unselectedIconImage = null;
    private Image selectedIconImage = null;

    public AddInAction1View(String title, CommandListener parentCommandListener) {
        super(title);

        // Load the images for action button #1
        unselectedIconImage = ImageLoader.load(ImageLoader.ADD);
        selectedIconImage = ImageLoader.load(ImageLoader.ADD);

        // Create a new IconCommand that gets placed to action #1
        addCommand = Compatibility.getCommand(unselectedIconImage,
            selectedIconImage,
            Compatibility.toLowerCaseIfFT("Add"),
            Command.SCREEN,
            1);

        this.parentCommandListener = parentCommandListener;
        this.addCommand(addCommand);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(this);

        helpStringItem = new StringItem("",
            HELP_TEXT,
            StringItem.PLAIN);
        this.append(helpStringItem);
    }

    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
        helpStringItem.setPreferredSize(w, helpStringItem.getPreferredHeight());
    }

    public void commandAction(Command c, Displayable d) {
        if (c == addCommand) {
            // If content has not yet been added...
            if (!contentAdded) {
                // delete all existing Form elements and add the StringItem
                this.deleteAll();
                contentAdded = true;
            }
            contentStringItem = new StringItem("",
                CONTENT_TEXT,
                StringItem.PLAIN);
            this.append(contentStringItem);
        }
        else {
            parentCommandListener.commandAction(c, d);
        }
    }
}
