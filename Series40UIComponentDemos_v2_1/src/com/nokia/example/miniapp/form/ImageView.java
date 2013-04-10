/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.form;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.ImageLoader;
import javax.microedition.lcdui.*;

/**
 * Demonstrates the usage of the ImageItem class
 */
public class ImageView
    extends FormExampleView {

    public ImageView(CommandListener commandListener, String title) {
        super(title, commandListener);
        removeCommand(Commands.OK);

        // Load the image
        Image image = ImageLoader.load(ImageLoader.PICTURE);

        // If the image was loaded successfully, create and show the ImageItem
        if (image != null) {
            ImageItem imageItem = new ImageItem(Compatibility.toLowerCaseIfFT(
                "Image"), image,
                Item.LAYOUT_DEFAULT,
                "Test image");
            // Center the image
            imageItem.setLayout(Item.LAYOUT_EXPAND | Item.LAYOUT_CENTER);
            this.append(imageItem);
        }
    }

    protected void storeCurrentValues() {
        // No need to implement, no changeable values
    }

    protected void cancelChanges() {
        // No need to implement, no changeable values
    }

    protected boolean confirmChanges() {
        // No need to confirm changes.
        return false;
    }
}
