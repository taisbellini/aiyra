package br.ufrgs.inf.tlbellini.lib;

public class PajeEvent extends PajeObject {

    private PajeTraceEvent event;
    private PajeContainer container;
    private PajeType type;
    private double time;

    public PajeEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time) {
        this.setEvent(event);
        this.setContainer(container);
        this.setType(type);
        this.setTime(time);
    }

    public PajeTraceEvent getEvent() {
        return event;
    }

    public void setEvent(PajeTraceEvent event) {
        this.event = event;
    }

    public PajeType getType() {
        return type;
    }

    public void setType(PajeType type) {
        this.type = type;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    private PajeContainer getContainer() {
        return container;
    }

    private void setContainer(PajeContainer container) {
        this.container = container;
    }

}
