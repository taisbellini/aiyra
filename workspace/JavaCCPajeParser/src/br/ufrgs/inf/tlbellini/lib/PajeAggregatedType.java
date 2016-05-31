package br.ufrgs.inf.tlbellini.lib;

public class PajeAggregatedType {

    private PajeValue aggregatedValue;
    private PajeType aggregatedType;


    public PajeValue getAggregatedValue() {
        return aggregatedValue;
    }
    public void setAggregatedValue(PajeValue aggregatedValue) {
        this.aggregatedValue = aggregatedValue;
    }
    public PajeType getAggregatedType() {
        return aggregatedType;
    }
    public void setAggregatedType(PajeType aggregatedType) {
        this.aggregatedType = aggregatedType;
    }
}
