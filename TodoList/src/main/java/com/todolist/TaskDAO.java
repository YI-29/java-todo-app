package com.todolist;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/todolist";
    private static final String USER = "root";
    private static final String PASSWORD = "Zaq12wsx";

    //INSERT文を実行
    public void insertTask(String task, String deadline) throws SQLException {
        String sql = "INSERT INTO todos (task, deadline) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task);
            stmt.setDate(2, Date.valueOf(deadline));
            stmt.executeUpdate();
        }
    }
    //DELETE文を実行
    public void deleteTask(String task, String deadline) throws SQLException {
        String sql = "DELETE FROM todos WHERE task = ? AND deadline = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task);
            stmt.setDate(2, Date.valueOf(deadline));
            stmt.executeUpdate();
        }
    }
    public void updateTask(String oldTask, String oldDeadline, String newTask, String newDeadline) throws SQLException {
        String sql = "UPDATE todos SET task = ?, deadline = ? WHERE task = ? AND deadline = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTask);
            stmt.setDate(2, Date.valueOf(newDeadline));
            stmt.setString(3, oldTask);
            stmt.setDate(4, Date.valueOf(oldDeadline));
            stmt.executeUpdate();
        }
    }

}
