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
import java.util.Date;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;

/**
 * Demonstrates the usage of the date picker, in full touch also called
 * the tumbler.
 */
public class TumblerView
    extends FormExampleView {

    private DateField dateField;
    private DateField timeField;
    private DateField dateTimeField;
    private Date date;
    private Date time;
    private Date dateTime;

    public TumblerView(CommandListener commandListener, String title) {
        super(title, commandListener);

        // Create a time picker
        dateField = new DateField(Compatibility.toLowerCaseIfFT("Time field"),
            DateField.TIME);
        this.append(dateField);

        // Create a date picker
        timeField = new DateField(Compatibility.toLowerCaseIfFT("Date field"),
            DateField.DATE);
        this.append(timeField);

        // Create a date and time picker
        dateTimeField = new DateField(Compatibility.toLowerCaseIfFT(
            "Date time field"), DateField.DATE_TIME);
        this.append(dateTimeField);
    }

    protected boolean confirmChanges() {
        return !areDatesEqual(date, dateField)
            || !areDatesEqual(time, timeField)
            || !areDatesEqual(dateTime, dateTimeField);
    }

    protected void storeCurrentValues() {
        date = dateField.getDate();
        time = timeField.getDate();
        dateTime = dateTimeField.getDate();
    }

    protected void cancelChanges() {
        dateField.setDate(date);
        timeField.setDate(time);
        dateTimeField.setDate(dateTime);
    }

    private boolean areDatesEqual(Date date, DateField dateField) {
        Date fieldDate = dateField.getDate();
        if (date == null) {
            return fieldDate == null;
        }
        if (fieldDate == null) {
            return date == null;
        }
        return date.equals(fieldDate);
    }
}
