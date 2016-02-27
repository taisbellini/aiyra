package br.ufrgs.inf.tlbellini.lib;

import java.util.ArrayList;
import java.util.Vector;

public class PajeTraceEvent extends PajeObject {
	
	private int line;
	private ArrayList<String> fields = new ArrayList<String>();
	private PajeEventDefinition pajeEventDef;
	
	public PajeTraceEvent(){
		this.setLine(-1);
	};
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	
	public void setPajeEventDef(int id, ArrayList<PajeEventDefinition> defs){
		for(PajeEventDefinition def: defs)
		{
			if(def.uniqueID == id)
				this.pajeEventDef = def;
		}
	}
	public PajeEventDefinition getPajeEventDef()
	{
		return this.pajeEventDef;
	}
	
	public void setFields(ArrayList<String> f){
		this.fields.addAll(f);
	}
	
	public String valueForField(PajeFieldName fieldName){
		int index = pajeEventDef.indexForField(fieldName);
		if(index >= 0)
			return fields.get(index);
		else
			return "";
	}
	
	

}
