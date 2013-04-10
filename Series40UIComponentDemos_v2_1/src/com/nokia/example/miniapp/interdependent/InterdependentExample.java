/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.interdependent;

import com.nokia.example.miniapp.utils.BackStack;
import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.example.miniapp.utils.Strings;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class InterdependentExample
    extends MIDlet
    implements ItemStateListener, CommandListener, ItemCommandListener {

    private final int FIRST_CHOICE = 0;
    private final int SECOND_CHOICE = 1;
    private final int THIRD_CHOICE = 2;
    private final int DONE_BUTTON = 3;
    private final String[] LABELS = {
        "First choice",
        "Second choice",
        "Third choice"
    };
    private final String[] INITIAL_CHOICES = Compatibility.toLowerCaseIfFT(
        new String[]{
            "Choice item 1",
            "Choice item 2",
            "Choice item 3"
        });
    private Form form;
    private ChoiceGroup choiceGroup1;
    private ChoiceGroup choiceGroup2;
    private ChoiceGroup choiceGroup3;
    private StringItem doneButton;

    public void startApp() {
        form = new Form(Strings.getTitle(Strings.INTERDEPENDENT));
        choiceGroup1 = new ChoiceGroup(LABELS[FIRST_CHOICE],
            Choice.POPUP,
            INITIAL_CHOICES,
            null);
        form.append(choiceGroup1);
        form.addCommand(Commands.BACK);
        form.addCommand(Commands.INFORMATION);
        form.setItemStateListener(this);
        form.setCommandListener(this);
        Display.getDisplay(this).setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    /**
     * Catches the changes in the ChoiceGroups and creates/removes form elements
     * accordingly
     *
     * @param item The item that changed
     */
    public void itemStateChanged(Item item) {
        int parentChoice = ((ChoiceGroup) item).getSelectedIndex();
        String parent = String.valueOf(parentChoice + 1);

        // The first ChoiceGroup was changed. If they exist, delete 2nd and 3rd
        // ChoiceGroup and re-create the second one.
        if (item.getLabel().equals(LABELS[FIRST_CHOICE])) {
            String[] choices = {
                "choice item " + parent + "-1",
                "choice item " + parent + "-2",
                "choice item " + parent + "-3"
            };
            if (choiceGroup2 != null) {
                form.delete(1);
                choiceGroup2 = null;
            }
            if (choiceGroup3 != null) {
                form.delete(1);
                choiceGroup3 = null;
            }
            if (doneButton != null) {
                form.delete(1);
                doneButton = null;
            }
            choiceGroup2 = new ChoiceGroup(LABELS[SECOND_CHOICE],
                Choice.POPUP,
                choices,
                null);
            form.append(choiceGroup2);
        }
        // The second ChoiceGroup was changed. If the third one exists, delete
        // and recreate it.
        else if (item.getLabel().equals(LABELS[SECOND_CHOICE])) {
            // Figure out the selected index of the first ChoiceGroup
            int grandParentChoice = ((ChoiceGroup) form.get(FIRST_CHOICE)).
                getSelectedIndex();
            String grandParent = String.valueOf(grandParentChoice + 1);
            String[] choices = {
                "choice item " + grandParent + "-" + parent + "-1",
                "choice item " + grandParent + "-" + parent + "-2",
                "choice item " + grandParent + "-" + parent + "-3"
            };
            if (doneButton != null) {
                form.delete(DONE_BUTTON);
                doneButton = null;
            }
            if (choiceGroup3 != null) {
                form.delete(THIRD_CHOICE);
                choiceGroup3 = null;
            }
            choiceGroup3 = new ChoiceGroup(LABELS[THIRD_CHOICE],
                Choice.POPUP,
                choices,
                null);
            form.append(choiceGroup3);
        }
        else if (item.getLabel().equals(LABELS[THIRD_CHOICE])) {
            if (doneButton == null) {
                doneButton = new StringItem(null, "DONE", Item.BUTTON);
                doneButton.setDefaultCommand(Commands.SELECT);
                doneButton.setItemCommandListener(this);
                form.append(doneButton);
            }
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.INFORMATION) {
            Display.getDisplay(this).setCurrent(new InformationView(
                Strings.INTERDEPENDENT, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            Display.getDisplay(this).setCurrent(form);
        }
        else if (c == Commands.SELECT || c == Commands.ALERT_SAVE_YES) {
            // save selection
            notifyDestroyed();
        }
        else if (c == Commands.BACK) {
            if (doneButton == null) {
                Alert alert = new Alert(Compatibility.toLowerCaseIfFT(
                    "Discard changes"));
                alert.setString(
                    "Do you want to continue filling the form or cancel all changes?");
                alert.addCommand(Commands.ALERT_CONTINUE);
                alert.addCommand(Commands.ALERT_CANCEL);
                alert.setCommandListener(this);
                Display.getDisplay(this).setCurrent(alert);
            }
            else {
                Display.getDisplay(this).setCurrent(new SaveChangesAlert(this));
            }
        }
        else if (c == Commands.ALERT_CONTINUE || c == Commands.ALERT_SAVE_BACK) {
            Display.getDisplay(this).setCurrent(form);
        }
        else if (c == Commands.ALERT_CANCEL || c == Commands.ALERT_SAVE_NO) {
            // discard selection
            notifyDestroyed();
        }
    }

    public void commandAction(Command c, Item item) {
        commandAction(c, form);
    }
}
