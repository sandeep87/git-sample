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
import com.nokia.uihelpers.Grid;
import com.nokia.example.miniapp.utils.ImageLoader;

public class CategoryGridView
    extends Grid {

    public static final String[] NAMES = {
        "Search",
        "Comments",
        "Appointments",
        "Favourites",
        "All",
        "Downloads",
        "Contacts",
        "Profile",
        "Exports",
        "Tags",
        "Alerts",
        "Archive",
        "Ideas",
        "Settings",
        "Information"
    };
    private static final String[] IMAGES = {
        ImageLoader.CATEGORY_SEARCH,
        ImageLoader.CATEGORY_COMMENTS,
        ImageLoader.CATEGORY_APPOINTMENTS,
        ImageLoader.CATEGORY_FAVOURITES,
        ImageLoader.CATEGORY_ALL,
        ImageLoader.CATEGORY_DOWNLOADS,
        ImageLoader.CATEGORY_CONTACTS,
        ImageLoader.CATEGORY_PROFILE,
        ImageLoader.CATEGORY_EXPORTS,
        ImageLoader.CATEGORY_TAGS,
        ImageLoader.CATEGORY_ALERTS,
        ImageLoader.CATEGORY_ARCHIVE,
        ImageLoader.CATEGORY_IDEAS,
        ImageLoader.CATEGORY_SETTINGS,
        ImageLoader.CATEGORY_INFORMATION
    };

    public CategoryGridView(int amountOfCategories) {
        super("Category grid");

        this.addCommand(Commands.INFORMATION);
        this.addCommand(Commands.BACK);
        
        this.setSelectCommand(Commands.LIST_SELECT);

        for (int i = 0; i < amountOfCategories; i++) {
            append(NAMES[i % NAMES.length],
                ImageLoader.load(IMAGES[i % IMAGES.length]));
        }
    }

    protected void showNotify() {
        super.showNotify();
        int width = getWidth();
        int columns = width / 100;
        int columnWidth = (width - getTheme().getScrollBarMarginRight()
            - getTheme().getScrollBarWidth()) / columns;
        setElementSize(columnWidth, 70);
    }
}
