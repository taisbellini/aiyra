package br.ufrgs.inf.tlbellini.lib;

import java.util.ArrayList;

public class PajeComponent extends PajeObject {

    private ArrayList<PajeComponent> outputComponent;
    private PajeComponent inputComponent;


    public void setOutputComponent(PajeComponent outputComponent) {
        this.outputComponent.add(outputComponent);;
    }


    public void setInputComponent(PajeComponent inputComponent) {
        this.inputComponent = inputComponent;
    }

}
