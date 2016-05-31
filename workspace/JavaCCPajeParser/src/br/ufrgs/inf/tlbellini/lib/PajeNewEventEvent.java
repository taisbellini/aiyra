package br.ufrgs.inf.tlbellini.lib;

public class PajeNewEventEvent extends PajeEventEvent {

    public PajeNewEventEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time, PajeValue value) {
        super(event, container, type, time);
        this.setValue(value);
    }

}
