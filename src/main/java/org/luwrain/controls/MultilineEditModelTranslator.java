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

import org.luwrain.core.*;

// Expects that hot point is not related to the content 
// Hot point position may be adjusted to the content changes only on endEditTrans 

public class MultilineEditModelTranslator implements MultilineEditModel
{
    protected final MutableLines lines;
    protected final HotPointControl hotPoint;
    protected String tabSeq = "\t";

    public MultilineEditModelTranslator(MutableLines lines, HotPointControl hotPoint)
    {
	this.lines = lines;
	this.hotPoint = hotPoint;
	NullCheck.notNull(lines, "lines");
	NullCheck.notNull(hotPoint, "hotPoint");
    }

    public MultilineEditModelTranslator(MutableLines lines, HotPointControl hotPoint,
					String tabSeq)
    {
	this.lines = lines;
	this.hotPoint = hotPoint;
	this.tabSeq = tabSeq;
	NullCheck.notNull(lines, "lines");
	NullCheck.notNull(hotPoint, "hotPoint");
	NullCheck.notNull(tabSeq, "tabSeq");
    }

    @Override public int getHotPointX()
    {
	return hotPoint.getHotPointX();
    }

    @Override public int getHotPointY()
    {
	return hotPoint.getHotPointY();
    }

    @Override public int getLineCount()
    {
	return lines.getLineCount();
    }

    @Override public String getLine(int index)
    {
	return lines.getLine(index);
    }

    @Override public String getTabSeq()
    {
	return tabSeq;
    }

    @Override public char deleteChar(int pos, int lineIndex)
    {
	final String line = lines.getLine(lineIndex);
	if (line == null ||
	    pos < 0 || pos >= line.length())
	    return '\0';
	beginEditTrans();
	lines.setLine(lineIndex, line.substring(0, pos) + line.substring(pos + 1));
	if (hotPoint.getHotPointY() == lineIndex && hotPoint.getHotPointX() > pos)
	    hotPoint.setHotPointX(hotPoint.getHotPointX() - 1);
	endEditTrans();
	return line.charAt(pos);
    }

    @Override public boolean deleteRegion(int fromX, int fromY,
					  int toX, int toY)
    {
	if (lines.getLineCount() < 1 ||
	    fromY > toY ||
	    (fromY == toY && fromX > toX) ||
	    toY >= lines.getLineCount())
	    return false;
	if (fromY == toY)
	{
	    final String line = lines.getLine(fromY);
	    if (line == null || line.isEmpty())
		return false;
	    final int fromPos = fromX < line.length()?fromX:line.length();
	    final int toPos = toX < line.length()?toX:line.length();
	    if (fromPos >= toPos)
		return false;
	    beginEditTrans();
	    lines.setLine(fromY, line.substring(0, fromPos) + line.substring(toPos));
	    if (hotPoint.getHotPointY() == fromY)
	    {
		if (hotPoint.getHotPointX() >= fromPos && hotPoint.getHotPointX() < toPos)
		    hotPoint.setHotPointX(fromPos); else
		    if (hotPoint.getHotPointX() >= toPos)
			hotPoint.setHotPointX(hotPoint.getHotPointX() - (toPos - fromPos));
	    }
	    endEditTrans();
	    return true;
	}
	final String firstLine = lines.getLine(fromY);
	if (firstLine == null)
	    return false;
	final int fromPos = fromX < firstLine.length()?fromX:firstLine.length();
	final String endingLine = lines.getLine(toY);
	if (endingLine == null)
	    return false;
	final int toPos = toX <endingLine.length()?toX:endingLine.length();
	beginEditTrans();
	lines.setLine(fromY, firstLine.substring(0, fromPos) + endingLine.substring(toPos));
	for(int i = fromY + 1;i <= toY;++i)
	    lines.removeLine(fromY + 1);
	if ((hotPoint.getHotPointY() == fromY && hotPoint.getHotPointX() >= fromPos) ||
	    (hotPoint.getHotPointY() > fromY && hotPoint.getHotPointY() < toY) ||
	    (hotPoint.getHotPointY() == toY && hotPoint.getHotPointX() < toX))
	{
	    hotPoint.setHotPointY(fromY);
	    hotPoint.setHotPointX(fromX);
	} else
	    if (hotPoint.getHotPointY() == toY && hotPoint.getHotPointX() >= toPos)
	    {
		hotPoint.setHotPointY(fromY);
		hotPoint.setHotPointX(hotPoint.getHotPointX() - toPos + fromPos);
	    } else
		if (hotPoint.getHotPointY() > toY)
		    hotPoint.setHotPointY(hotPoint.getHotPointY() - toY + fromY);
	endEditTrans();
	return true;
    }

