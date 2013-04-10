/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.menus;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import javax.microedition.lcdui.*;

public class OptionsView extends List {

    private final Command OPTIONS_ITEM_1 = new Command(
                Compatibility.toLowerCaseIfFT("Options item 01"),
                Command.SCREEN, 1);
    private final Command OPTIONS_ITEM_2 = new Command(
                Compatibility.toLowerCaseIfFT("Options item 02"),
                Command.SCREEN, 2);
    private final Command OPTIONS_ITEM_3 = new Command(
                Compatibility.toLowerCaseIfFT("Options item 03"),
                Command.SCREEN, 3);
    private final Command CONTEXT_ITEM_1 = new Command(
                Compatibility.toLowerCaseIfFT("Context item 01"),
                Command.ITEM, 1);
    private final Command CONTEXT_ITEM_2 = new Command(
                Compatibility.toLowerCaseIfFT("Context item 02"),
                Command.ITEM, 2);
    private final Command CONTEXT_ITEM_3 = new Command(
                Compatibility.toLowerCaseIfFT("Context item 03"),
                Command.ITEM, 3);

    public OptionsView(CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("Options"), List.IMPLICIT);
        for (int i = 0; i < 10; i++) {
            this.append(Compatibility.toLowerCaseIfFT("list item 0") +
                    String.valueOf(i), null);
        }

        // Add commands to the List; the commands get added to
        // Action #2 menu
        this.addCommand(OPTIONS_ITEM_1);
        this.addCommand(OPTIONS_ITEM_2);
        this.addCommand(OPTIONS_ITEM_3);
        // Add ITEM commands to the List; the commands appear
        // after a long press on a list item
        this.addCommand(CONTEXT_ITEM_1);
        this.addCommand(CONTEXT_ITEM_2);
        this.addCommand(CONTEXT_ITEM_3);
        this.addCommand(Commands.BACK);
        this.setSelectCommand(SELECT_COMMAND);
        this.setCommandListener(commandListener);
    }
}
