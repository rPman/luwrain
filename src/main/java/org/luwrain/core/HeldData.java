/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.core;

final public class HeldData
{
    public Object[] rawObjects = new Object[0];
    public String[] strings = new String[0];
    public String comment = "";

    public HeldData(String[] strings)
    {
	this.strings = strings;
    }

    public HeldData(String[] strings, String comment)
    {
	this.strings = strings;
	this.comment = comment;
    }

    public HeldData(String[] strings, Object[] rawObjects)
    {
	this.strings = strings;
	this.rawObjects = rawObjects;
    }

    public HeldData(String[] strings,
		    Object[] rawObjects,
		    String comment)
    {
	this.strings = strings;
	this.rawObjects = rawObjects;
	this.comment = comment;
    }

    public boolean isEmpty()
    {
	return strings == null || strings.length < 1;
    }
}
