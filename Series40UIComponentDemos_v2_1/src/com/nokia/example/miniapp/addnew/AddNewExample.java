/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.miniapp.addnew;

import com.nokia.example.miniapp.utils.Commands;
import com.nokia.uihelpers.Compatibility;
import com.nokia.example.miniapp.utils.InformationView;
import com.nokia.example.miniapp.utils.RMSUtils;
import com.nokia.example.miniapp.utils.SaveChangesAlert;
import com.nokia.example.miniapp.utils.Strings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

/**
 * An example demonstrating how to add new content to a form.
 */
public class AddNewExample
    extends MIDlet
    implements CommandListener, ItemCommandListener {

    private static final String RMS_NAME = "AddNewExample";
    private final Command CLEAR_ALL = new Command(
        Compatibility.toLowerCaseIfFT("Clear all"), Command.SCREEN, 1);
    private Form form;
    private StringItem buttonAddName;
    private Vector originalNames = new Vector();

    public void startApp() {
        if (form == null) {
            form = new Form(Strings.getTitle(Strings.ADDNEW));
            form.addCommand(Commands.DONE);
            form.addCommand(Commands.INFORMATION);
            form.addCommand(Commands.EXIT);
            form.addCommand(CLEAR_ALL);
            form.setCommandListener(this);
            buttonAddName = new StringItem("", "ADD NAME", Item.BUTTON);
            buttonAddName.setDefaultCommand(new Command("Select",
                Command.ITEM, 0));
            buttonAddName.setItemCommandListener(this);
            form.append(buttonAddName);
            loadState();
        }
        Display.getDisplay(this).setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    private void addName(String name) {
        TextField field = new TextField("Name " + form.size(), name, 50,
            TextField.ANY);
        form.insert(form.size() - 1, field);
        Display.getDisplay(this).setCurrentItem(buttonAddName);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Commands.INFORMATION) {
            Display.getDisplay(this).setCurrent(new InformationView(
                Strings.ADDNEW, this));
        }
        else if (c == Commands.INFORMATION_BACK) {
            Display.getDisplay(this).setCurrent(form);
        }
        else if (c == Commands.DONE) {
            saveState();
            notifyDestroyed();
        }
        else if (c == Commands.EXIT) {
            if (hasChanges()) {
                Display.getDisplay(this).setCurrent(new SaveChangesAlert(this));
            }
            else {
                notifyDestroyed();
            }
        }
        else if (c == Commands.ALERT_SAVE_BACK) {
            Display.getDisplay(this).setCurrent(form);
        }
        else if (c == Commands.ALERT_SAVE_NO) {
            notifyDestroyed();
        }
        else if (c == Commands.ALERT_SAVE_YES) {
            saveState();
            notifyDestroyed();
        }
        else if (c == CLEAR_ALL) {
            while (form.size() > 1) {
                form.delete(0);
            }
        }
    }

    public void commandAction(Command c, Item item) {
        addName("");
    }

    private boolean hasChanges() {
        if (originalNames.size() != form.size() - 1) {
            return true;
        }
        for (int i = 0; i < originalNames.size(); i++) {
            String originalName = (String) originalNames.elementAt(i);
            String currentName = ((TextField) form.get(i)).getString();
            if (!originalName.equals(currentName)) {
                return true;
            }
        }
        return false;
    }

    private void saveState() {
        ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);
            dout.writeInt(form.size() - 1);
            for (int i = 0, size = form.size() - 1; i < size; i++) {
                dout.writeUTF(((TextField) form.get(i)).getString());
            }
            RMSUtils.save(RMS_NAME, bout.toByteArray());
        }
        catch (IOException e) {
        }
        finally {
            try {
                if (bout != null) {
                    bout.close();
                }
            }
            catch (IOException e) {
            }
        }
    }

    private void loadState() {
        byte[] data = RMSUtils.load(RMS_NAME);
        if (data != null) {
            try {
                DataInputStream din =
                    new DataInputStream(new ByteArrayInputStream(data));
                int size = din.readInt();
                for (int i = 0; i < size; i++) {
                    String name = din.readUTF();
                    originalNames.addElement(name);
                    addName(name);
                }
            }
            catch (IOException e) {
            }
        }
        if (originalNames.isEmpty()) {
            originalNames.addElement("");
            addName("");
        }
    }
}
