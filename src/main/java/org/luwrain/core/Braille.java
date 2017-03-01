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

class Braille
{
    private Registry registry;
    private org.luwrain.base.Braille braille;
    private boolean active = false;
    private String errorMessage = "";

    void init(Registry registry, org.luwrain.base.Braille braille,
	      org.luwrain.base.EventConsumer eventConsumer)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(eventConsumer, "eventConsumer");
	this.braille = braille;
	if (braille == null)
	{
	    active = false;
	    errorMessage = "No braille support in the operating system";
	    return;
	}
	final Settings.Braille settings = Settings.createBraille(registry);
	if (!settings.getEnabled(false))
	    return;
	final InitResult res = braille.init(eventConsumer);
	if (res.success())
	{
	    active = true;
	    errorMessage = "";
	} else
	{
	    active = false;
	    errorMessage = res.message();
	}
    }

    void textToSpeak(String text)
    {
	NullCheck.notNull(text, "text");
	if (braille == null)
	    return;
	braille.writeText(text);
    }

    boolean isActive()
    {
	return active;
    }

    String getDriver()
    {
	return braille != null?braille.getDriverName():"";
    }

    String getErrorMessage()
    {
	return errorMessage;
    }

    int getDisplayWidth()
    {
	return braille != null?braille.getDisplayWidth():0;
    }

    int getDisplayHeight()
    {
	return braille != null?braille.getDisplayHeight():0;
    }
}
