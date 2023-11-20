# Hangman-game
Java Hangman Game - Client-Server Application
This project is a Java-based implementation of the classic Hangman game, designed with a client-server architecture. It consists of two main components: the server (Servidor) and the client (Cliente).

Server (Servidor):
The server application is responsible for managing the game logic. It selects a secret word from a predefined list and handles guess attempts from the client. For each guessed letter, the server checks its presence in the secret word, updates the game state, and sends feedback to the client. The server supports multiple game sessions with the ability to start new games and manage guess attempts.

Client (Cliente):
The client application provides a graphical user interface (GUI) for the user to interact with the game. It allows players to guess letters, displays the current state of the word (with underscores for unguessed letters), and shows the remaining number of attempts. The client communicates with the server to send guesses and receive updates, offering a responsive and interactive gaming experience.

Key Features:

Client-server communication using Java sockets.
Dynamic handling of guess attempts and game state updates.
GUI for client-side interaction, enhancing user experience.
Scalable architecture allowing for potential future enhancements like multiple player support or a larger word database.

Technologies Used:

Java for core application development.
Java Swing for the client GUI.
Networking with Java Sockets for client-server communication.
