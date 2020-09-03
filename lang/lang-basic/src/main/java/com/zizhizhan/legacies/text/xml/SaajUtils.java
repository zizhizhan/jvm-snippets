package com.zizhizhan.legacies.text.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class SaajUtils {

    private static final int MESSAGE_LEN = 1024;

    public static SOAPConnection createConnection() throws SOAPException {
        SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
        return factory.createConnection();
    }

    public static SOAPMessage message(String xml) throws ParserConfigurationException, SAXException, IOException, SOAPException {
        DocumentBuilder builder = createDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));
        return message(doc, SOAPConstants.SOAP_1_2_PROTOCOL);
    }

    public static SOAPMessage message(Document doc, String protocol) throws SOAPException {
        MessageFactory factory = MessageFactory.newInstance(protocol);
        SOAPMessage message = factory.createMessage();
        SOAPBody body = message.getSOAPBody();
        body.addDocument(doc);
        message.saveChanges();

        return message;
    }

    public static String soapBody(SOAPMessage message) throws SOAPException, TransformerException {
        SOAPBody soapBody = message.getSOAPBody();
        return toString((Node) soapBody.getChildElements().next());
    }

    public static String toString(SOAPMessage message) throws SOAPException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(MESSAGE_LEN);
        message.writeTo(baos);
        return baos.toString();
    }

    public static String toString(Node node) throws TransformerException {
        Writer out = new StringWriter();
        Result result = new StreamResult(out);
        Source source = new DOMSource(node);
        Transformer transformer = createTransformer();
        transformer.transform(source, result);
        return out.toString();
    }

    public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        return createDocumentBuilder(null, null);
    }

    public static DocumentBuilder createDocumentBuilder(Map<String, Object> attrs, Schema schema)
            throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        /*
         * factory.setCoalescing(true); factory.setXIncludeAware(true);
         * factory.setIgnoringComments(true);
         * factory.setExpandEntityReferences(true);
         * factory.setIgnoringElementContentWhitespace(true);
         * factory.setValidating(true);
         */
        // Feature can override the above setting.
        // Attribute can override feature.
        if (attrs != null) {
            for (String name : attrs.keySet()) {
                factory.setAttribute(name, attrs.get(name));
            }
        }
        factory.setSchema(schema);
        return factory.newDocumentBuilder();
    }

    public static Transformer createTransformer() throws TransformerConfigurationException {
        Map<String, String> props = new HashMap<String, String>();
        props.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
        props.put(OutputKeys.STANDALONE, "no");
        props.put(OutputKeys.INDENT, "yes");
        props.put("{http://xml.apache.org/xslt}indent-amount", "2");
        return createTransformer(props);
    }

    public static Transformer createTransformer(Map<String, String> props) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        for (String name : props.keySet()) {
            transformer.setOutputProperty(name, props.get(name));
        }
        return transformer;
    }


}
