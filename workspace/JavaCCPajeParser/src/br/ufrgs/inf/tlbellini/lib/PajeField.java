package br.ufrgs.inf.tlbellini.lib;

public class PajeField {

    private PajeFieldName field;
    private PajeFieldType type;

    public PajeField(PajeFieldName field, PajeFieldType type) {
        this.setField(field);
        this.setType(type);
    }

    public PajeField() {
    }

    public PajeFieldName getField() {
        return field;
    }

    public void setField(PajeFieldName field) {
        this.field = field;
    }

    public PajeFieldType getType() {
        return type;
    }

    public void setType(PajeFieldType type) {
        this.type = type;
    }

}
