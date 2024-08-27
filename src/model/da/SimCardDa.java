package model.da;

import model.entity.simcard.Provider;
import model.entity.simcard.SimCard;
import model.utils.JdbcProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimCardDa implements AutoCloseable {
    private final Connection connection;

    private PreparedStatement preparedStatement;

    JdbcProvider jdbcProvider = new JdbcProvider();

    public SimCardDa() throws SQLException {
        connection = jdbcProvider.getConnection();
    }

    public void save(SimCard simCard) throws SQLException {
        simCard.setId(jdbcProvider.getNextId("SIMCARD_SEQ"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO SIMCARD_TBL VALUES (?,?,?,?,?,?,?,?)"
        );
        Date sqlDate = Date.valueOf(simCard.getSignupDate());

        if (simCard.isMonthly()) {
            simCard.setPaymentPlan("Monthly");
        } else if (simCard.isManual()) {
            simCard.setPaymentPlan("Manual");
        }

        preparedStatement.setInt(1,simCard.getId());
        preparedStatement.setString(2,simCard.getName());
        preparedStatement.setString(3,simCard.getPhoneNumber());
        preparedStatement.setString(4, String.valueOf(simCard.getProvider()));
        preparedStatement.setString(5, String.valueOf(simCard.getPaymentPlan()));
        preparedStatement.setDate(6, sqlDate);
        preparedStatement.setInt(7, simCard.getLastInvoice());
        preparedStatement.setInt(8, simCard.getBalance());
        preparedStatement.execute();
    }

    public void edit(SimCard simCard) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "UPDATE SIMCARD_TBL SET PROFILE_NAME = ?, PHONE_NUMBER=?, PHONE_NUMBER = ?, PROVIDER = ?, PAYMENT_PLAN = ?, SIGNUP_DATE = ?, LAST_INVOICE = ?, BALANCE = ?  WHERE ID=?"
        );
        Date sqlDate = Date.valueOf(simCard.getSignupDate());

        preparedStatement.setString(1,simCard.getName());
        preparedStatement.setString(2,simCard.getPhoneNumber());
        preparedStatement.setString(3, String.valueOf(simCard.getProvider()));
        preparedStatement.setString(4, simCard.getPaymentPlan());
        preparedStatement.setDate(5, sqlDate);

        // it is important to note that im not really SURE HOW this works in terms of code.
        preparedStatement.setInt(6, simCard.getLastInvoice());
        preparedStatement.setInt(7, simCard.getBalance());
        preparedStatement.setInt(8, simCard.getId());
        preparedStatement.execute();
    }

    public void delete(int id) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM SIMCARD_TBL WHERE ID=?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    public List<SimCard> findAll() throws SQLException {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM SIMCARD_TBL ORDER BY ID"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        List<SimCard> simCardList = new ArrayList<>();

        while (resultSet.next()) {
            SimCard simcard =
                    SimCard
                            .builder()
                            .id(resultSet.getInt("ID"))
                            .name(resultSet.getString("PROFILE_NAME"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .provider(Provider.valueOf(resultSet.getString("PROVIDER")))
                            .paymentPlan(resultSet.getString("PAYMENT_PLAN"))
                            .signupDate(resultSet.getDate("SIGNUP_DATE").toLocalDate())
                            .lastInvoice(resultSet.getInt("LAST_INVOICE"))
                            .balance(resultSet.getInt("BALANCE"))
                            .build();
            simCardList.add(simcard);
        }
        return simCardList;
    }

    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
