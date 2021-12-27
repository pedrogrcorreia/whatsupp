package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class FriendsRequests implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    User requester;
    User receiver;
    Timestamp requestTime;
    int request_status;
    Timestamp answerTime;

    public FriendsRequests() {
    }

    public FriendsRequests(User requester, User receiver, Timestamp requestTime, int request_status,
            Timestamp answerTime) {
        this.requester = requester;
        this.receiver = receiver;
        this.requestTime = requestTime;
        this.request_status = request_status;
        this.answerTime = answerTime;
    }

    public FriendsRequests(User requester, User receiver, Timestamp requestTime, Timestamp answerTime) {
        this.requester = requester;
        this.receiver = receiver;
        this.requestTime = requestTime;
        this.answerTime = answerTime;
    }

    public User getRequester() {
        return requester;
    }

    public User getReceiver() {
        return receiver;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public Timestamp getAnswerTime() {
        return answerTime;
    }

    public int getStatus() {
        return request_status;
    }
}
