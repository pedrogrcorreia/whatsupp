package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream bout;
        ObjectOutputStream oout;
        bout = new ByteArrayOutputStream();
        oout = new ObjectOutputStream(bout);
        oout.writeUnshared(Strings.SERVER_PING);

        System.out.println(bout.size());
//        System.out.println(Arrays.toString(bout.toByteArray()));
        for(byte b : bout.toByteArray()){
            System.out.print((char)b);
        }
        System.out.println();
        System.out.println(Strings.MaxSize());
        String s = Strings.SERVER_PING+"123";

        System.out.println(s.contains(Strings.SERVER_PING.toString()));
//        serverPacket.setData(bout.toByteArray());
//        serverPacket.setLength(bout.size());
    }
}
