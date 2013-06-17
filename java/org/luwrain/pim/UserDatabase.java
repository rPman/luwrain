/*
   Copyright 2012-2013 Michael Pozhidaev <msp@altlinux.org>

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

package org.luwrain.pim;

import java.sql.*;

public class UserDatabase
{
    private Connection defaultDb = null;
    private String driver;
    private String url;
    private String login;
    private String passwd;

    public UserDatabase(String driver,
			String url,
			String login,
			String passwd)
    {
	this.driver = driver;
	this.url = url;
	this.login = login;
	this.passwd = passwd;
    }

    private Connection connect() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
	Class.forName (driver).newInstance ();
	return DriverManager.getConnection (url, login, passwd);
    }

    public Connection getDefaultConnection()
    {
	try {
	    if (defaultDb == null)
		defaultDb = connect();
	}
	catch (Exception e)
	{
	    //FIXME:Log warning;
	    defaultDb = null;
	    return null;
	}
	return defaultDb;
    }

    public Connection getNewConnection()
    {
	try {
    return connect();
    }
    catch(Exception e)
    {
	//FIXME:Log warning;
	return null;
    }
    }
}
