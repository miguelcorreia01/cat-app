# Cat App

A modern Android application built with Jetpack Compose that allows users to explore cat breeds, search through them, and save their favorites.  
This project demonstrates Clean Architecture principles and modern Android development practices.

---

## Index

1. [Tech Stack](#-tech-stack)
2. [Features](#-features)
3. [Architecture](#-architecture)
4. [Development Strategies](#-development-strategies)
   - [1. Dependency Injection](#1-dependency-injection)
   - [2. Architecture Decisions](#2-architecture-decisions)
   - [3. Data Management Strategy](#3-data-management-strategy)
   - [4. UI/UX Design Decisions](#4-uiux-design-decisions)
   - [5. Performance Optimizations](#5-performance-optimizations)

---

## Tech Stack

- Jetpack Compose
- Kotlin
- Hilt
- Retrofit
- Room

 ## Features
  **Cat Breeds Discovery**: Browse through a comprehensive list of cat breeds
- **Search Functionality**: Real-time search through breed names
- **Favorites System**: Save and manage your favorite cat breeds locally
- **Detailed Information**: View comprehensive details about each breed
- **Offline Support**: Access saved data even without internet connection
- **Pagination**: Efficient loading

---

## Architecture
The app follows MVVM Clean Architecture principles with clear separation of concerns:

- **Domain Layer**: Contains business logic and models
- **Data Layer**: Handles data operations
- **UI Layer**: UI components and ViewModels using Jetpack Compose

---

## Development Strategies
### 1. **Dependency Injection**
- Used Hilt for DI across ViewModels, repositories, and data sources.

### 2. **Architecture Decisions**
- Adopted Clean Architecture for scalability and testability.
- Used Flow and StateFlow for reactive data updates.

### **3. Data Management Strategy**
- **Repository Pattern**: Centralized data access through repositories
- **Room Database**: Local storage for offline support and favorites
- **Retrofit**: HTTP client for API communication with automatic JSON parsing

### **4. UI/UX Design Decisions**
- **Grid Layout**: 2-column grid for optimal breed display
- **Search Functionality**: Real-time filtering
- **Favorites System**: Heart icon toggle with visual feedback

### **5. Performance Optimizations**
- **Paging**: Efficient memory usage with lazy loading
- **Image Loading**: Coil for efficient image caching and loading
- **Background Processing**: Coroutines for async operations
