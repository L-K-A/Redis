import com.lxq.model.Role;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Demo {



        /*@Test
        public void testSpeed() {
            Jedis jedis = new Jedis("localhost", 6379);
//        jedis.auth()
            int i = 0;
            long start = System.currentTimeMillis(); //开始的毫秒数

            try {
                while (true) {
                    long end = System.currentTimeMillis();
                    if (end - start >= 1000) {
                        break;
                    }
                    i++;
                    jedis.set("test" + i, i + "");
                }

            } finally {
                jedis.close();
            }

            System.out.println("每秒操作：" + i + "次");
        }*/

    @Test
    public void testPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(50);
        poolConfig.setMaxTotal(500);
        poolConfig.setMaxWaitMillis(20000);

        JedisPool jedisPool = new JedisPool(poolConfig, "localhost");
        Jedis jedis = jedisPool.getResource();
        int i = 0;
        long start = System.currentTimeMillis(); //开始的毫秒数

        try {
            while (true) {
                long end = System.currentTimeMillis();
                if (end - start >= 1000) {
                    break;
                }
                i++;
                jedis.set("test" + i, i + "");
            }

        } finally {
            jedis.close();
        }

        System.out.println("每秒操作：" + i + "次");
//        jedis.auth()

    }
@Test
    public void tes() {
        ApplicationContext alc = new ClassPathXmlApplicationContext("spring.xml");
         RedisTemplate redisTemplate= alc.getBean(RedisTemplate.class);
        Role role=new Role(1,"lxq");
        redisTemplate.opsForValue().set("ro",role);
        Role ee= (Role) redisTemplate.opsForValue().get("ro");
        System.out.println(ee.getRoleName());
    }


}


