package br.ufrgs.inf.tlbellini.lib;

public class PajeType extends PajeObject {
	
	protected String name;
	protected String alias;
	protected PajeType parent;
	protected int depth;
	
	public PajeType(String name, String alias, PajeType parent){
		this.name = name;
		this.alias = alias;
		if (parent != null){
			this.depth = parent.depth +1;
		}else{
			this.depth = 0;
		}
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public String getId(){
		if(alias.isEmpty()){
			return name;
		}else{
			return alias;
		}
	}
	
	public PajeType getParent(){
		return parent;
	}
	
	public boolean isCategorizedType(){
		return false;
	}
	
	public PajeTypeNature getNature(){
		return PajeTypeNature.UndefinedType;
	}
	
	public boolean hasAncestral(PajeType tp){
		if(this.getParent().equals(tp))
			return true;
		else
			return this.getParent().hasAncestral(tp);
	}

}
