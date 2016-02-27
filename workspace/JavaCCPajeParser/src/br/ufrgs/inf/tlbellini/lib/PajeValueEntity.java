package br.ufrgs.inf.tlbellini.lib;

public class PajeValueEntity extends PajeDoubleTimedEntity {
	
	private PajeValue value;

	public PajeValueEntity(PajeContainer container,PajeType type, double startTime,PajeValue value,PajeTraceEvent event) {
		super(container, type, startTime, event);
		this.setValue(value);
		
	}

	public PajeValue getValue() {
		return value;
	}

	public void setValue(PajeValue value) {
		this.value = value;
	}

}
