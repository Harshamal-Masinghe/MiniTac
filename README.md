# Mini Tac - A Variant of Tic Tac Toe for Android

## Introduction

The project **"Mini Tac"** is an Android game developed as a variant of the classic Tic Tac Toe game. It provides an engaging gaming experience for users on Android devices. Developed using Kotlin and Java, the game leverages Android SDK and Gradle for seamless building and dependency management.

## App Description

**"Mini Tac"** is designed as an offline game specifically for Android devices. It offers a user-friendly interface and intuitive gameplay, making it accessible to a wide range of users. The game features an onboarding screen to welcome users and provide a brief introduction to the game. Upon proceeding, users enter the main screen where they can initiate gameplay.

## Key Features

1. **Onboarding Screen**: Welcomes users and provides a brief overview of the game.
2. **Main Screen**: Allows users to start a new game.
3. **Gameplay**: Implemented on a 3x3 grid, where players (user as "X" and the app as "O") take turns to place their marks. The game ends when a player achieves three marks in a row horizontally, vertically, or diagonally, or when the grid is filled with no winner.
4. **Timer**: Tracks game duration and displays both current and record time.
5. **UI and Data Management**: Utilizes LiveData and the Observer pattern for updating the UI based on changes in game data. Game data is stored in a singleton object, and the game's UI is defined in XML layout files.
6. **Game Logic**: Implemented to check for a winner after each move, with the app's move delayed to simulate thinking time. Game data is saved using SharedPreferences.

## Target Audience

The target audience for **"Mini Tac"** includes Android device users of all ages who enjoy casual gaming experiences. Its simple yet engaging gameplay makes it suitable for both children and adults.

## Necessity

The development of **"Mini Tac"** addresses the need for entertaining and accessible offline gaming options on Android devices. With its intuitive design and classic gameplay, it provides users with a fun and engaging pastime, particularly in situations where internet connectivity may be limited.

## Conclusion

In conclusion, **"Mini Tac"** offers a compelling variant of the classic Tic Tac Toe game for Android users. With its user-friendly interface, intuitive gameplay, and thoughtful implementation of features such as timers and data management, the game provides an enjoyable gaming experience for users of all ages. As a project, its development showcases proficiency in Android app development using Kotlin and Java, along with effective utilization of Android SDK and related technologies.

## Instructions to Run the Game

To run **"Mini Tac"** on your Android device, follow these steps:

1. **Install Android Studio**: Ensure that you have Android Studio installed on your machine. If not, download and install it from the official [Android Developer website](https://developer.android.com/studio).
2. **Clone the Project**: Clone the project from the GitHub repository to your local machine using Git:
    ```bash
    git clone https://github.com/Harshamal-Masinghe/MiniTac.git
    ```
3. **Open the Project**: Open the cloned project in Android Studio by navigating to `File > Open` and selecting the project directory.
4. **Connect Your Android Device**: Connect your Android device to your machine using a USB cable. Alternatively, you can use the Android Emulator provided by Android Studio.
5. **Build and Run the Project**: In Android Studio, click on the 'Run' button (green play icon) to build and run the project. Android Studio will compile the code and install the game on your connected Android device or emulator.
6. **Launch the Game**: Once the installation is complete, open the game on your device or emulator. You can find the game icon in the app drawer or on the home screen.
7. **Enjoy Playing**: Start a new game from the main screen, follow the on-screen instructions, and enjoy playing **"Mini Tac"**!
