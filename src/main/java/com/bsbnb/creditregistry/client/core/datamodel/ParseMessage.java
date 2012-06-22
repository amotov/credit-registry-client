package com.bsbnb.creditregistry.client.core.datamodel;

import com.bsbnb.creditregistry.client.language.LanguageUtil;
import com.bsbnb.creditregistry.client.util.LocaleUtil;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexandr.Motov
 */
public class ParseMessage {
    
    public static final String SEVERITY_ERROR = LanguageUtil.get(LocaleUtil.getDefault(), "ui-severity-error");
    public static final String SEVERITY_FATAL_ERROR = LanguageUtil.get(LocaleUtil.getDefault(), "ui-severity-fatal-error");
    public static final String SEVERITY_WARNING = LanguageUtil.get(LocaleUtil.getDefault(), "ui-severity-warning");
    
    private String severity;
    private int lineNumber;
    private int columnNumber;
    private String message;
    private Map<CustomIdentifier, Object> customValues;

    public ParseMessage(String severity, int lineNumber, int columnNumber, String message, Map<CustomIdentifier, Object> customValue) {
        this(severity, lineNumber, columnNumber, message);
        this.customValues = customValue;
    }
    
    public ParseMessage(String severity, int lineNumber, int columnNumber, String message) {
        this.severity = severity;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.message = message;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public void addCustomValue(CustomIdentifier customIdentifier, Object value) {
        if (!customValues.containsKey(customIdentifier))
            customValues.put(customIdentifier, value);
    }

    public void setCustomValue(Map<CustomIdentifier, Object> customValue) {
        this.customValues = customValue;
    }
    
    public Map<CustomIdentifier, Object> getCustomValues() {
        if (customValues == null)
            customValues = new HashMap<CustomIdentifier, Object>();
        return customValues;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParseMessage other = (ParseMessage) obj;
        if ((this.severity == null) ? (other.severity != null) : !this.severity.equals(other.severity)) {
            return false;
        }
        if (this.lineNumber != other.lineNumber) {
            return false;
        }
        if (this.columnNumber != other.columnNumber) {
            return false;
        }
        if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.severity != null ? this.severity.hashCode() : 0);
        hash = 97 * hash + this.lineNumber;
        hash = 97 * hash + this.columnNumber;
        hash = 97 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }
    
}
