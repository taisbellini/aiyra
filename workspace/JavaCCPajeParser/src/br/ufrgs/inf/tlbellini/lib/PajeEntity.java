package br.ufrgs.inf.tlbellini.lib;

import java.util.Map;

public class PajeEntity extends PajeObject {


    private PajeContainer container;
    private PajeType type;
    Map<String, String> extraFields;

    public PajeEntity(PajeContainer container, PajeType type2, PajeTraceEvent event) {
        this.setContainer(container);
        this.setType(type2);
        //TODO add event extra fields
    }


    public PajeType getType() {
        return type;
    }


    public void setType(PajeType type) {
        this.type = type;
    }


    public PajeContainer getContainer() {
        return container;
    }


    public void setContainer(PajeContainer container) {
        this.container = container;
    }


}
