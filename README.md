# SoundSoulmate – Personalized Music Discovery App (Full-Stack Java + Android)

**SoundSoulmate** is a full-stack mobile application that personalizes music recommendations based on user preferences and real-time Spotify data. Built with Android (Java) on the client side and Spring Boot on the backend, the app enables users to enter a seed song and receive curated suggestions via a custom-built recommendation engine. It includes Spotify OAuth integration, live chat via WebSockets, and user analytics for enhanced engagement.

Developed collaboratively using a structured development lifecycle, the project balances recommendation accuracy, responsive UI, and foundational security practices.

## Security-Conscious Design Principles

- **Spotify OAuth Integration**  
  Implements secure OAuth 2.0 token handling and session validation to ensure user account protection when linking Spotify.

- **Scoped API Access**  
  All Spotify and internal API calls are scoped by access token, ensuring least-privilege design in both music data retrieval and user profile interaction.

- **WebSocket Session Control**  
  Chat system enforces authenticated WebSocket connections and isolates messages between authorized users to prevent spoofing or cross-user injection.

- **Firebase Data Validation Rules**  
  Realtime database access is protected by authentication state and structured path-level access control to enforce proper authorization.

- **Input Validation & Data Sanitization**  
  Backend endpoints apply strict validation for all song, user, and message inputs to mitigate injection and data integrity issues.

## Features

- **Personalized Music Recommendations**  
  Input a song to receive curated tracks using a Recurrent Recommender Network (RRN)-based logic trained on user preference patterns.

- **Spotify Account Linking**  
  Authenticate via Spotify to import top artists, saved tracks, and allow playlist creation or modification directly from the app.

- **Real-Time Chat System**  
  In-app messaging system allows users to chat with friends using secure WebSocket communication.

- **User Profiles & Analytics**  
  Users can view personalized music stats including top songs, listening history, and discovery preferences.

- **Social Discovery & Matching**  
  Browse other users’ profiles and connect based on shared music tastes and interaction history.

## Architecture Overview

### Frontend

- **Platform**: Android (Java, XML Layouts)  
- **Features**:
  - Spotify login and token handling
  - Input interface for music discovery
  - Playlist interaction and display
  - Gesture controls and user profile views

### Backend

- **Platform**: Spring Boot (Java)  
- **Database**: Firebase Realtime Database  
- **Components**:
  - REST endpoints for music discovery and profile access
  - WebSocket server for chat and live communication
  - Spotify OAuth 2.0 token exchange and API proxying
  - RRN-based recommendation module for song selection

## Setup Instructions

### Android Frontend

1. Clone the repository:
   ```bash
   git clone https://github.com/GrantPierce94/SoundSoulmate.git
Open /Frontend in Android Studio
Sync Gradle and run the app using an Android emulator or device

Backend Server
Install Java 11+
Configure Firebase credentials and environment variables
Navigate to /Backend and build:
./gradlew build
java -jar build/libs/soundsoulmate-server.jar

Project Structure
```
├── Frontend/              # Android app
│   ├── activities/        # Login, Discovery, Profile, Chat
│   ├── services/          # Spotify access, chat client
│   └── ui/                # Layouts and screen flows
│
├── Backend/               # Spring Boot server
│   ├── controllers/       # REST endpoints
│   ├── services/          # Recommender logic and chat handling
│   └── spotify/           # OAuth integration and API proxy
│
├── sql/                   # Firebase rules and mock data (if applicable)
└── README.md              # Project documentation
```

Development & Testing Workflow
Manual black-box testing on emulators and devices
Token lifecycle and authentication failure testing
Spotify API mock testing
GitHub Actions CI/CD for backend builds and linting
Milestone-driven waterfall development process

Tools & Technologies
Frontend: Android SDK, Java, XML Layouts
Backend: Spring Boot, REST APIs, WebSockets
Database: Firebase Realtime Database
Integrations: Spotify Web API (OAuth 2.0)
CI/CD: GitLab
Version Control: Git + GitHub

Project Management: GitHub Projects, milestone planning

Contributors
- **Grant Pierce** ([GitHub: @GrantPierce94](https://github.com/GrantPierce94)) – *Backend & DevOps Lead*  
  - Integrated Spotify OAuth and playlist functionality  
  - Developed backend services and deployed CI/CD pipelines
  - Led application packaging and system testing

- **Jack** – *Frontend Developer & Chat System Engineer*  
  - Built Android UI for login, chat, and profile components  
  - Implemented real-time WebSocket chat system  
  - Designed swipe/gesture-based music interface

- **Danny** – *Backend Developer*  
  - Developed RRN logic for personalized song suggestions
  - Tuned backend responses and Firebase schema
  - Contributed to model training and testing

License
This project is licensed under the MIT License. See the LICENSE file for more details.
