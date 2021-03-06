/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.core;

import java.util.*;

class CommandManager
{
    class Entry 
    {
	public Luwrain luwrain;
	public String name = "";
	public Command command;

	public Entry(Luwrain luwrain,
		     String name,
		     Command command)
	{
	    this.luwrain = luwrain;
	    this.name = name;
	    this.command = command;
	    if (luwrain == null)
		throw new NullPointerException("luwrain may not be null");
	    if (name == null)
		throw new NullPointerException("name may not be null");
	    if (name.trim().isEmpty())
		throw new IllegalArgumentException("name may not be empty");
	    if (command == null)
		throw new NullPointerException("command may not be null");
	}
    }

    private TreeMap<String, Entry> commands = new TreeMap<String, Entry>();

    public boolean add(Luwrain luwrain, Command command)
    {
	if (luwrain == null)
	    throw new NullPointerException("luwrain may not be null");
	if (command == null)
	    throw new NullPointerException("command may not be null");
	final String name = command.getName();
	if (name == null || name.trim().isEmpty())
	    return false;
	if (commands.containsKey(name))
	    return false;
	commands.put(name, new Entry(luwrain, name, command));
	return true;
    }

    public boolean run(String name)
    {
	if (name == null)
	    throw new NullPointerException("name may not be null");
	if (name.trim().isEmpty())
	    throw new IllegalArgumentException("name may not be empty");
	if (!commands.containsKey(name))
	    return false;
	final Entry entry = commands.get(name);
	entry.command.onCommand(entry.luwrain);
	return true;
    }

    public String[] getCommandNames()
    {
	final LinkedList<String> res = new LinkedList<String>();
	for(Map.Entry<String, Entry> e: commands.entrySet())
	    res.add(e.getKey());
	String[] str = res.toArray(new String[res.size()]);
	Arrays.sort(str);
	return str;
    }

    void addOsCommands(Luwrain luwrain, Registry registry)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(registry, "registry");
	registry.addDirectory(Settings.OS_COMMANDS_PATH);
	for(String s: registry.getDirectories(Settings.OS_COMMANDS_PATH))
	{
	    if (s.trim().isEmpty())
		continue;
	    final OsCommands.OsCommand cmd = new OsCommands.OsCommand();
	    if (cmd.init(Settings.createOsCommand(registry, Registry.join(Settings.OS_COMMANDS_PATH, s))))
	    add(luwrain, cmd);
	}
    }
}
