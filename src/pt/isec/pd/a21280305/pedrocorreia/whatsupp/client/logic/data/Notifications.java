package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.server_connection.ClientRequestInfo;

public class Notifications extends Data implements Runnable {

    ObjectInputStream oin;
    ObjectOutputStream oout;
    List<SharedMessage> notLog;
    List<String> friends;

    public Notifications(ObjectInputStream oin, ObjectOutputStream oout, List<SharedMessage> notLog) {
        super();
        this.oin = oin;
        this.oout = oout;
        this.notLog = notLog;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SharedMessage response = (SharedMessage) oin.readObject();
                if (response.getMsgType() == Strings.USER_REQUEST_INFO_SUCCESS) {
                    Data.friends = ((ClientRequestInfo) response.getClientServerConnection()).getFriends();
                }
                synchronized (notLog) {
                    notLog.add(response);
                    notLog.notify();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Couldn't load the message:\r\n\t" + e);
            } catch (IOException e) {
                System.out.println("IOException:\r\n\t" + e);
                e.printStackTrace();
                return;
            }
        }
    }

}
