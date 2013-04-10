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
import javax.microedition.lcdui.*;

public class NoContentView
    extends Form {

    private final StringItem noContentStringItem;

    public NoContentView(CommandListener parentCommandListener) {
        super(Compatibility.toLowerCaseIfFT("Indicate"));

        // Create and display the "no content" string
        noContentStringItem = new StringItem("", "no content");
        this.append(noContentStringItem);

        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(parentCommandListener);
    }

    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
        noContentStringItem.setPreferredSize(w, noContentStringItem.
            getPreferredHeight());
    }
}
