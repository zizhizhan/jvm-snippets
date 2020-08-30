package com.zizhizhan.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
public class HelloJdbc {

    /**
     * SET PASSWORD FOR 'deploy'@'localhost' = password('Hello@123456');
     * FLUSH PRIVILEGES;
     *
     * SHOW GRANTS FOR 'deploy'@'localhost';
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        String user = "deploy";
        String password = "Hello@123456";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {

            try (ResultSet rs = st.executeQuery("SELECT VERSION();")) {
                if (rs.next()) {
                    log.info("MySQL Version: {}", rs.getString(1));
                }
            }

            try (ResultSet rs = st.executeQuery("SHOW tables;")) {
                StringBuilder sb = new StringBuilder("Tables in DB: ").append('\n');
                while (rs.next()) {
                    sb.append('\t').append(rs.getString(1)).append('\n');
                }
                log.info("SHOW TABLES And Return: \n{}", sb);
            }
        }
    }
}
