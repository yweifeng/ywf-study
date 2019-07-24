import com.ywf.InfluxdbApp;
import com.ywf.entity.CodeInfo;
import com.ywf.service.InfluxdbService;
import org.influxdb.dto.QueryResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = InfluxdbApp.class)
public class InfluxdbTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private InfluxdbService influxdbService;

    @Test
    public void testInsert() {
        //测试插入数据
        Map<String, String> tags = new HashMap<String, String>();
        Map<String, Object> fields = new HashMap<String, Object>();
        List<CodeInfo> list = new ArrayList<CodeInfo>();

        CodeInfo info1 = new CodeInfo();
        info1.setId(1L);
        info1.setName("BANKS");
        info1.setCode("ABC");
        info1.setDescr("中国农业银行");
        info1.setDescrE("ABC");
        info1.setCreatedBy("system");
        info1.setCreatedAt(new Date().getTime());

        CodeInfo info2 = new CodeInfo();
        info2.setId(2L);
        info2.setName("BANKS");
        info2.setCode("CCB");
        info2.setDescr("中国建设银行");
        info2.setDescrE("CCB");
        info2.setCreatedBy("system");
        info2.setCreatedAt(new Date().getTime()+10000);

        list.add(info1);
        list.add(info2);

        for (CodeInfo info : list) {

            tags.put("TAG_CODE", info.getCode());
            tags.put("TAG_NAME", info.getName());

            fields.put("ID", info.getId());
            fields.put("NAME", info.getName());
            fields.put("CODE", info.getCode());
            fields.put("DESCR", info.getDescr());
            fields.put("DESCR_E", info.getDescrE());
            fields.put("CREATED_BY", info.getCreatedBy());
            fields.put("CREATED_AT", info.getCreatedAt());

            influxdbService.insert(tags, fields, "codeinfo");
        }
    }

    @Test
    public void testQuery() {
        String command = "select * from codeinfo where time<='2019-07-25 00:00:00' order by time desc limit 50";
        QueryResult results = influxdbService.query(command);

        if (results.getResults() == null) {
            return;
        }
        List<CodeInfo> lists = new ArrayList<CodeInfo>();
        for (QueryResult.Result result : results.getResults()) {

            List<QueryResult.Series> series = result.getSeries();
            if (null != series) {
                for (QueryResult.Series serie : series) {
                    List<List<Object>> values = serie.getValues();
                    List<String> columns = serie.getColumns();
                    lists.addAll(getQueryData(columns, values));
                }
            }
        }

        Assert.assertTrue((!lists.isEmpty()));
        Assert.assertEquals(2, lists.size());
    }

    /***整理列名、行数据***/
    private List<CodeInfo> getQueryData(List<String> columns, List<List<Object>> values) {
        List<CodeInfo> lists = new ArrayList<CodeInfo>();

        for (List<Object> list : values) {
            CodeInfo info = new CodeInfo();
            BeanWrapperImpl bean = new BeanWrapperImpl(info);
            for (int i = 0; i < list.size(); i++) {

                String propertyName = setColumns(columns.get(i));//字段名
                Object value = list.get(i);//相应字段值
                bean.setPropertyValue(propertyName, value);
            }

            lists.add(info);
        }

        return lists;
    }

    /***转义字段***/
    private String setColumns(String column) {
        String[] cols = column.split("_");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cols.length; i++) {
            String col = cols[i].toLowerCase();
            if (i != 0) {
                String start = col.substring(0, 1).toUpperCase();
                String end = col.substring(1).toLowerCase();
                col = start + end;
            }
            sb.append(col);
        }
        return sb.toString();
    }
}
