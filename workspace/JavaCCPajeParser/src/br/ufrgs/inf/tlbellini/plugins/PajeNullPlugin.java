package br.ufrgs.inf.tlbellini.plugins;

import br.ufrgs.inf.tlbellini.lib.PajeContainer;
import br.ufrgs.inf.tlbellini.lib.PajeEntity;
import br.ufrgs.inf.tlbellini.lib.PajeType;
import br.ufrgs.inf.tlbellini.lib.PajeUserEvent;
import br.ufrgs.inf.tlbellini.lib.PajeUserLink;
import br.ufrgs.inf.tlbellini.lib.PajeUserState;
import br.ufrgs.inf.tlbellini.lib.PajeUserVariable;
import br.ufrgs.inf.tlbellini.lib.PajeValue;

public class PajeNullPlugin extends PajePlugin {
	
	public PajeNullPlugin(){
	}

	@Override
	public void addType(PajeType newType) {
		
	}

	@Override
	public void addValue(PajeValue value) {
		
	}

	@Override
	public void addNewContainer(PajeContainer newContainer) {
		
	}

	@Override
	public void addDestroyedContainer(PajeContainer pajeContainer) {
		
	}

	@Override
	public void addState(PajeUserState newState) {
		
	}

	@Override
	public void pushState(PajeUserState newState) {
		
	}

	@Override
	public void popState(PajeUserState state) {
		
	}

	@Override
	public void addLink(PajeUserLink link) {
		
	}

	@Override
	public void addVar(PajeEntity first, PajeEntity last, PajeUserVariable newValue) {
		
	}

	@Override
	public void addEvent(PajeUserEvent event) {
		
	}

	@Override
	public void finish() {
		
	}

	@Override
	public void setVar(PajeUserVariable var) {
		// TODO Auto-generated method stub
		
	}

}
