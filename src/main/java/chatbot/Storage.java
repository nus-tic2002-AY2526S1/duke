package chatbot;

import chatbot.tasks.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /** Load tasks from file into memory */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return tasks;
        }

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" \\| ");
            try {
                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task task = null;

                switch (type) {
                    case "T": // Todo
                        task = new Todo(description);
                        if (parts.length >= 4) {
                            task.setPriority(Integer.parseInt(parts[3]));
                        }
                        break;

                    case "D": // Deadline
                        if (parts.length >= 4) {
                            task = new Deadline(description, parts[3]);
                            if (parts.length >= 5) {
                                task.setPriority(Integer.parseInt(parts[4]));
                            }
                        }
                        break;

                    case "E": // Event
                        if (parts.length >= 5) {
                            task = new Event(description, parts[3], parts[4]);
                            if (parts.length >= 6) {
                                task.setPriority(Integer.parseInt(parts[5]));
                            }
                        }
                        break;

                    default:
                        System.out.println("⚠️ Unknown task type in storage: " + type);
                }

                if (task != null) {
                    if (isDone) task.markAsDone();
                    tasks.add(task);
                }

            } catch (Exception e) {
                System.out.println("⚠️ Skipping corrupted line: " + line);
            }
        }
        sc.close();
        return tasks;
    }

    /** Save tasks to file */
    public void save(ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(filePath);

        for (Task t : tasks) {
            StringBuilder sb = new StringBuilder();

            if (t instanceof Todo) {
                sb.append("T | ")
                        .append(t.isDone ? "1" : "0")
                        .append(" | ").append(t.description)
                        .append(" | ").append(t.getPriority());
            }

            if (t instanceof Deadline) {
                Deadline d = (Deadline) t;
                sb.append("D | ")
                        .append(d.isDone ? "1" : "0")
                        .append(" | ").append(d.description)
                        .append(" | ").append(d.getByRaw())
                        .append(" | ").append(d.getPriority());
            }

            if (t instanceof Event) {
                Event e = (Event) t;
                sb.append("E | ")
                        .append(e.isDone ? "1" : "0")
                        .append(" | ").append(e.description)
                        .append(" | ").append(e.getFromRaw())
                        .append(" | ").append(e.getToRaw())
                        .append(" | ").append(e.getPriority());
            }

            fw.write(sb.toString() + System.lineSeparator());
        }
        fw.close();
    }
}