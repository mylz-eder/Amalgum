package model.da;

import model.entity.product.Category;
import model.entity.product.Product;
import model.entity.product.Status;
import model.utils.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDa implements AutoCloseable {
    private final Connection connection;
    private PreparedStatement preparedStatement;
    JdbcProvider jdbcProvider = new JdbcProvider();


    public ProductDa() throws SQLException {
        connection = jdbcProvider.getConnection();
    }

    public void save(Product product) throws SQLException {
        product.setId(jdbcProvider.getNextId("PRODUCT_SEQ"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO PRODUCT_TBL VALUES(?,?,?,?,?,?)"
        );

        preparedStatement.setInt(1, product.getId());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setString(3, String.valueOf(product.getStatus()));
        preparedStatement.setInt(4,  product.getCount());
        preparedStatement.setInt(5, product.getPrice());
        preparedStatement.setString(6, String.valueOf(product.getCategory()));
        preparedStatement.execute();
    }

    public void edit (Product product) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "UPDATE PRODUCT_TBL SET NAME = ?, STATUS = ?, PRODUCT_COUNT = ?, PRODUCT_PRICE = ?, PRODUCT_CATEGORY = ?  WHERE ID=?"
        );
        preparedStatement.setString(1, product.getName());
        preparedStatement.setString(2, product.getStatus().name());
        preparedStatement.setInt(3, product.getCount());
        preparedStatement.setInt(4, product.getPrice());
        preparedStatement.setString(5, String.valueOf(product.getCategory()));
        preparedStatement.setInt(6, product.getId());
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM PRODUCT_TBL WHERE ID=?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    public List<Product> findAll() throws SQLException {
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRODUCT_TBL ORDER BY ID"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Product> productList = new ArrayList<>();

        while (resultSet.next()) {
            Product product =
                    Product
                            .builder()
                            .id(resultSet.getInt("ID"))
                            .name(resultSet.getString("NAME"))
                            .status(Status.valueOf(resultSet.getString("STATUS")))
                            .count(resultSet.getInt("PRODUCT_COUNT"))
                            .price(resultSet.getInt("PRODUCT_PRICE"))
                            .category(Category.valueOf(resultSet.getString("PRODUCT_CATEGORY")))
                            .build();
            productList.add(product);
        }
        return productList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
