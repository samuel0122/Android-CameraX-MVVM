# Android CameraX MVVM

*Capture photos, record videos, and preview media with modern Android development practices.*

This project demonstrates how to implement **CameraX** in an Android application using the **MVVM** (Model-View-ViewModel) view pattern, combined with a **Clean Architecture** architectural pattern. The project includes features like capturing photos, recording videos, and previewing media with modern Android development practices.

## 📋 Features

- 📷 **Photo & Video Capture**: Take high-quality pictures and record videos using CameraX, with full support for exposure control, flash, and front/back camera switching.  
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
│   ├── 📂 mediaStorage     # Handles loading and saving media on disk
│   ├── 📂 dataPreferences  # Manages preferences using DataStore (e.g. last media URI)
│   ├── 🗒️ MediaRepository  # Repository to access media files (photos & videos)
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


## 📸 Screenshots

| | | |
|---|---|---|
| <img src="screenshots/01-Empty_main.png" alt="Empty Main Screen" width="200"/> <br> **Main Screen (empty)** | <img src="screenshots/02-Camera_options.png" alt="Camera Options" width="200"/> <br> **Camera Options** | <img src="screenshots/03-Camera_picture.png" alt="Taking Picture" width="200"/> <br> **Taking a Picture** |
| <img src="screenshots/04-Camera_selfie.png" alt="Camera Selfie" width="200"/> <br> **Selfie Mode** | <img src="screenshots/05-Camera_picture_preview.png" alt="Picture Preview" width="200"/> <br> **Picture Preview** | <img src="screenshots/06-Main_with_picture.png" alt="Main with Picture" width="200"/> <br> **Main with Captured Photo** |
| <img src="screenshots/07-Camera_record.png" alt="Camera Record" width="200"/> <br> **Video Recording Screen** | <img src="screenshots/08-Camera_recording_selfie.png" alt="Recording Selfie" width="200"/> <br> **Recording in Selfie Mode** | <img src="screenshots/09-Camera_record_preview.png" alt="Recording Preview" width="200"/> <br> **Recorded Video Preview** |
| <img src="screenshots/10-Main_with_video.png" alt="Main with Video" width="200"/> <br> **Main with Saved Video** | <img src="screenshots/11-Recorded_video_view.png" alt="Recorded Video View" width="200"/> <br> **Playback Recorded Video** | |


## 🧑‍💻 Author

- [@samuel0122](https://www.github.com/samuel0122)

