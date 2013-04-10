/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import com.nokia.mid.ui.VirtualKeyboard;
import javax.microedition.lcdui.*;

/**
 * A generic zoomable view
 */
public class ZoomView
    extends Canvas {

    protected int posX;
    protected int posY;
    protected int deltaX;
    protected int deltaY;
    protected ScaledImage scaledImage;

    public ZoomView() {
        try {
            Class.forName("com.nokia.mid.ui.VirtualKeyboard");
            VirtualKeyboard.hideOpenKeypadCommand(true);
        }
        catch (ClassNotFoundException e) {
        }
        scaledImage = ZoomHelper.initImage();
        repaint();
    }

    /**
     * Scales the image
     * @param scale 
     * @param shouldIncrement
     * @return 
     */
    protected Image scaleImage(float scale, boolean shouldIncrement) {
        scaledImage = ZoomHelper.scaleImage(scaledImage,
            scale,
            shouldIncrement);
        return scaledImage.image;
    }

    /**
     * Clear the screen and draw the image
     * @param g 
     */
    protected void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();

        setImagePlacementPoint();

        g.setColor(0);
        g.fillRect(0, 0, w, h);

        if (scaledImage.image != null) {
            g.drawImage(scaledImage.image,
                posX,
                posY,
                Graphics.HCENTER | Graphics.VCENTER);
        }
    }

    /**
     * Adjusts the image placement based on the screen dimensions. Needs to 
     * be checked to accommodate orientation changes.
     */
    void setImagePlacementPoint() {
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();

        if (scaledImage.imageWidth > canvasWidth && deltaX != 0) {
            posX += deltaX;
            if (posX < (canvasWidth - scaledImage.imageWidth / 2)) {
                posX = canvasWidth - scaledImage.imageWidth / 2;
            }
            else if (posX > (scaledImage.imageWidth / 2)) {
                posX = (scaledImage.imageWidth / 2);
            }
        }
        else {
            posX = canvasWidth / 2;
        }

        if (scaledImage.imageHeight > canvasHeight && deltaY != 0) {
            posY += deltaY;
            if (posY < (canvasHeight - scaledImage.imageHeight / 2)) {
                posY = canvasHeight - scaledImage.imageHeight / 2;
            }
            else if (posY > (scaledImage.imageHeight / 2)) {
                posY = (scaledImage.imageHeight / 2);
            }
        }
        else {
            posY = canvasHeight / 2;
        }
    }
}
