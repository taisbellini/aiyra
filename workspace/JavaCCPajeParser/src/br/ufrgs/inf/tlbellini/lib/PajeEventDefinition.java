package br.ufrgs.inf.tlbellini.lib;

import java.util.ArrayList;
import java.util.Vector;

public class PajeEventDefinition {

    private PajeEventId pajeEventId;
    public int uniqueID;
    public Vector<String> userDefinedFieldNames;
    public ArrayList<PajeField> fields = new ArrayList<PajeField>();

    public PajeEventDefinition(PajeEventId id, int unique/* int line, PajeDefinitions definitions*/) {
        setPajeEventId(id);
        uniqueID = unique;
    }

    public void addFields(ArrayList<PajeField> newFields) {

        fields.addAll(newFields);
    }

    public void addField(PajeField newField) {

        fields.add(newField);
    }

    public int indexForField(PajeFieldName fieldName) {

        for (PajeField f : fields) {
            if(f.getField().equals(fieldName)) {
                return fields.indexOf(f);
            }
        }
        return -1; // if the field is not found returns -1 (could throw an exception?)


    }

    public PajeEventId getPajeEventId() {
        return pajeEventId;
    }

    public void setPajeEventId(PajeEventId pajeEventId) {
        this.pajeEventId = pajeEventId;
    }


}
