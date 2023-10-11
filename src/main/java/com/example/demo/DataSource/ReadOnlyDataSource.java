package com.example.demo.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;



public class ReadOnlyDataSource extends HikariDataSource {
    public ReadOnlyDataSource(HikariConfig configuration) {
        super(configuration);
    }

    public ReadOnlyDataSource() {
        super();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return super.getConnection();
    }

}
