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

package org.luwrain.controls;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.util.*;
import org.luwrain.hardware.*;
import org.luwrain.os.*;

/**
 * The area for browsing of directories.  This class behaves as a panel in
 * old-style file commander. The user can explore directory content and
 * move around it, traversing over near directories. Custom filters and
 * comparators are supported.
 */
public class CommanderArea2 extends ListArea
{
    public static final String PARENT_DIR = "..";

    static public class Entry
    {
	enum Type {REGULAR, DIR, PARENT, SYMLINK, SPECIAL};

	private Path path;
	private Type type;
	private boolean selected;

	Entry(Path path) throws IOException
	{
	    NullCheck.notNull(path, "path");
	    this.path = path;
	    this.type = readType(path);
	    this.selected = false;
	}

	Entry(Path path, Type type)
	{
	    NullCheck.notNull(path, "path");
	    NullCheck.notNull(type, "type");
	    this.path = path;
	    this.type = type;
	    this.selected = false;
	}

	public Path path() { return path; }
public Type type() { return type; }
	public boolean selected() { return selected; }
	public String baseName() { return path.getFileName().toString(); }

	@Override public boolean equals(Object o)
	{
	    if (o == null || !(o instanceof Entry))
		return false;
	    final Entry e = (Entry)o;
	    return path.equals(e.path) && type == e.type;
	}

	static private Type readType(Path path) throws IOException
	{
	    NullCheck.notNull(path, "path");
	    final BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
	    if (attr.isDirectory())
		return Type.DIR;
	    if (attr.isSymbolicLink())
		return Type.SYMLINK;
	    if (attr.isRegularFile())
		return Type.REGULAR;
	    return Type.SPECIAL;
	}

    }

    public interface Filter
{
    boolean commanderEntrySuits(Entry entry);
}

    public interface CommanderAppearance 
    {
	String getCommanderName(Path path);
	void introduceLocation(Path path);
	String getScreenLine(Entry entry);
	void introduceEntry(Entry entry, boolean brief);
    }

static public class CommanderParams
{
    public ControlEnvironment environment;
    public CommanderAppearance appearance;
    public boolean selecting = false;
    public Filter filter = null;//FIXME:
    public Comparator comparator = new ByNameCommanderComparator();
}

    public static class AppearanceImpl implements ListItemAppearance
    {
	//	protected ControlEnvironment environment;
	protected CommanderAppearance commanderAppearance;

	public AppearanceImpl(CommanderAppearance commanderAppearance)
	{
	    NullCheck.notNull(commanderAppearance, "commanderAppearance");
	    this.commanderAppearance = commanderAppearance;
	}

	@Override public void introduceItem(Object item, int flags)
	{
	    NullCheck.notNull(item, "item");
	    commanderAppearance.introduceEntry((Entry)item, (flags & BRIEF) > 0);
	}

	@Override public String getScreenAppearance(Object item, int flags)
	{
	    NullCheck.notNull(item, "item");
	    final Entry entry = (Entry)item;

	    NullCheck.notNull(entry, "entry");
	    final boolean selected = entry.selected();
	    final CommanderArea2.Entry.Type type = entry.type();
	    final String name = commanderAppearance.getScreenLine(entry);
	    final StringBuilder b = new StringBuilder();
	    b.append(selected?"*":" ");
	    switch(type)
	    {
	    case DIR:
		b.append("[");
		break;
	    case SPECIAL:
		b.append("!");
		break;
	    case SYMLINK:
		b.append("{");
		break;
	    default:
		b.append(" ");
	    }
	    b.append(name);
	    switch(type)
	    {
	    case DIR:
		b.append("]");
		break;
	    case SYMLINK:
		b.append("}");
		break;
	    }
	    return new String(b);
	}

	@Override public int getObservableLeftBound(Object item)
	{
	    return 2;
	}

	@Override public int getObservableRightBound(Object item)
	{
	    NullCheck.notNull(item, "item");
	    return commanderAppearance.getScreenLine((Entry)item).length() + 2;
	}
    }

    static public class ModelImpl implements ListArea.Model
    {
Filter filter = null;
	Comparator comparator = null;

	Path current;
	Entry[] entries;//null means the content is inaccessible

	ModelImpl(Filter filter, Comparator comparator)
	{
	    //filter may be null
	    NullCheck.notNull(comparator, "comparator");
	    this.filter = filter;
	    this.comparator = comparator;
	}

	void load(Path path)
	{
	    NullCheck.notNull(path, "path");
	    current = path;
	    entries = loadEntries(path, filter, comparator);
	}

	@Override public int getItemCount()
	{
	    return entries != null?entries.length:0;
	}

	@Override public Object getItem(int index)
	{
	    return (entries != null && index < entries.length)?entries[index]:null;
	}

	@Override public boolean toggleMark(int index)
	{
	    return false;
	}

	@Override public void refresh()
	{
	    entries = loadEntries(current, filter, comparator);
	}
    }

protected CommanderAppearance commanderAppearance;
    protected boolean selecting = false;

