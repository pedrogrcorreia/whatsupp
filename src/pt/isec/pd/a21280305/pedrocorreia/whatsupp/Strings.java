package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

public enum Strings {
    SERVER_REGISTER_REQUEST("Request to register a server."),
    SERVER_REGISTER_SUCCESS("Registered the server successfully."),
    SERVER_REGISTER_FAIL("Failed to register the server."),
    JDBC_DRIVE("com.mysql.cj.jdbc.Driver"),
    SERVER_PING("Server pinging.");
    private final String description;


    Strings(String s) { description = s; }
    @Override
    public String toString(){ return description; }

    public static int MaxSize(){
        Strings longest = SERVER_REGISTER_REQUEST;
        for(Strings s: Strings.values()){
            if (longest.toString().length() < s.toString().length()) {
                longest = s;
            }
        }
        ByteArrayOutputStream bout;
        ObjectOutputStream oout;
        bout = new ByteArrayOutputStream();
        try {
            oout = new ObjectOutputStream(bout);
            oout.writeUnshared(longest);
            for(byte b : bout.toByteArray()){
                System.out.print((char)b);
            }
        }catch(IOException e){

        }

        return bout.size();
    }
//    SERVER_REGISTER_REQUEST,
//    SERVER_REGISTER_SUCCESS,
//    SERVER_REGISTER_FAIL
}
