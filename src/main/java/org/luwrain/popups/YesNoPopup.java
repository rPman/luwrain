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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;

public class YesNoPopup implements Popup, PopupClosingRequest
{
    protected final Luwrain luwrain;
    public final PopupClosingTranslator closing = new PopupClosingTranslator(this);
    protected final String name;
    protected String text = "";
    protected boolean res;
    protected final boolean defaultRes;
    protected final Set<Popup.Flags> popupFlags;

    public YesNoPopup(Luwrain luwrain,
		      String name, String text,
		      boolean defaultRes, Set<Popup.Flags> popupFlags)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(name, "name");
	NullCheck.notNull(text, "text");
	NullCheck.notNull(popupFlags, "popupFlags");
	this.luwrain = luwrain;
	this.name = name;
	this.text = text;
	this.defaultRes = defaultRes;
	this.res = defaultRes;
	this.popupFlags = popupFlags;
    }

    @Override public int getLineCount()
    {
	return 1;
    }

    @Override public String getLine(int index)
    {
	return index == 0?text:"";
    }

    @Override public int getHotPointX()
    {
	return text.length();
    }

    @Override public int getHotPointY()
    {
	return 0;
    }

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (closing.onKeyboardEvent(event))
	    return true;
	if (!event.isSpecial())
	{
	    final char c = event.getChar();
	    if (EqualKeys.equalKeys(c, 'y'))
	    {
		res = true;
		closing.doOk();
		return true;
	    }
	    if (EqualKeys.equalKeys(c, 'n'))
	    {
		res = false;
		closing.doOk();
		return true;
	    }
	    return false;
	}
	if (event.isModified())
	    return false;
	switch(event.getSpecial())
	{
	case ENTER:
	    closing.doOk();
	    return true;
	case ARROW_UP:
	case ARROW_DOWN:
	case ARROW_LEFT:
	case ARROW_RIGHT:
	    luwrain.say(text);
	    return true;
	default:
	    return false;
	}
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	switch (event.getCode())
	{
	case INTRODUCE:
	    luwrain.silence();
	    luwrain.playSound(Sounds.INTRO_POPUP);
	    luwrain.say(text);
	    return true;
	default:
	return closing.onEnvironmentEvent(event);
	}
    }

    @Override public boolean onAreaQuery(AreaQuery query)
    {
	NullCheck.notNull(query, "query");
	if (query.getQueryCode() == AreaQuery.REGION && (query instanceof RegionQuery))
	{
	    final RegionQuery regionQuery = (RegionQuery)query;
	    regionQuery.answer(new RegionContent(new String[]{text}));
	    return true;
	}
	return false;
    }

    @Override public Action[] getAreaActions()
    {
	return new Action[0];
    }

    @Override public String getAreaName()
    {
	return name;
    }

    public boolean result()
    {
	return res;
    }

    @Override public boolean onOk()
    {
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

    @Override public Set<Popup.Flags> getPopupFlags()
    {
	return popupFlags;
    }
}
