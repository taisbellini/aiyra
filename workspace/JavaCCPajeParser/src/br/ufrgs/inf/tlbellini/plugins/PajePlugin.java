package br.ufrgs.inf.tlbellini.plugins;

import br.ufrgs.inf.tlbellini.lib.*;

public abstract class PajePlugin {
	
	public static int MAXLINES;
	
	
	public abstract void newType(PajeType newType);
	public abstract void newValue(PajeValue value);
	public abstract void newCreatedContainer(PajeContainer newContainer);
	public abstract void destroyedContainer(PajeContainer pajeContainer);
	public abstract void setState(PajeUserState newState);
	public abstract void pushState(PajeUserState newState);
	public abstract void popState(PajeUserState state);
	public abstract void startLink(PajeUserLink link);
	public abstract void endLink(PajeUserLink link);
	public abstract void newCompletedLink(PajeUserLink link);
	public abstract void updateVar(PajeEntity first, PajeEntity last, PajeUserVariable newValue);
	public abstract void setVar(PajeEntity first, PajeEntity last, PajeUserVariable var);
	public abstract void newEvent(PajeUserEvent event);
	public abstract void finish();

}
