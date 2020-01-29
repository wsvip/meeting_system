package com.ws.domain.redis;

import com.ws.common.utils.MD5Utils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void stringRedisTest(){
        stringRedisTemplate.opsForValue().set("abc","cba");
        Assert.assertEquals("cba",stringRedisTemplate.opsForValue().get("abc"));
    }
    @Test
    public void redisTempTest(){
        String abc = redisTemplate.opsForValue().get("abc").toString();
        System.err.println(abc);
    }
    /*
         1
        202cb962ac59075b964b07152d234b70
        2
        d9b1d7db4cd6e70935368a1efb10e377
        3
        7363a0d0604902af7b70b271a0b96480
        4
        1cc39ffd758234422e1f75beadfc5fb2
        5
        30cd2f99101cdd52cc5fda1e996ee137
        30cd2f99101cdd52cc5fda1e996ee137
     */
    @Test
    public  void testMd5(){
        String pwd="123";

        for (int i=0;i<5;i++){
            System.err.println(i);
            pwd= MD5Utils.encode(pwd);
            System.err.println(pwd);
        }
        System.err.println(pwd);
    }

}
