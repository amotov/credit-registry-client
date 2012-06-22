package com.bsbnb.creditregistry.client.core.datamodel;

import java.util.Arrays;

/**
 *
 * @author Alexandr.Motov
 */
public class CustomIdentifier {

    public static final String SEPARATOR_ELEMENT = "/";
    public static final String SEPARATOR_ATTRIBUTE = "#";
    
    public static final int TYPE_BEFORE_THE_END_OF_THE_PARENT = 0;
    public static final int TYPE_UNTIL_THE_END = 1;
    
    private String name;
    private String path;
    private String[] elements;
    private String attribute;
    private int type;

    public CustomIdentifier(String name, String path, String attribute, int type) {
        this.name = name;
        this.path = path;
        this.attribute = attribute;
        this.type = type;
        
        elements = path.split(SEPARATOR_ELEMENT);
    }
    
    public CustomIdentifier(String name, String path, String attribute) {
        this(name, path, attribute, TYPE_UNTIL_THE_END);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String[] getElements() {
        return elements;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomIdentifier other = (CustomIdentifier) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        if (!Arrays.deepEquals(this.elements, other.elements)) {
            return false;
        }
        if ((this.attribute == null) ? (other.attribute != null) : !this.attribute.equals(other.attribute)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 79 * hash + Arrays.deepHashCode(this.elements);
        hash = 79 * hash + (this.attribute != null ? this.attribute.hashCode() : 0);
        hash = 79 * hash + this.type;
        return hash;
    }
    
}
