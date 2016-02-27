package br.ufrgs.inf.tlbellini.lib;

public class PajeResetStateEvent extends PajeStateEvent {

	public PajeResetStateEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time) {
		super(event, container, type, time);
	}

}
