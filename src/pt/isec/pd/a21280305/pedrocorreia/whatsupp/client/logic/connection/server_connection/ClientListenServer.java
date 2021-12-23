package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;

/**
 * Class that represents a {@code Thread} running to
 * listening to requests from the Server.
 * This {@code Thread} only starts after an user is
 * logged in, because all events before are listened
 * from the methods called.
 */

public class ClientListenServer extends Thread {

    ObjectInputStream oin;
    ObjectOutputStream oout;
    List<SharedMessage> list;

    public ClientListenServer(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> list) {
        this.oin = oin;
        this.oout = oout;
        this.list = list;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SharedMessage response = (SharedMessage) oin.readObject();
                synchronized (list) {
                    list.add(response);
                    list.notifyAll();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Couldn't load the message:\r\n\t" + e);
            } catch (IOException e) {
                System.out.println("IOException:\r\n\t" + e);
                return;
            }
        }
    }
}
