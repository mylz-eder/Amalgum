package model.da;



import model.entity.person.Occupation;
import model.entity.person.Person;
import model.utils.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDa implements AutoCloseable {

    private final Connection connection;

    private PreparedStatement preparedStatement;

    JdbcProvider jdbcProvider = new JdbcProvider();

    public PersonDa () throws SQLException {
        connection = jdbcProvider.getConnection();
    }


    public void save(Person person) throws SQLException {
        person.setId(jdbcProvider.getNextId("PERSON_SEQ"));

        if (person.isEntryExp()) {
            person.setExperience("Entry Level");
        } else if (person.isMiddleExp()) {
            person.setExperience("Middle Grade");
        } else if (person.isExpertExp()) {
            person.setExperience("Expert Degree");
        }

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PERSON_TBL VALUES (?,?,?,?,?,?,?)"
        );      Date sqlDate = Date.valueOf(person.getBirthDate());



        preparedStatement.setInt(1, person.getId());
        preparedStatement.setString(2, person.getFirstName());
        preparedStatement.setString(3, person.getLastName());
        preparedStatement.setInt(4, person.getAge());
        preparedStatement.setString( 5, String.valueOf(person.getOccupation()));
        preparedStatement.setDate( 6, sqlDate);
        preparedStatement.setString(7, String.valueOf(person.getExperience()));
        preparedStatement.execute();
    }

    // this is the edit method, which we use to alter the contents of already stored table content

    public void edit (Person person) throws SQLException {

        preparedStatement = connection.prepareStatement(
                "UPDATE PERSON_TBL SET NAME = ?, LAST_NAME = ?, AGE = ?, OCCUPATION = ?, BIRTHDATE= ? , EXPERIENCE = ? WHERE ID = ?"
        );      Date sqlDate = Date.valueOf(person.getBirthDate());

        if (person.isEntryExp()) {
            person.setExperience("Entry Level");
        } else if (person.isMiddleExp()) {
            person.setExperience("Middle Grade");
        } else if (person.isExpertExp()) {
            person.setExperience("Expert Degree");
        }

        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setInt(3, person.getAge());
        preparedStatement.setString( 4, String.valueOf(person.getOccupation()));
        preparedStatement.setDate( 5, sqlDate);
        preparedStatement.setString(6, String.valueOf(person.getExperience()));
        preparedStatement.setInt(7, person.getId());
        preparedStatement.execute();
    }

    public void delete(int id) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM PERSON_TBL WHERE ID=?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    //todo Figure out a way to create a search function, which will search / categorize the information based on the parameters selected in the application

    public List<Person> findAll() throws SQLException {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON_TBL ORDER BY ID"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Person> personList = new ArrayList<>();

        while (resultSet.next()) {
            Person person =
                    Person
                            .builder()
                            .id(resultSet.getInt("ID"))
                            .firstName(resultSet.getString("NAME"))
                            .lastName(resultSet.getString("LAST_NAME"))
                            .age(resultSet.getInt("AGE"))
                            .occupation(Occupation.valueOf(resultSet.getString("OCCUPATION")))
                            .birthDate(resultSet.getDate("BIRTHDATE").toLocalDate())
                            .experience(resultSet.getString("EXPERIENCE"))
                            .build();
            personList.add(person);
        }
        return personList;
    }

    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
