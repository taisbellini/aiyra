package br.ufrgs.inf.tlbellini.lib;

public class PajeLinkEvent extends PajeCategorizedEvent {
	
	private PajeContainer linkedContainer;
	
	private String key;

	public PajeLinkEvent(PajeTraceEvent event, PajeContainer container, PajeContainer linkedContainer, PajeType type, double time, PajeValue val, String key) {
		super(event, container, type, time);
		this.setKey(key);
		this.setLinkedContainer(linkedContainer);
		this.setValue(val);
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public PajeContainer getLinkedContainer() {
		return linkedContainer;
	}


	public void setLinkedContainer(PajeContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
	}

}
