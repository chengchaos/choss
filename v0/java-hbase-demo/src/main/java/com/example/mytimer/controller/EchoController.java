package com.example.mytimer.controller;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
public class EchoController {



    @RequestMapping("/v1/echo")
    public Map<String, Object> echo(HttpServletRequest request, HttpServletResponse response) {

        request.getParameterMap()
                .forEach((key, value) -> System.out.println("key :" + key + "; value : " + key));

        System.out.println("method = "+ request.getMethod());
        try (InputStream inputStream = request.getInputStream();

             //FileOutputStream fos = new FileOutputStream("d:\\test.txt")
        ) {

            int length = request.getContentLength();
            System.out.println("length ========== " + length);

            if (length > 0) {
                byte[] buff = new byte[length];
                for (int i = 0; i < length; i++) {
                    byte b = (byte) inputStream.read();
                    buff[i] = b;
                    // fos.write(b);
                }
                System.out.println(new String(buff));
            }

            int r = new Random().nextInt(5);
            System.out.println("random : "+ r);
            TimeUnit.MILLISECONDS.sleep(r * 1000L);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("方法执行完成！");
        Map<String, Object> res = Collections.singletonMap("code", "OK");
        String result = JSONObject.wrap(res).toString();
//        response.setContentLength(result.length());

        System.out.println("result = "+ result);

        return res;

    }
}
