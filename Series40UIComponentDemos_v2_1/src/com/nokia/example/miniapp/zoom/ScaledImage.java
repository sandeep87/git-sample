/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import javax.microedition.lcdui.Image;

/**
 * A helper class to contain the image to scale and it's parameters
 */
public class ScaledImage {

    public Image image;
    public float scaleIndex;
    public int srcWidth;
    public int srcHeight;
    public int imageWidth;
    public int imageHeight;
    public int[] rgbImageData;

    public ScaledImage() {
    }

    public ScaledImage(Image image,
        float scaleIndex,
        int srcWidth,
        int srcHeight,
        int imageWidth,
        int imageHeight,
        int[] rgbImageData) {
        this.image = image;
        this.scaleIndex = scaleIndex;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.rgbImageData = rgbImageData;
    }
}
