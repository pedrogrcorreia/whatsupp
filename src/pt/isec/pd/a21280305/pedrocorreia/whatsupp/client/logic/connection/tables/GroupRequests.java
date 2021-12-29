package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

public class GroupRequests implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    User requester;
    Group group;
    Timestamp requestTime;
    Timestamp answerTime;
    int status;

    public GroupRequests(User requester, Group group, Timestamp requestTime, Timestamp answerTime, int status){
        this.requester = requester;
        this.group = group;
        this.requestTime = requestTime;
        this.answerTime = answerTime;
        this.status = status;
    }

    public GroupRequests(User requester, Group group, Timestamp requestTime, int status){
        this.requester = requester;
        this.group = group;
        this.requestTime = requestTime;
        this.status = status;
    }

    public GroupRequests(User requester, Group group){
        this.requester = requester;
        this.group = group;
    }

    public User getRequester(){
        return requester;
    }

    public Group getGroup(){
        return group;
    }

    public Timestamp getRequestTime(){
        return requestTime;
    }

    public Timestamp getAnswerTime(){
        return answerTime;
    }

    public int getStatus(){
        return status;
    }
}
