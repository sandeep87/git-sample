/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.canvas;

import com.nokia.uihelpers.Button;
import com.nokia.uihelpers.Button.Listener;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.mid.ui.LCDUIUtil;
import javax.microedition.lcdui.*;

/**
 * The ExampleCanvas changes it's appearance and elements based 
 * on which example is shown. The customization is made by calling
 * various methods (e.g. setFullScreen for fullscreen examples) in
 * CanvasExample.java.
 */
public class ExampleCanvas
    extends Canvas
    implements Listener {

    private final String INFORMATION_LABEL = "INFORMATION";
    private final String BACK_LABEL = "BACK";
    private final int BACK_BUTTON_WIDTH = 100;
    private final int MARGIN = 10;
    private int buttonHeight;
    private String content;
    private int textY;
    private boolean isNavigationElementsVisible = false;
    private CommandListener parent;
    private Button informationButton;
    private Button backButton;
    private Button[] buttons;

    /**
     * Constructor
     * @param title
     * @param content 
     */
    public ExampleCanvas(String title, String content) {
        this.content = content;
        this.setTitle(title);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        textY = this.getHeight() / 4;
        buttonHeight = Button.getDefaultHeight();
    }

    /**
     * Constructor
     * @param title
     * @param content
     * @param parent Command handler to which button commends are delegated
     */
    public ExampleCanvas(String title, String content, CommandListener parent) {
        this(title, content);
        this.parent = parent;
    }

    public void setFullScreen(boolean fullScreen) {
        this.setFullScreenMode(fullScreen);
        toggleNavigationElements(fullScreen);
    }

    /**
     * Creates the buttons
     * @param visible 
     */
    private void toggleNavigationElements(boolean visible) {
        isNavigationElementsVisible = visible;

        if (visible) {
            this.removeCommand(Commands.BACK);
            this.removeCommand(Commands.INFORMATION);
        }
        else {
            this.addCommand(Commands.BACK);
            this.addCommand(Commands.INFORMATION);
        }

        if (informationButton == null) {
            int yPosition = this.getHeight() / 2 - buttonHeight / 2;
            informationButton = new Button(INFORMATION_LABEL, MARGIN,
                yPosition,
                this.getWidth() - 2 * MARGIN, buttonHeight, Button.REGULAR);
            informationButton.addListener(this);
        }
        if (backButton == null) {
            int yPosition = this.getHeight() - buttonHeight - MARGIN;
            int xPosition = this.getWidth() - BACK_BUTTON_WIDTH - MARGIN;
            backButton = new Button(BACK_LABEL, xPosition, yPosition,
                BACK_BUTTON_WIDTH, buttonHeight, Button.REGULAR);
            backButton.addListener(this);
        }
        if (buttons == null) {
            buttons = new Button[2];
            buttons[0] = informationButton;
            buttons[1] = backButton;
            if (!hasPointerEvents()) {
                buttons[0].setFocused(true);
            }
        }
    }

    private void setButtonLocations() {
        if (backButton != null) {
            backButton.setX(this.getWidth() - BACK_BUTTON_WIDTH - MARGIN);
            backButton.setY(this.getHeight() - buttonHeight - MARGIN);
        }
        if (informationButton != null) {
            informationButton.setWidth(this.getWidth() - 2 * MARGIN);
            informationButton.setX(MARGIN);
            informationButton.setY(this.getHeight() / 2 - buttonHeight / 2);
        }

        textY = this.getHeight() / 4;
    }

    /**
     * Sets the foreground color for the buttons
     * @param color 
     */
    public void setForegroundColor(int color) {
        if (informationButton != null) {
            informationButton.setForegroundColor(color);
        }
        if (backButton != null) {
            backButton.setForegroundColor(color);
        }
    }

    /**
     * Sets the background color for the buttons
     * @param color 
     */
    public void setBackgroundColor(int color) {
        if (informationButton != null) {
            informationButton.setBackgroundColor(color);
        }
        if (backButton != null) {
            backButton.setBackgroundColor(color);
        }
    }

    /**
     * Shows or hides the status bar
     * @param visible 
     */
    public void setStatusBarVisible(boolean visible) {
        Boolean v = (visible ? Boolean.TRUE : Boolean.FALSE);
        LCDUIUtil.setObjectTrait(this, "nokia.ui.canvas.status_zone", v);
    }

    protected void sizeChanged(int width, int height) {
        setButtonLocations();
    }

    public void paint(Graphics g) {
        // Clear the screen by drawing a white rectangle
        int w = getWidth();
        int h = getHeight();
        g.setColor(255, 255, 255); // white
        g.fillRect(0, 0, w, h);

        // Draw the text
        g.setColor(0, 51, 240); // blue
        g.drawString(content, w / 2, textY,
            Graphics.TOP | Graphics.HCENTER);

        // Draw the buttons, if applicable
        if (isNavigationElementsVisible) {
            renderButtons(g);
        }
    }

    /**
     * Renders the buttons
     * @param g 
     */
    private void renderButtons(Graphics g) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].render(g);
        }
    }

    /**
     * Catches pointerReleased events and sends them to buttons
     * which handle hit detection
     * @param x
     * @param y 
     */
    protected void pointerReleased(int x, int y) {
        if (buttons != null) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].touchUp(x, y);
            }
        }
    }
    
    protected void pointerPressed(int x, int y) {
        if (buttons != null) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].touchDown(x, y);
            }
        }
        repaint();
    }

    protected void keyPressed(int keyCode) {
        if (isNavigationElementsVisible) {
            switch (getGameAction(keyCode)) {
                case DOWN:
                case UP:
                case LEFT:
                case RIGHT:
                    boolean button0WasFocused = buttons[0].isFocused();
                    buttons[0].setFocused(!button0WasFocused);
                    buttons[1].setFocused(button0WasFocused);
                    repaint();
                    break;
                case FIRE:
                    for (int i = 0; i < buttons.length; i++) {
                        if (buttons[i].isFocused()) {
                            buttons[i].notifyClicked();
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Catches the clicked events for the Buttons. Delegates
     * the commands to the parent CommandListener.
     * @param button 
     */
    public void clicked(Button button) {
        if (button == informationButton) {
            parent.commandAction(Commands.INFORMATION, this);
        }
        else if (button == backButton) {
            parent.commandAction(Commands.BACK, this);
        }
    }
}