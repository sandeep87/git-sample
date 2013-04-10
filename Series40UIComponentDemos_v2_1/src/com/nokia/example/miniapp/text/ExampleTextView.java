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
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.IDisplayer;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import javax.microedition.lcdui.*;

public class ExampleTextView
    extends TextBox
    implements CommandListener {

    private static final int MAX_SIZE = 512;
    private static final String TEXT = "Lorem ipsum dolor sit amet, "
        + "consectetur adipisicing elit,"
        + "sed do eiusmod tempor incididunt ut labore et";
    private static final String EDITABLE_TITLE =
        Compatibility.toLowerCaseIfFT("Editable text");
    private static final String NONEDITABLE_TITLE =
        Compatibility.toLowerCaseIfFT("Noneditable text");
    private final Command CLEAR_COMMAND = new Command(
        Compatibility.toLowerCaseIfFT("Clear all"), Command.SCREEN, 1);
    private IDisplayer displayer;
    private CommandListener parentCommandListener;

    /**
     * Constructs the view
     * @param editable Whether the TextBox should be editable or not
     */
    public ExampleTextView(boolean editable, IDisplayer displayer,
        CommandListener parentCommandListener) {
        this(editable ? EDITABLE_TITLE : NONEDITABLE_TITLE, editable, displayer,
            parentCommandListener);
    }

    /**
     * Constructs the view
     * @param title title of the view
     * @param editable Whether the TextBox should be editable or not
     */
    public ExampleTextView(String title, boolean editable, IDisplayer displayer,
        CommandListener parentCommandListener) {
        super(title, TEXT, MAX_SIZE, TextField.UNEDITABLE);

        this.displayer = displayer;
        this.parentCommandListener = parentCommandListener;
        // If the TextBox is editable, make it accept all input and 
        // add a clear command to the options menu
        if (editable) {
            this.setConstraints(TextField.ANY);
            this.addCommand(CLEAR_COMMAND);
        }
        this.addCommand(Commands.DONE);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(this);
    }

    /**
     * Handle commands here and delegate some to the parent command listener
     * @param c
     * @param d 
     */
    public void commandAction(Command c, Displayable d) {
        if (c == CLEAR_COMMAND) {
            // Clear the TextBox contents
            this.setString(null);
        }
        else if (c == Commands.INFORMATION) {
            displayer.setCurrent(new InformationView(Strings.TEXT, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            displayer.setCurrent(this);
        }
        else {
            parentCommandListener.commandAction(c, d);
        }
    }
}
