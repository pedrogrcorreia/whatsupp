package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.connection.tables;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class GroupRequests implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    User requester;
    Group group;
    Timestamp requestTime;
    Timestamp answerTime;
    int status;

}
