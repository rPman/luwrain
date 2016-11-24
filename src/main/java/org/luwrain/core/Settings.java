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

public interface Settings
{
    static final String OS_COMMANDS_PATH = "/org/luwrain/os/commands";
    static final String OS_SHORTCUTS_PATH = "/org/luwrain/os/shortcuts";
    static final String I18N_PATH = "/org/luwrain/i18n";
    static final String PERSONAL_INFO_PATH = "/org/luwrain/personal";
    static final String BACKGROUND_SOUNDS_PATH = "/org/luwrain/sounds/background";

public interface I18n
{
    String getCharsets(String defValue);
    void setCharsets(String value);
}

    static public RegistryKeys keys = new RegistryKeys();

public interface OsCommand
{
    String getName(String defValue);
    String getCommand(String defValue);
    boolean getShowResultingMessage(boolean defValue);
}

public interface OsShortcut
{
    String getName(String defValue);
    String getCommand(String defValue);
    boolean getShowResultingMessage(boolean defValue);
}

    public interface PersonalInfo
    {
	String getFullName(String defValue);
	String getDefaultMailAddress(String defValue);
	String getSignature(String defValue);
	void setFullName(String value);
	void setDefaultMailAddress(String value);
	void setSignature(String value);
    }

public interface FileTypeAppInfo
{
    boolean getTakesUrls(boolean defValue);
    boolean getTakesMultiple(boolean defValue);
    void setTakesUrls(boolean value);
    void setTakesMultiple(boolean value);
}

    public interface InteractionParams
    {
	int getWindowLeft(int defValue);
	int getWindowTop(int defValue);
	int getWindowWidth(int defValue);
	int getWindowHeight(int defValue);
	int getMarginLeft(int defValue);
	int getMarginTop(int defValue);
	int getMarginRight(int defValue);
	int getMarginBottom(int defValue);
	int getFontColorRed(int defValue);
	int getFontColorGreen(int defValue);
	int getFontColorBlue(int defValue);
	int getFont2ColorRed(int defValue);
	int getFont2ColorGreen(int defValue);
	int getFont2ColorBlue(int defValue);
	int getBkgColorRed(int defValue);
	int getBkgColorGreen(int defValue);
	int getBkgColorBlue(int defValue);
	int getSplitterColorRed(int defValue);
	int getSplitterColorGreen(int defValue);
	int getSplitterColorBlue(int defValue);
	int getInitialFontSize(int defValue);
	String getFontName(String defValue);
	void setWindowLeft(int value);
	void setWindowTop(int value);
	void setWindowWidth(int value);
	void setWindowHeight(int value);
	void setMarginLeft(int value);
	void setMarginTop(int value);
	void setMarginRight(int value);
	void setMarginBottom(int value);
	void setFontColorRed(int value);
	void setFontColorGreen(int value);
	void setFontColorBlue(int value);
	void setFont2ColorRed(int value);
	void setFont2ColorGreen(int value);
	void setFont2ColorBlue(int value);
	void setBkgColorRed(int value);
	void setBkgColorGreen(int value);
	void setBkgColorBlue(int value);
	void setSplitterColorRed(int value);
	void setSplitterColorGreen(int value);
	void setSplitterColorBlue(int value);
	void setInitialFontSize(int value);
	void setFontName(String value);
    }

    public interface HotKey
    {
	String getSpecial(String defValue);
	String getCharacter(String defValue);
	boolean getWithControl(boolean defValue);
	boolean getWithShift(boolean defValue);
	boolean getWithAlt(boolean defValue);
    }

    public interface MainMenuSection
    {
	String getTitle(String defValue);
	void setTitle(String value);
	String getUniRefs(String defValue);
	void setUniRefs(String value);
    }

    public interface BackgroundSounds
    {
	String getStarting(String defValue);
	String getPopup(String defValue);
	String getFetching(String defValue);
	String getMainMenu(String defValue);
	String getWifi(String defValue);
	String getSearch(String defValue);
    }

