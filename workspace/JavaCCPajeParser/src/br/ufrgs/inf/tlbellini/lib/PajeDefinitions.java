package br.ufrgs.inf.tlbellini.lib;

import java.util.HashMap;
import java.util.Map;

public class PajeDefinitions {
	
	Map<String, PajeFieldName> fieldNameToID = new HashMap<String, PajeFieldName>();
	
	//obrigatorios??
	
	public PajeFieldName idFromFieldName(String field){
		return this.fieldNameToID.get(field);
	}

}
