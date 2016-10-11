package br.com.marks.address.dao;

import br.com.marks.address.ConnectionFactory;
import br.com.marks.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Created by marks on 09/10/2016.
 */
public class PersonDAO {
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public PersonDAO(){
        conn = ConnectionFactory.getConnection();
        ps = null;
        rs = null;
    }

    /**
     *
     * @return
     */
    public static boolean createTable(){
        boolean isOK = false;
        String sql = "CREATE TABLE PERSON(ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY, " +
                "NOME VARCHAR(255) NOT NULL,\n" +
                "SOBRENOME VARCHAR(255) NOT NULL, ENDERECO VARCHAR(255), CIDADE VARCHAR(255)," +
                "CEP BIGINT, DATANASCIMENTO DATE,\n" +
                "PRIMARY KEY (ID))";
        try {
            conn = ConnectionFactory.getConnection();
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            isOK = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(ps, rs, conn);
        }
        return isOK;
    }

    /**
     *
     * @param p
     */
    public static void persistPerson(Person p){
        try {
            String sql = "INSERT INTO PERSON(NOME, SOBRENOME, ENDERECO, CIDADE, CEP, DATANASCIMENTO) " +
                    "VALUES(?,?,?,?,?,?)";

            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setString(3, p.getStreet());
            ps.setString(4, p.getCity());
            ps.setInt(5, p.getPostalCode());
            ps.setDate(6, Date.valueOf(p.getBirthday()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(ps, rs, conn);
        }
    }

    public static ObservableList<Person> getListPerson(){
        ObservableList<Person> persons = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM PERSON";
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()){
                Person p = new Person();
                p.setFirstName(rs.getString("NOME"));
                p.setLastName(rs.getString("SOBRENOME"));
                p.setStreet(rs.getString("ENDERECO"));
                p.setCity(rs.getString("CIDADE"));
                p.setPostalCode(rs.getInt("CEP"));
                p.setBirthday(rs.getDate("DATANASCIMENTO").toLocalDate());
                persons.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(ps, rs, conn);
        }
        return persons;
    }
    /**
     *
     * @param ps
     * @param rs
     * @param conn
     */
    private static void closeConnection(PreparedStatement ps, ResultSet rs, Connection conn){
        try{
            if(ps != null) ps.close();
            if(rs != null) rs.close();
            if(conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
