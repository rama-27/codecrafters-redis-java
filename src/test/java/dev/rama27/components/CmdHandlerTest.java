package dev.rama27.components;


import lombok.SneakyThrows;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CmdHandlerTest {
    private CmdHandler cmdHandler;

    CmdHandlerTest(){
        cmdHandler=new CmdHandler(new Hashh(),new RespSerializer());
    }



    @Test
    public void testCmdHandlerPing(){
        String response=cmdHandler.ping();
        assert (response.equals("+PONG\r\n"));
    }

    @Test
    public void testCmdHandlerEcho(){
        String[] ss={"echo","hello"};
        String response=cmdHandler.echo(ss);
        assert (response.equals("$5\r\nhello\r\n"));
    }



}