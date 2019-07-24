package com.ywf.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InfluxdbService {

    @Value("${influxdb.username}")
    private String username;

    @Value("${influxdb.password}")
    private String password;

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.database}")
    private String database;

    @Autowired
    private InfluxDB influxDB;

    @Bean
    public InfluxDB createInfluxDB() {
        // 创建链接
        InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);

        // 设置数据存储策略
//        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT","defalut", database, "0", 1);
//        influxDB.query(new Query(command, database));
        return influxDB;
    }

    /**
     * 查询
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command) {
        return influxDB.query(new Query(command, database));
    }

    /**
     * 插入(influxdb不需要创建表，直接在插入时指定表，无则创建，有则插入)
     * @param tags 标签
     * @param fields 字段
     * @param measurement 表名
     */
    public void insert(Map<String, String> tags, Map<String, Object> fields, String measurement) {
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(database, "", builder.build());
    }

    /**
     * 删除数据
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command) {
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 创建数据库
     * @param dbName
     */
    public void createDB(String dbName) {
        influxDB.createDatabase(dbName);
    }

    /**
     * 删除数据库
     * @param dbName
     */
    public void deleteDB(String dbName) {
        influxDB.deleteDatabase(dbName);
    }
}
