# MEEBOT

MeeBot is a personal task management application that helps users keep track of their tasks. The project was developed as part of the TIC2002 Introduction to Software Engineering module, with the aim of applying sound software design principles.

---

## Table of Contents
1. [Project Description](#project-description)
2. [Installation and Setup](#installation-and-setup)
3. [First Launch Behaviour](#first-launch-behaviour)
4. [Using MeeBot](#using-meebot)
5. [Credits](#credits)


## Project Description
MeeBot helps users manage three main types of tasks - Todo, Deadline, and Event with support for recurring schedules.  
Users can add, list, mark, delete, search, filter, and sort tasks, all from a simple interface. The application supports both text commands and GUI controls, and all data is saved automatically to a JSON file.

Key aspects of the project:
- **Dual interface modes** (CLI & GUI)
- **Recurring task support** (daily, weekly, monthly, yearly)
- **Natural-language and standard date parsing**
- **Stable sorting** and **advanced task search/filter**
- **Persistent storage** using JSON


## Installation and Setup

### Requirements
- **JDK 17**
- **Gradle** or **Maven**

### Steps
```bash
# Clone the repository
git clone https://github.com/thebunnymama/meebot.git
cd meebot
```


### Running MeeBot
MeeBot can be launched in two modes:

**Command-Line Interface (CLI)**
```bash
# Using Gradle
./gradlew run --args="cli"

# Or using Maven
mvn -q package
java -jar target/meebot.jar
```
**Graphical User Interface (GUI)**
If no arguments are provided, MeeBot launches in GUI mode by default.
```bash
./gradlew run
# or
java -jar target/meebot.jar
```


## First Launch Behaviour
When MeeBot runs for the first time:
- A `.meebot` folder is created in your user directory.
- Inside it, a `data/` subfolder is created, containing `tasks.json` where your tasks are stored.
- Your tasks persist between sessions as long as `tasks.json` remains present.

**Important:** If `data/tasks.json` is missing or deleted, previously saved tasks are **not** reloaded (they are effectively lost!). A new, empty `tasks.json` will be created on the next run.

In subsequent runs, MeeBot may also create a `logs/` subfolder inside `.meebot`:
- `logs/task_load_errors.log` records tasks that failed to load to help diagnose issues.
- You can delete them if not needed; MeeBot will recreate logs when required.


## Using MeeBot

### Feature
| Command | Syntax | Example | Notes |
|---|---|---|---|
| Add Todo | `todo <description>` | `todo water plants` | Adds a simple todo. |
| Add Deadline | `deadline <desc> /by <date-time>` | `deadline pay bills /by 1/11/2025 2359` | Time optional (e.g., `1/11/2025`). |
| Recurring Deadline | `deadline <desc> /by <date-time> /repeat <interval> <count>` | `deadline pay bills /by 1/11/2025 /repeat monthly 12` | `interval`: `daily`, `weekly`, `monthly`, `yearly`. |
| Add Event | `event <desc> /from <date-time> /to <date-time>` | `event yoga /from 1/11/2025 1000 /to 1/11/2025 1100` | Start/end can include time or just date. |
| Recurring Event | `event <desc> /from <date-time> /to <date-time> /repeat <interval> <count>` | `event birthday /from 1/11/2025 /to 1/11/2025 /repeat yearly 6` | `interval`: `daily`, `weekly`, `monthly`, `yearly`. |
| List | `list` | `list` | Displays all tasks. |
| Mark   | `mark <index>` | `mark 1` | Use `list` to see task indices. |
| Unmark | `unmark <index>` | `unmark 1` | Reverts completion. |
| Delete | `delete <index>` | `delete 2` | **Irreversible action.** |
| Search | `search <kw1> <kw2> ...` | `search yoga plants` | OR-match across description. |
| Filter | `filter task:<type> & done:<true\|false> & date:<DD/MM/YYYY>` | `filter task:deadline & done:false & date:1/11/2025` | Use 1–3 conditions. Types: `todo`, `deadline`, `event`. |
| Sort | `sort /by <date \| status>` | `sort /by date` | Date: earliest first; Status: pending first. Stable sort. |
| Exit | `bye` | `bye` | Saves and exits. |


### Date & Time Input
MeeBot accepts natural language and standard formats for dates/times (works with `/by`, `/from`, `/to`).
- Dates: DD/MM/YYYY, YYYY-MM-DD (e.g., 1/11/2025, 2025-11-01)
- Times: HHmm, HH:mm, 3pm (e.g., 2359, 23:59)
- Natural language: today, tomorrow, this friday, next wednesday, in X days, end of this month, first monday of next month
```bash
# Examples
deadline return call /by 3pm
deadline prepare slides /by in 3 days
deadline file claims /by end of this month
event vacation /from friday /to next sunday
event hackathon /from next monday 10am /to next tuesday 12pm
```

### Common Issue
| Issue| Likely Cause | How to Fix |
|---|---|---|
| `Unrecognized command` | Typo or wrong syntax | Use correct forms: `todo <desc>`, `deadline <desc> /by <date-time>`, `event <desc> /from <start> /to <end>`. **Tip:** type the command keyword (e.g., deadline) or help to see syntax and examples. |
| `Invalid repeat interval or count` | Wrong repeat syntax | Use `/repeat <daily, weekly, monthly, yearly> <count>`, e.g., `/repeat monthly 12`. |
| `Could not parse date/time` | Unsupported or ambiguous date phrase | Use supported formats: see above section. |
| `Start must be before end` | `/from` is not earlier than `/to`    | Ensure start < end (both date and time), e.g., `/from next monday 10am /to next monday 12pm`. |
| `No task found` | Index outside current list range | Run `list` to see indices, then use a valid number. |
| Some tasks missing after restart | One or more entries failed to load | Check `~/.meebot/logs/task_load_errors.log`. Fix malformed lines; valid tasks still load. |
| All tasks gone after restart | `data/tasks.json` missing or deleted | Verify file exists: macOS/Linux `~/.meebot/data/tasks.json`, Windows `%USERPROFILE%\.meebot\data\tasks.json`. If missing, data is lost; a new file will be created. |
| Cannot save tasks (`Access denied`) | Folder permissions issue | Ensure write access to `~/.meebot/`. |
| GUI fails to start (JavaFX error) | Environment/JavaFX setup problem | Test CLI: `./gradlew run --args="cli"`. Ensure Java 17+. |


## Credits
Author: **Tan Jia Huan** — TIC2002 (AY25/26)

**Acknowledgements**
- Teaching staff for guidance and feedback.
- Documentation: [Java](https://docs.oracle.com/en/java/), [JavaFX](https://openjfx.io/), [Gradle](https://docs.gradle.org/), [JUnit 5](https://junit.org/junit5/).
- Interface icons: [Flaticon by Freepik](https://www.flaticon.com/uicons/interface-icons) (used with attribution per Flaticon terms).

**Libraries / Assets**
- JavaFX (GUI), JUnit 5 (tests).  
- Emoji via system fonts.
- Flaticon by Freepik (interface icons)
