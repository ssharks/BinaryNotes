package org.bn.compiler.parser.model;

import java.util.ArrayList;

public class AsnElementTypeList {
    
    public ArrayList<AsnElementType> elements;
    public boolean isExtensible;

    public AsnElementTypeList() {
        elements = new ArrayList<>();
    }

    public void addElement(AsnElementType element, boolean extendedElement) {
        element.isExtended = extendedElement;
        elements.add(element);
    }

    @Override
    public String toString() {
        return "";
    }
}
