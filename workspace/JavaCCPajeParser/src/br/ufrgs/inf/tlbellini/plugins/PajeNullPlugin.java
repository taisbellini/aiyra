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
		System.out.println("null option");
	}

	@Override
	public void addType(PajeType newType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addValue(PajeValue value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewContainer(PajeContainer newContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDestroyedContainer(PajeContainer pajeContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addState(PajeUserState newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushState(PajeUserState newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popState(PajeUserState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLink(PajeUserLink link) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addVar(PajeEntity first, PajeUserVariable newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEvent(PajeUserEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
