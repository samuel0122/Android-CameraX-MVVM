# Android CameraX MVVM

*Capture photos, record videos, and preview media with modern Android development practices.*

This project demonstrates how to implement **CameraX** in an Android application using the **MVVM** (Model-View-ViewModel) view pattern, combined with a **Clean Architecture** architectural pattern. The project includes features like capturing photos, recording videos, and previewing media with modern Android development practices.

## 📋 Features

- 📸 **Capture Photos**: Take high-quality pictures using CameraX.
- 🎥 **Record Videos**: Record videos with CameraX and manage storage efficiently.
- 👀 **Media Preview**: Preview captured photos and videos.
- 🧱 **Clean Architecture**: Clean separation in layers: Domain, Data and View.
- 🎯 **Jetpack Components**: Uses Navigation, DataStore, and more.
- 💉 **Dependency Injection**: Powered by Dagger Hilt for modular and testable code.


## 🛠️ Tech Stack

- **Kotlin**: 100% Kotlin-based code.
- **CameraX**: For camera functionality.
- **Jetpack**:
  - Navigation Component
  - DataStore for managing preferences
  - ViewModel and LiveData for reactive UI updates
- **ExoPlayer**: For video playback.
- **Dagger Hilt**: Dependency Injection.


## 🚀 Getting Started

### Prerequisites
1. Android Studio (latest stable version recommended)
2. Android SDK 21+

### Setup
1. [ ] Clone the repository:
    ```bash
    git clone https://github.com/samuel0122/Android-CameraX-MVVM.git
    cd Android-CameraX-MVVM
    ```
2. [ ] Open the project in Android Studio.
3. [ ] Sync Gradle to download dependencies.
4. [ ] Run the app on a physical or virtual device.

## 📂 Project Structure

```
📂 app/
├── 📂 core/                # Utility classes and extensions
├── 📂 data/                # Data layer (e.g., repositories, DataStore management)
├── 📂 di/                  # Dependency injection modules
├── 📂 domain/              # Domain layer (Use Cases, models and mappers)
├── 📂 ui/                  # View layer (Fragments, Adapters, ViewModels)
│   ├── 📂 camera/          # Media capture (photo/video) fragments and its ViewModels
│   ├── 📂 common/          # Common components that are shared along the whole app
│   ├── 📂 mainPage/        # Main app view and its ViewModel
│   ├── 📂 preview/         # Media preview (photo/video) fragments and its ViewModels
├── 🗒️ CameraXMvvmApp       # Application class for global inyection
├── 🗒️ MainActivity         # Application entry point and navigation host
```

## 🧑‍💻 Author

- [@samuel0122](https://www.github.com/samuel0122)

