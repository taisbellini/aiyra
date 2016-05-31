package br.ufrgs.inf.tlbellini.lib;

public class PajeVariableType extends PajeType {


    private PajeColor color;

    public PajeVariableType(String name, String alias, PajeType parent, PajeColor color) {
        super(name, alias, parent);
        this.setColor(color);
    }

    public PajeColor getColor() {
        return color;
    }

    protected void setColor(PajeColor color) {
        this.color = color;
    }

    public PajeTypeNature getNature() {
        return PajeTypeNature.VariableType;
    }

}
