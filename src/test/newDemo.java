import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.RedisSet;

import java.nio.channels.MembershipKey;
import java.util.*;

public class newDemo {

ApplicationContext ac=null;
RedisTemplate redisTemplate=null;
@Before
public void init(){
    ac=new ClassPathXmlApplicationContext("spring.xml");
    redisTemplate=ac.getBean(RedisTemplate.class);
}
@Test
public void test(){
    //赋值
    redisTemplate.opsForValue().set("key1","value1");
    redisTemplate.opsForValue().set("key2","value2");
    //取值
    System.out.println("key1:"+redisTemplate.opsForValue().get("key1"));
    System.out.println("key2:"+redisTemplate.opsForValue().get("key2"));
    //通过key值删除
    redisTemplate.delete("key1");
    //求长度
    Long l=redisTemplate.opsForValue().size("key2");
    System.out.println("长度为:"+l);
    //设置新值并返回旧值
    String old= (String) redisTemplate.opsForValue().getAndSet("key2","newkey2");
    System.out.println("旧值:"+old);
    //新值
    System.out.println("新值"+redisTemplate.opsForValue().get("key2"));
    //求字符串长度
    System.out.println("字符串:"+redisTemplate.opsForValue().get("key2",0,1));
    //追加
    Integer app=redisTemplate.opsForValue().append("key2","zhui");
    System.out.println("追加:"+app);
}

    /**
     * 计算
     */
    @Test
    public  void test1(){
        redisTemplate.opsForValue().set("i", "9");
        printCurrValue("i");
        //加
        redisTemplate.opsForValue().increment("i", 1);
        printCurrValue("i");
        //减
        redisTemplate.getConnectionFactory().getConnection().decr(redisTemplate.getKeySerializer().serialize("i"));
        printCurrValue("i");
        //减去指定数
        redisTemplate.getConnectionFactory().getConnection().decrBy(redisTemplate.getKeySerializer().serialize("i"), 6);
        printCurrValue("i");
        //操作浮点型
        redisTemplate.opsForValue().increment("i", 1.5);
        printCurrValue("i");
    }

    /**
     * hash
     * @param
     */
    @Test
    public void test2(){
        String new1="hash";
        Map<String,Object> map=new HashMap<>();
        map.put("news1","one");
        map.put("news2","two");
        //hmset 多个赋值
        redisTemplate.opsForHash().putAll(new1,map);
        System.out.println(new1);
        //hset 单个赋值
        redisTemplate.opsForHash().put(new1,"three","2");
        printHashValue(new1,"three");
        //hexists 判断是否存在
        boolean b= redisTemplate.opsForHash().hasKey(new1,"three");
        System.out.println(b);
        //hgetall 获取所有
        Map keymap=redisTemplate.opsForHash().entries(new1);
        System.out.println("所有:"+keymap);
        //hincrby 加1
        redisTemplate.opsForHash().increment(new1,"three",1);
        printHashValue(new1,"news1");
        //hincrbyfloat 加浮点
        redisTemplate.opsForHash().increment(new1,"three",0.4);
        printHashValue(new1,"three");
        //hvals 只取value
        List list=redisTemplate.opsForHash().values(new1);
        System.out.println(list);
        //hkeys 只取key
        Set keyset= redisTemplate.opsForHash().keys(new1);
        System.out.println(keyset);
        //hmget
        List<String> mlsit=new ArrayList<>();
        mlsit.add("news1");
        mlsit.add("news2");
        redisTemplate.opsForHash().multiGet(new1,mlsit);
        //hsetnx
        boolean sc=redisTemplate.opsForHash().putIfAbsent(new1,"three","newthree");
        System.out.println("是否成功"+sc);
        //hdel
        System.out.println("删除几个:"+redisTemplate.opsForHash().delete(new1,"news1","news2"));

    }

    /**
     * list
     * @param
     */
    @Test
    public  void  testlist(){
        redisTemplate.delete("list");
        redisTemplate.opsForList().leftPush("list","nodes1");
        List<String> nodeList =new ArrayList<>();
        for (int i=2;i>=1;i--) {
            nodeList.add("node"+i);
        }


        //lpush 多个值从左边插入链表
        redisTemplate.opsForList().leftPushAll("list",nodeList);
        //右边
        redisTemplate.opsForList().rightPush("list","new2");

    }

    public void printCurrValue(String key) {
        String i = (String) redisTemplate.opsForValue().get(key);
        System.out.println(i);
    }

    public void printHashValue(String key, String field) {
        System.out.println(redisTemplate.opsForHash().get(key, field));
    }

    public void printList(String key) {
        Long size = redisTemplate.opsForList().size(key);
        List valueList = redisTemplate.opsForList().range(key, 0, size);
        System.out.println(valueList);
    }
}
