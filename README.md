SoundSoulmate – Personalized Music Discovery App
SoundSoulmate is a full-stack mobile application that connects users with new music based on their listening preferences. By integrating with Spotify’s API and leveraging a recommendation engine, users can input a seed song and receive curated suggestions. The app also includes real-time chat, friend interactions, and user profile analytics.

Features
Personalized Music Recommendations
Enter a song to receive curated tracks using a Recurrent Recommender Network (RRN)-based algorithm.

Spotify Integration
Link a Spotify account to fetch listening history, favorite tracks, and allow playlist creation directly from the app.

User Profiles
View personal listening statistics such as top artists and all-time favorite songs.

Real-Time Chat
Communicate with friends through an in-app messaging system built with WebSocket technology.

Social Discovery
Browse friends’ profiles and connect through shared musical interests.

Architecture Overview
This is a full-stack application with separation between frontend and backend components.

Frontend
Platform: Android (Java, XML Layouts)

Functionality:

User login and authentication

Song input and discovery UI

Spotify playlist linking and control

Swipe gesture support for playlist adds

Backend
Platform: Spring Boot (Java)

Database: Firebase Realtime Database

Functionality:

Recommender logic

WebSocket chat server

Spotify OAuth and API communication

User data management

DevOps & Development Workflow
Methodology: Waterfall approach, structured through requirements gathering, design, development, testing, and deployment.

Version Control: Git and GitHub with centralized branching and pull request workflow.

CI/CD: Implemented with GitHub Actions for automated builds and backend testing.

Project Management: Used GitHub Projects and internal planning documents to coordinate work and milestone completion.

Testing: Manual black-box testing on emulators and physical devices.

Installation
Android Frontend
Clone the repository:

bash
Copy
Edit
git clone https://github.com/GrantPierce94/SoundSoulmate.git
Open the /Frontend directory in Android Studio.

Sync Gradle and run the application using an emulator or Android device.

Backend Server
Ensure Java 11+ is installed.

Navigate to the /Backend directory:

bash
Copy
Edit
./gradlew build
java -jar build/libs/soundsoulmate-server.jar
Firebase credentials should be properly configured in the environment.

How to Use
Create an account or log in using your credentials.

Connect your Spotify account.

Enter a favorite song to receive recommendations.

Swipe through the suggestions and add songs to your Spotify playlist.

View profile statistics or interact with friends via chat.

Technologies Used
Android (Java)

Spring Boot

Spotify Web API

WebSockets

Firebase Realtime Database

GitHub Actions (CI/CD)

Contributors
Grant Pierce – Backend integration, Spotify OAuth, CI/CD

Jack – Android development, WebSocket implementation

Danny – Recommendation algorithm, Firebase configuration

License
This project is licensed under the MIT License. See the LICENSE file for details.
