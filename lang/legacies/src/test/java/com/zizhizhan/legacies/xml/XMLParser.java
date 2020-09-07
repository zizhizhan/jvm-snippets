package com.zizhizhan.legacies.xml;

import java.io.*;
import java.util.*;

import javax.xml.transform.*;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLParser {

    private final DocumentBuilder builder;
    private final Transformer trans;
    private final Properties outputKeys;

    private String uri;
    protected Document doc;

    private void ParseChecked() throws TransformerException, IOException {
        if (doc == null) {
            throw new TransformerException("XML document is empty!");
        } else if (uri == null) {
            throw new IOException("uri is empty?");
        }
    }

    protected XMLParser(boolean whitespace) throws ParserConfigurationException,
            TransformerConfigurationException, TransformerFactoryConfigurationError {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(whitespace);

        builder = factory.newDocumentBuilder();
        trans = TransformerFactory.newInstance().newTransformer();
        outputKeys = new Properties();
    }

    protected XMLParser() throws ParserConfigurationException,
            TransformerConfigurationException, TransformerFactoryConfigurationError {
        this(true);
    }

    public static XMLParser newInstance() throws TransformerConfigurationException,
            ParserConfigurationException, TransformerFactoryConfigurationError {

        return new XMLParser();
    }

    public static XMLParser newInstance(boolean whitespace) throws TransformerConfigurationException,
            ParserConfigurationException, TransformerFactoryConfigurationError {

        return new XMLParser(whitespace);
    }

    private void init(Document doc) {
        String encoding = doc.getInputEncoding();
        String version = doc.getXmlVersion();
        String mediaType = "text/xml";
        setOutputKeys(encoding, version, mediaType);
    }

    public Document parse(File file) throws SAXException, IOException {
        doc = builder.parse(file);
        uri = file.getPath();
        init(doc);
        return doc;
    }

    public Document parse(String uri) throws SAXException, IOException {
        doc = builder.parse(uri);
        this.uri = uri;
        init(doc);
        return doc;
    }

    public Document parse(InputStream is) throws SAXException, IOException {
        doc = builder.parse(is);
        init(doc);
        return doc;
    }

    public Document parse(Node node) {
        doc = (Document) node;
        init(doc);
        return doc;
    }

    public void setOutputKeys(String encoding) {
        setOutputKeys(encoding, "1.0");
    }

    public void setOutputKeys(String encoding, String version) {
        setOutputKeys(encoding, version, "text/xml");
    }

    public void setOutputKeys(String encoding, String version, String mediaType) {

        if (encoding != null && encoding.equals("")) {
            outputKeys.put(OutputKeys.ENCODING, encoding);
        }
        if (version != null && version.equals("")) {
            outputKeys.put(OutputKeys.VERSION, version);
        }
        if (mediaType != null && mediaType.equals("")) {
            outputKeys.put(OutputKeys.MEDIA_TYPE, mediaType);
        }
    }

    public void setDocumentType(DocumentType dtd) {
        if (dtd != null) {
            String systemid = dtd.getSystemId();
            String publicid = dtd.getPublicId();
            if (systemid != null) {
                outputKeys.put(OutputKeys.DOCTYPE_SYSTEM, systemid);
            } else if (publicid != null) {
                outputKeys.put(OutputKeys.DOCTYPE_PUBLIC, publicid);
            }
        }
    }

    public void save(Node source, OutputStream os) throws TransformerException {
        if (source instanceof Document) {
            DocumentType dtd = ((Document) source).getDoctype();
            if (dtd != null) {
                setDocumentType(dtd);
            }
        }
        trans.setOutputProperties(outputKeys);
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource ds = new DOMSource(source);
        StreamResult sr = new StreamResult(os);
        trans.transform(ds, sr);
    }

    public void save(Node source, File file) throws FileNotFoundException, TransformerException {
        FileOutputStream os = new FileOutputStream(file);
        save(source, os);
    }

    public void save(Node source, String uri) throws FileNotFoundException, TransformerException {
        FileOutputStream os = new FileOutputStream(uri);
        save(source, os);
    }

    public void save(OutputStream os) throws TransformerException, IOException {

        ParseChecked();
        save(doc, os);
    }

    public void save(String filepath) throws TransformerException, IOException {
        ParseChecked();
        save(doc, filepath);
    }

    public void save() throws TransformerException, IOException {
        ParseChecked();
        save(doc, uri);
    }

    public void set(String id, String value) {
        Element item = doc.getElementById(id);
        if (item != null) {
            item.setTextContent(value);
        }
    }

    public void setCDATASection(Node node, String value) {
        if (node.hasChildNodes()) {
            Node tmp = node.getFirstChild();
            Node xp;
            while (tmp != null) {
                xp = tmp;
                System.out.println(tmp.getTextContent());
                tmp = tmp.getNextSibling();
                node.removeChild(xp);
            }
        }
        node.appendChild(doc.createCDATASection(value));
    }

    public void setCDATASection(String value) {
        Node script = doc.getElementsByTagName("script").item(0);
        setCDATASection(script, value);
    }

    public void travelTree() {
        travelTree(doc);
    }

    public void travelTree(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            System.out.println("node=" + node.getNodeName() + ":" + node.getNodeValue() + ":" + node.getTextContent());
            if (node.hasChildNodes()) {
                travelTree(node.getChildNodes());
            }
        }
    }

    public void travelTree(Node node) {
        System.out.println("" + node + "--->" + node.getNodeType() + ":" + node.getNodeName() + ":" + node.getNodeValue() + ":" + node.getTextContent());
        Node child = node.getFirstChild();
        while (child != null) {
            travelTree(child);
            child = child.getNextSibling();
        }
    }

    public void printNodeInfo(Node node) {
        System.out.println(node.getNodeName() + " : " + node.getNodeValue());
    }

    public String NodeType(short type) {
        String str = "";
        switch (type) {
            case Node.ELEMENT_NODE:
                str = "Element";
                break;
            case Node.ATTRIBUTE_NODE:
                str = "Attribute";
                break;
            case Node.TEXT_NODE:
                str = "Text";
                break;
            case Node.CDATA_SECTION_NODE:
                str = "CDATASection";
                break;
            case Node.ENTITY_REFERENCE_NODE:
                str = "EntityReference";
                break;
            case Node.ENTITY_NODE:
                str = "Entity";
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                str = "ProcessingInstruction";
                break;
            case Node.COMMENT_NODE:
                str = "Comment";
                break;
            case Node.DOCUMENT_NODE:
                str = "Document";
                break;
            case Node.DOCUMENT_TYPE_NODE:
                str = "DocumentType";
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                str = "DocumentFragment";
                break;
            case Node.NOTATION_NODE:
                str = "Notation";
                break;
        }
        return str;
    }

    public void NodeEnumInfo() {
        System.out.println("  NoteType:");
        System.out.println("  ELEMENT_NODE :                   " + Node.ELEMENT_NODE);
        System.out.println("  ATTRIBUTE_NODE :                 " + Node.ATTRIBUTE_NODE);
        System.out.println("  TEXT_NODE :                      " + Node.TEXT_NODE);
        System.out.println("  CDATA_SECTION_NODE :             " + Node.CDATA_SECTION_NODE);
        System.out.println("  ENTITY_REFERENCE_NODE :          " + Node.ENTITY_REFERENCE_NODE);
        System.out.println("  ENTITY_NODE :                    " + Node.ENTITY_NODE);
        System.out.println("  PROCESSING_INSTRUCTION_NODE :    " + Node.PROCESSING_INSTRUCTION_NODE);
        System.out.println("  COMMENT_NODE :                   " + Node.COMMENT_NODE);
        System.out.println("  DOCUMENT_NODE :                  " + Node.DOCUMENT_NODE);
        System.out.println("  DOCUMENT_TYPE_NODE :             " + Node.DOCUMENT_TYPE_NODE);
        System.out.println("  DOCUMENT_FRAGMENT_NODE :         " + Node.DOCUMENT_FRAGMENT_NODE);
        System.out.println("  NOTATION_NODE :                  " + Node.NOTATION_NODE);

        System.out.println("\r\n  Document Position:");
        System.out.println("  DOCUMENT_POSITION_DISCONNECTED :             " + Node.DOCUMENT_POSITION_DISCONNECTED);
        System.out.println("  DOCUMENT_POSITION_PRECEDING :                " + Node.DOCUMENT_POSITION_PRECEDING);
        System.out.println("  DOCUMENT_POSITION_FOLLOWING :                " + Node.DOCUMENT_POSITION_FOLLOWING);
        System.out.println("  DOCUMENT_POSITION_CONTAINS :                 " + Node.DOCUMENT_POSITION_CONTAINS);
        System.out.println("  DOCUMENT_POSITION_CONTAINED_BY :             " + Node.DOCUMENT_POSITION_CONTAINED_BY);
        System.out.println("  DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC :  " + Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC);

        System.out.println("\nclass : " + Node.class);
    }

	public static void main(String[] args) throws ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XMLParser xu = XMLParser.newInstance();
		xu.NodeEnumInfo();
		xu.parse(XMLParser.class.getResourceAsStream("config.xml"));
		xu.travelTree();
		String str = "\nvar s = 1;\ncccc\n";
		xu.setCDATASection(str);
	}

}
