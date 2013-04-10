/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import com.nokia.mid.ui.CanvasGraphicsItem;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class ZoomButtons
    extends CanvasGraphicsItem {

    private static final int WIDTH = 40;
    public static final int HEIGHT = 80;
    private static final int CHANGE_DELAY = 300;
    private static final Font font = Font.getFont(Font.FACE_SYSTEM,
        Font.STYLE_PLAIN, Font.SIZE_LARGE);
    private int minValue;
    private int maxValue;
    private int value;
    private ZoomListener listener;
    private Timer timer;
    private boolean plusDown = false;
    private boolean minusDown = false;

    public interface ZoomListener {

        public void valueChanged(int value);
    }

    public ZoomButtons(int minValue, int maxValue, int startValue) {
        super(WIDTH, HEIGHT);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = startValue;
    }

    public void setChangeListener(ZoomListener listener) {
        this.listener = listener;
    }

    public void handlePointerDown(int x, int y) {
        if (!isVisible()) {
            return;
        }

        plusDown = hitsPlus(x, y);
        minusDown = hitsMinus(x, y);
        repaint();
    }

    public void handlePointerUp(int x, int y) {
        plusDown = false;
        minusDown = false;
        if (!isVisible()) {
            return;
        }
        repaint();

        if (hitsPlus(x, y)) {
            setValue(value + 1);
        }
        else if (hitsMinus(x, y)) {
            setValue(value - 1);
        }
    }

    private boolean hitsPlus(int x, int y) {
        return (x >= this.getPositionX()
            && x < (this.getPositionX() + this.getWidth())
            && y >= this.getPositionY()
            && y < (this.getPositionY() + this.getHeight() / 2));
    }

    private boolean hitsMinus(int x, int y) {
        return (x >= this.getPositionX()
            && x < (this.getPositionX() + this.getWidth())
            && y >= (this.getPositionY() + this.getHeight() / 2)
            && y < (this.getPositionY() + this.getHeight()));
    }

    public void setValue(int newValue) {
        if (newValue < minValue) {
            newValue = minValue;
        }
        else if (newValue > maxValue) {
            newValue = maxValue;
        }
        if (newValue == value) {
            return;
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

        g.setColor(plusDown ? 0x999999 : 0x666666);
        g.fillRect(0, 0, w, h / 2);

        g.setColor(minusDown ? 0x999999 : 0x666666);
        g.fillRect(0, h / 2, w, h / 2);

        g.setFont(font);
        final int f = font.getHeight() / 2;
        g.setColor(plusDown ? 0xffffff : 0xdddddd);
        g.drawString("+", w / 2, h / 4 + f, Graphics.BOTTOM | Graphics.HCENTER);

        g.setColor(minusDown ? 0xffffff : 0xdddddd);
        g.drawString("-", w / 2, 3 * h / 4 + f, Graphics.BOTTOM
            | Graphics.HCENTER);
    }
}