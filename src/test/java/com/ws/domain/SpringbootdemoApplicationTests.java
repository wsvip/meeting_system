package com.ws.domain;

import com.ws.bean.Sys_User;
import com.ws.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootdemoApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() throws UnknownHostException {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        try {
            url = new URL("http://2000019.ip138.com/");//爬取的网站是百度搜索ip时排名第一的那个
            urlconn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream()));
            String buf = null;
            String get = null;
            while ((buf = br.readLine()) != null) {
                get += buf;
            }
            int where, end;
            for (where = 0; where < get.length() && get.charAt(where) != '['; where++) ;
            for (end = where; end < get.length() && get.charAt(end) != ']'; end++) ;
            get = get.substring(where + 1, end);
            System.err.println(get);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
