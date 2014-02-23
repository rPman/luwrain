/*
   Copyright 2012-2014 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.langs.en;

import java.util.*;

public class NotepadStringConstructor implements org.luwrain.app.notepad.StringConstructor
{
    public String appName()
    {
	return "Notepad";
    }

    public String introduction()
    {
	return "Editing";
    }

    public String newFileName()
    {
	return "New file.txt";
    }

    public String errorSavingFile()
    {
	return "An error occurred while saving the file";
    }

    public String fileIsSaved()
    {
	return "File was successfully saved!";
    }

    public String savePopupName()
    {
	return "Save file";
    }

    public String savePopupPrefix()
    {
	return "Enter the name of the file to save as:";
    }

    public String saveChangesPopupName()
    {
	return "Unsaved changes";
    }

    public String saveChangesPopupQuestion()
    {
	return "Do you want to save changes?";
    }
}
