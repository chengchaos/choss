package luxe.chaos.choss.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StringTest {


    final private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSubstreing() {

        String dir1 = "/pdffile/downfile/pc/";
        String dir2 = dir1.substring(0, dir1.lastIndexOf("/"));

        System.out.println("dir1 = " + dir1);
        System.out.println("dir2 = " + dir2);

        String dir3 = dir1.substring(dir2.lastIndexOf("/") + 1);
        System.out.println("dir3 = " + dir3);

        String file1 = dir1 + "readme.pdf";
        String file2 = file1.substring(0, file1.lastIndexOf("/") + 1);
        System.out.println(file2);

    }

    @Test
    public void numberFormatTest() {

        int i = 1;

        DecimalFormat decimalFormat = new DecimalFormat("0000000000");

        String x = decimalFormat.format(1.0);
        System.out.println(x);

        double pi = Math.PI;
        System.out.println(new DecimalFormat("00.000").format(pi));//03.142
        System.out.println(new DecimalFormat("00.000").format(i));//03.142
        System.out.println(new DecimalFormat("00000").format(i));//03.142
        System.out.println(new DecimalFormat("000000000").format(i));//03.142
    }

    @Test
    public void jsonTest() throws JsonProcessingException {

        Map<String, Object> data0 = Maps.newHashMap();

        data0.put("username", "chengchao");
        data0.put("birthday", new Date());
        data0.put("money", 23423423.68D);

        ArrayList<String> girls = Lists.newArrayList();
        data0.put("girls", girls);

        girls.add("Alice");
        girls.add("Lolr");

        String json = this.mapper.writeValueAsString(data0);

        System.out.println(json);
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, typeReference);

        System.out.println(map);

    }
}
