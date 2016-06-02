
package org.luwrain.settings;

import org.luwrain.core.NullCheck;
import org.luwrain.cpanel.*;

public class StandardFactory implements Factory
{
    static private final String ELEMENT_PREFIX = "org.luwrain.settings.StandardFactory:";

    static private final Element hotKeys = new SimpleElement(StandardElements.KEYBOARD, ELEMENT_PREFIX + "HotKeys");
    static private final Element fileTypes = new SimpleElement(StandardElements.ROOT, ELEMENT_PREFIX + "FileTypes");
    static private final Element mainMenu = new SimpleElement(StandardElements.UI, ELEMENT_PREFIX + "MainMenu");
    static private final Element sysInfo = new SimpleElement(StandardElements.ROOT, ELEMENT_PREFIX + "SysInfo");
    static private final Element speechParams = new SimpleElement(StandardElements.SPEECH, ELEMENT_PREFIX + "SpeechParams");
    static private final Element speechChannels = new SimpleElement(StandardElements.SPEECH, ELEMENT_PREFIX + "SpeechChannels");
    static private final Element speechCurrent = new SimpleElement(StandardElements.SPEECH, ELEMENT_PREFIX + "SpeechCurrent");

    static private final Element soundsList = new SimpleElement(StandardElements.SOUNDS, ELEMENT_PREFIX + "SoundsList");
    static private final Element soundSchemes = new SimpleElement(StandardElements.SOUNDS, ELEMENT_PREFIX + "SoundSchemes");

    @Override public Element[] getElements()
    {
	return new Element[]{
	    StandardElements.ROOT,
	    StandardElements.APPLICATIONS,
	    StandardElements.UI,
	    fileTypes,
	    StandardElements.NETWORK,
	    StandardElements.KEYBOARD,
	    StandardElements.SOUNDS,
	    StandardElements.SPEECH,
	    StandardElements.BRAILLE,
	    sysInfo,
	    StandardElements.HARDWARE,
	    StandardElements.EXTENSIONS,
	    StandardElements.WORKERS,
	    hotKeys,
	    mainMenu,
	    speechParams,
	    speechCurrent,
	    speechChannels,
	    soundSchemes,
	    soundsList,
	};
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	return new Element[0];
    }

    @Override public org.luwrain.cpanel.Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");

	//Standard elements
	if (el.equals(StandardElements.ROOT))
	    return new SimpleSection(StandardElements.ROOT, "Панель управления");
	if (el.equals(StandardElements.APPLICATIONS))
	    return new SimpleSection(StandardElements.APPLICATIONS, "Приложения");
	if (el.equals(StandardElements.KEYBOARD))
	    return new SimpleSection(StandardElements.KEYBOARD, "Клавиатура");
	if (el.equals(StandardElements.SOUNDS))
	    return new SimpleSection(StandardElements.SOUNDS, "Звук");
	if (el.equals(StandardElements.BRAILLE))
	    return new BrailleSection();
	if (el.equals(StandardElements.SPEECH))
	    return new SimpleSection(StandardElements.SPEECH, "Речь");
	if (el.equals(StandardElements.NETWORK))
	    return new SimpleSection(StandardElements.NETWORK, "Сеть");
	if (el.equals(StandardElements.HARDWARE))
	    return new SimpleSection(StandardElements.HARDWARE, "Оборудование");
	if (el.equals(StandardElements.UI))
	    return new SimpleSection(StandardElements.UI, "Интерфейс пользователя");
	if (el.equals(StandardElements.EXTENSIONS))
	    return new SimpleSection(StandardElements.EXTENSIONS, "Расширения");
	if (el.equals(StandardElements.WORKERS))
	    return new SimpleSection(StandardElements.WORKERS, "Фоновые задачи");

	//Other sections
	if (el.equals(hotKeys))
	    return new SimpleSection(hotKeys, "Общие горячие клавиши", (controlPanel)->HotKeys.create(controlPanel.getCoreInterface()));
	if (el.equals(fileTypes))
	    return new SimpleSection(fileTypes, "Типы файлов", (controlPanel)->FileTypes.create(controlPanel.getCoreInterface()));
	if (el.equals(mainMenu))
	    return new SimpleSection(mainMenu, "Главное меню");
	if (el.equals(sysInfo))
	    return new SimpleSection(sysInfo, "Информация о системе", (controlPanel)->SysInfo.create(controlPanel.getCoreInterface()));

	if (el.equals(speechParams))
	    return new SimpleSection(speechParams, "Основные параметры", (controlPanel)->SpeechParams.create(controlPanel.getCoreInterface()));
	if (el.equals(speechCurrent))
	    return new SimpleSection(speechCurrent, "Загруженные каналы", (controlPanel)->SpeechCurrent.create(controlPanel.getCoreInterface()));
	if (el.equals(speechChannels))
	    return new SimpleSection(speechChannels, "Настройка каналов");
	if (el.equals(soundsList))
	    return new SimpleSection(soundsList, "Звуки системных событий", (controlPanel)->SoundsList.create(controlPanel.getCoreInterface()));
	if (el.equals(soundSchemes))
	    return new SimpleSection(soundSchemes, "Звуковые схемы", (controlPanel)->SoundSchemes.create(controlPanel.getCoreInterface()));
	return null;
    }
}
