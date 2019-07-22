package com.ywf.config;

import com.ywf.service.HBaseService;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HbaseConfiguration {

    @Value("${HBase.nodes}")
    private String nodes;

    @Value("${HBase.maxsize}")
    private String maxsize;

    @Bean
    public HBaseService getHbaseService(){
        System.out.println(nodes);
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",nodes);
        conf.set("hbase.client.keyvalue.maxsize",maxsize);

        return new HBaseService(conf);
    }
}
