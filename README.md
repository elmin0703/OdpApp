# ODP App – Open Data Portal BiH

ODP App is an Android application built with **Kotlin** and **Jetpack Compose** that allows users to explore, filter, and visualize datasets from the [Open Data Portal of Bosnia and Herzegovina (IDDEEA)](http://odp.iddeea.gov.ba).  
It offers offline functionality, powerful filtering/sorting, and interactive visualizations, making open data accessible on mobile devices.

---

## Features
**Splash & Home Screen** – intuitive entry point with dataset selection  
**Browse datasets** – e.g. population statistics, registered vehicles  
**Filtering & Sorting** – by entity, canton, municipality, etc.  
**Detail views** – inspect single records with full info  
**Favorites** – save and revisit important entries  
**Offline support** – local caching with Room database  
**Data visualization** – bar and donut charts for statistics  
**Share functionality** – share selected dataset entries via apps  

---

## Tech Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose + Material 3  
- **Architecture:** MVVM (Model–View–ViewModel) + UDF (Unidirectional Data Flow)  
- **Networking:** Retrofit + Kotlinx Serialization  
- **Database:** Room  
- **State Management:** Coroutines + Flow + ViewModel + StateFlow  
- **Navigation:** Jetpack Navigation for Compose  

---

## Architecture
The app follows **MVVM architecture** for clean separation of concerns:

- **UI Layer (View):** Composables render the state.  
- **ViewModel Layer:** Exposes UI state via `StateFlow`, handles user events.  
- **Data Layer (Repository + DAO):** Provides data from API (Retrofit) and local DB (Room).  
 

---

## Screens Overview
- **Splash Screen** – app initialization  
- **Home Screen** – dataset cards and navigation  
- **Dataset Detail Screen** – list with filtering, sorting, pagination  
- **Row Detail Screen** – detailed info, favorite & share actions  
- **Favorites Screen** – saved entries  
- **Statistics Screen** – charts and graphs
- **API Authentification Screen** - authentication token reset

---

## Installation

### Build Tools
1. Android Gradle Plugin
2. Jetpack Compose (enabled)
3. Min SDK: 26 Target SDK: 34
### How to Run
1. Clone the repository from GitHub 
2. Open the project in Android Studio
3. Run Gradle sync
4. Launch the app on an emulator or a physical device

### APK download
[Click here to download APK for installing app on your phone](https://drive.google.com/file/d/1i4z98PKQruJ81ONERbggfzBowj5N7Mf7/view?usp=sharing)

## Author
* Author: Elmin Bekrić
- Technologies used: Android SDK, Kotlin, Jetpack Compose
+ Development period: May–June 2025
