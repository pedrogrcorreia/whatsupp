package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Client;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.ClientObservable;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.graphic.states.*;

public class ClientUI extends BorderPane {
    private ClientObservable clientObservable;
    private MenuItem menu;
    private PrincipalPane principalPane;

    public ClientUI(ClientObservable clientObservable){
        this.clientObservable = clientObservable;
        createView();
        createMenus();
        registerObserver();
        update();
    }

    private void registerObserver(){
        clientObservable.addPropertyChangeListener("DEBUG", event -> update());
    }

    private void createView(){
//        principalPane = new PrincipalPane(clientObservable);
//        setCenter(principalPane);
    }

    private void createMenus(){
        MenuBar menuBar = new MenuBar();
        setTop(menuBar);

        Menu file = new Menu("_File");
        menu = new MenuItem("Exit");
//        MenuItem exit = new MenuItem("Exit");

        file.getItems().addAll(menu);

//        menu.setOnAction(e -> clientObservable.close());
        menu.setOnAction((ActionEvent e)-> {
            Stage janela2 = (Stage) this.getScene().getWindow();
            fireEvent( new WindowEvent(janela2, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        menuBar.getMenus().addAll(file);
    }

    private void update(){
        System.out.println(clientObservable.getAtualState());
        switch(clientObservable.getAtualState()){
            case INITIAL_OPTION -> {
                InitialStatePane initialStatePane = new InitialStatePane(clientObservable);
                setCenter(initialStatePane);
            }
            case REGISTER_USER -> {
                RegisterStatePane registerStatePane = new RegisterStatePane(clientObservable);
                setCenter(registerStatePane);
            }
            case LOGIN_USER -> {
                LoginStatePane loginStatePane = new LoginStatePane(clientObservable);
                setCenter(loginStatePane);
            }
            case LOGGED_IN -> {
                UserStatePane userStatePane = new UserStatePane(clientObservable);
                setCenter(userStatePane);
            }
        }
    }
}
