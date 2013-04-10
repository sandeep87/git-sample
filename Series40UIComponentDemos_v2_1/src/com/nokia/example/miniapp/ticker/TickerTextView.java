/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.ticker;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import javax.microedition.lcdui.*;

public class TickerTextView
    extends TextBox {

    private static final int MAX_SIZE = 512;
    private static final String TEXT = "Lorem ipsum dolor sit amet, "
        + "consectetur adipisicing elit,"
        + "sed do eiusmod tempor incididunt ut labore et";

    /**
     * Constructs the text box and adds a Ticker to the view
     *
     * @param commandListener
     */
    public TickerTextView(String title, CommandListener commandListener) {
        super(title, TEXT, MAX_SIZE, TextField.UNEDITABLE);
        Ticker ticker = new Ticker("ticker text just ticking by...");
        this.setTicker(ticker);

        this.addCommand(Commands.INFORMATION);
        if (Compatibility.supportsIconCommands()) {
            this.addCommand(Commands.EMPTY_ICON);
        }
        this.setCommandListener(commandListener);
    }
}
