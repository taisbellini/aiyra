package br.ufrgs.inf.tlbellini.lib;

public class PajePushStateEvent extends PajeStateEvent {

	public PajePushStateEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time, PajeValue value) {
		super(event, container, type, time);
		this.setValue(value);
	}

}
