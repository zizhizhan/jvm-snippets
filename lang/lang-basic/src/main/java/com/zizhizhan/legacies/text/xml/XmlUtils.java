package com.zizhizhan.legacies.text.xml;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlUtils {

    public static String format(String xmlContent) throws TransformerException {
        StringWriter writer = new StringWriter();
        format(new StringReader(xmlContent), writer);
        return writer.toString();
    }

    public static void format(Reader reader, Writer writer) throws TransformerException {
        Transformer transformer = createTransformer(XmlUtils.class.getResourceAsStream("formatter.xsl"));
        transform(reader, writer, transformer);
    }

    public static String transform(String xmlContent, Transformer transformer) throws TransformerException {
        Writer writer = new StringWriter();
        transform(new StringReader(xmlContent), writer, transformer);
        return writer.toString();
    }

    public static void transform(Reader reader, Writer writer, Transformer transformer) throws TransformerException {
        transformer.transform(new StreamSource(reader), new StreamResult(writer));
    }

    public static Transformer createTransformer(String template) throws TransformerException {
        Source templateSource = new StreamSource(new StringReader(template));
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer(templateSource);
    }

    public static Transformer createTransformer(InputStream template) throws TransformerException {
        Source templateSource = new StreamSource(template);
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer(templateSource);
    }

    public static Transformer createTransformer(Source templateSource) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer(templateSource);
    }

    public static void main(String[] args) throws TransformerException {
        System.out.println(format("<a><b>123<!--cctv--></b><!--cctv--><c>xxx</c></a>"));
    }


}
