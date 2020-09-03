package com.zizhizhan.legacies.serde.jaxb;

import com.zizhizhan.legacies.serde.model.Contact;
import com.zizhizhan.legacies.serde.model.User;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import javax.xml.bind.Marshaller.Listener;

@Slf4j
public class JaxbSample {

    public static void main(String[] args) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(User.class);
        Marshaller marshaller = ctx.createMarshaller();
        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("James.Zhan");
        user.setContact(new Contact("zhiqiangzhan@gmail.com", "13798520139", ""));

        String xml = marshal(marshaller, user);
        System.out.println(xml);

        StringReader sr = new StringReader(xml);

        user = (User) unmarshaller.unmarshal(sr);
        System.out.println(user.getId());
    }

    public static String marshal(Marshaller marshaller, Object user) throws JAXBException {

        marshaller.setListener(new Listener() {
            @Override
            public void beforeMarshal(Object source) {
                log.info(String.format("before: %s", source));
            }

            @Override
            public void afterMarshal(Object source) {
                log.info(String.format("after: %s", source));
            }
        });

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setEventHandler(new ValidationEventHandlerImpl());

        StringWriter out = new StringWriter();
        marshaller.marshal(user, out);
        return out.toString();
    }

    private static class ValidationEventHandlerImpl implements ValidationEventHandler {
        public boolean handleEvent(ValidationEvent ve) {
            ValidationEventLocator vel = ve.getLocator();
            StringBuilder errorMsg = new StringBuilder(128);
            if (null != vel) {
                errorMsg.append("Node: ").append(vel.getNode()).append(" Line:Col[")
                        .append(vel.getLineNumber()).append(":")
                        .append(vel.getColumnNumber()).append("]:");
            }
            log.warn("Severity:" + ve.getSeverity() + " Error Message: " + errorMsg.toString() + ve.getMessage());
            return true;
        }
    }

}
