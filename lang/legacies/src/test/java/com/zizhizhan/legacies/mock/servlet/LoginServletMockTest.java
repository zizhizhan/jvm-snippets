package com.zizhizhan.legacies.mock.servlet;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.easymock.*;
import org.junit.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LoginServletMockTest {

    @Test
    public void loginOK() throws ServletException, IOException {
        IMocksControl ctrl = createControl();
        HttpServletRequest request = ctrl.createMock(HttpServletRequest.class);
        final ServletContext context = ctrl.createMock(ServletContext.class);
        RequestDispatcher dispatcher = ctrl.createMock(RequestDispatcher.class);

        expect(request.getParameter("username")).andReturn("admin");
        expect(request.getParameter("password")).andReturn("123456");
        expect(context.getNamedDispatcher("dispatcher")).andReturn(dispatcher);
        dispatcher.forward(request, null);

        ctrl.replay();

        LoginServlet servlet = new LoginServlet() {
            private static final long serialVersionUID = 1L;

            @Override
            public ServletContext getServletContext() {
                return context;
            }
        };
        servlet.doPost(request, null);

        ctrl.verify();
    }

    @Test
    public void loginFailed() throws Exception{
        LoginServlet servlet = new LoginServlet();
        HttpServletRequest request = createMock(HttpServletRequest.class);

        expect(request.getParameter("username")).andReturn("admin");
        expect(request.getParameter("password")).andReturn("1234");
        replay(request);

        try {
            servlet.doPost(request, null);
            fail("Not caught exception!");
        } catch (RuntimeException re) {
            assertEquals("Login failed.", re.getMessage());
        }
        verify(request);
    }


    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(LoginServletMockTest.class);
    }

}