    public interface SoundScheme 
    {
	String getBlocked(String defValue);
	String getCancel(String defValue);
	String getChatMessage(String defValue);
	String getCommanderLocation(String defValue);
	String getCopied(String defValue);
	String getCut(String defValue);
	String getDeleted(String defValue);
	String getDocSection(String defValue);
	String getDone(String defValue);
	String getEmptyLine(String defValue);
	String getEndOfLine(String defValue);
	String getError(String defValue);
	String getEventNotProcessed(String defValue);
	String getFatal(String defValue);
	String getGeneralTime(String defValue);
	String getIntroApp(String defValue);
	String getIntroPopup(String defValue);
	String getIntroRegular(String defValue);
	String getListItem(String defValue);
	String getMainMenuEmptyLine(String defValue);
	String getMainMenuItem(String defValue);
	String getMainMenu(String defValue);
	String getNoApplications(String defValue);
	String getNoContent(String defValue);
	String getNoItemsAbove(String defValue);
	String getNoItemsBelow(String defValue);
	String getNoLinesAbove(String defValue);
	String getNoLinesBelow(String defValue);
	String getOk(String defValue);
	String getParagraph(String defValue);
	String getPaste(String defValue);
	String getRegionPoint(String defValue);
	String getSearch(String defValue);
	String getShutdown(String defValue);
	String getStartup(String defValue);
	String getTermBell(String defValue);
	String getTableCell(String defValue);
	void setBlocked(String value);
	void setCancel(String value);
	void setChatMessage(String value);
	void setCommanderLocation(String value);
	void setCopied(String value);
	void setCut(String value);
	void setDeleted(String value);
	void setDocSection(String value);
	void setDone(String value);
	void setEmptyLine(String value);
	void setEndOfLine(String value);
	void setError(String value);
	void setEventNotProcessed(String value);
	void setFatal(String value);
	void setGeneralTime(String value);
	void setIntroApp(String value);
	void setIntroPopup(String value);
	void setIntroRegular(String value);
	void setListItem(String value);
	void setMainMenuEmptyLine(String value);
	void setMainMenuItem(String value);
	void setMainMenu(String value);
	void setNoApplications(String value);
	void setNoContent(String value);
	void setNoItemsAbove(String value);
	void setNoItemsBelow(String value);
	void setNoLinesAbove(String value);
	void setNoLinesBelow(String value);
	void setOk(String value);
	void setParagraph(String value);
	void setPaste(String value);
	void setRegionPoint(String value);
	void setSearch(String value);
	void setShutdown(String value);
	void setStartup(String value);
	void setTermBell(String value);
	void setTableCell(String value);
    }

    public interface UserInterface
    {
	String getLaunchGreeting(String defValue);
	boolean getFilePopupSkipHidden(boolean defValue);
	boolean getEmptyLineUnderRegularLists(boolean defValue);
	boolean getCyclingRegularLists(boolean defValue);
	boolean getEmptyLineAbovePopupLists(boolean defValue);
	boolean getCyclingPopupLists(boolean defValue);
	void setLaunchGreeting(String value);
	void setFilePopupSkipHidden(boolean value);
	void setEmptyLineUnderRegularLists(boolean value);
	void setCyclingRegularLists(boolean value);
	void setEmptyLineAbovePopupLists(boolean value);
	void setCyclingPopupLists(boolean value);
    }

public interface Braille
{
    boolean getEnabled(boolean defValue);
    void setEnabled(boolean value);
}

    public interface SpeechParams
    {
	int getPitch(int defValue);
	int getRate(int defValue);
	void setPitch(int value);
	void setRate(int value);
    }

    public interface SpeechChannelBase
    {
	String getType(String defValue);
	String getName(String defValue);
	void setType(String value);
	void setName(String value);
    }

    static public InteractionParams createInteractionParams(Registry registry)
    {
	return RegistryProxy.create(registry, keys.interactionParams(), InteractionParams.class);
    }

    static public HotKey createHotKey(Registry registry, String path)
    {
	return RegistryProxy.create(registry, path, HotKey.class);
    }

    static public MainMenuSection createMainMenuSection(Registry registry, String path)
    {
	return RegistryProxy.create(registry, path, MainMenuSection.class);
    }

    static public UserInterface createUserInterface(Registry registry)
    {
	return RegistryProxy.create(registry, keys.ui(), UserInterface.class);
    }

    static public SoundScheme createCurrentSoundScheme(Registry registry)
    {
	return RegistryProxy.create(registry, keys.currentSoundScheme(), SoundScheme.class);
    }

    static public BackgroundSounds createBackgroundSounds(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, BACKGROUND_SOUNDS_PATH, BackgroundSounds.class);
    }


    static public Braille createBraille(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, keys.braille(), Braille.class);
    }

    static public SpeechParams createSpeechParams(Registry registry)
    {
	return RegistryProxy.create(registry, keys.speech(), SpeechParams.class);
    }

    static public SpeechChannelBase createSpeechChannelBase(Registry registry, String path)
    {
	return RegistryProxy.create(registry, path, SpeechChannelBase.class);
    }

    static public FileTypeAppInfo createFileTypeAppInfo(Registry registry, String path)
    {
	return RegistryProxy.create(registry, path, FileTypeAppInfo.class);
    }

    static public PersonalInfo createPersonalInfo(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, PERSONAL_INFO_PATH, PersonalInfo.class);
    }

    static public OsCommand createOsCommand(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notEmpty(path, "path");
    return RegistryProxy.create(registry, path, OsCommand.class);
    }

    static public OsShortcut createOsShortcut(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notEmpty(path, "path");
    return RegistryProxy.create(registry, path, OsShortcut.class);
    }


    static public I18n createI18n(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, I18N_PATH, I18n.class);
    }

    static public String[] getI18nCharsets(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	final String value = createI18n(registry).getCharsets("");
	if (value.trim().isEmpty())
	    return new String[0];
	final LinkedList<String> res = new LinkedList<String>();
	for(String s: value.split(":", -1))
	    if (!s.trim().isEmpty())
		res.add(s.trim());
	return res.toArray(new String[res.size()]);
    }
}