    public CommanderArea2(CommanderParams params, Path current)
    {
	super(constructListParams(params));
	this.commanderAppearance = params.appearance;
	this.selecting = params.selecting;
	if (!Files.isDirectory(current))
	    throw new IllegalArgumentException("current must address a directory");
	model().load(current);
    }

    public boolean find(String fileName, boolean announce)
    {
	NullCheck.notNull(fileName, "fileName");
	if (fileName.isEmpty())
	    throw new IllegalArgumentException("fileName may not be null");
	if (isEmpty())
	    return false;
	int index = 0;
	while(index < model().entries.length && !model().entries[index].baseName().equals(fileName))
	    ++index;
	if (index >= model().entries.length)
	    return false;
	hotPointY = index;
	hotPointX = 0;
	environment.onAreaNewHotPoint(this);
	if (announce)
	    commanderAppearance.introduceEntry(model().entries[hotPointY], false);
	return true;
    }


    public boolean find(Path path, boolean announce)
    {
	NullCheck.notNull(path, "path");
	if (isEmpty())
	    return false;
	int index = 0;
	while(index < model().entries.length && !model().entries[index].path().equals(path))
	    ++index;
	if (index >= model().entries.length)
	    return false;
	hotPointY = index;
	hotPointX = 0;
	environment.onAreaNewHotPoint(this);
	if (announce)
	    commanderAppearance.introduceEntry(model().entries[hotPointY], false);
	return true;
    }

    /*
     * Returns the list of currently selected files. If user marked some
     * files or directories, this method returns list of them, regardless what
     * entry is under the cursor. Otherwise, this method returns exactly the
     * entry under the current cursor position or an empty array, if the cursor is at
     * the empty string in the bottom of the area. The parent directory entry
     * is always ignored. This method never returns {@code >null}.
     *
     * @return The list of currently selected entries 
     */
    public Path[] selected()
    {
	if (isEmpty())
	    return new Path[0];
	if (selecting)
	{
	    final LinkedList<Path> paths = new LinkedList<Path>();
	    for(Entry e: model().entries)
		if (e.selected() && e.type() != Entry.Type.PARENT)
		    paths.add(e.path());
	    if (!paths.isEmpty())
		return paths.toArray(new Path[paths.size()]);
	}
	final Entry e = cursorAtEntry();
	if (e == null || e.type() == Entry.Type.PARENT)
	    return new Path[0];
	return new Path[]{e.path()};
    }

    /**
     * Returns the location being currently observed.  In general, this
     * method may return null if the object isn't associated with any
     * particular location but in practice this should happen quite rarely.  
     *
     * @return The location being observed
     */
    public Path opened()
    {
	return model().current;
    }

    /**
     * Returns the entry exactly under the cursor. This method returns the
     * entry without taking into account where the user marks are. If the cursor is at
     * the empty line in the bottom of the area this method returns null. The parent directory entry is returned
     * as usual.
     *
     * @return The entry under the cursor
     */
    public Path cursorAt()
    {
	return !isEmpty() && hotPointY >= 0 && hotPointY < model().entries.length?model().entries[hotPointY].path():null;
    }

    public Entry cursorAtEntry()
    {
	return !isEmpty() && hotPointY >= 0 && hotPointY < model().entries.length?model().entries[hotPointY]:null;
    }

    public void setFilter(Filter filter)
    {
	NullCheck.notNull(filter, "filter");
	//	this.filter = filter;
    }

    public boolean isEmpty()
    {
	return model().entries == null || model().entries.length < 1;
    }

    @Override public int getLineCount()
    {
	return !isEmpty()?model().entries.length + 1:1;
    }

    @Override public String getLine(int index)
    {
	if (isEmpty())
	    return index == 0?noContentStr():"";
	return index < model().entries.length?commanderAppearance.getScreenLine(model().entries[index]):"";
    }

    @Override public boolean onAreaQuery(AreaQuery query)
    {
	NullCheck.notNull(query, "query");
	if (query.getQueryCode() == AreaQuery.CURRENT_DIR)
	{
	    final CurrentDirQuery currentDirQuery = (CurrentDirQuery)query;
	    currentDirQuery.setCurrentDir(model().current.toString());
	    return true;
	}
	return super.onAreaQuery(query);
    }

    @Override public String getAreaName()
    {
	if (model().current == null)
	    return "-";
	return commanderAppearance.getCommanderName(model().current);
    }

    /*
    private boolean onEnter(KeyboardEvent event)
    {
	if (noContentCheck())
	    return true;
	if (hotPointY >= entries.size())
	{
	    if (clickHandler == null)
		return false;
	    final Path[] selected = selected();
	    if (selected == null || selected.length < 1)
		return false;
	    return clickHandler.onCommanderClick(null, selected);
	}
	final Entry entry = entries.get(hotPointY);
	if (Files.isDirectory(entry.path()))//Explicit check because it could be a symlink to directory
	{
	    final Path parent = current.getParent();
	    if (entry.parent() && parent != null)
		open(parent, current.getFileName().toString()); else
		open(entry.path(), null);
	    appearance.introduceLocation(current);
	    return true;
	} //directory
	if (clickHandler == null)
	    return false;
	return clickHandler.onCommanderClick(entry.path(), selected());
    }
    */

