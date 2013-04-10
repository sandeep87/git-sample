/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */ 

package com.nokia.example.miniapp.keypads;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.Compatibility;
import com.nokia.uihelpers.orientation.Orientation;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Keypads extends MIDlet implements CommandListener {
    
    private final int MAX_CHARS = 256;
    private final int[] TYPES = {
        TextField.ANY,
        TextField.DECIMAL,
        TextField.EMAILADDR,
        TextField.INITIAL_CAPS_SENTENCE,
        TextField.INITIAL_CAPS_WORD,
        TextField.NUMERIC,
        TextField.PASSWORD,
        TextField.PHONENUMBER,
        TextField.SENSITIVE,
        TextField.URL
    };
    private final String[] TITLES = Compatibility.toLowerCaseIfFT(new String[]{
        "Keypad normal",
        "Keypad decimal",
        "Keypad email",
        "Keypad initial caps sentence",
        "Keypad initial caps word",
        "Keypad numeric",
        "Keypad password",
        "Keypad phonenumber",
        "Keypad sensitive",
        "Keypad URL"
    });
    
    private BackStack backStack;
    private Form keypadsForm;
    
    public void startApp() {
        keypadsForm = new Form(Compatibility.toLowerCaseIfFT("Keypads"));
        if (Compatibility.isNonTouch() || Compatibility.isTouchAndType()) {
            keypadsForm.addCommand(Commands.OK);
        }
        keypadsForm.addCommand(Commands.INFORMATION);
        keypadsForm.addCommand(Commands.BACK);
        keypadsForm.setCommandListener(this);
        
        // Create TextFields with the different constraints and
        // insert them to the form
        for (int i = 0; i < TYPES.length; i++) {
            TextField textField = new TextField(TITLES[i], "", MAX_CHARS, TYPES[i]);
            keypadsForm.append(textField);
        }
        
        Orientation.enableOrientations();
        backStack = new BackStack(this);
        Display.getDisplay(this).setCurrent(keypadsForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.INFORMATION) {
            backStack.forward(new InformationView(Strings.KEYPADS, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            backStack.back();
        }
        else if (c == Commands.BACK) {
            // Show the save changes? -dialog if there are any changes
            boolean hasChanges = false;
            for (int i = 0; i < TYPES.length; i++) {
                if (((TextField)keypadsForm.get(i)).getString().length() > 0) {
                    hasChanges = true;
                    break;
                }
            }
            if (hasChanges) {
                backStack.forward(new SaveChangesAlert(this));
            }
            else {
                backStack.back();
            }
        }
        else if (c == Commands.OK) {
            backStack.back();
        }
        else if (c == Commands.ALERT_SAVE_YES) {
            backStack.back(2);
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            backStack.back(2);
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            backStack.back();
        }
    }
}