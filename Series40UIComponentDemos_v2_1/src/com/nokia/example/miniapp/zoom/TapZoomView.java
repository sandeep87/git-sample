/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import com.nokia.uihelpers.gesture.SafeGestureInteractiveZone;
import com.nokia.uihelpers.gesture.SafeGestureListener;
import com.nokia.uihelpers.gesture.SafeGestureRegistrationManager;
import com.nokia.uihelpers.gesture.SafeGestureEvent;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.Commands;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.CommandListener;

/**
 * Demonstrates zooming via double-tapping. Note that all of the drawing happens
 * in ZoomView, this class just extends ZoomView to provide the interaction
 * layer on top
 */
public class TapZoomView
    extends ZoomView
    implements SafeGestureListener {

    private final short SMALL = 0;
    private final short LARGE = 1;
    private final float SMALL_SCALE = 1f;
    private final float LARGE_SCALE = 2f;
    private final int DOUBLE_TAP_TIME = 500; // in ms
    private Timer timer;
    private short size = SMALL;
    private boolean tappedOnce = false;

    public TapZoomView(CommandListener commandListener) {
        // Register a gesture listener for single taps
        // Note that there is no out-of-the-box support for double tapping
        if (Compatibility.hasGestureSupport()) {
            SafeGestureRegistrationManager.setListener(this, this);
            
            SafeGestureInteractiveZone gestureZone = new SafeGestureInteractiveZone();
            gestureZone.setGesture(SafeGestureInteractiveZone.GESTURE_TAP);
            gestureZone.setRectangle(0, 0, this.getWidth(), this.getHeight());

            SafeGestureRegistrationManager.register(this, gestureZone);
        }

        this.setTitle("tap zoom");

        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(commandListener);
    }

    public void gestureAction(Object container,
        SafeGestureInteractiveZone gestureInteractiveZone,
        SafeGestureEvent gestureEvent) {

        if (gestureEvent.getType() == SafeGestureInteractiveZone.GESTURE_TAP) {
            // If the event was a tap, cancel the timer...
            if (timer != null) {
                timer.cancel();
            }
            // ...if there was a tap before, it means this is the second tap
            // of the double tap, thus now the zooming should occur
            if (tappedOnce) {
                if (size == SMALL) {
                    scaledImage.image = scaleImage(SMALL_SCALE, false);
                    size = LARGE;
                }
                else {
                    scaledImage.image = scaleImage(LARGE_SCALE, false);
                    size = SMALL;
                }
            }
            // otherwise just register this as the first tap
            else {
                tappedOnce = true;
            }

            // Create a new timer that resets the first tap flag after the
            // maximum time available for the second tap to occur
            timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    tappedOnce = false;
                }
            }, DOUBLE_TAP_TIME);

            repaint();
        }
    }
}
