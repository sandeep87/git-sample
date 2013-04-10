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
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.DelayedTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CommandListener;

public class ButtonsZoomView
    extends ZoomView {

    private static int HIDE_ZOOM_SLIDER_DELAY = 2000;
    private ZoomButtons zbs;
    private DelayedTask hideZBsTask = null;
    private float nontouchScale = 1;

    public ButtonsZoomView(CommandListener commandListener) {
        super();

        // Add buttons for touch & type devices
        if (hasPointerEvents()) {
            // Vertically center the buttons
            int buttonsY = (int) ((getHeight() - ZoomButtons.HEIGHT) / 2);

            zbs = new ZoomButtons(-3, 3, 0);
            zbs.setParent(this);
            zbs.setPosition(10, buttonsY);
            zbs.setVisible(false);
            zbs.setChangeListener(new ZoomButtons.ZoomListener() {

                public void valueChanged(int value) {
                    float scale = 1.0f + (value < 0 ? 0.25f : (1.0f / 3.0f))
                        * value;
                    scaledImage.image = scaleImage(scale, false);
                    repaint();
                }
            });
        }

        this.setTitle(Compatibility.toLowerCaseIfFT("Button zoom"));
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(commandListener);
        // Orientation.enableOrientations();
    }

    public boolean isNontouch() {
        return !hasPointerEvents();
    }

    protected void showNotify() {
        super.showNotify();
        if (hasPointerEvents()) {
            showZoomButtons();
        }
    }

    protected void sizeChanged(int width, int height) {
        // zs.setSize(zs.getWidth(), (int) (0.9 * height));
    }

    protected void keyPressed(int keyCode) {
        int gameAction = getGameAction(keyCode);
        switch (gameAction) {
            case Canvas.UP:
                if (nontouchScale + 0.1f <= 2f) {
                    nontouchScale += 0.1f;
                }
                break;
            case Canvas.DOWN:
                if (nontouchScale + 0.1f >= 0.2f) {
                    nontouchScale -= 0.1f;
                }
                break;
        }
        scaledImage.image = scaleImage(nontouchScale, false);
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        zbs.handlePointerDown(x, y);
        showZoomButtons();
        repaint();
    }

    protected void pointerReleased(int x, int y) {
        zbs.handlePointerUp(x, y);
        showZoomButtons();
        repaint();
    }

    protected void pointerDragged(int x, int y) {
        zbs.handlePointerDown(x, y);
        showZoomButtons();
        repaint();
    }

    private synchronized void showZoomButtons() {
        zbs.setVisible(true);
        if (hideZBsTask == null) {
            hideZBsTask = new DelayedTask(HIDE_ZOOM_SLIDER_DELAY) {

                public void run() {
                    hideZoomSlider();
                }
            };
            hideZBsTask.start();
        }
        else {
            hideZBsTask.resetDelay();
        }
    }

    private synchronized void hideZoomSlider() {
        zbs.setVisible(false);
        hideZBsTask = null;
        repaint();
    }
}
