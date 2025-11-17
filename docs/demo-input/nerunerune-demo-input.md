# Nerunerune App demo

## Launch Nerunerune App on desktop

```
cd desktop/demo
java -jar Nerunerune.jar
```

## Startup & Help

Displays all available commands and usage instructions

```
command
```

## Task Creation

### Add todo

Creates simple todo tasks

```
todo buy groceries
```

```
todo buy groceries
```

```
todo read new book
```

### Add Deadline

Creates deadline tasks with exact dates, times, and keyword shortcuts (tomorrow, next month)

```
deadline submit assignment /by 12-11-2025 2359
```

```
deadline pay electricity bill /by 18-12-2025 1800
```

```
deadline task for tomorrow /by tomorrow
```

```
deadline task for next week /by next week
```

```
deadline task for next month /by next month
```

```
deadline overdue task /by 03-08-2025
```

```
deadline overdue task 2 /by 02-07-2025
```

### Add Event

Creates events with specific start and end times

```
event team meeting /from 18-12-2025 1400 /to 18-12-2025 1600

```

```
event project meeting /from 13-11-2025 1900 /to 13-11-2025 2300

```

## View All Tasks

Display all tasks in the list with status indicator done[X] /undone [ ]

```
list
```

## Mark & Unmark Task

Marks tasks as complete, uses backdated to mark past tasks, then unmarks to show reversibility.

Marking operates as FIFO, while unmarking operates as LIFO

```
mark buy groceries
```

```
list
```

```
mark buy groceries
```

```
list
```

```
unmark buy groceries
```

```
list
```

```
mark backdated
```

```
list
```

## Find Tasks

Searches for tasks by keyword (partial matching) with case-sensitive matching

```
find Assignment
```

```
find assignment
```

```
find meeting
```

```
find over
```

## Schedule Viewing - Various dates

Shows tasks filtered by date and date ranges (today, tomorrow, specific dates, next week, next month) and demonstrates range scheduling feature. tasks is also group by task type within dates

```
schedule 18-12-2025
```

```
schedule yesterday
```

```
schedule today
```

```
schedule tomorrow
```

```
schedule next week
```

```
schedule next month
```

```
schedule 15-12-2025
```

## Delete Operations

Deletes tasks by description or index and bulk-deletes all completed tasks

```
list
```

```
delete submit assignment
```

```
list
```

```
delete 1
```

```
list
```

```
delete all done
```

```
list
```

## Error Handling

Demonstrates proper error messages for missing descriptions, invalid dates, invalid time, overlapping event, swap parameter, missing parameter, non-existent tasks, empty input, invalid commands and single word command taking argument

### Missing Description

```
todo
```

```
deadline /by next month
```

```
event /from 01-12-2025 1100 /to 01-12-2025 1430
```

### Invalid Date

```
deadline invalid date /by invalid-date
```

```
deadline invalid date /by 45-45-2040
```

```
event invalid date /from invalid-date /to invalid-date
```

```
event invalid date /from 35-35-3022 /to 36-36-3022
```

```
schedule 40-35-2024
```

### Invalid Time

```
deadline invalid time /by 11-11-2025 2500
```

```
event invalid time /from 11-11-2025 1565 /to 11-11-2025 1785
```

### Overlapping Event

overlap with: `event team meeting /from 18-12-2025 1400 /to 18-12-2025 1600`

```
event overlapping event /from 18-12-2025 1400 /to 18-12-2025 1600
```

### Event /to and /from Swap

```
event swap parameter /to 11-11-2025 1400 /from 11-11-2025 1300
```

### Missing Parameter

```
deadline missing by 05-11-2025
```

```
event missing to /from 05-11-2025 0900
```

```
event missing from /to 05-11-2025 1000
```

### Non Existent Task

```
mark nonexistent task
```

```
unmark nonexistent task
```

```
delete nonexistent task
```

### Empty Input

```

```

### Invalid Command

```
lsit
```

```
help me
```

### Single Word Command Taking Argument

```
command list
```

## Storage Test

Exits app, relaunches it, and shows that all tasks were saved and persisted

```
java -jar Nerunerune.jar
```

```
list
```

Close app again

Open tasks.txt and corrupt it, launch app and check folder to see corrupted file rename to tasksArchive.txt and fresh new tasks.txt created. 

Add a task and check task saved to new file

**`nerunerune/data/tasks.txt`**

**`nerunerune/data/tasksArchive.txt`**

```
java -jar Nerunerune.jar
```

```
todo new task after corruption
```

```
list
```