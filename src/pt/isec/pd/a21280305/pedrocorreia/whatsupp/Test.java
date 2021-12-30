package pt.isec.pd.a21280305.pedrocorreia.whatsupp;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Test extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    }

    abstract class foo {
        public foo() {
            System.out.println("Object created");
        }
    }

    class bar extends foo {
        public bar() {
            super();
        }
    }

    public static void main(String[] args) throws IOException {



//        String query = new String("DELETE FROM friends_requests WHERE (requester_user_id = " +
//                1 + " AND friend_user_id = " + 2 + ")" +
//                "OR requester_user_id = " + 2 + " AND friend_user_id = " + 1 + ")");
//        System.out.println(query);
        // ByteArrayOutputStream bout;
        // ObjectOutputStream oout;
        // bout = new ByteArrayOutputStream();
        // oout = new ObjectOutputStream(bout);
        // oout.writeUnshared(Strings.SERVER_PING);

        // System.out.println(bout.size());
        // // System.out.println(Arrays.toString(bout.toByteArray()));
        // for(byte b : bout.toByteArray()){
        // System.out.print((char)b);
        // }
        // System.out.println();
        // System.out.println(Strings.MaxSize());
        // String s = Strings.SERVER_PING+"123";

        // System.out.println(s.contains(Strings.SERVER_PING.toString()));
        // serverPacket.setData(bout.toByteArray());
        // serverPacket.setLength(bout.size());;
    }
}