    @Override public boolean insertRegion(int x, int y, String[] content)
    {
	NullCheck.notNullItems(content, "content");
	final String[] text = content;
	if (text.length < 1)
	    return true;
	final String firstLine = text[0];
	final String lastLine = text[text.length - 1];
	if (y >= lines.getLineCount())
	{
	    final boolean needToMoveHotPoint = hotPoint.getHotPointY() > lines.getLineCount();
	    beginEditTrans();
	    while(lines.getLineCount() < y)
		lines.addLine("");
	    if (x > 0)
		lines.addLine(TextUtils.sameCharString(' ', x) + text[0]); else
		lines.addLine(text[0]);
	    for(int i = 1;i < text.length;++i)
		lines.addLine(text[i]);
	    if (needToMoveHotPoint)
		hotPoint.setHotPointX(text[text.length - 1].length());
	    hotPoint.setHotPointY(lines.getLineCount() - 1);
	    endEditTrans();
	    return true;
	}
	if (text.length == 1)
	{
	    String line = lines.getLine(y);
	    if (line == null)
		line = "";
	    while (line.length() < x)
		line += ' ';
	    final boolean needToMoveHotPoint = (hotPoint.getHotPointY() == y && x >= hotPoint.getHotPointX());
	    beginEditTrans();
	    lines.setLine(y, line.substring(0, x) + firstLine + line.substring(x));
	    if (needToMoveHotPoint)
		hotPoint.setHotPointX(hotPoint.getHotPointX() + firstLine.length());
	    endEditTrans();
	    return true;
	}
	//New text has multiple lines
	String line = lines.getLine(y);
	if (line == null)
	    line = "";
	while (line.length() < x)
	    line += ' ';
	beginEditTrans();
	lines.setLine(y, line.substring(0, x) + text[0]);
	for(int i = 1;i < text.length - 1;++i)
	    lines.insertLine(y + i, text[i]);
	lines.insertLine(y+ text.length - 1, text[text.length - 1] + line.substring(x));
	if (hotPoint.getHotPointY() > y)
	    hotPoint.setHotPointY(hotPoint.getHotPointY() + text.length - 1); else
	    if (hotPoint.getHotPointY() == y && hotPoint.getHotPointX() >= x)
	    {
		hotPoint.setHotPointY(y + text.length - 1);
		hotPoint.setHotPointX(hotPoint.getHotPointX() - x + lastLine.length());
	    }
	endEditTrans();
	return true;
    }

    @Override public void insertChars(int pos, int lineIndex, String str)
    {
	beginEditTrans();
	while(lineIndex >= lines.getLineCount())
	    lines.addLine("");
	String line = lines.getLine(lineIndex);
	if (line == null)
	    line = "";
	while(line.length() < pos)
	    line += " ";
	lines.setLine(lineIndex, line.substring(0, pos) + (str != null?str:"") + line.substring(pos));
	if (hotPoint.getHotPointY() == lineIndex && hotPoint.getHotPointX() >= pos)
	    hotPoint.setHotPointX(hotPoint.getHotPointX() + (str != null?str.length():0));
	endEditTrans();
    }

    @Override public void mergeLines(int firstLineIndex)
    {
	if (firstLineIndex < 0 || firstLineIndex + 1 >= lines.getLineCount())
	    return;
	beginEditTrans();
	final int origLineLen = lines.getLine(firstLineIndex).length();
	lines.setLine(firstLineIndex, lines.getLine(firstLineIndex) + lines.getLine(firstLineIndex + 1));
	lines.removeLine(firstLineIndex + 1);
	if (hotPoint.getHotPointY() == firstLineIndex + 1)
	{
	    hotPoint.setHotPointY(hotPoint.getHotPointY() - 1);
	    hotPoint.setHotPointX(hotPoint.getHotPointX() + origLineLen);
	} else
	    if (hotPoint.getHotPointY() > firstLineIndex + 1)
		hotPoint.setHotPointY(hotPoint.getHotPointY() - 1);
	endEditTrans();
    }

    @Override public String splitLines(int pos, int lineIndex)
    {
	if (lineIndex < 0 || lineIndex >= lines.getLineCount())
	    return "";
	beginEditTrans();
	String line = lines.getLine(lineIndex);
	if (line == null)
	    line = "";
	while (line.length() < pos)
	    line += ' ';
	lines.setLine(lineIndex, line.substring(0, pos));
	lines.insertLine(lineIndex + 1, line.substring(pos));
	if (hotPoint.getHotPointY() == lineIndex && hotPoint.getHotPointX() >= pos)
	{
	    hotPoint.setHotPointY(lineIndex + 1);
	    hotPoint.setHotPointX(hotPoint.getHotPointX() - pos);
	} else
	    if (hotPoint.getHotPointY() > lineIndex)
		hotPoint.setHotPointY(hotPoint.getHotPointY() + 1);
	endEditTrans();
	return lines.getLine(lineIndex + 1);
    }

    private void beginEditTrans()
    {
	lines.beginLinesTrans();
	hotPoint.beginHotPointTrans();
    }

    private void endEditTrans()
    {
	hotPoint.endHotPointTrans();
	lines.endLinesTrans();
    }
}
