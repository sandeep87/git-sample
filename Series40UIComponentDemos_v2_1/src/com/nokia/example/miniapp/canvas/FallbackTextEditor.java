/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */ 

package com.nokia.example.miniapp.canvas;

import javax.microedition.lcdui.*;


public class FallbackTextEditor
    extends TextBox 
    implements CommandListener {

    private static final int MAX_SIZE = 1024;
    private CommandListener parent;
    
    public FallbackTextEditor(CommandListener parent) {
        super("Fallback text editor", "This is a fallback text editor "
                + "implemented as a TextBox, shown on devices that do "
                + "not have the TextEditor class.", MAX_SIZE, TextField.ANY);
        this.parent = parent;
    }
    
    public void commandAction(Command c, Displayable d) {
        parent.commandAction(c, d);
    }
}
