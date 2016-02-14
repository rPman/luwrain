/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.popups;

import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
//import org.luwrain.util.*;

public class CommanderPopup extends CommanderArea implements CommanderArea.ClickHandler, Popup, PopupClosingRequest
{
    public static final int ACCEPT_REGULAR_FILES = 1;
    public static final int ACCEPT_DIRECTORIES = 2;
    public static final int ACCEPT_ALL = ACCEPT_REGULAR_FILES | ACCEPT_DIRECTORIES;
    public static final int ACCEPT_MULTIPLE_SELECTION = 16;

    protected Luwrain luwrain;
    public final PopupClosing closing = new PopupClosing(this);
    private String name;
    private int flags;
    private int popupFlags;

    static private CommanderArea.Params constructCommanderParams()
    {
	return null;
    }

    public CommanderPopup(Luwrain luwrain, String name,
			  Path path, int flags,
			  int popupFlags)
    {
	super(constructCommanderParams(), null);
	this.luwrain = luwrain;
	this.name = name;
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(name, "name");
    }

@Override     public boolean onCommanderClick(Path current, Path[] selected)
    {
	return closing.doOk();
    }

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	if (closing.onKeyboardEvent(event))
	    return true;
	if (!event.isSpecial() && !event.isModified())
	    switch(event.getChar())
	    {
	    case '=':
		setFilter(new CommanderFilters.AllFiles());
		refresh();
		return true;
	    case '-':
		setFilter(new CommanderFilters.NoHidden());
		refresh();
		return true;
	    default:
		return super.onKeyboardEvent(event);
	    }
	if (event.isSpecial() &&
	    event.getSpecial() == KeyboardEvent.Special.ENTER &&
	    event.withShiftOnly())
	    return openMountedPartitions();
	return super.onKeyboardEvent(event);
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	if (closing.onEnvironmentEvent(event))
	    return true;
	return super.onEnvironmentEvent(event);
    }

    @Override public String getAreaName()
    {
	return name + super.getAreaName();
    }

    @Override public boolean onOk()
    {
	final Path[] selected = selected();
	if (selected  == null || selected.length < 1)
	    return false;
	return true;
    }

    @Override public boolean onCancel()
    {
	return true;
    }

    @Override public Luwrain getLuwrainObject()
    {
	return luwrain;
    }

    @Override public EventLoopStopCondition getStopCondition()
    {
	return closing;
    }

    @Override public boolean noMultipleCopies()
    {
	return (popupFlags & Popup.NO_MULTIPLE_COPIES) != 0;
    }

    @Override public boolean isWeakPopup()
    {
	return (popupFlags & Popup.WEAK) != 0;
    }

    private boolean openMountedPartitions()
    {
	final File f = Popups.mountedPartitionsAsFile(luwrain, popupFlags);
	if (f == null)
	    return true;
	open(f.toPath(), null);
	return true;
    }
}
