package com.zizhizhan.legacies.thirdparty.jaxb.v0;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XmlUtils is for processing xml file.
 * 
 * @author <a href="mailto:v-jzhan@expedia.com">James Zhan</a>
 */
public class XmlUtils {
	private static XPath xpath;

	static {
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
	}

	private XmlUtils() {

	}

	/**
	 * Parse the content of the given <code>InputStream</code> as an XML
	 * document and return a new DOM {@link Document} object. An
	 * <code>IllegalArgumentException</code> is thrown if the
	 * <code>InputStream</code> is null.
	 * 
	 * @param is
	 *            InputStream containing the content to be parsed.
	 * @return <code>Document</code> result of parsing the
	 *         <code>InputStream</code>
	 * @exception IOException
	 *                If any IO errors occur.
	 * @exception SAXException
	 *                If any parse errors occur.
	 * @see org.xml.sax.DocumentHandler
	 */
	/**
	 * Parse the content of the given InputStream as an XML document and return
	 * a new DOM object.
	 * 
	 * @param is
	 *            InputStream containing the content to be parsed.
	 * @return Document result of parsing the InputStream.
	 * @throws Exception
	 *             if any exception occur.
	 */
	public static Document parse(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(is);
		return doc;
	}

	/**
	 * Parse the content of the given file as an XML document and return a new
	 * DOM object.
	 * 
	 * @param f
	 *            file containing the content to be parsed.
	 * @return Document result of parsing the file.
	 * @throws Exception
	 *             if any exception occur.
	 */
	public static Document parse(File f) throws Exception {
		return parse(new FileInputStream(f));
	}

	/**
	 * Eval the xpath expression as a NodeList.
	 * 
	 * @param context
	 *            context node.
	 * @param expression
	 *            xpath expression.
	 * @return NodeList result of eval the xpath.
	 * @throws XPathException
	 *             if any XPathException occur.
	 */
	public static NodeList selectNodeList(Node context, String expression) throws XPathException {
		return (NodeList) eval(context, expression, XPathConstants.NODESET);
	}

	/**
	 * Eval the xpath expression as a Node.
	 * 
	 * @param context
	 *            context node.
	 * @param expression
	 *            xpath expression.
	 * @return Node result of eval the xpath.
	 * @throws XPathException
	 *             if any XPathException occur.
	 */
	public static Node selectSingleNode(Node context, String expression) throws XPathException {
		return (Node) eval(context, expression, XPathConstants.NODE);
	}

	/**
	 * Eval the xpath expression as an Object.
	 * 
	 * @param context
	 *            context node.
	 * @param expression
	 *            xpath expression.
	 * @param qname
	 *            return type.
	 * @return Object result of eval the xpath.
	 * @throws XPathException
	 *             if any XPathException occur.
	 */
	public static Object eval(Node context, String expression, QName qname) throws XPathException {
		return xpath.evaluate(expression, context, qname);
	}

	/**
	 * Remove the node for context node, specified by xpath expression.
	 * 
	 * @param context
	 *            context node.
	 * @param expression
	 *            xpath expression.
	 * @throws XPathException
	 *             if any XPathException occur.
	 */
	public static void remove(Node context, String expression) throws XPathException {
		Node node = selectSingleNode(context, expression);
		remove(node);
	}

	/**
	 * Remove the nodelist for context node, specified by xpath expression.
	 * 
	 * @param context
	 *            context node.
	 * @param expression
	 *            xpath expression.
	 * @throws XPathException
	 *             if any XPathException occur.
	 */
	public static void removeAll(Node context, String expression) throws XPathException {
		NodeList list = selectNodeList(context, expression);
		removeAll(list);
	}

	/**
	 * Remove the list from it's ower document.
	 * 
	 * @param list
	 *            node list.
	 */
	public static void removeAll(NodeList list) {
		for (int i = 0; i < list.getLength(); i++) {
			remove(list.item(i));
		}
	}

	/**
	 * Remove every childs for specified node. if it's firstChild == null, then
	 * the node is an empty node.
	 * 
	 * @param node
	 *            current node.
	 */
	public static void removeChilds(Node node) {
		Node child;
		while ((child = node.getFirstChild()) != null) {
			node.removeChild(child);
		}
	}

	/**
	 * Remove the node from it's ower document.
	 * 
	 * @param node
	 *            node.
	 */
	public static void remove(Node node) {
		node.getParentNode().removeChild(node);
	}

	/**
	 * Add child for current node with given tagName.
	 * 
	 * @param node
	 *            current node.
	 * @param tagName
	 *            child node 's tag.
	 * @return child node.
	 */
	public static Element addElement(Node node, String tagName) {
		Document doc = node.getOwnerDocument();
		Element el = doc.createElement(tagName);
		node.appendChild(el);
		return el;
	}

	/**
	 * Update the element 's tagName with new namespace label, and also all it's
	 * childs.
	 * 
	 * @param el
	 *            current element.
	 * @param ns
	 *            namespace label.
	 * @return new elecment.
	 */
	public static Element updateNamespaceHierarchy(Element el, String ns) {
		Document doc = el.getOwnerDocument();
		String localName = el.getLocalName() == null ? el.getTagName() : el.getLocalName();
		Element el2 = doc.createElement(ns + ":" + localName);
		if (el.hasChildNodes()) {
			Node child = el.getFirstChild();
			while (child != null) {
				if (Node.ELEMENT_NODE == child.getNodeType()) {
					el2.appendChild(updateNamespaceHierarchy((Element) child, ns));
				} else {
					el2.appendChild(child.cloneNode(true));
				}
				child = child.getNextSibling();
			}
		}
		return el2;
	}

	/**
	 * Replace current element 's tagName with new tagName.
	 * 
	 * @param el
	 *            current element.
	 * @param newTagName
	 *            new tag name.
	 * @throws Exception
	 *             if any exception occur.
	 */
	public static void replcaeElementName(Element el, String newTagName) throws Exception {
		Document doc = el.getOwnerDocument();
		Element el2 = doc.createElement(newTagName);
		Node child;
		while ((child = el.getFirstChild()) != null) {
			Node n = el.removeChild(child);
			if (Node.ELEMENT_NODE == n.getNodeType()) {
				el2.appendChild(n);
			}
		}
		el.getParentNode().replaceChild(el2, el);
	}

	/**
	 * Save current document as a output stream.
	 * 
	 * @param doc
	 *            document for save.
	 * @param os
	 *            output destination.
	 * @throws TransformerException
	 *             if any TransfomerException occur.
	 */
	public static void saveAs(Document doc, OutputStream os) throws TransformerException {
		Properties formats = new Properties();
		formats.setProperty(OutputKeys.INDENT, "yes");
		formats.setProperty(OutputKeys.ENCODING, "utf-8");
		saveAs(doc, os, formats);
	}

	/**
	 * Save current document as a output stream.
	 * 
	 * @param doc
	 *            document for save.
	 * @param os
	 *            output destination.
	 * @param formats
	 *            output format.
	 * @throws TransformerException
	 *             if any TransfomerException occur.
	 */
	public static void saveAs(Document doc, OutputStream os, Properties formats) throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();

		transformer.setOutputProperties(formats);

		DOMSource ds = new DOMSource(doc);
		StreamResult sr = new StreamResult(os);

		transformer.transform(ds, sr);
	}

}
