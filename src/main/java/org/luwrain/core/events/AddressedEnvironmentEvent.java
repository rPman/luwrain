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

package org.luwrain.core.events;

import org.luwrain.core.*;

public class AddressedEnvironmentEvent extends EnvironmentEvent
{
    private Area destArea;
    private Object instanceObj = null;

    public AddressedEnvironmentEvent(Area destArea, Code code)
    {
	super(code);
	NullCheck.notNull(destArea, "destArea");
	this.destArea = destArea;
    }

    public Area getDestArea()
    {
	return destArea;
    }

    public void setInstanceObj(Object instanceObj)
    {
	NullCheck.notNull(instanceObj, "instanceObj");
	this.instanceObj = instanceObj;
    }

    public Object getInstanceObj()
    {
	return instanceObj;
    }
}
