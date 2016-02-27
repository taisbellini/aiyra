package br.ufrgs.inf.tlbellini.lib;

public class PajeUserEvent extends PajeSingleTimedEntity {
	
	private PajeValue value;

	public PajeUserEvent(PajeContainer container, PajeType type, double time, PajeValue value, PajeTraceEvent event) {
		super(container, type, time, event);
		this.setValue(value);
	}

	public PajeValue getValue() {
		return value;
	}

	public void setValue(PajeValue value) {
		this.value = value;
	}

}
