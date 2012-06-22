package com.bsbnb.creditregistry.client.core.datamodel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.xml.sax.SAXException;

public class Document {

    private final File path;
    private final String content;
    private final boolean temporary;
    private final String encoding;
    private final String mimeType;
    private List<ParseMessage> parseMessages = new ArrayList<ParseMessage>();
    private List<CustomIdentifier> customIdentifiers = new LinkedList<CustomIdentifier>();

    public Document(File path, String content, boolean temporary, String encoding, String mimeType) {
        this.path = path;
        this.content = content;
        this.temporary = temporary;
        this.encoding = encoding;
        this.mimeType = mimeType;
    }

    public File getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public String getEncoding() {
        return encoding;
    }
    
    public String getMimeType() {
        return mimeType;
    }

    public List<ParseMessage> getParseMessages() {
        return parseMessages;
    }

    public void setParseMessages(List<ParseMessage> parseMessages) {
        this.parseMessages = parseMessages;
    }
    
    public void addParseMessage(String severity, int lineNumber, int columnNumber, String message) {
        parseMessages.add(new ParseMessage(severity, lineNumber, columnNumber, message));
    }
    
    public void addParseMessage(ParseMessage message) {
        parseMessages.add(message);
    }
    
    public void clearParseMessage() {
        parseMessages.removeAll(parseMessages);
    }
    
    public void addCustomIdentifier(CustomIdentifier customIdentifier) {
        customIdentifiers.add(customIdentifier);
    }
    
    public void clearCustomIdentifier() {
        customIdentifiers.clear();
    }

    public List<CustomIdentifier> getCustomIdentifiers() {
        return customIdentifiers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Document other = (Document) obj;
        if (this.path != other.path && (this.path == null || !this.path.equals(other.path))) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (this.temporary != other.temporary) {
            return false;
        }
        if ((this.encoding == null) ? (other.encoding != null) : !this.encoding.equals(other.encoding)) {
            return false;
        }
        if ((this.mimeType == null) ? (other.mimeType != null) : !this.mimeType.equals(other.mimeType)) {
            return false;
        }
        if (this.parseMessages != other.parseMessages && (this.parseMessages == null || !this.parseMessages.equals(other.parseMessages))) {
            return false;
        }
        if (this.customIdentifiers != other.customIdentifiers && (this.customIdentifiers == null || !this.customIdentifiers.equals(other.customIdentifiers))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 19 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 19 * hash + (this.temporary ? 1 : 0);
        hash = 19 * hash + (this.encoding != null ? this.encoding.hashCode() : 0);
        hash = 19 * hash + (this.mimeType != null ? this.mimeType.hashCode() : 0);
        hash = 19 * hash + (this.parseMessages != null ? this.parseMessages.hashCode() : 0);
        hash = 19 * hash + (this.customIdentifiers != null ? this.customIdentifiers.hashCode() : 0);
        return hash;
    }
    
}
