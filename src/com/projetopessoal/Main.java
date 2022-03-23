package com.projetopessoal;

import com.projetopessoal.db.DB;
import com.projetopessoal.entities.Order;
import com.projetopessoal.entities.OrderStatus;
import com.projetopessoal.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException {
        var connection = DB.getConnection();
        var statement = connection.createStatement();
        {
            Map<Long, Order> orderMap = new HashMap<>();
            Map<Long, Product> productMap = new HashMap<>();
            ResultSet resultSet = statement.executeQuery(Main.query());
            while (resultSet.next()) {
                long orderId = resultSet.getLong("order_id");
                long productId = resultSet.getLong("product_id");
                if (!orderMap.containsKey(orderId)) {
                    Order order = new Order();
                    order.setId(orderId);
                    order.setLatitude(resultSet.getDouble("latitude"));
                    order.setLongitude(resultSet.getDouble("longitude"));
                    order.setMoment(resultSet.getTimestamp("moment").toInstant());
                    order.setStatus(OrderStatus.values()[resultSet.getInt("status")]);
                    orderMap.put(orderId, order);
                }
                if (!productMap.containsKey(productId)) {
                    Product product = new Product();
                    product.setId(productId);
                    product.setName(resultSet.getString("name"));
                    product.setDescription(resultSet.getString("description"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setImageUri(resultSet.getString("image_uri"));
                    productMap.put(productId, product);
                }
                Order order = orderMap.get(orderId);
                order.getProductList().add(productMap.get(productId));
            }
            if (!orderMap.isEmpty()) {
                for (Long orderId : orderMap.keySet()) {
                    Order order = orderMap.get(orderId);
                    imprimeNoConsole(order);
                    for (Product product : order.getProductList()) {
                        imprimeNoConsole(product);
                    }
                    quebrarLinha();
                }
            }
        }
        DB.closeConnection();
    }

    static String query() {
        return "SELECT * FROM tb_order "
                + "INNER JOIN tb_order_product ON tb_order.id = tb_order_product.order_id "
                + "INNER JOIN tb_product ON tb_product.id = tb_order_product.product_id";
    }

    static void quebrarLinha() {
        System.out.println();
    }

    static void imprimeNoConsole(Object object) {
        System.out.println(object);
    }
}
