/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.confirmation;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.IDisplayer;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.example.miniapp.utils.Strings;
import javax.microedition.lcdui.*;

public class FormConfirmationView
    extends Form
    implements CommandListener {

    private final String[] CHOICES = Compatibility.toLowerCaseIfFT(new String[]{
            "Choice item 1",
            "Choice item 2",
            "Choice item 3"
        });
    private IDisplayer displayer;
    private CommandListener parentCommandListener;
    private ChoiceGroup choiceGroup;
    private int selected;

    public FormConfirmationView() {
        super(Compatibility.toLowerCaseIfFT("Form confirmation"));

        this.addCommand(Commands.OK);
        this.addCommand(Commands.BACK);
        this.addCommand(Commands.INFORMATION);
        this.setCommandListener(this);

        StringItem stringItem = new StringItem(null,
            "do a change in the exclusive choice group. After that"
            + "\n(check) no confirmation\n(back) confirmation");
        this.append(stringItem);

        choiceGroup = new ChoiceGroup("exclusive choice group",
            ChoiceGroup.EXCLUSIVE,
            CHOICES,
            null);
        this.append(choiceGroup);
    }

    public void setParentCommandListener(CommandListener listener) {
        parentCommandListener = listener;
    }

    public void setDisplayer(IDisplayer displayer) {
        this.displayer = displayer;
    }

    /*
     * Returns the selected item from the choice group, called from the parent
     * view
     */
    public int getSelected() {
        return choiceGroup.getSelectedIndex();
    }

    /*
     * Sets the selected index to the choice group, called from the parent view
     */
    public void setSelected(int selectedIndex) {
        selected = selectedIndex;
        if (selectedIndex >= 0 && selectedIndex < CHOICES.length) {
            choiceGroup.setSelectedIndex(selectedIndex, true);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Handles commands and delegates them to the parent command handler when
     * necessary
     *
     * @param c
     * @param d
     */
    public void commandAction(Command c, Displayable d) {
        if (c == Commands.OK) {
            // Store the selected choice and go back to parent view
            parentCommandListener.commandAction(c, d);
        }
        else if (c == Commands.BACK) {
            if (selected != choiceGroup.getSelectedIndex()) {
                displayer.setCurrent(new SaveChangesAlert(this));
            }
            else {
                parentCommandListener.commandAction(Commands.BACK, this);
            }
        }
        else if (c == Commands.ALERT_SAVE_YES) {
            parentCommandListener.commandAction(Commands.OK, this);
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            parentCommandListener.commandAction(Commands.BACK, this);
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            displayer.setCurrent(this);
        }
        else if (c == Commands.INFORMATION) {
            // Show the information view
            displayer.setCurrent(new InformationView(
                Strings.CONFIRMATION, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            displayer.setCurrent(this);
        }
    }
}
