package com.zizhizhan.framework.easymock;

import static org.easymock.EasyMock.*;

import java.sql.*;

import lombok.extern.slf4j.Slf4j;
import org.easymock.IMocksControl;
import org.junit.Test;

@Slf4j
public class ResultSetTests {

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ResultSetTests.class);
    }

    @Test
    public void exec() {
        IMocksControl ctrl = createControl();
        ResultSet mockResultSet = ctrl.createMock(ResultSet.class);

        try {
            mockResultSet.next();

            expectLastCall().andReturn(true).times(3);
            expectLastCall().andReturn(false).times(1);
            mockResultSet.getString(1);
            expectLastCall().andReturn("Record001").times(1);
            expectLastCall().andReturn("Record002").times(1);
            expectLastCall().andReturn("Record003").times(1);
            mockResultSet.getString(2);
            expectLastCall().andReturn("Asia Pacific").times(1);
            expectLastCall().andReturn("Europe").times(1);
            expectLastCall().andReturn("America").times(1);
            mockResultSet.getDouble(3);
            expectLastCall().andReturn(350.0).times(1);
            expectLastCall().andReturn(1350.0).times(1);
            expectLastCall().andReturn(5350.0).times(1);

            ctrl.replay();

            while (mockResultSet.next()) {
                System.out.println(mockResultSet.getString(2));
            }
            // ctrl.verify();
        } catch (Exception ex) {
            log.info("unexpected error.", ex);
        }
    }
}
