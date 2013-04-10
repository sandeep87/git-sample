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
import com.nokia.example.miniapp.utils.ImageLoader;
import javax.microedition.lcdui.*;

public class TickerFormView
    extends Form {

    /**
     * Constructs the form and adds a Ticker to the view
     *
     * @param commandListener
     */
    public TickerFormView(String title, CommandListener commandListener) {
        super(title);
        Ticker ticker = new Ticker("ticker text just ticking by...");
        this.setTicker(ticker);

        // Load the image
        Image image = ImageLoader.load(ImageLoader.PICTURE);

        // If the image was loaded successfully, create and show the ImageItem
        if (image != null) {
            ImageItem imageItem = new ImageItem("Image", image,
                Item.LAYOUT_DEFAULT,
                "Test image");
            // Center the image
            imageItem.setLayout(Item.LAYOUT_EXPAND | Item.LAYOUT_CENTER);
            this.append(imageItem);
        }

        // A StringItem that acts as a button
        StringItem stringItem = new StringItem("labeled button", "ACTION",
            StringItem.BUTTON);
        Command selectCommand = new Command("Select", Command.SCREEN, 1);
        stringItem.addCommand(selectCommand);
        this.append(stringItem);

        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(commandListener);
    }
}
