package dev.rama27.components;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RespSerializerTest {
    final private RespSerializer respSerializer=new RespSerializer();

    @Test
    public void testMultCmds(){
        String msg="*2\r\n*1\r\n$4\r\nping\r\n*2\r\n$4\r\necho\r\n$9\r\nspiderman\r\n";
        List<String[]> response=respSerializer.deserialize(msg.getBytes(StandardCharsets.UTF_8));

        System.out.println("========================================");
        for(String[] ss:response){
            System.out.println("========================================");
            for (String s:ss){
                System.out.println(s);
            }
            System.out.println("========================================");
        }
        System.out.println("========================================");
        String[] exp1={"ping"};
        String[] exp2={"echo","spiderman"};

        assertArrayEquals(exp1,response.getFirst());
        assertArrayEquals(exp2, response.getLast());
    }

    @Test
    public void testSerializingStringArray(){
        String[] ss={"dir","/tmp/redis-files"};

        String expected="*2\r\n$3\r\ndir\r\n$16\r\n/tmp/redis-files\r\n";
        String actual=respSerializer.serialize(ss);
        System.out.println("actual === "+actual);
        System.out.println("expected === "+expected);
        assertEquals(expected,actual);
    }

}