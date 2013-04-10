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
import com.nokia.mid.ui.CanvasGraphicsItem;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ZoomSlider
    extends CanvasGraphicsItem {

    private static final int SLIDER_WIDTH = 30;
    private static final int HANDLE_SIZE = 12;
    private static final int BACKGROUND_WIDTH = 4;
    private static final int CHANGE_DELAY = 300;
    private static Image SLIDER_HANDLE = ImageLoader.load(
        ImageLoader.SLIDER_HANDLE);
    private int minValue;
    private int maxValue;
    private int minY;
    private int maxY;
    private int value;
    private ZoomListener listener;
    private Timer timer;

    public interface ZoomListener {

        public void valueChanged(int value);
    }

    public ZoomSlider(int h, int minValue, int maxValue, int startValue) {
        super(SLIDER_WIDTH, h);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = startValue;

        // Calculate the actual limits so that the handle fits inside those
        maxY = h - HANDLE_SIZE / 2;
        minY = 0 + HANDLE_SIZE / 2;
    }

    public void setChangeListener(ZoomListener listener) {
        this.listener = listener;
    }

    public void handlePointerEvent(int x, int y) {
        boolean withinBounds = (x >= this.getPositionX()
            && x < (this.getPositionX() + this.getWidth())
            && y >= this.getPositionY()
            && y < (this.getPositionY() + this.getHeight()));

        if (!(isVisible() && withinBounds)) {
            return;
        }

        // Normalize coordinates to take into account the slider position
        x -= getPositionX();
        y -= getPositionY();

        // Calculate actual value based on event y value
        int newValue = maxValue - (int) (((float) y / maxY) * (maxValue
            - minValue));
        setValue(newValue);
        repaint();
    }

    public void setValue(int newValue) {
        if (newValue < minValue) {
            newValue = minValue;
        }
        else if (newValue > maxValue) {
            newValue = maxValue;
        }
        this.value = newValue;

        if (this.listener != null) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            final int timerValue = newValue;
            timer.schedule(new TimerTask() {

                public void run() {
                    listener.valueChanged(timerValue);
                }
            }, CHANGE_DELAY);
        }
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        // Draw background
        g.setColor(0x666666);
        g.fillRect((int) w / 2 - BACKGROUND_WIDTH / 2, 0, BACKGROUND_WIDTH, h);

        // Calculate handle position based on the value: 100% = top, 0% = bottom
        int currentY = minY + (int) ((float) (maxValue - value) / (maxValue
            - minValue) * (maxY - minY));
        g.setColor(0x00ff00);
        g.drawImage(SLIDER_HANDLE, (int) w / 2, currentY - HANDLE_SIZE / 2,
            Graphics.TOP | Graphics.HCENTER);
    }
}