package br.ufrgs.inf.tlbellini.lib;

public class PajeUserVariable extends PajeDoubleTimedEntity {
	
	private double value;

	public PajeUserVariable(PajeContainer container, PajeType type, double time, double value, PajeTraceEvent event) {
		super(container, type, time, event);
		this.setValue(value);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void addValue(double addVal) {
		this.value += addVal;	
	}
	
	public void subValue(double subVal){
		this.value -= subVal;
	}

}
