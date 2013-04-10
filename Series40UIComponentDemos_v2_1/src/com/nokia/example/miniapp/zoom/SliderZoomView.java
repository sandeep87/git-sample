/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.DelayedTask;
import javax.microedition.lcdui.CommandListener;

public class SliderZoomView
    extends ZoomView {

    private static int HIDE_ZOOM_SLIDER_DELAY = 2000;
    private ZoomSlider zs;
    private DelayedTask hideZSTask = null;

    public SliderZoomView(CommandListener commandListener) {
        super();

        // Fill 90% of the available height and vertically center the slider
        int sliderHeight = (int) (getHeight() * 0.9);
        int sliderY = (int) ((getHeight() - sliderHeight) / 2);

        zs = new ZoomSlider(sliderHeight, 0, 100, 50);
        zs.setParent(this);
        zs.setPosition(10, sliderY);
        zs.setVisible(false);
        zs.setChangeListener(new ZoomSlider.ZoomListener() {

            public void valueChanged(int value) {
                float scale = 1.0f + 0.01f * (value - 50);
                scaledImage.image = scaleImage(scale, false);
                repaint();
            }
        });

        this.setTitle("slider zoom");
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(commandListener);
        // Orientation.enableOrientations();
    }

    protected void showNotify() {
        super.showNotify();
        showZoomSlider();
    }

    protected void sizeChanged(int width, int height) {
        // zs.setSize(zs.getWidth(), (int) (0.9 * height));
    }

    protected void pointerPressed(int x, int y) {
        if (zs.isVisible()) {
            zs.handlePointerEvent(x, y);
        }
    }

    protected void pointerReleased(int x, int y) {
        if (zs.isVisible()) {
            zs.handlePointerEvent(x, y);
        }
        else {
            showZoomSlider();
            repaint();
        }
    }

    protected void pointerDragged(int x, int y) {
        if (zs.isVisible()) {
            zs.handlePointerEvent(x, y);
            showZoomSlider();
            repaint();
        }
    }

    private synchronized void showZoomSlider() {
        zs.setVisible(true);
        if (hideZSTask == null) {
            hideZSTask = new DelayedTask(HIDE_ZOOM_SLIDER_DELAY) {

                public void run() {
                    hideZoomSlider();
                }
            };
            hideZSTask.start();
        }
        else {
            hideZSTask.resetDelay();
        }
    }

    private synchronized void hideZoomSlider() {
        zs.setVisible(false);
        hideZSTask = null;
        repaint();
    }
}
