package org.bn.compiler.parser.model;

import java.util.ArrayList;

public class AsnElementTypeList {
    
    public ArrayList<AsnElementType> elements;
    public boolean isExtensible;
    public int numOfRootElements;

    public AsnElementTypeList() {
        elements = new ArrayList<>();
    }

    public void addElement(AsnElementType element, boolean extendedElement) {
        if (!extendedElement) {
            numOfRootElements++;
        }
        elements.add(element);
    }

    @Override
    public String toString() {
        return "";
    }
}
