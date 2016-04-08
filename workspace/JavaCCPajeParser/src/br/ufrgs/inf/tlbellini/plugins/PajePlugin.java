package br.ufrgs.inf.tlbellini.plugins;

import br.ufrgs.inf.tlbellini.lib.*;

public abstract class PajePlugin {
	
	public static int MAXLINES;
	

	public abstract void addType(PajeType newType);
	public abstract void addValue(PajeValue value);
	public abstract void addNewContainer(PajeContainer newContainer);
	public abstract void addDestroyedContainer(PajeContainer pajeContainer);
	public abstract void addState(PajeUserState newState);
	public abstract void pushState(PajeUserState newState);
	public abstract void popState(PajeUserState state);
	public abstract void addLink(PajeUserLink link);
	public abstract void addVar(PajeEntity first, PajeEntity last, PajeUserVariable newValue);
	public abstract void setVar(PajeUserVariable var);
	public abstract void addEvent(PajeUserEvent event);
	public abstract void finish();
	public abstract void endSimulation();

}
