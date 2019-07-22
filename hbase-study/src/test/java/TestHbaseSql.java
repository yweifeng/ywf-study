import com.ywf.HbaseApp;
import com.ywf.service.HBaseService;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = HbaseApp.class)
public class TestHbaseSql {

    @Resource
    private HBaseService hbaseService;

    /**
     * 测试删除、创建表、rowkey查询、时间范围查询
     */
    @Test
    public void testHbase() {
        //创建表
        String tablename = "movepoint";
        hbaseService.deleteTable(tablename);
        hbaseService.createTableBySplitKeys(tablename, Arrays.asList("f","back"),hbaseService.getSplitKeys(null));

        // 插入模拟数据

        String rowKey1 = getKeyByTime("movepoint_001", "2019-07-22 08:58:40");
        String rowKey2 = getKeyByTime("movepoint_001", "2019-07-24 08:58:40");

        String startRowKey = getKeyByTime("movepoint_001", "2019-07-22 00:58:40");
        String endRowKey = getKeyByTime("movepoint_001", "2019-07-23 08:58:40");

        System.out.println("rowKey1=" + rowKey1);
        System.out.println("rowKey2=" + rowKey2);
        System.out.println("startRowKey=" + startRowKey);
        System.out.println("endRowKey=" + endRowKey);

        hbaseService.putData(tablename,rowKey1,"f",new String[]{"name","type","create_time"},new String[]{"警员001","1","2019-07-22 08:58:40"});

        hbaseService.putData(tablename,rowKey2,"f",new String[]{"name","type","create_time"},new String[]{"警员001","1","2019-07-24 08:58:40"});

        //查询数据
        //1. 根据rowKey查询
        Map<String,String> result1 = hbaseService.getRowData(tablename,rowKey1);
        System.out.println("+++++++++++根据rowKey查询+++++++++++");
        result1.forEach((k,value) -> {
            System.out.println(k + "---" + value);
        });

        // 根据符合条件查询
        Map<String,Map<String,String>> result = hbaseService.getResultScanner(tablename, startRowKey, endRowKey);
        result.forEach((rowKey,columnMap) -> {
            System.out.println("-----------------------");
            System.out.println("rowKey:" + rowKey);
            System.out.println("+++++++++++行数据+++++++++++");
            columnMap.forEach((k,value) -> {
                System.out.println(k + "---" + value);
            });
            System.out.println("-----------------------");
        });
    }

    public String getKeyByTime(String pre, String time) {
        String res = "";
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            res = pre + c.getTimeInMillis();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return res;
    }

}
