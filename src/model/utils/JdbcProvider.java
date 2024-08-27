package model.utils;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcProvider {

    private final BasicDataSource basicDataSource = new BasicDataSource();

    public Connection getConnection() throws SQLException {
        basicDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        basicDataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
        basicDataSource.setUsername("internallogin");
        basicDataSource.setPassword("internallogin1234");
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxTotal(20);
        return basicDataSource.getConnection();
    }

    public int getNextId(String sequenceName) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s.NEXTVAL AS NEXT FROM DUAL", sequenceName));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int nextId = resultSet.getInt("NEXT");
        preparedStatement.close();
        connection.close();
        return nextId;
    }
}
