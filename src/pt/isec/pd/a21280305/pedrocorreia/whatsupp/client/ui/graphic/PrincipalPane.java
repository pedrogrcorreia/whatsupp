package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.InitialStatePane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.LoginStatePane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.RegisterStatePane;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.UserStatePane;

public class PrincipalPane extends StackPane {
    private ClientObservable clientObservable;


    public PrincipalPane(ClientObservable clientObservable){
        this.clientObservable = clientObservable;
        createView();
        registerObserver();
        update();
    }

    private void registerObserver(){
        clientObservable.addPropertyChangeListener("DEBUG", e -> update());
    }

    private void createView(){
        InitialStatePane initialStatePane = new InitialStatePane(clientObservable);
        LoginStatePane loginStatePane = new LoginStatePane(clientObservable);
        RegisterStatePane registerStatePane = new RegisterStatePane(clientObservable);
        UserStatePane userStatePane = new UserStatePane(clientObservable);
        getChildren().addAll(initialStatePane, loginStatePane, registerStatePane, userStatePane);
        //StackPane fill = new StackPane(initialStatePane, loginStatePane, registerStatePane, userStatePane);
        //setCenter(fill);
        //fill.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
    }

    private void update(){

    }
}
