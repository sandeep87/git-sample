/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.dialogs;

import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.ImageLoader;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.Strings;
import com.nokia.uihelpers.orientation.Orientation;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class DialogExample
    extends MIDlet
    implements CommandListener {

    private final String[] DIALOG_ITEMS = Compatibility.toLowerCaseIfFT(
        new String[]{
            "Timed",
            "Text, 1 button",
            "Text, 1 button, image",
            "Text, btn, img, prog bar",
            "Text, btn, img, wait bar",
            "Text, 2 buttons",
            "Txt, 3 btns, img, wait bar",
            "Form, determinate progress",
            "Form, indeterminate progress",
            "Alert - error",
            "Alert - warning",
            "Alert - info",
            "Alert - confirmation",
            "Alert - alarm"
        });
    private final String[] TITLES = Compatibility.toLowerCaseIfFT(new String[]{
            "Timed dialog",
            "Information 1",
            "Information 2",
            "Download 1",
            "Download 2",
            "Confirmation",
            "Installation",
            "Alert",
            "Alert",
            "Alert - error",
            "Alert - warning",
            "Alert - info",
            "Alert - confirmation",
            "Alert - alarm"
        });
    private final String[] MESSAGES = {
        "Alert times out after 3 seconds",
        Commands.ALERT_OK.getLabel() + " closes the note and continues",
        Commands.ALERT_OK.getLabel() + " closes the note and continues",
        "Determinate progress indicator",
        "In-determinate progress indicator",
        Commands.ALERT_OK.getLabel() + " closes the note and continues, "
        + Commands.ALERT_CANCEL.getLabel() + " aborts the action",
        Commands.ALERT_HELP.getLabel() + " provides more information",
        "",
        "",
        "By default, time out after 5 seconds",
        "By default, time out after 5 seconds",
        "By default, time out after 5 seconds",
        "By default, time out after 5 seconds",
        "By default, time out after 5 seconds"
    };
    private final int TIMED = 0;
    private final int TEXT_1_BUTTON = 1;
    private final int TEXT_IMAGE_1_BUTTON = 2;
    private final int TEXT_IMAGE_DETERMINATE_1_BUTTON = 3;
    private final int TEXT_IMAGE_INDETERMINATE_1_BUTTON = 4;
    private final int TEXT_2_BUTTONS = 5;
    private final int TEXT_IMAGE_INDETERMINATE_3_BUTTONS = 6;
    private final int FORM_DETERMINATE = 7;
    private final int FORM_INDETERMINATE = 8;
    private final int ALERT_ERROR = 9;
    private final int ALERT_WARNING = 10;
    private final int ALERT_INFO = 11;
    private final int ALERT_CONFIRMATION = 12;
    private final int ALERT_ALARM = 13;
    
    private Alert alert;
    private List dialogList;
    private Image downloadImage;
    private Image installImage;
    private Image infoImage;
    private BackStack backStack;
    private Timer timer;

    /**
     * Start the app, create and show the initial List view,
     * setup listeners and enable orientation support
     */
    public void startApp() {
        dialogList = new List(Strings.getTitle(Strings.DIALOGS),
            List.IMPLICIT, DIALOG_ITEMS, null);
        dialogList.setCommandListener(this);
        dialogList.setSelectCommand(Commands.SELECT);
        dialogList.addCommand(Commands.EXIT);
        dialogList.addCommand(Commands.INFORMATION);
        dialogList.setFitPolicy(List.TEXT_WRAP_ON);

        downloadImage = ImageLoader.load(ImageLoader.DOWNLOAD);
        installImage = ImageLoader.load(ImageLoader.INSTALL);
        infoImage = ImageLoader.load(ImageLoader.INFO);
        backStack = new BackStack(this);

        Orientation.enableOrientations();
        Display.getDisplay(this).setCurrent(dialogList);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.SELECT) {
            int index = dialogList.getSelectedIndex();
            if (index < FORM_DETERMINATE || index > FORM_INDETERMINATE) {
                displayDialog(index);
            }
            else {
                displayForm(index);
            }
        }
        else if (c == Commands.EXIT || c == Commands.ALERT_OK
            || c == Commands.ALERT_HELP
            || c == Commands.INFORMATION_BACK) {
            backStack.back();
        }
        else if (c == Commands.CANCEL || c == Commands.ALERT_CANCEL) {
            if (timer != null) {
                timer.cancel();
            }
            backStack.back();
        }
        else if (c == Commands.INFORMATION) {
            backStack.forward(new InformationView(Strings.DIALOGS, this));
        }
    }

    /**
     * Demonstrates various kinds of Alerts
     * @param type 
     */
    private void displayDialog(int type) {
        alert = new Alert(TITLES[type]);
        alert.setString(MESSAGES[type]);
        alert.setCommandListener(this);

        final Gauge gauge;

        switch (type) {
            default:
            case TIMED:
                // Generates an alert that times out after 3 seconds
                // or when the user taps outside of the alert
                alert.setTimeout(3000);
                break;
            case TEXT_1_BUTTON:
                alert.addCommand(Commands.ALERT_OK);
                break;
            case TEXT_IMAGE_1_BUTTON:
                alert.setImage(infoImage);
                alert.addCommand(Commands.ALERT_OK);
                alert.setType(AlertType.INFO);
                break;
            case TEXT_IMAGE_DETERMINATE_1_BUTTON:
                // Creates an alert with a determinate gauge
                // The alert is dismissed when the gauge reaches finish 
                // (10 seconds) or Cancel is pressed
                gauge = new Gauge(null, false, 10, 0);
                alert.setIndicator(gauge);
                alert.setImage(downloadImage);
                timer = new Timer();
                // A timer handles the gauge
                timer.schedule(new TimerTask() {

                    public void run() {
                        if (gauge.getValue() == gauge.getMaxValue()) {
                            backStack.back();
                            this.cancel();
                        }
                        else {
                            gauge.setValue(gauge.getValue() + 1);
                        }
                    }
                }, 0, 1000);
                alert.addCommand(Commands.ALERT_CANCEL);
                alert.setType(AlertType.INFO);
                break;
            case TEXT_IMAGE_INDETERMINATE_1_BUTTON:
                // Creates an alert with an indeterminate gauge
                gauge = new Gauge(null, false, Gauge.INDEFINITE,
                    Gauge.CONTINUOUS_RUNNING);
                alert.setIndicator(gauge);
                alert.setImage(downloadImage);
                alert.addCommand(Commands.ALERT_CANCEL);
                alert.setType(AlertType.INFO);
                break;
            case TEXT_2_BUTTONS:
                alert.addCommand(Commands.ALERT_OK);
                alert.addCommand(Commands.ALERT_CANCEL);
                alert.setType(AlertType.CONFIRMATION);
                break;
            case TEXT_IMAGE_INDETERMINATE_3_BUTTONS:
                // Creates an alert with an indeterminate gauge and
                // three buttons
                gauge = new Gauge(null, false, Gauge.INDEFINITE,
                    Gauge.CONTINUOUS_RUNNING);
                alert.setIndicator(gauge);
                alert.setImage(installImage);
                alert.addCommand(Commands.ALERT_OK);
                alert.addCommand(Commands.ALERT_CANCEL);
                alert.addCommand(Commands.ALERT_HELP);
                break;
            case ALERT_ERROR:
                alert.setType(AlertType.ERROR);
                break;
            case ALERT_WARNING:
                alert.setType(AlertType.WARNING);
                break;
            case ALERT_INFO:
                alert.setType(AlertType.INFO);
                break;
            case ALERT_CONFIRMATION:
                alert.setType(AlertType.CONFIRMATION);
                break;
            case ALERT_ALARM:
                alert.setType(AlertType.ALARM);
                break;
        }

        // For all other Alerts than TIMED and ALERT_ERROR, _WARNING, _INFO,
        // _CONFIRMATION, _ALARM, set the automatic timeout to forever
        if (type != TIMED && type < ALERT_ERROR) {
            alert.setTimeout(Alert.FOREVER);
        }

        if (alert.getTimeout() == Alert.FOREVER) {
            backStack.forward(alert);
        } else {
            Display.getDisplay(this).setCurrent(alert);
        }
    }

    private void displayForm(int type) {
        final Form form = new Form("");
        switch (type) {
            case FORM_DETERMINATE:
                form.setTitle(Compatibility.toLowerCaseIfFT(
                    "Dialog-form progress"));
                form.append(Compatibility.toLowerCaseIfFT(
                    "Determinate progress indicator"));
                final Gauge gauge = new Gauge("V1.43.2234.5", false, 10, 0);
                timer = new Timer();
                // A timer handles the gauge
                timer.schedule(new TimerTask() {

                    public void run() {
                        if (gauge.getValue() == gauge.getMaxValue()) {
                            if (form.isShown()) {
                                backStack.back();
                            }
                            this.cancel();
                        }
                        else {
                            gauge.setValue(gauge.getValue() + 1);
                        }
                    }
                }, 0, 1000);
                form.append(gauge);
                form.append(new ImageItem(Compatibility.toLowerCaseIfFT(
                    "Download update"), ImageLoader.load(
                    "/dialog_download.png"), Item.LAYOUT_CENTER, "download"));
                break;
            case FORM_INDETERMINATE:
                form.setTitle(Compatibility.toLowerCaseIfFT("Dialog-form wait"));
                form.append(Compatibility.toLowerCaseIfFT(
                    "Indeterminate progress indicator"));
                form.append(new Gauge("44323_02.png", false, Gauge.INDEFINITE,
                    Gauge.CONTINUOUS_RUNNING));
                form.append(new ImageItem(Compatibility.toLowerCaseIfFT(
                    "Upload image"), ImageLoader.load(
                    "/image.jpg"), Item.LAYOUT_CENTER, "upload"));
                break;
            default:
                break;
        }
        try {
            Class.forName("com.nokia.mid.ui.IconCommand");
            StringItem cancel = new StringItem(null, "CANCEL", Item.BUTTON);
            cancel.setDefaultCommand(Commands.CANCEL);
            cancel.setItemCommandListener(new ItemCommandListener() {

                public void commandAction(Command c, Item item) {
                    DialogExample.this.commandAction(c, form);
                }
            });
            form.append(cancel);
        }
        catch (ClassNotFoundException e) {
            form.addCommand(Commands.CANCEL);
        }
        form.addCommand(Commands.INFORMATION);
        form.setCommandListener(this);

        backStack.forward(form);
    }
}
