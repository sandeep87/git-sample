/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.categorybar;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.example.miniapp.utils.ImageLoader;
import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.IconCommand;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;

public class CategoryBarView
    extends Form {

    private final String[] VIEW_NAMES = {
        "search",
        "comments",
        "appointments",
        "favourites",
        "all",
        "downloads",
        "contacts",
        "profile",
        "exports",
        "tags",
        "alerts",
        "archive",
        "ideas",
        "settings",
        "information"
    };
    private int amountOfCategories;

    public CategoryBarView(int amountOfCategories) {
        super("search");

        this.amountOfCategories = amountOfCategories;
        this.addCommand(Commands.INFORMATION);
    }

    /**
     * A method to fake different views
     * @param index of the view to change to
     */
    public void setActive(int index) {
        if (index >= 0 && index < VIEW_NAMES.length) {
            this.setTitle(VIEW_NAMES[index]);
            String name = String.valueOf(amountOfCategories) + " categories";
            StringItem stringItem = new StringItem("", name);
            this.deleteAll();
            this.append(stringItem);
        }
    }

    /**
     * A factory method to create a CategoryBar to display in the parent view. 
     * Amount of categories is specified during the construction of this object.
     * @return CategoryBar with the requested amount of categories
     */
    public CategoryBar createCategoryBar() {
        Vector commands = new Vector();

        if (amountOfCategories >= 4) {
            Image search = ImageLoader.load(ImageLoader.CATEGORY_SEARCH);
            Image comments = ImageLoader.load(ImageLoader.CATEGORY_COMMENTS);
            Image appointment = ImageLoader.load(
                ImageLoader.CATEGORY_APPOINTMENTS);
            Image favourites = ImageLoader.load(ImageLoader.CATEGORY_FAVOURITES);

            // Passing null as the second image makes the phone draw the selected
            // image with the current highlight color
            commands.addElement(new IconCommand("search", search, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("comments", comments, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("appointment", appointment, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("favourites", favourites, null,
                Command.SCREEN, 1));
        }
        if (amountOfCategories >= 6) {
            Image allExtend = ImageLoader.load(ImageLoader.CATEGORY_ALL);
            Image downloadsExtend = ImageLoader.load(
                ImageLoader.CATEGORY_DOWNLOADS);

            commands.addElement(new IconCommand("all", allExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("downloads", downloadsExtend,
                null, Command.SCREEN, 1));
        }
        if (amountOfCategories >= 15) {
            Image contactsExtend = ImageLoader.load(
                ImageLoader.CATEGORY_CONTACTS);
            Image profileExtend = ImageLoader.load(
                ImageLoader.CATEGORY_PROFILE);
            Image exportsExtend = ImageLoader.load(
                ImageLoader.CATEGORY_EXPORTS);
            Image tagsExtend =
                ImageLoader.load(ImageLoader.CATEGORY_TAGS);
            Image alertsExtend = ImageLoader.load(
                ImageLoader.CATEGORY_ALERTS);
            Image archiveExtend = ImageLoader.load(
                ImageLoader.CATEGORY_ARCHIVE);
            Image ideasExtend = ImageLoader.load(
                ImageLoader.CATEGORY_IDEAS);
            Image settingsExtend = ImageLoader.load(
                ImageLoader.CATEGORY_SETTINGS);
            Image informationExtend = ImageLoader.load(
                ImageLoader.CATEGORY_INFORMATION);

            commands.addElement(new IconCommand("contacts", contactsExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("profile", profileExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("exports", exportsExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("tags", tagsExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("alerts", alertsExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("archive", archiveExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("ideas", ideasExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("settings", settingsExtend, null,
                Command.SCREEN, 1));
            commands.addElement(new IconCommand("information", informationExtend,
                null, Command.SCREEN, 1));
        }

        IconCommand[] iconCommands = new IconCommand[amountOfCategories];

        for (int i = 0; i < amountOfCategories; i++) {
            iconCommands[i] = (IconCommand) commands.elementAt(i);
        }

        return new CategoryBar(iconCommands, true);
    }
}
