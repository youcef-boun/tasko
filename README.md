# **Tasko – Effortless Task Management for Everyday Productivity**

> A modern Android app to organize, prioritize, and track your daily and upcoming tasks with a beautiful, intuitive interface.

---

## 📝 Description

Tasko is a productivity-focused Android application designed to help users efficiently manage their daily and upcoming tasks.

With a clean, modern UI built using **Jetpack Compose**, Tasko enables users to add, update, search, and organize tasks by priority and due date. The app leverages a local Room database for persistent storage, ensuring tasks are always available offline. Users can toggle between pending and completed tasks, attach images, and use a swipeable calendar to plan ahead.

> **Tasko is ideal for anyone seeking a robust, real-world task management solution on Android.**

---

## 🔄 User Flow

1. Launch the app to view today's tasks.  
2. Add new tasks with title, description, priority, due date, and optional image.  
3. Switch between "Today", "Upcoming", and "Search" views using the bottom navigation bar.  
4. Use the calendar to browse and manage tasks for any date.  
5. Mark tasks as completed, update, or delete them as needed.  
6. Search tasks by title or description for quick access.

---

## ✨ Features

- 📅 **Today & Upcoming Views** – Easily switch between tasks due today and those scheduled for the future.  
- 🗂 **Task CRUD** – Add, update, and delete tasks with support for title, description, priority (1–4), due date, and image attachment.  
- 🔍 **Search** – Instantly filter tasks by title or description.  
- 🏷 **Priority Management** – Assign and visually distinguish tasks by priority level.  
- ✅ **Mark as Completed** – Toggle tasks between pending and completed states.  
- 🗓 **Swipeable Calendar** – Select any date to view and manage tasks for that day.  
- 🖼 **Image Attachments** – Add images to tasks for richer context.  
- 🌙 **Dark & Light Theme** – Seamless support for system dark/light mode.  
- 🏃 **Offline-First** – All data is stored locally using Room, ensuring full offline functionality.  
- 🧩 **Modern MVVM Architecture** – Clean separation of concerns for maintainability and scalability.

---

## 📸 Screenshots

<div align="center">
  <img src="app/src/main/java/com/example/tasko/Screenshots/photo_2025-07-20_20-13-40.jpg" width="200" alt="Search" />
  <img src="app/src/main/java/com/example/tasko/Screenshots/photo_2025-07-20_20-13-43.jpg" width="200" alt="Upcoming" />
  <img src="app/src/main/java/com/example/tasko/Screenshots/photo_2025-07-20_20-13-46.jpg" width="200" alt="Today" />
  <img src="app/src/main/java/com/example/tasko/Screenshots/photo_2025-07-20_20-13-48.jpg" width="200" alt="Add Task" />
</div>

---

## ⚙️ Tech Stack

| Component             | Technology                             |
|----------------------|-----------------------------------------|
| **Language**          | Kotlin                                 |
| **UI Framework**      | Jetpack Compose (Material 3)           |
| **Architecture**      | MVVM (Model-View-ViewModel)            |
| **Persistence**       | Room Database (with migration support) |
| **Image Loading**     | Coil                                   |
| **Coroutines**        | For async and reactive flows           |
| **DI**                | Manual via ViewModelFactory            |
| **Testing**           | JUnit, AndroidX Test                   |
| **Others**            | Compose Navigation, Material Icons     |

---

## 🚀 What I Learned / Key Challenges Solved

Building Tasko deepened my expertise in modern Android development, especially with Jetpack Compose and MVVM architecture.

I implemented a robust offline-first data layer using Room, including schema migrations and reactive flows with Kotlin Coroutines. Designing a swipeable calendar and dynamic UI with Compose required advanced state management and custom composables. I also tackled challenges around image handling, efficient search/filtering, and providing a seamless user experience across dark and light themes.

> This project reflects my ability to deliver **production-quality**, maintainable code and intuitive UX in a real-world mobile application.
