package br.ufrgs.inf.tlbellini.lib;

public class PajeSetStateEvent extends PajeStateEvent {

    public PajeSetStateEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time, PajeValue value) {
        super(event, container, type, time);
        this.setValue(value);
    }

}
