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

public class NoOptionsView extends List {

    public NoOptionsView(CommandListener commandListener) {
        super(Compatibility.toLowerCaseIfFT("No options"), List.IMPLICIT);
        for (int i = 0; i < 10; i++) {
            this.append(Compatibility.toLowerCaseIfFT("list item 0") + 
                    String.valueOf(i), null);
        }

        this.addCommand(Commands.BACK);
        this.setSelectCommand(SELECT_COMMAND);
        this.setCommandListener(commandListener);
    }
}
