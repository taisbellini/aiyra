package br.ufrgs.inf.tlbellini.lib;

import java.util.ArrayList;

public class PajeData extends PajeObject {

    public ArrayList<String> bytes; //array list? ou string?
    public int length;
    public int capacity;

    PajeData(int capacity) {
        this.capacity = capacity;
        // li que a medida que vai incrementando ele ocupa mt espaco entao eh melhor inicializar (1)
        this.bytes = new ArrayList<String>(capacity);
    }

    public void increaseCapacity(int increase) {
        this.capacity += increase;
        bytes.ensureCapacity(this.capacity);
    }

}
