# WhatsUpp
## Pratical Assignment of Distribuited Programming, ISEC 2021/2022

### Java/MySQL
    - Distributed programming
    - DB management
    - Concurrency in Java

### TO-DO
#### Server Manager
- [ ] Multicast

#### Server
- [X] Register on Server Manager
- [X] Ping Server Manager
- [X] Create connection with clients (1 thread for each client)
- [X] Connect to db (on client connecting)
- [X] Login user
- [X] Register user 
- [ ] Return friends list
- [ ] Return message list
- [ ] Return groups list

#### Client
- [X] Connect to Server
- [X] Reconnect to Server
- [X] Login
- [X] Register
- [X] Notification until login
- [X] How to get a notification after login
    - [ ] A thread that receives the responses from server?
    - [X] A class derived from Data that implements a runnable?
- [ ] After login request for all info
    - [X] Get friends
    - [-] Get messages
    - [-] Get groups
    - [-] Get files
#### Shared
- [X] Server Manager <-> Server Communication
- [X] Server Manager <-> Client Communication
- [X] Client <-> Server Communication