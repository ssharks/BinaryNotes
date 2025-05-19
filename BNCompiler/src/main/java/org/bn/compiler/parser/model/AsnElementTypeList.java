package org.bn.compiler.parser.model;

import java.util.ArrayList;

public class AsnElementTypeList {
    
    public ArrayList<AsnElementType> elements;
    public boolean isExtensible;

    public AsnElementTypeList() {
        elements = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "";
    }
}
