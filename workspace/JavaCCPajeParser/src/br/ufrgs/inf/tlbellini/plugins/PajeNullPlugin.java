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
	public void newType(PajeType newType) {
		
	}

	@Override
	public void newValue(PajeValue value) {
		
	}

	@Override
	public void newCreatedContainer(PajeContainer newContainer) {
		
	}

	@Override
	public void destroyedContainer(PajeContainer pajeContainer) {
		
	}

	@Override
	public void setState(PajeUserState newState) {
		
	}

	@Override
	public void pushState(PajeUserState newState) {
		
	}

	@Override
	public void popState(PajeUserState state) {
		
	}

	@Override
	public void newCompletedLink(PajeUserLink link) {
		
	}

	@Override
	public void updateVar(PajeEntity first, PajeEntity last, PajeUserVariable newValue) {
		
	}

	@Override
	public void newEvent(PajeUserEvent event) {
		
	}

	@Override
	public void finish() {
		
	}

	@Override
	public void setVar(PajeEntity first, PajeEntity last, PajeUserVariable var) {
		
	}

	@Override
	public void startLink(PajeUserLink link) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endLink(PajeUserLink link) {
		// TODO Auto-generated method stub
		
	}

}
