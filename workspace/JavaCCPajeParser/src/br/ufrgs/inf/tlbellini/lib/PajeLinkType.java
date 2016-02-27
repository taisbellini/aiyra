package br.ufrgs.inf.tlbellini.lib;

public class PajeLinkType extends PajeCategorizedType {

	private PajeType startType;
	private PajeType endType;
	
	public PajeLinkType(String name, String alias, PajeType parent, PajeType start, PajeType end){
		super(name, alias, parent);
		this.setStartType(start);
		this.setEndType(end);
	}

	public PajeType getStartType() {
		return startType;
	}

	public void setStartType(PajeType startType) {
		this.startType = startType;
	}

	public PajeType getEndType() {
		return endType;
	}

	public void setEndType(PajeType endType) {
		this.endType = endType;
	}
	
	public PajeTypeNature getNature(){
		return PajeTypeNature.LinkType;
	}
	
	
}
