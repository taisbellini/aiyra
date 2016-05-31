package br.ufrgs.inf.tlbellini.lib;

public class PajeSingleTimedEntity extends PajeEntity {

    private double startTime;

    public PajeSingleTimedEntity(PajeContainer container, PajeType type, double time, PajeTraceEvent event) {
        super(container, type, event);
        this.setStartTime(time);
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    //to check time order
    public double getEndTime() {
        return -1;
    }

}
