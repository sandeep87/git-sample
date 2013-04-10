/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.zoom;

import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.CategoryBarUtils;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.ImageLoader;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.orientation.Orientation;
import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.ElementListener;
import com.nokia.mid.ui.IconCommand;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * An example demonstrating different ways of zooming content
 */
public class ZoomExample
    extends MIDlet
    implements CategoryBarUtils.ElementListener, CommandListener {

    private final int PINCH_VIEW = 0;
    private final int BUTTONS_VIEW = 0;
    private final int SLIDER_VIEW = 1;
    private final int TAP_VIEW = 2;
    private final String[] NONTOUCH_VIEWS = {
        "Buttons"
    };
    private final String[] TOUCHTYPE_VIEWS = {
        "Buttons",
        "Slider",
        "Double tap"
    };
    private List zoomList;
    private CategoryBar categoryBar;
    private BackStack backStack;
    private PinchZoomView pinchZoomView;
    private ButtonsZoomView buttonsZoomView;
    private SliderZoomView sliderZoomView;
    private TapZoomView tapZoomView;

    /**
     * Create a CategoryBar and initialize & display the first view
     */
    public void startApp() {
        backStack = new BackStack(this);
        Orientation.enableOrientations();
        try {
            Class.forName("com.nokia.mid.ui.CategoryBar");
            Class.forName("com.nokia.mid.ui.ElementListener");
            Class.forName("com.nokia.mid.ui.IconCommand");
            Image pinchImage = ImageLoader.load(ImageLoader.CATEGORY_PINCH);
            Image sliderImage = ImageLoader.load(ImageLoader.CATEGORY_SLIDER);
            Image tapImage = ImageLoader.load(ImageLoader.CATEGORY_TAP);

            IconCommand[] iconCommands = new IconCommand[]{
                new IconCommand("pinch", pinchImage, null, Command.SCREEN, 1),
                new IconCommand("slider", sliderImage, null, Command.SCREEN, 1),
                new IconCommand("zoom", tapImage, null, Command.SCREEN, 1)
            };

            categoryBar = new CategoryBar(iconCommands, true);
            categoryBar.setVisibility(true);
            CategoryBarUtils.setListener(categoryBar, this);
            Display.getDisplay(this).setCurrent(getPinchZoomView());
        }
        catch (ClassNotFoundException e) {
            buttonsZoomView = new ButtonsZoomView(this);
            zoomList = new List(Strings.getTitle(Strings.ZOOM),
                List.IMPLICIT,
                buttonsZoomView.isNontouch() ? NONTOUCH_VIEWS : TOUCHTYPE_VIEWS,
                null);
            zoomList.setCommandListener(this);
            zoomList.setSelectCommand(Commands.SELECT);
            zoomList.addCommand(Commands.EXIT);
            zoomList.addCommand(Commands.INFORMATION);
            Display.getDisplay(this).setCurrent(zoomList);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    /**
     * Handle the CategoryBar events, change views or go back
     * @param cb
     * @param selectedIndex 
     */
    public void notifyElementSelected(CategoryBar cb, int selectedIndex) {
        Display d = Display.getDisplay(this);

        switch (selectedIndex) {
            case PINCH_VIEW:
                d.setCurrent(getPinchZoomView());
                break;
            case SLIDER_VIEW:
                d.setCurrent(getSliderZoomView());
                break;
            case TAP_VIEW:
                d.setCurrent(getTapZoomView());
                break;
            case ElementListener.BACK:
                backStack.back();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.INFORMATION) {
            if (categoryBar != null) {
                categoryBar.setVisibility(false);
            }
            backStack.forward(new InformationView(Strings.ZOOM, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            if (categoryBar != null) {
                categoryBar.setVisibility(true);
            }
            backStack.back();
        }
        else if (c == Commands.BACK || c == Commands.EXIT) {
            backStack.back();
        }
        else if (c == Commands.SELECT) {
            int i = zoomList.getSelectedIndex();
            Displayable view;
            switch (i) {
                default:
                case BUTTONS_VIEW:
                    view = getButtonsZoomView();
                    break;
                case SLIDER_VIEW:
                    view = getSliderZoomView();
                    break;
                case TAP_VIEW:
                    view = getTapZoomView();
                    break;
            }
            view.addCommand(Commands.BACK);
            backStack.forward(view);
        }
    }

    private PinchZoomView getPinchZoomView() {
        if (pinchZoomView == null) {
            pinchZoomView = new PinchZoomView(this);
        }
        return pinchZoomView;
    }

    private ButtonsZoomView getButtonsZoomView() {
        return buttonsZoomView;
    }

    private SliderZoomView getSliderZoomView() {
        if (sliderZoomView == null) {
            sliderZoomView = new SliderZoomView(this);
        }
        return sliderZoomView;
    }

    private TapZoomView getTapZoomView() {
        if (tapZoomView == null) {
            tapZoomView = new TapZoomView(this);
        }
        return tapZoomView;
    }
}