    /*
    private boolean onBackspace(KeyboardEvent event)
    {
	//noContent() isn't applicable here, we should be able to leave the directory, even if it doesn't have any content
	if (current == null)
	    return false;
	final Path parent = current.getParent();
	if (parent == null)
	    return false;
	open(parent, current.getFileName().toString());
	appearance.introduceLocation(current);
	return true;
    }
    */

    /*
    private boolean onInsert(KeyboardEvent event)
    {
	if (!selecting)
	    return false;
	if (noContentCheck())
	    return true;
	if (hotPointY >= entries.size())
	{
	    environment.hint(Hints.EMPTY_LINE);
	    return true;
	}
	entries.get (hotPointY).selected = !entries.get(hotPointY).selected;
	++hotPointY;
	onNewHotPointY(false);
	return true;
    }
    */

    /*
    private boolean onOk(EnvironmentEvent event)
    {
	if (noContentCheck())
	    return true;
	if (hotPointY >= entries.size())
	{
	    Path[] selected = selected();
	    if (selected == null || selected.length < 1)
		return false;
	    if (clickHandler == null)
		return false;
	    return clickHandler.onCommanderClick(null, selected);
	}
	final Entry entry = entries.get(hotPointY);
	if (clickHandler == null)
	    return false;
	return clickHandler.onCommanderClick(entry.path(), selected());
    }
    */

    /*
    private boolean onChar(KeyboardEvent event)
    {
	if (noContentCheck())
	    return true;
	final char c = event.getChar();
	String beginning = "";
	if (hotPointY < entries.size())
	{
	    final String name = entries.get(hotPointY).baseName();
	    final int pos = hotPointX < name.length()?hotPointX:name.length();
	    beginning = name.substring(0, pos);
	}
	final String mustBegin = beginning + c;
	for(int i = 0;i < entries.size();++i)
	{
	    final String name = entries.get(i).baseName();
	    if (!name.startsWith(mustBegin))
		continue;
	    hotPointY = i;
	    ++hotPointX;
	    environment.onAreaNewHotPoint(this);
	    appearance.introduceEntry(entries.get(hotPointY), true);
	    return true;
	}
	return false;
    }
    */

    //Doesn't produce any speech announcement
    public void open(Path path, String desiredSelected)
    {
	NullCheck.notNull(path, "path");
	if (!Files.isDirectory(path))
	    throw new IllegalArgumentException("path must address a directory");
	model().load(path);
	hotPointX = 0;
	hotPointY = 0;
	if (isEmpty())
	{
	    notifyNewContent();
	    return;
	}
	if (desiredSelected != null && !desiredSelected.isEmpty())
	    for(hotPointY = 0;hotPointY < model().entries.length;++hotPointY)
		if (model().entries[hotPointY].baseName().equals(desiredSelected))
		    break;
	if (hotPointY >= model().entries.length)
	    hotPointY = 0;
	notifyNewContent();
    }

    static private Entry[] loadEntries(Path path,
				       Filter filter, Comparator comparator)
    {
	NullCheck.notNull(path, "path");
	NullCheck.notNull(filter, "filter");
	NullCheck.notNull(comparator, "comparator");
	try {
	    final LinkedList<Path> paths = new LinkedList<Path>();
	    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
		    for (Path p : directoryStream)
			paths.add(p);
		}
	    final LinkedList<Entry> res = new LinkedList<Entry>();
	    if (path.getParent() != null)
		res.add(new Entry(path.resolve(PARENT_DIR), Entry.Type.PARENT));
	    for(Path p: paths)
	    {
		final Entry e = new Entry(p);
		if (filter == null || filter.commanderEntrySuits(e))
		    res.add(e);
	    }
	    final Entry[] toSort = res.toArray(new Entry[res.size()]);
	    Arrays.sort(toSort, comparator);
	    return toSort;
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	    return null;
	}
    }

    @Override protected String noContentStr()
    {
	return environment.staticStr(LangStatic.COMMANDER_NO_CONTENT);
    }

    private void notifyNewContent()
    {
	environment.onAreaNewContent(this);
	environment.onAreaNewHotPoint(this);
	environment.onAreaNewName(this);
    }

    @Override public ModelImpl model()
    {
	return (ModelImpl)model;
    }

    static private ListArea.Params constructListParams(CommanderParams params)
    {
	NullCheck.notNull(params, "params");
	NullCheck.notNull(params.environment, "params.environment");
	NullCheck.notNull(params.comparator, "params.comparator");
	final ListArea.Params listParams = new ListArea.Params();
	listParams.environment = params.environment;
	listParams.model = new ModelImpl(params.filter, params.comparator);
	listParams.appearance = new AppearanceImpl(params.appearance);
	listParams.name = "#CommanderArea#";//Never used, getAreaName() overridden
	return listParams;
    }
}
