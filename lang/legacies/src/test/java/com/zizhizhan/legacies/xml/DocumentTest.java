package com.zizhizhan.legacies.xml;

import lombok.extern.slf4j.Slf4j;
import org.easymock.*;
import org.junit.*;
import org.w3c.dom.*;

import static org.easymock.EasyMock.*;

@Slf4j
public class DocumentTest {

    @Test
    public void createElement() {
        IMocksControl ctrl = createControl();
        Document doc = ctrl.createMock(Document.class);
        Element el = ctrl.createMock(Element.class);
        expect(doc.createElement("html")).andReturn(el);

        ctrl.replay();

        Element el2 = doc.createElement("html");
        log.info("{} vs. {}", el2, el);

        ctrl.verify();
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(DocumentTest.class);
    }

}
