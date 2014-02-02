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

package org.luwrain.app.commander;

import org.luwrain.core.*;

public class CommanderApp implements Application, CommanderActions
{
    private Object instance = null;
    private CommanderStringConstructor stringConstructor = null;
    private PanelArea leftPanel;
    private PanelArea rightPanel;
    private TasksArea tasks;

    public boolean onLaunch(Object instance)
    {
	Object o = Langs.requestStringConstructor("commander");
	if (o == null)
	    return false;
	stringConstructor = (CommanderStringConstructor)o;
	leftPanel = new PanelArea(this, stringConstructor, PanelArea.LEFT);
	rightPanel = new PanelArea(this, stringConstructor, PanelArea.RIGHT);
	tasks = new TasksArea(this, stringConstructor);
	this.instance = instance;
	return true;
    }

    public AreaLayout getAreasToShow()
    {
	return new AreaLayout(AreaLayout.LEFT_RIGHT_BOTTOM, leftPanel, rightPanel, tasks);
    }

    public void gotoLeftPanel()
    {
	Dispatcher.setActiveArea(instance, leftPanel);
    }

    public void gotoRightPanel()
    {
	Dispatcher.setActiveArea(instance, rightPanel);
    }

    public void gotoTasks()
    {
	Dispatcher.setActiveArea(instance, tasks);
    }

    public void closeCommander()
    {
	Dispatcher.closeApplication(instance);
    }

    public void openFiles(String[] fileNames)
    {
	Log.debug("commander", "need to open " + fileNames.length + " files");
	if (fileNames != null && fileNames.length > 0)
	    Dispatcher.open(fileNames);
    }
}