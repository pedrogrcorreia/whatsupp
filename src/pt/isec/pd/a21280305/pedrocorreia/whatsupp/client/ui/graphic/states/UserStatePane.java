package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states;

import javafx.scene.layout.BorderPane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;

public class UserStatePane extends BorderPane {
    private ClientObservable clientObservable;

    public UserStatePane(ClientObservable clientObservable){
        this.clientObservable = clientObservable;
    }
}
