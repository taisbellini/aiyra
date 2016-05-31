package br.ufrgs.inf.tlbellini.lib;

public class PajeEndLinkEvent extends PajeLinkEvent {

    public PajeEndLinkEvent(PajeTraceEvent event, PajeContainer container, PajeContainer endContainer, PajeType type, double time, PajeValue value, String key) {
        super(event, container, endContainer, type, time, value, key);
    }

}
