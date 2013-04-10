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
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;

public class TickerListView
    extends List {

    private final int NUM_OF_ITEMS = 10;

    /**
     * Constructs the list and adds a Ticker to the view
     *
     * @param commandListener
     */
    public TickerListView(String title, CommandListener commandListener) {
        super(title, List.IMPLICIT);
        for (int i = 0; i < NUM_OF_ITEMS; i++) {
            this.append("list item " + String.valueOf(i), null);
        }
        Ticker ticker = new Ticker("ticker text just ticking by...");
        this.setTicker(ticker);

        this.addCommand(Commands.INFORMATION);
        this.setSelectCommand(Commands.LIST_SELECT);
        this.setCommandListener(commandListener);
    }
}
