/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import com.nokia.example.miniapp.utils.ImageLoader;
import javax.microedition.lcdui.Image;

/**
 * A helper class for initializing and scaling images
 */
public class ZoomHelper {

    /**
     * Initializes a new ScaledImage
     * @return ScaledImage
     */
    protected static ScaledImage initImage() {
        ScaledImage scaledImage = new ScaledImage();

        scaledImage.image = ImageLoader.load(ImageLoader.PICTURE);

        if (scaledImage.image != null) {
            scaledImage.srcWidth =
                scaledImage.imageWidth =
                scaledImage.image.getWidth();
            scaledImage.srcHeight =
                scaledImage.imageHeight =
                scaledImage.image.getHeight();
            scaledImage.scaleIndex = 1;
        }

        return scaledImage;
    }

    /**
     * Scales the image either to a defined scale or by an increment. Increment
     * scaling used in pinch-to-zoom, defined scale in tap and slider zooms.
     * @param scaledImage the image to scale
     * @param scale the scale factor
     * @param increment true to add the scale factor to the scaleIndex, false
     *                  to scale directly to the scale factor
     * @return ScaledImage when scaled
     */
    protected static ScaledImage scaleImage(ScaledImage scaledImage,
        float scale,
        boolean increment) {
        try {
            if (increment) {
                scaledImage.scaleIndex += scale;
                if (scaledImage.scaleIndex < 0.1f) {
                    scaledImage.scaleIndex = 0.1f;
                }
            }
            else {
                scaledImage.scaleIndex = scale;
            }

            if (scaledImage.rgbImageData == null) {
                scaledImage.rgbImageData = new int[scaledImage.srcWidth
                    * scaledImage.srcHeight];
                scaledImage.image.getRGB(scaledImage.rgbImageData, 0,
                    scaledImage.imageWidth, 0, 0,
                    scaledImage.imageWidth,
                    scaledImage.imageHeight);
            }

            int newImageWidth = (int) (scaledImage.srcWidth
                * scaledImage.scaleIndex);
            int newImageHeight = (int) (scaledImage.srcHeight
                * scaledImage.scaleIndex);

            int rgbImageScaledData[] = new int[newImageWidth * newImageHeight];

            // calculations and bit shift operations to optimize the for loop
            int tempScaleRatioWidth = ((scaledImage.srcWidth << 16)
                / newImageWidth);
            int tempScaleRatioHeight = ((scaledImage.srcHeight << 16)
                / newImageHeight);

            int i = 0;

            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                    rgbImageScaledData[i++] =
                        scaledImage.rgbImageData[(scaledImage.srcWidth
                        * ((y * tempScaleRatioHeight) >> 16))
                        + ((x * tempScaleRatioWidth) >> 16)];
                }
            }

            scaledImage.image = Image.createRGBImage(rgbImageScaledData,
                newImageWidth,
                newImageHeight,
                true);
            scaledImage.imageWidth = newImageWidth;
            scaledImage.imageHeight = newImageHeight;
        }
        catch (OutOfMemoryError e) {
            scaledImage.scaleIndex -= scale;
            e.printStackTrace();
            System.out.println("Out of memory " + e.getMessage());
        }

        return scaledImage;
    }
}