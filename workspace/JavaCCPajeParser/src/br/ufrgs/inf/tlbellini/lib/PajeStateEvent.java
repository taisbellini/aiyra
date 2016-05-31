package br.ufrgs.inf.tlbellini.lib;

public class PajeStateEvent extends PajeCategorizedEvent {

    public PajeStateEvent(PajeTraceEvent event, PajeContainer container, PajeType type, double time) {
        super(event, container, type, time);
    }

}
