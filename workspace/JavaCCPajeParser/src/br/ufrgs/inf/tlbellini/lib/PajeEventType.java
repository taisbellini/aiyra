package br.ufrgs.inf.tlbellini.lib;

public class PajeEventType extends PajeCategorizedType {

    public PajeEventType(String name, String alias, PajeType parent) {
        super(name, alias, parent);
    }

    public PajeTypeNature getNature() {
        return PajeTypeNature.EventType;
    }

}
