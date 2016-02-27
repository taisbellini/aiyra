package br.ufrgs.inf.tlbellini.lib;

public class PajeUserLink extends PajeValueEntity {
	
	private String key;
	private PajeContainer startContainer;
	private PajeContainer endContainer;

	public PajeUserLink(PajeContainer container, PajeType type, double time, PajeValue value, String key, PajeContainer startContainer, PajeTraceEvent event) {
		super(container, type, time, value, event);
		this.setKey(key);
		this.setStartContainer(startContainer);
		this.setContainer(container);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public PajeContainer getStartContainer() {
		return startContainer;
	}

	public void setStartContainer(PajeContainer startContainer) {
		this.startContainer = startContainer;
	}

	public PajeContainer getEndContainer() {
		return endContainer;
	}

	public void setEndContainer(PajeContainer endContainer) {
		this.endContainer = endContainer;
	}

}
