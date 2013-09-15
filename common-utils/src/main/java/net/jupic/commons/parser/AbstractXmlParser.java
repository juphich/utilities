package net.jupic.commons.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.jupic.commons.parser.exception.MessageParsingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author Chang jung pil
 *
 */
public abstract class AbstractXmlParser<D> implements MessageParser<D> {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected DocumentBuilder documentBuilder;
	protected XPath xpath;
	
	protected AbstractXmlParser() {
		try {
			initialize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize() throws ParserConfigurationException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		dbfactory.setNamespaceAware(true);
		
		documentBuilder = dbfactory.newDocumentBuilder();
		
		XPathFactory xfactory = XPathFactory.newInstance();
		xpath = xfactory.newXPath();
	}
	
	public abstract D parse(InputSource source) throws IOException, MessageParsingException;
	
	@Override
	public D parse(InputStream source) throws IOException, MessageParsingException {
		return parse(new InputSource(source));
	}
	
	public D parse(Reader source) throws IOException, MessageParsingException {
		return parse(new InputSource(source));
	}
	
	protected DocumentBuilder getDocumentBuilder() {
		return this.documentBuilder;
	}
	
	protected Document getDocument(InputSource source) throws SAXException, IOException {
		return documentBuilder.parse(source);
	}
	
	protected XPathExpression getExpression(String expression) throws XPathExpressionException {
		return xpath.compile(expression);
	}
	
	protected Node getNode(Object item, String expression) throws XPathExpressionException {
		return (Node) getExpression(expression).evaluate(item, XPathConstants.NODE);
	}
	
	protected String getValue(Object item, String expression) throws XPathExpressionException {
		Node node = getNode(item, expression);
		if (node != null) {
			return node.getTextContent();
		} else {
			return null;
		}
	}
}
