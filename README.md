# Whatsupp
### Project Goal
    - Distributed programming
    - Database management
    - Multithread programming in Java
    - Concurrency in Java
    - Client application using JavaFX framework

### Summary
This project is distributed between three apps:
- A Client that communicates to a server. The goal of this client is to send messages between users
or between groups created by users.
    - Client <-UDP-> Server Manager
    - Client <-TCP-> Server
- A Server responsible to communicate the changes to a MySQL database.
    - Server <-UDP-> Server Manager
    - Server <-TCP-> Client
- A Server Manager to register active servers and redirect clients to them.
    - Server Manager <-UDP-> Servers, Clients
    - Server Manager can receives through UDP directly or by Multicast.

### Run
Download the latest release. There will be the `.jar` files compiled to run.
JavaFX is required.
The script to create the MySQL database is on the root directory of this repo.

Run Server Manager
```
java -jar ServerManager.jar <UdpListeningPort>
```

Run Server
```
java -jar Server.jar <dbAddress> <ServerManagerAddress> <ServerManagerPort>
```

Run Server (Multicast)
```
java -jar Server.jar <dbAddress>
```

Run Client
```
java --module-path "path/to/javafx" --add-modules javafx.controls,javafx.fxml -jar whatsupp.jar <ServerManagerAddress> <ServerManagerPort>
```

