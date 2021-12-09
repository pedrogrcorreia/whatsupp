package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

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
        String string = "";
        for(Strings s: Strings.values()){
            if (string.length()< s.name().length()) {
                string= s.name();
            }
        }
        byte[] aux = string.getBytes();
        return aux.length;
    }
//    SERVER_REGISTER_REQUEST,
//    SERVER_REGISTER_SUCCESS,
//    SERVER_REGISTER_FAIL
}
