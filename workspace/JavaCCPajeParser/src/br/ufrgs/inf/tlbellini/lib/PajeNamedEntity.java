package br.ufrgs.inf.tlbellini.lib;

public class PajeNamedEntity extends PajeDoubleTimedEntity {

    private String name;

    public PajeNamedEntity(PajeContainer container, PajeType type, double time, String name, PajeTraceEvent event) {
        super(container, type, time, event);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
