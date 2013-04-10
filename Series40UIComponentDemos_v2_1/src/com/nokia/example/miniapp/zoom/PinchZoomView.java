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
import javax.microedition.lcdui.CommandListener;

/**
 * Demonstrates zooming via pinching. Note that all of the drawing happens
 * in ZoomView, this class just extends ZoomView to provide the interaction
 * layer on top
 */
public class PinchZoomView
    extends ZoomView
    implements SafeGestureListener {

    private int mouseDownX;
    private int mouseDownY;

    public PinchZoomView(CommandListener commandListener) {
        super();

        // Register to listen to the pinch event
        if (Compatibility.hasGestureSupport()) {
            SafeGestureRegistrationManager.setListener(this, this);
        
            SafeGestureInteractiveZone gestureZone = new SafeGestureInteractiveZone();
            gestureZone.setGesture(SafeGestureInteractiveZone.GESTURE_PINCH);
            gestureZone.setRectangle(0, 0, this.getWidth(), this.getHeight());

            SafeGestureRegistrationManager.register(this, gestureZone);
        }

        this.setTitle("pinch zoom");

        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(commandListener);
    }

    protected void pointerPressed(int x, int y) {
        mouseDownX = x;
        mouseDownY = y;
    }

    protected void pointerReleased(int x, int y) {
        deltaX = 0;
        deltaY = 0;
    }

    protected void pointerDragged(int x, int y) {
        deltaX = x - mouseDownX;
        deltaY = y - mouseDownY;
        mouseDownX = x;
        mouseDownY = y;
        repaint();
    }

    /**
     * Handles the gesture events
     * @param container
     * @param gestureInteractiveZone
     * @param gestureEvent 
     */
    public void gestureAction(Object container,
        SafeGestureInteractiveZone gestureInteractiveZone,
        SafeGestureEvent gestureEvent) {
        if (gestureEvent.getType() == SafeGestureInteractiveZone.GESTURE_PINCH) {
            if (gestureEvent.getPinchDistanceChange() < 0) {
                // If the gesture was inwards, scale the image smaller
                scaledImage.image = scaleImage(-0.1f, true);
            }
            else if (gestureEvent.getPinchDistanceChange() > 0) {
                // If the gesture was outwards, scale the image larger
                scaledImage.image = scaleImage(0.1f, true);
            }
            repaint();
        }
    }
}
