# Burnout Tracker - Product Requirements Document

## 1. Product Overview

**Burnout Tracker** is a Gen Z-focused Android app that helps users identify, track, and recover from burnout. It combines mental health tracking with financial awareness to show how stress impacts spending habits.

**Target Audience**: Gen Z professionals and students experiencing burnout
**Platform**: Android (Material 3)
**Backend**: Firebase (Auth, Firestore, Analytics)
**Payments**: Razorpay + PayPal

---

## 2. Core Features

### 2.1 Authentication
- Email/password sign up and login
- Google Sign-In (optional)
- Mock mode for development (mock@mock.com / mock)

### 2.2 Daily Stress Check-in
- 5-level stress scale (1-5) with labels
- Optional mood selection (emoji-based)
- Optional notes
- Save to Room database with timestamp
- Visual stress level indicator on home screen

### 2.3 Expense Tracking
- Log expenses with category (food, transport, health, entertainment, other)
- Amount in local currency
- Optional stress correlation (was this stress spending?)
- View spending by category
- Total spending summary

### 2.4 Journal
- Free-form text entries
- Linked to stress level at time of entry
- View by date
- Search entries

### 2.5 Recovery Plans
- Create recovery plans with goals
- Add actions to plans
- Track completion status
- View active vs completed plans

### 2.6 Insights & Trends
- Stress level trends over time (weekly/monthly)
- Spending patterns analysis
- Correlation between stress and spending
- Average stress calculation

### 2.7 Profile
- User display name
- Current streak (consecutive days of check-ins)
- Total entries count
- Achievements (optional gamification)
- Settings (notifications, theme)

---

## 3. UI Screens

### 3.1 Onboarding (4 pages)
- Welcome
- Stress tracking explanation
- Expense tracking explanation
- Get started

### 3.2 Home
- Current stress level display (large indicator)
- Quick action buttons (check-in, expense, journal)
- Recent entries preview
- Streak counter

### 3.3 Check-in
- Stress level selector (1-5)
- Mood emoji selector
- Notes text field
- Save button

### 3.4 Expense
- Amount input
- Category selector
- Stress correlation toggle
- Save button

### 3.5 Journal
- New entry button
- List of past entries
- Each entry shows: date, stress level, preview text

### 3.6 Insights
- Stress trend chart (placeholder for now)
- Spending summary
- Average stress

### 3.7 Recovery
- List of recovery plans
- Create new plan
- View plan details with actions

### 3.8 Profile
- User info display
- Streak and stats
- Sign out button

---

## 4. Data Models

### StressEntry
- id: String (UUID)
- timestamp: Long
- stressLevel: Int (1-5)
- mood: String (emoji)
- notes: String (optional)

### Expense
- id: String (UUID)
- timestamp: Long
- amount: Double
- category: String
- isStressRelated: Boolean
- notes: String (optional)

### RecoveryPlan
- id: String (UUID)
- title: String
- description: String
- isActive: Boolean
- createdAt: Long

### CompletedAction
- id: String (UUID)
- planId: String (FK)
- actionText: String
- completedAt: Long

---

## 5. Technical Architecture

### 5.1 Layers
- **UI**: Jetpack Compose + Material 3
- **Domain**: Use cases + Repository interfaces
- **Data**: Room + Firebase + Repository implementations

### 5.2 Dependencies
- Room (local persistence)
- Hilt (DI)
- Navigation Compose
- Firebase Auth + Firestore
- Gson (TypeConverters)
- Coroutines + Flow

### 5.3 Build
- compileSdk: 35
- minSdk: 26
- Kotlin 2.0.21
- JDK 17

---

## 6. Phased Implementation

### Phase 1: Core Data Flow (MVP)
- Wire up ViewModels to Room
- Connect Check-in screen to persistence
- Connect Expense screen to persistence
- Connect Journal screen to Room queries
- Display real data on Home screen

### Phase 2: Recovery & Insights
- Wire up Recovery screen
- Implement Recovery Plan CRUD
- Connect Insights to real trend data

### Phase 3: Polish & Release
- Profile with real stats
- Achievements system
- Error handling
- Loading states

---

## 7. Success Metrics

- App builds and runs without crashes
- All screens show real data (not hardcoded)
- Data persists across app restarts
- Stress check-in completes in < 30 seconds
- Expense logging completes in < 20 seconds
