package com.zizhizhan.legacies.serde.fastinfoset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import com.zizhizhan.legacies.serde.model.Contact;
import com.zizhizhan.legacies.serde.model.User;

public class FastinfosetSample {

    public static void main(String[] args) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(User.class);
        Marshaller marshaller = ctx.createMarshaller();
        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("James.Zhan");
        user.setContact(new Contact("zhiqiangzhan@gmail.com", "13798520139", ""));
        byte[] bytes = marshalFastInfosetStax(marshaller, user);

        System.out.println(new String(bytes, StandardCharsets.ISO_8859_1));

        user = (User) unmarshalFastInfosetStax(unmarshaller, bytes);
        System.out.println(user.getId());
    }

    public static byte[] marshalFastInfosetStax(Marshaller marshaller, Object obj) throws JAXBException, XMLStreamException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLStreamWriter writer = new StAXDocumentSerializer(baos);
        try {
            marshaller.marshal(obj, writer);
        } finally {
            writer.close();
        }
        System.out.println(baos.size());
        return baos.toByteArray();
    }

    public static Object unmarshalFastInfosetStax(Unmarshaller unmarshaller, byte[] messageBytes) throws Exception {
        XMLStreamReader streamReader = new StAXDocumentParser(new ByteArrayInputStream(messageBytes));

        try {
            return unmarshaller.unmarshal(streamReader);
        } finally {
            streamReader.close();
        }
    }

}
