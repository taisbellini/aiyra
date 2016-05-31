package br.ufrgs.inf.tlbellini.lib;

public class PajeStartLinkEvent extends PajeLinkEvent {

    public PajeStartLinkEvent(PajeTraceEvent event, PajeContainer container, PajeContainer linkedContainer, PajeType type, double time, PajeValue val, String key) {
        super(event, container, linkedContainer, type, time, val, key);
    }

}
