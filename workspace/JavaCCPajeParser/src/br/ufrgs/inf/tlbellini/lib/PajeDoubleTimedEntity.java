package br.ufrgs.inf.tlbellini.lib;

public class PajeDoubleTimedEntity extends PajeSingleTimedEntity {

    private double endTime;

    public PajeDoubleTimedEntity(PajeContainer container, PajeType type, double time, PajeTraceEvent event) {
        super(container, type, time, event);
        this.endTime = -1;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

}
