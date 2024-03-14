package com.bear.reseeding.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionExample {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 建立数据库连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC", "root", "root");
            // 开始事务
            conn.setAutoCommit(false);

            // 在这里执行大量的插入操作
            // 例如，向表中插入多行数据
            pstmt = conn.prepareStatement("INSERT INTO students (name, age) VALUES (?, ?)");
            pstmt.setString(1, "John");
            pstmt.setInt(2, 25);
            pstmt.executeUpdate();

            pstmt.setString(1, "Alice");
            pstmt.setInt(2, 23);
            pstmt.executeUpdate();
            pstmt.setString(1, "Alice");
            pstmt.setString(2, null);
            pstmt.executeUpdate();

            // 添加更多的插入操作...

            // 如果所有插入操作都成功，提交事务
            conn.commit();
        } catch (SQLException e) {
            // 如果出现任何数据库错误，回滚事务
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // 最后关闭 PreparedStatement 和 Connection
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
