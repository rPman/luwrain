//Reads and saves

package org.luwrain.settings;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;

class UserInterface extends FormArea implements SectionArea
{
    private final ControlPanel controlPanel;
    private final Luwrain luwrain;
    private final Settings.UserInterface sett;

    UserInterface(ControlPanel controlPanel)
    {
	super(new DefaultControlEnvironment(controlPanel.getCoreInterface()), controlPanel.getCoreInterface().i18n().getStaticStr("CpUserInterfaceGeneral"));
	NullCheck.notNull(controlPanel, "controlPanel");
	this.controlPanel = controlPanel;
	this.luwrain = controlPanel.getCoreInterface();
	this.sett = Settings.createUserInterface(luwrain.getRegistry());
	fillForm();
    }

    private void fillForm()
    {
	addEdit("launch-greeting", luwrain.i18n().getStaticStr("CpLaunchingGreetingText"), sett.getLaunchGreeting(""));
	//	addCheckbox("file-popup-skip-hidden", "Исключать скрытые файлы в о всплывающих окнах:", settings.getFilePopupSkipHidden(false));
	//	addCheckbox("empty-line-under-regular-lists", "Добавлять пустую строку в конце списков:", settings.getEmptyLineUnderRegularLists(false));
	//	addCheckbox("empty-line-above-popup-lists", "Добавлять пустую строку в начало всплывающих списков:", settings.getEmptyLineAbovePopupLists(false));
	//	addCheckbox("cycling-regular-lists", "Зацикливать навигацию по спискам:", settings.getCyclingRegularLists(false));
	//	addCheckbox("cycling-popup-lists", "Зацикливать навигацию по всплывающим спискам:", settings.getCyclingPopupLists(false));
    }

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onKeyboardEvent(event))
	    return true;
	return super.onKeyboardEvent(event);
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onEnvironmentEvent(event))
	    return true;
	return super.onEnvironmentEvent(event);
    }

    @Override public boolean saveSectionData()
    {
	sett.setLaunchGreeting(getEnteredText("launch-greeting"));
	return true;
    }

    boolean save()
    {
/*
	final Luwrain luwrain = controlPanel.getCoreInterface();
	final Registry registry = luwrain.getRegistry();
	if (!registry.setString(registryKeys.desktopIntroductionFile(), getEnteredText("desktop-introduction-file")))
	    return false;
	if (!registry.setString(registryKeys.launchGreeting(), getEnteredText("launch-greeting")))
	    return false;
	return true;
*/
	return false;
    }

    static UserInterface create(ControlPanel controlPanel)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	return new UserInterface(controlPanel);
    }
}
