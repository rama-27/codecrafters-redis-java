package dev.rama27.components;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class CmdHandlerTest {
    private final CmdHandler cmdHandler=new CmdHandler();



    @Test
    public void testCmdHandlerPing(){
        String response=CmdHandler.ping();
        assert (response.equals("+PONG\r\n"));
    }

    @Test
    public void testCmdHandlerEcho(){
        String[] ss={"echo","hello"};
        String response=CmdHandler.echo(ss);
        assert (response.equals("$5\r\nhello\r\n"));
    }



}