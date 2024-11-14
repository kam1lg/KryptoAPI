package com.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbService {


    //połączenie z bazą domyślną XAMPP - baza o nazwie "kryptowaluty"
    private Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/kryptowaluty";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);

    }

    //inserty do tabeli "kursy" za pomocą pstmt
    public void insertData(String date, Double btcRate, Double ethRate, Double ltcRate, Double xrpRate, Double bchRate) throws SQLException {
        String sql = "INSERT INTO kursy (data, BTC, ETH, LTC, XRP, BCH) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setObject(2, btcRate);
            pstmt.setObject(3, ethRate);
            pstmt.setObject(4, ltcRate);
            pstmt.setObject(5, xrpRate);
            pstmt.setObject(6, bchRate);
            pstmt.executeUpdate();
        }
    }
}
