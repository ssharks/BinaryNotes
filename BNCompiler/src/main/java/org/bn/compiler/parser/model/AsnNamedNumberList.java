package org.bn.compiler.parser.model;

import java.util.ArrayList;

public class AsnNamedNumberList {
    
    public ArrayList<AsnNamedNumber> namedNumbers;
    public boolean isExtensible;
    public boolean sortOnAddition = false;

    public AsnNamedNumberList() {
        namedNumbers = new ArrayList<>();
    }

    public AsnNamedNumberList(boolean sortOnAddition) {
        namedNumbers = new ArrayList<>();
        this.sortOnAddition = sortOnAddition;
    }

    // sort by value when adding a new named number
    public void addNamedNumber(AsnNamedNumber namedNumber) {
        namedNumbers.add(namedNumber);
        if (sortOnAddition) {
            namedNumbers.sort((a, b) -> a.value().compareTo(b.value()));
        }
    }

    /** Returns the total number of elements in the list */
    public int count() {
        return namedNumbers.size();
    }
}
