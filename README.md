# WhatsUpp
## Pratical Assignment of Distribuited Programming, ISEC 2021/2022

### Java/MySQL
    - Distributed programming
    - DB management
    - Concurrency in Java

### TO-DO
#### Server Manager
- [ ] Multicast
- [X] Sends notifications to all active servers

#### Server
- [X] Register on Server Manager
- [X] Ping Server Manager
- [X] Create connection with clients (1 thread for each client)
- [X] Connect to db (on client connecting)
- [X] Login user
- [X] Register user 
- [X] Return friends list
- [X] Return message list
- [] Return groups list
- [X] Sends notifications on message sent and deleted to Server Manager
- [] Send notifications on register

#### Client
- [X] Connect to Server
- [X] Reconnect to Server
- [X] Login
- [X] Register
- [X] Notification until login
- [X] How to get a notification after login
    - [ ] A thread that receives the responses from server?
    - [X] A class derived from Data that implements a runnable?
- [X] Get friends
- [] Get friends requests
- [X] Get messages
- [] Get groups
- [] Get files
- [X] Send messages
- [X] Notifications for messages sent and deleted
- [] Notifications for friends requests and new users
- [] Beautify panes

#### Shared
- [X] Server Manager <-> Server Communication
- [X] Server Manager <-> Client Communication
- [X] Client <-> Server Communication