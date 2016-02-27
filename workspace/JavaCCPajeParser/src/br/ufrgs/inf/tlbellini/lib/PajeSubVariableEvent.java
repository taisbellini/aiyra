package br.ufrgs.inf.tlbellini.lib;

public class PajeSubVariableEvent extends PajeVariableEvent {

	public PajeSubVariableEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time, double val) {
		super(event, container, type, time, val);
	}

}
