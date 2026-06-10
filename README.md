# Burnout Tracker - Gen Z Financial Burnout App

A mobile application designed to help Gen Z users track, understand, and recover from financial burnout.

## Features

- **Daily Stress Check-in** - Quick 30-second stress level logging
- **Mood Journal** - Track your emotional state alongside finances
- **Burnout Insights** - Visualize trends and correlations
- **Recovery Plans** - Personalized stress-reduction activities
- **Gamification** - Streaks and achievements to maintain habits

## Tech Stack

- **Language:** Kotlin 2.0+
- **UI:** Jetpack Compose with Material 3
- **Architecture:** MVVM + Clean Architecture
- **DI:** Hilt
- **Database:** Room
- **Backend:** Firebase (Auth, Firestore, Functions)
- **CI/CD:** GitHub Actions
- **Payments:** Razorpay + PayPal (RevenueCat later)

## Getting Started

1. Clone the repository
2. Add your `google-services.json` to the `app/` directory
3. Build and run on Android device or emulator

## CI/CD

- **build.yml** - Runs on PR/push, builds debug APK, runs tests
- **release.yml** - Triggered by version tags, builds release APK
- **deploy.yml** - Manual trigger, builds AAB, deploys to Play Store

## Project Structure

```
burnout-tracker/
├── app/src/main/java/com/burnouttracker/
│   ├── di/                    # Hilt modules
│   ├── data/
│   │   ├── local/            # Room database
│   │   ├── remote/           # Firebase services
│   │   └── repository/       # Repository implementations
│   ├── domain/
│   │   ├── model/            # Domain models
│   │   ├── repository/       # Repository interfaces
│   │   └── usecase/          # Use cases
│   └── ui/
│       ├── theme/            # Material 3 theme
│       ├── navigation/       # Navigation graph
│       ├── home/             # Home screen
│       ├── checkin/          # Daily check-in
│       ├── journal/          # Mood journal
│       ├── insights/         # Insights screen
│       ├── recovery/         # Recovery plans
│       └── profile/          # Profile screen
├── .github/workflows/        # CI/CD pipelines
└── build.gradle.kts          # Project configuration
```

## License

Private - All rights reserved.
