/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.form;

import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.ImageLoader;
import javax.microedition.lcdui.*;

/**
 * Demonstrates the usage of the popup ChoiceGroup
 */
public class ExclusivePopupView
    extends FormExampleView {

    private final int ITEM_COUNT = 9;
    private Image image = null;
    private ChoiceGroup choiceGroup1;
    private ChoiceGroup choiceGroup2;
    private int choiceGroup1Value = 0;
    private int choiceGroup2Value = 0;

    public ExclusivePopupView(CommandListener commandListener, String title) {
        super(title, commandListener);

        // Load the image
        image = ImageLoader.load(ImageLoader.ICON);

        // Create a ChoiceGroup with dummy items
        choiceGroup1 = new ChoiceGroup(Compatibility.toLowerCaseIfFT(
            "Popup choicegroup"), Choice.POPUP);
        for (int i = 1; i <= ITEM_COUNT; i++) {
            choiceGroup1.append(Compatibility.toLowerCaseIfFT("Choice item ")
                + String.valueOf(i), null);
        }
        this.append(choiceGroup1);

        // Create a ChoiceGroup with dummy items with images
        choiceGroup2 = new ChoiceGroup(Compatibility.toLowerCaseIfFT(
            "Popup choicegroup"), Choice.POPUP);
        for (int i = 1; i <= ITEM_COUNT; i++) {
            choiceGroup2.append(Compatibility.toLowerCaseIfFT("Choice item ")
                + String.valueOf(i), image);
        }
        this.append(choiceGroup2);
    }

    protected boolean confirmChanges() {
        return choiceGroup1Value != choiceGroup1.getSelectedIndex()
            || choiceGroup2Value != choiceGroup2.getSelectedIndex();
    }

    protected void storeCurrentValues() {
        choiceGroup1Value = choiceGroup1.getSelectedIndex();
        choiceGroup2Value = choiceGroup2.getSelectedIndex();
    }

    protected void cancelChanges() {
        choiceGroup1.setSelectedIndex(choiceGroup1Value, true);
        choiceGroup2.setSelectedIndex(choiceGroup2Value, true);
    }
}
