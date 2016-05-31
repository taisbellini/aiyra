package br.ufrgs.inf.tlbellini.lib;

public class PajeStateType extends PajeCategorizedType {

    public PajeStateType(String name, String alias, PajeType parent) {
        super(name, alias, parent);
    }

    public PajeTypeNature getNature() {
        return PajeTypeNature.StateType;
    }
}
