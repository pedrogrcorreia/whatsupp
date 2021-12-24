package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.ui.console;

import java.io.Console;
import java.util.Scanner;

import pt.isec.pd.a21280305.pedrocorreia.whatsupp.SharedMessage;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.Strings;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Client;
import pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic.Situation;

class Notification extends Thread {
    Client client;
    ConsoleUI console;
    SharedMessage notification;
    String notificationMessage;

    public Notification(Client client, ConsoleUI console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void run() {
        while (true) {
            notification = client.getNotification();
            // System.out.println(notification);
            // if (notification.equals("erro")) {
            // System.out.println("Trying another server");
            // client.contactServerManager();
            // Platform.runLater(() -> update());
            // }
            notificationMessage = notification.getMsg();
            if (notification.getMsgType() == Strings.USER_FAILED_LOGIN) {
                notificationMessage += " (" + notification.getMsgType().name() + ")";
                // Platform.runLater(() -> update());
            }

            // Platform.runLater(() -> update());
            notification = null;
            notificationMessage = "";
        }
    }
}

public class ConsoleUI {
    Client client;
    static boolean firstRun = true;
    static boolean errorConnection = false;
    static boolean running = true;
    Scanner sc = new Scanner(System.in);

    public ConsoleUI(Client client) {
        this.client = client;
    }

    private void enter() {

    }

    private void contact() {
        int option = 0;
        if (firstRun) {
            firstRun = false;
            System.out.println("Welcome to whatsupp!");
        }
        System.out.println("Choose the option you want.");
        if (!errorConnection) {
            System.out.println("1. Connect\n2. Quit");
        } else {
            System.out.println("1. Try Connection Again\n2. Quit");
        }
        if (sc.hasNextInt()) {
            option = sc.nextInt();
        } else {
            System.out.println("Enter a valid input.");
        }
        switch (option) {
            case 1:
                client.contactServerManager();
                errorConnection = true;
                break;
            case 2:
                running = false;
                break;
            default:
                System.out.println("Enter a valid option.");
                break;
        }
    }

    private void initialOptions() {
        Notification n = new Notification(client, this);
        n.start();
        int option = 0;
        System.out.println("\nConnected successfully!!!");
        System.out.println("1. Login\n2. Register\n3. Quit");
        if (sc.hasNextInt()) {
            option = sc.nextInt();
        } else {
            System.out.println("Enter a valid input.");
        }
        switch (option) {
            case 1:
                client.initialStatus("login");
                break;
            case 2:
                client.initialStatus("register");
                break;
            case 3:
                running = false;
                break;
            default:
                System.out.println("Enter a valid option.");
        }
    }

    private void login() {
        String username;
        String password;

        Console console = System.console();
        username = new String(console.readLine("Please enter your username: "));
        password = new String(console.readPassword("Please enter your password: "));
        client.login(username, password);
    }

    private void register() {
        String fname;
        String lname;
        String username;
        String password;
        String confPassword;
        Console console = System.console();
        fname = new String(console.readLine("Please enter your first name: "));
        lname = new String(console.readLine("Please enter your last name: "));
        username = new String(console.readLine("Please enter your username: "));
        password = new String(console.readPassword("Please enter your password: "));
        confPassword = new String(console.readPassword("Please confirm your password: "));
        client.register(username, password, confPassword, fname, lname);
    }

    private void userState() {
        System.out.println("You successfully logged in.");
        System.out.println("\nChoose the option you want: ");
        System.out.println("1. Send a message.");
        System.out.println("2. See friends list.");
        System.out.println("3. Create a group.");
        System.out.println("4. Ask user for friendship.");
        System.out.println("5. Join a group.");
        System.out.println("6. Change account settings.");
    }

    public synchronized void prints() {
        while (running) {
            Situation sit = client.getAtualState();
            switch (sit) {
                case ENTER_STATE:
                    enter();
                    break;
                case CONTACT_SERVER_MANAGER:
                    contact();
                    break;
                case INITIAL_OPTION:
                    initialOptions();
                    break;
                case LOGGED_IN:
                    userState();
                    break;
                case LOGIN_USER:
                    login();
                    break;
                case REGISTER_USER:
                    register();
                    break;
                default:
                    break;

            }
        }
        // initialOptions();
    }
}
