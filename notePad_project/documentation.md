# Smart Notepad - Project Documentation

---

## 1. Overview

The **Smart Notepad** is a **JavaFX-based text editor** designed to provide a simple yet powerful interface for creating, editing, and saving text documents. It includes features such as **theming, real-time statistics, and basic formatting** to enhance user productivity.

---

## 2. Objective

The primary goals of this project are:

- Create a **user-friendly text editor** with essential features such as Open, Save, and Format.
- Implement **real-time statistics** including word count, sentence count, and reading time.
- Provide **customizable themes** (Light, Dark, Blue, and Green) to improve user experience.
- Ensure **cross-platform compatibility** on Windows, macOS, and Linux using JavaFX.

---

## 3. Significance

### Educational Significance
- Demonstrates concepts of **JavaFX**, **Object-Oriented Programming (OOP)**, and **event-driven programming**.

### Practical Significance
- Serves as a **lightweight alternative** to large text editors for quick note-taking.

### Extensibility
- Can be expanded with advanced features such as:
  - Spell checking
  - Syntax highlighting
  - Find and replace
  - Plugin support

---

## 4. Problem Statement

### Conflict Between JDKs

The development laptop contains **two separate JDK installations**:

1. One JDK used for **Vaadin** projects.
2. Another JDK configured for **JavaFX** applications.

This causes **PATH conflicts**, making JavaFX applications fail to launch.

### Solution

A batch file named `run_javafx.bat` explicitly sets the correct JavaFX JDK path before executing the application, ensuring the proper environment is used.

---

# 5. Implementation Scope

The Smart Notepad is a desktop application built using JavaFX and includes the following functionality:

- Basic file operations:
  - New
  - Open
  - Save
  - Save As
  - Exit

- Text formatting:
  - Bold
  - Italic
  - Text Color
  - Font Size

- Real-time text statistics:
  - Word Count
  - Sentence Count
  - Estimated Reading Time

- Theme support:
  - Light
  - Dark
  - Blue
  - Green

---

## 6. Key Features

| Feature | Description |
|------|------|
| File Operations | New, Open, Save, Save As, Exit |
| Text Formatting | Bold, Italic, Text Color, Font Size |
| List Formatting | Bullet points (Numbered lists removed due to static `1.` issue) |
| Themes | Light, Dark, Blue, Green (applied via `ThemeManager`) |
| Real-Time Stats | Word count, sentence count, reading time (via `WordStats`) |
| Error Handling | User-friendly error dialogs (e.g., file not found) |

---

## 7. High-Level Description

### User Interface Components

#### Menu Bar
Contains the following menus:

- File
- Edit
- Format
- List
- View

#### Text Area
The main editing area where users write and edit text.

- Wrap text enabled
- Supports formatting and styling

#### Status Bar
Displays:

- Word count
- Sentence count
- Estimated reading time

---

### Workflow

1. User opens the application.
2. The default **Light Theme** is automatically applied.
3. The user types text.
4. Statistics update in real time.
5. The user saves the document.
6. The file is stored on disk (default folder: `notePad_project`).
7. The user can switch themes at any time.
8. The UI updates instantly to reflect the selected theme.

---

### Conflict Resolution

The `run_javafx.bat` file ensures the correct JavaFX JDK is used, preventing conflicts with the Vaadin JDK installation.

---

## 8. Code Explanation

### Core Classes

| Class | Purpose |
|------|------|
| `NotepadApp` | Main class responsible for UI setup, file operations, and event handling |
| `ThemeManager` | Applies and manages themes (Light, Dark, Blue, Green) |
| `WordStats` | Calculates and updates word count, sentence count, and reading time |

---

### Key Methods

| Method | Description |
|------|------|
| `setupTextAreaListeners()` | Listens for text changes and updates statistics |
| `applyTheme()` | Applies the selected theme to the Scene and TextArea |
| `updateStats()` | Recalculates word count, sentence count, and reading time |
| `confirmClose()` | Prompts the user to save unsaved changes before closing |
| `showTextColorPicker()` | Opens a color picker dialog to change text color |

---

### File Handling Methods

#### `saveFile()`
- Saves the current document.
- If the document is untitled, calls `saveFileAs()`.

#### `saveFileAs()`
- Opens a file chooser dialog.
- Allows the user to select a save location and file name.

#### `openFile()`
- Loads the selected file into the text area.

---

### Formatting Features

#### Bold and Italic
- Toggles font weight and font posture.

#### Text Color
- Uses JavaFX `ColorPicker`.
- Applies color through CSS using `-fx-text-fill`.

#### Font Size
Available sizes:

- 8
- 10
- 12
- 14
- 16
- 18
- 20

---

## 9. Conclusion

The **Smart Notepad** successfully demonstrates the development of a fully functional JavaFX text editor with essential text editing capabilities.

### Achievements

- Provides file management features.
- Supports text formatting and theming.
- Displays real-time writing statistics.
- Handles errors gracefully.
- Resolves JDK conflicts using `run_javafx.bat`.

### Future Enhancements

Potential features for future versions include:

- Find and Replace
- Multiple Tabs
- Auto Save
- Spell Checker
- Syntax Highlighting
- Plugin System

The project offers a clean, user-friendly interface and serves as an excellent foundation for more advanced desktop applications built with JavaFX.