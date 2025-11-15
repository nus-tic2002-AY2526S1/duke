package com.golden.storage;

/**
 * Storage: deals with loading tasks from the file and saving tasks in the file
 * save / write to file
 * load file - read / parse file - returns customList
 */


import com.golden.config.StaticConfig;
import com.golden.core.CustomList;
import com.golden.exceptions.BotException;
import com.golden.task.Task;
import com.golden.exceptions.storageErrors.*;
import com.golden.parser.TaskParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    // this is a pointer to a file object that may or may not exist
    // final keyword used here to bind a storage instance to one filepath for its lifetime
    private final File file;

    private String getFilepath(){
        return StaticConfig.INIT_FILEPATH;
    }
    /** EAGER policy: ensure that target file exists now than later
     *  Rationale: project requirement states to load data from hard disk **when chatbot starts up**
     *  therefore, decision to ensure file can be created in constructor method of Storage class
     */
    public Storage() throws StorageFileNotFoundException {
        String filepath = getFilepath();
        if (filepath == null || filepath.trim().isEmpty()){
            throw new StorageFileNotFoundException("Empty path provided in initial configuration!");
        }
        File f = new File(filepath).getAbsoluteFile();
        ensureDirectoryExistsOrCreate(f);
        this.file = f;      // only reference to file is created at this moment
        ensureFileExistsOrCreate(this.file);    //immediately throw error if file cannot be created
    }

    /**
     * Private class helper method to ensure parent directory exists, is a directory and is writable.
     */
    private void ensureDirectoryExistsOrCreate(File targetFile) throws StorageFileNotFoundException {
        File dir = targetFile.getParentFile();
        if (dir == null)
            return;    // indicates filepath in current working directory

        if (!dir.exists()) {
            // if directory doesn't exist, try to create the directory tree for first run
            if (!dir.mkdirs()) {
                throw new StorageFileNotFoundException(
                        "Directory does not exist and could not be created: " + dir.getPath());
            }
        }
        if (!dir.isDirectory()) {
                throw new StorageFileNotFoundException("This is not a directory: " + dir.getPath());
            }
        if (!dir.canWrite()) {
            throw new StorageFileNotFoundException("This directory is not writable: " + dir.getPath());
        }
    }

    /**
     * Private class helper method to ensure file object exists, is a file, is readable and writable.
     */
    private void ensureFileExistsOrCreate(File f) throws StorageFileNotFoundException {
        try {
            // validate directory exists first
            ensureDirectoryExistsOrCreate(f);    //re-check directory in case it got deleted
            if (!file.exists()) {
                // create empty file on first run
                if (!file.createNewFile()) {
                    throw new StorageFileNotFoundException(
                            "Failed to create file: " + file.getPath());
                }
            }
            if (!file.isFile()) {
                throw new StorageFileNotFoundException(
                        "Path is not a regular file: " + file.getPath());
            }
            if (!file.canRead()) {
                throw new StorageFileNotFoundException(
                        "File is not readable: " + file.getPath());
            }
            if (!file.canWrite()) {
                throw new StorageFileNotFoundException(
                        "File is not writable: " + file.getPath());
            }
        } catch (java.io.IOException e){
            throw new StorageFileNotFoundException(
                "I/O error ensuring file exists: " + file.getPath());
        }
    }

    /**
     *  Returns an array of Tasks after parsing the Storage-bound file line-by-line.
     *
     * @return  an ArrayList of Task objects of this {@code Throwable} instance (which may
     *          read an empty or non-existence file).
     */
    public ArrayList<Task> loadFile() throws BotException {
        // Ensure folder & file exis; otherwise, create empty file on first run
        ensureFileExistsOrCreate(this.file);

        final ArrayList<Task> tasks = new ArrayList<>();
        int lineNo = 0;

        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                lineNo++;
                // skip blank or comment lines
                if (line.isBlank() || line.trim().startsWith("#")){
                    continue;
                }
                Task t = TaskParser.parseStorageLine(line, lineNo);
                if (t != null){
                    tasks.add(t);
                }

            }
        } catch (java.io.FileNotFoundException e) {
            // Rethrow (i.e. wrap) FileNotFoundException as a custom checked 'Storage..Exception'
            throw new StorageFileNotFoundException(
                    "Unable to open file: " + file.getPath()
            );
        }
//        catch (BotException e ){
//            System.out.println(e.toString());
//        }
        return tasks;
    }

    /**
     *  Returns a File that records the list of tasks stored in the 'tasklist' object.
     *
     * @param   tasklist The CustomList of Task objects bound to this app instance.
     *
     * @return  a File that saves the current state of Tasks or a {@code Throwable} instance.
     */
    public void writeToFile(CustomList tasklist) throws StorageFileParseException{
        try (FileWriter fw = new FileWriter(file, /* append */ false);
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (Task t : tasklist) {
                if (t == null) continue;                 // defensive
                bw.write(t.serialise());                  // "T | 0 | desc", etc.
                bw.newLine();                            // platform newline
            }
            bw.flush();                                   // explicit (optional)

        } catch (IOException e) {
            // Rethrow (i.e. wrap) as a checked StorageException
            throw new StorageFileParseException("Failed to write tasks to: " + file.getPath());
        }
    }
}


