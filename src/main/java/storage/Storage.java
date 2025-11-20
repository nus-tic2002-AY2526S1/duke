package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import task.Task;
import task.Todo;
import task.Deadline;
import task.Event;
import util.DukeException;

public class Storage {
    private final Path dataDir = Paths.get("data");
    private final Path dataFile = dataDir.resolve("duke.txt");

    public List<Task> load() throws DukeException {
        try {
            if (Files.notExists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            if (Files.notExists(dataFile)) {
                Files.createFile(dataFile);
                return new ArrayList<Task>();
            }

            List<Task> tasks = new ArrayList<Task>();
            try (BufferedReader br = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
                String line;
                int lineNo = 0;
                while ((line = br.readLine()) != null) {
                    lineNo++;
                    line = line.trim();
                    if (line.isEmpty())
                        continue;
                    try {
                        tasks.add(deserialize(line));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Warning: Skipping corrupted line " + lineNo + ": " + line);
                    }
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new DukeException("Failed to load tasks: " + e.getMessage(), e);
        }
    }

    public void save(List<Task> tasks) throws DukeException {
        try {
            if (Files.notExists(dataDir))
                Files.createDirectories(dataDir);
            Path tmp = dataDir.resolve("duke.txt.tmp");
            try (BufferedWriter bw = Files.newBufferedWriter(
                    tmp, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (Task t : tasks) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
            Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new DukeException("Failed to save tasks: " + e.getMessage(), e);
        }
    }

    private String serialize(Task t) {
        if (t instanceof Todo) {
            return join("T", doneFlag(t), escape(t.getDescription()));

        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;

            return join("D", doneFlag(t), escape(t.getDescription()), escape(d.getByRaw()));

        } else if (t instanceof Event) {
            Event e = (Event) t;
            return join("E", doneFlag(t), escape(t.getDescription()),
                    escape(e.getFromRaw()), escape(e.getToRaw()));
        }
        throw new IllegalArgumentException("Unknown task type: " + t.getClass());
    }

    private Task deserialize(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3)
            throw new IllegalArgumentException("Not enough fields");

        String type = parts[0];

        boolean done;
        if ("1".equals(parts[1])) {
            done = true;
        } else if ("0".equals(parts[1])) {
            done = false;
        } else {
            throw new IllegalArgumentException("Invalid done flag: " + parts[1]);
        }

        String desc = unescape(parts[2]);
        Task t;

        if ("T".equals(type)) {
            t = new Todo(desc);

        } else if ("D".equals(type)) {
            if (parts.length < 4)
                throw new IllegalArgumentException("Deadline missing 'by'");
            String byRaw = unescape(parts[3]); // keep raw
            t = new Deadline(desc, byRaw); // use raw constructor

        } else if ("E".equals(type)) {
            if (parts.length < 5)
                throw new IllegalArgumentException("Event missing 'from' or 'to'");
            String fromRaw = unescape(parts[3]); // keep raw
            String toRaw = unescape(parts[4]);
            t = new Event(desc, fromRaw, toRaw); // use raw constructor

        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }

        if (done)
            t.markDone();
        return t;
    }

    private static String join(String... parts) {
        return String.join(" | ", parts);
    }

    private String doneFlag(Task t) {
        return t.isDone() ? "1" : "0";
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }

    private String unescape(String s) {
        return s.replace("\\|", "|").replace("\\\\", "\\");
    }
}
