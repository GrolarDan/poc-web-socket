# POC WebSocket Server

This application is a proof of concept for a WebSocket server that allows multiple clients to connect and exchange messages in real-time.  
It is built using Java 21 and the Spring Boot framework.

It contains two modules:
1. **chat-client**: This module is a JavaFX application that serves as the client for PJ
2. **chat-server**: This module is a Spring Boot application that serves as the WebSocket server and website client for Players

The communication is broadcasted privately between PJ and Players.  
There is no public chat available between Players.  
The result is, that PJ can see all messages sent by Players, but Player can see messages sent by PJ targeted only to them.

## Pre-requisites

[Java 21](https://adoptium.net/temurin/releases/)
[Apache Maven](https://maven.apache.org/download.cgi)

## Chat application

**ChatMessage**
```json
{
  "type": "string",
  "sender": "string",
  "receiver": "string",
  "content": "string"
}
```

Where `type` can be one of the following:
- `CHAT` - a message sent by a player or PJ to another player or PJ
- `JOIN` - a player has joined the chat
- `JOIN_PJ` - PJ has joined the chat
- `LEAVE` - a player or PJ has left the chat

### Chat Server

The chat server publish two REST endpoints:
- `GET /chat/pjUserName` - returns the username of the PJ
- `GET /chat/users` - returns the list of connected players

and three WebSocket endpoints:
- `/ws?username={username}` - used by the client to connect to the server
- `/chat.addPjUser` - used by the PJ to connect to the server
- `/chat.addUser` - used by the players to connect to the server
- `/chat.sendMessage` - used by the players and PJ to send messages to each other

### Chat Client - PJ

The PJ client is a JavaFX application that connects to the chat server and allows the PJ to send messages to players.

1. Connect to the chat server using the WebSocket endpoint `/ws?username={pjUserName}`
2. On connect
   1. get the list of subscribed players from the REST endpoint `GET /chat/users`
   2. send a message to the WebSocket endpoint `/chat.addPjUser` to notify the server and all subscribers that PJ is connected
   ```json
   {
     "type": "JOIN_PJ",
     "sender": "{pjUserName}"
   }
   ```
3. Subscribe to the WebSocket endpoint `/topic/public` to receive a notification about added new player to enhance the list of players (listen only to `JOIN` messages)
4. Subscribe to the WebSocket endpoint `/user/queue/private` to receive messages from players
5. Send messages to players using the WebSocket endpoint `/app/chat.sendMessage`
    ```json
    {
      "type": "CHAT",
      "sender": "{pjUserName}",
      "receiver": "{playerUserName}",
      "content": "{messageContent}"
    }
    ```

### Chat Client - Player

The Player client is a html5 application that connects to the chat server and allows the player to send messages to PJ.

1. Connect to the chat server using the WebSocket endpoint `/ws?username={playerUserName}`
2. On connect
   1. get the username of the PJ from the REST endpoint `GET /chat/pjUserName`
   2. send a message to the WebSocket endpoint `/chat.addUser` to notify the server and all subscribers that Player is connected
   ```json
   {
     "type": "JOIN",
     "sender": "{playerUserName}"
   }
   ```
3. Subscribe to the WebSocket endpoint `/topic/public` to receive a notification about added PJ (listen only to `JOIN_PJ` messages)
4. Subscribe to the WebSocket endpoint `/user/queue/private` to receive messages from PJ
5. Send messages to PJ using the WebSocket endpoint `/app/chat.sendMessage`
    ```json
    {
      "type": "CHAT",
      "sender": "{playerUserName}",
      "receiver": "{pjUserName}",
      "content": "{messageContent}"
    }
    ```

