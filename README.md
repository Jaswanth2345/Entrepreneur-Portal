# BizVid 📱🚀

*My first Android app development project using Android Studio.*

BizVid is a modern and intuitive Android application designed to fuel the entrepreneurial spirit by offering a centralized hub where users can access inspirational content, business ideas, startup journeys, and success stories.

Whether you’re an aspiring founder, a student exploring the startup ecosystem, or simply curious about the business world, BizVid serves as your go-to platform for learning, motivation, and exploration.

Download the APK file and run.

---

## ✨ Features

- *Splash Screen*: Engaging welcome screen with smooth transition to login.
- *User Authentication*:
  - *Sign Up*: New users can easily create an account.
  - *Log In*: Secure login with saved credentials using Firebase.
- *Home Screen*:
  - *Video Directory*: Curated entrepreneurial videos via YouTube.
  - *Locate Startups*: Discover startups based on location, both locally and globally.
  - *Roadmap Generator*: AI-powered guide to manage startup investments (powered by Gemini API).
  - *ChatBox (Chatbot)*: Smart virtual assistant for business-related queries (powered by Gemini API).
- *Profile Customization*: Personal settings with theme options.
- *Motivational Content*: Dynamic success stories and entrepreneurial journeys.

---

## 🛠 Tech Stack

Android Studio (Kotlin): Primary development environment and language for building the app.

YouTube Data API: Integrated to embed and stream motivational videos of successful entrepreneurs.

Gemini API (Google's AI Model): Used for chatbot implementation and generating personalized startup roadmaps through REST API calls.

Postman: Utilized for testing and validating RESTful API calls including Gemini API and Firebase endpoints.

Firebase Authentication & Realtime Database: Managed secure user login and real-time data syncing.

SharedPreferences: Implemented for local data storage to retain user preferences and session data.

Modern UI/UX: Designed with edge-to-edge layouts, responsive views, and smooth navigation for an engaging user experience.

---

## 📷 Screenshots

(You can add screenshots here later, if you want. Example placeholder below.)

| Splash Screen | Home Screen | Locate Startups |
| :-----------: | :----------: | :-------------: |
| (image here)  | (image here) | (image here)    |

---

## 📚 How to Run

1. Clone this repository:
   bash
   git clone https://github.com/your-username/BizVid.git
2. Open the project in **Android Studio**.
3. In your project's `local.properties` file, add your Gemini API key:
   bash
   GEMINI_API_KEY=YOUR_API_KEY
4. Build and run the project on an emulator or real Android device.

## Important:

- Without setting your Gemini API key, the ChatBot and Roadmap Generator features will not function.

## 📜 License
- This project is for educational purposes.
