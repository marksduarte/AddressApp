package br.com.marks.address;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by marks on 09/10/2016.
 */
public class ConnectionFactory {

    public static Connection getConnection(){
        Connection conn = null;
        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            System.out.println("Conectando ao Banco de Dados...");
            conn = DriverManager.getConnection("jdbc:derby:dbAgenda;create=true;");
            System.out.println("Banco de dados conectado...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
