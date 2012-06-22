package com.bsbnb.creditregistry.client.util;

import com.bsbnb.creditregistry.client.core.datamodel.CustomIdentifier;
import com.bsbnb.creditregistry.client.core.datamodel.ParseMessage;
import com.sun.org.apache.xml.internal.serialize.Method;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.omg.CORBA.portable.CustomValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexandr.Motov
 */
public class XMLUtil {
    
    public static class XMLParserHandler extends DefaultHandler {
        
        private static final Logger LOGGER = LoggerFactory.getLogger(XMLParserHandler.class);
        
        private List<CustomIdentifier> customIdentifiers;
        private Map<CustomIdentifier, Integer> position = new HashMap<CustomIdentifier, Integer>();
        private Map<CustomIdentifier, Object> values = new HashMap<CustomIdentifier, Object>();

        public XMLParserHandler(List<CustomIdentifier> customIdentifiers) {
            this.customIdentifiers = customIdentifiers;
        }
        
        final List<ParseMessage> parseMessages = new ArrayList<ParseMessage>();
        
        public void warning(SAXParseException e) throws SAXException {
        	Map<CustomIdentifier, Object> currentValue = new HashMap<CustomIdentifier, Object>();
        	currentValue.putAll(values);
            parseMessages.add(new ParseMessage(ParseMessage.SEVERITY_WARNING, e.getLineNumber(), e.getColumnNumber(), e.getLocalizedMessage(), currentValue));
        }

        public void error(SAXParseException e) throws SAXException {
        	Map<CustomIdentifier, Object> currentValue = new HashMap<CustomIdentifier, Object>();
        	currentValue.putAll(values);
            parseMessages.add(new ParseMessage(ParseMessage.SEVERITY_ERROR, e.getLineNumber(), e.getColumnNumber(), e.getLocalizedMessage(), currentValue));
        }

        public void fatalError(SAXParseException e) throws SAXException {
        	Map<CustomIdentifier, Object> currentValue = new HashMap<CustomIdentifier, Object>();
        	currentValue.putAll(values);
            parseMessages.add(new ParseMessage(ParseMessage.SEVERITY_FATAL_ERROR, e.getLineNumber(), e.getColumnNumber(), e.getLocalizedMessage(), currentValue));
        }

        @Override
        public void startDocument() throws SAXException {
            
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            for (CustomIdentifier customIdentifier : customIdentifiers) {
                String[] elements = customIdentifier.getElements();
                
                if (position.containsKey(customIdentifier)) {
                    int currentPosition = position.get(customIdentifier).intValue();
                    if (currentPosition < elements.length - 1) {
                        String expectedElement = elements[currentPosition + 1];
                        if (qName.equals(expectedElement)) {
                            position.remove(customIdentifier);
                            position.put(customIdentifier, currentPosition + 1);
                            currentPosition++;
                        }
                    }
                    if (currentPosition == elements.length - 1) {
                    	if (customIdentifier.getAttribute() != null && !values.containsKey(customIdentifier)) {
	                    	int length = attributes.getLength();
	                    	for (int i=0; i < length; i++) {
	                    		String attribute = attributes.getQName(i);
	                    		if (attribute.equals(customIdentifier.getAttribute())) {
	                    			String value = attributes.getValue(i);
	                    			values.put(customIdentifier, value);
	                    		}
	                        }
                    	}
                    }
                } else {
                    String expectedElement = elements[0];
                    if (qName.equals(expectedElement)) {
                        position.put(customIdentifier, 0);
                    }
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            for (CustomIdentifier customIdentifier : customIdentifiers) {
                String[] elements = customIdentifier.getElements();
                
                if (position.containsKey(customIdentifier)) {
                    int currentPosition = position.get(customIdentifier).intValue();
                    String currentElement = elements[currentPosition];
                    if (qName.equals(currentElement)) {
                        if (values.containsKey(customIdentifier)) {
                            if (customIdentifier.getType() == CustomIdentifier.TYPE_UNTIL_THE_END && currentPosition == (elements.length - 1)) {
                                if (customIdentifier.getAttribute() != null) {
                                    values.remove(customIdentifier);
                                }
                            }
                        }
                        position.remove(customIdentifier);
                        if (currentPosition != 0)
                            position.put(customIdentifier, currentPosition - 1);
                    }
                }
            }
        }

        @Override
        public void endDocument() throws SAXException {
            
        }

        public List<ParseMessage> getParseMessages() {
            return parseMessages;
        }

    }
    
    
    public static void validate(InputStream xmlFileIS/*, String schemaFilePath*/, DefaultHandler handler)
            throws SAXNotSupportedException, ParserConfigurationException, SAXNotRecognizedException, SAXException, IOException {
        //java.io.File schemaFile = null;

        try {
            //schemaFile = new java.io.File(schemaFilePath);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);

            factory.setFeature("http://xml.org/sax/features/validation", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);

            SAXParser saxParser = factory.newSAXParser();
            saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            //saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schemaFile);
            saxParser.getXMLReader().setFeature("http://apache.org/xml/features/allow-java-encodings",true);

            saxParser.parse(xmlFileIS, handler);
            saxParser.reset();
        } finally {
            //schemaFile = null;
        }
    }

    public static final String format(java.io.File xmlFile, String encoding) throws IOException, ParserConfigurationException, SAXException {
        /*FileInputStream fileInputStream = new FileInputStream(xmlFile);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        byte[] byteArray = new byte[(int) xmlFile.length()];
        dataInputStream.readFully(byteArray);
        String xmlString = new String(byteArray);*/

        String xmlString = FileUtils.readFileToString(xmlFile, encoding);
        return format(xmlString, encoding);
    }
    
    public static final String format(String xmlString, String encoding) throws IOException, ParserConfigurationException, SAXException {
        return prettyPrintWithXMLSerializer(buildDocument(xmlString, encoding), encoding);
    }

    public static final Document buildDocument(String xmlString, String encoding) 
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes(encoding)));
    }

    public static String prettyPrintWithDOM3LS(Document document) {
        DOMImplementation domImplementation = document.getImplementation();
        if (domImplementation.hasFeature("LS", "3.0") && domImplementation.hasFeature("Core", "2.0")) {
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation.getFeature("LS", "3.0");
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
            if (domConfiguration.canSetParameter("format-pretty-print", Boolean.TRUE)) {
                lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
                LSOutput lsOutput = domImplementationLS.createLSOutput();
                lsOutput.setEncoding("UTF-8");
                StringWriter stringWriter = new StringWriter();
                lsOutput.setCharacterStream(stringWriter);
                lsSerializer.write(document, lsOutput);
                return stringWriter.toString();
            } else {
                throw new RuntimeException("DOMConfiguration 'format-pretty-print' parameter isn't settable.");
            }
        } else {
            throw new RuntimeException("DOM 3.0 LS and/or DOM 2.0 Core not supported.");
        }
    }

    public static String prettyPrintWithXMLSerializer(Document document, String encoding) throws IOException {
        StringWriter stringWriter = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(stringWriter, new OutputFormat(Method.XML, encoding, true));
        serializer.serialize(document);
        return stringWriter.toString();
    }

    public static String prettyPrintWithTrAX(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        DOMSource domSource = new DOMSource(document);
        transformer.transform(domSource, streamResult);
        return stringWriter.toString();
    }
}
