package br.ufrgs.inf.tlbellini.lib;

import java.util.HashMap;
import java.util.Map;

public class PajeCategorizedType extends PajeType {
	
	private Map<String, PajeValue> values = new HashMap<String, PajeValue>();
	private Map<String, PajeColor> colors = new HashMap<String, PajeColor>();

	public PajeCategorizedType(String name, String alias, PajeType parent) {
		super(name, alias, parent);
	}
	
	public boolean isCategorizedType(){
		return true;
	}

	Map<String, PajeValue> getValues() {
		return values;
	}

	public Map<String, PajeColor> getColors() {
		return colors;
	}

	public void addValue(String name, String alias, PajeColor color){
		PajeValue newValue = new PajeValue(name, alias, this, color); 
		this.getValues().put(newValue.getId(), newValue);
		this.getColors().put(newValue.getId(), color);
	}
	
	public void addColor(String newKey, PajeColor newColor){
		this.getColors().put(newKey, newColor);
	}
	
	public boolean hasValueForIdentifier(String key){
		if(values.containsKey(key))
			return true;
		else
			return false;
	}

	public PajeValue valueForIdentifier(String key){
		if(hasValueForIdentifier(key))
			return values.get(key);
		else
			return null;
	}
		
	

}
