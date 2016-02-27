package br.ufrgs.inf.tlbellini.lib;

public class PajeSetVariableEvent extends PajeVariableEvent {

	public PajeSetVariableEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time, double val) {
		super(event, container, type, time, val);
	}

}
