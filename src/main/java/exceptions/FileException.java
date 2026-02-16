package exceptions;

/**
 * Thrown for any errors during file creation or access
 */
public class FileException extends GrootException {
    /**
     * Main message for all the sub FileExceptions
     *
     * @param message message from sub FileExceptions
     */
    public FileException(String message) {
        super(message);
    }

    /**
     * Thrown when directory is unable to be created
     */
    public static class UnableToCreateDirectoryException extends FileException {
        public UnableToCreateDirectoryException() {
            super("Unable to create directory.");
        }
    }

    /**
     * Thrown when tasklist file is unable to be created
     */
    public static class UnableToCreateFileException extends FileException {
        public UnableToCreateFileException() {
            super("Error while creating file.");
        }
    }

    /**
     * Thrown when tasklist file does not exist
     */
    public static class FileDoesNotExistException extends FileException {
        public FileDoesNotExistException() {
            super("tasklist.txt does not exist. Check that tasklist.txt exists in data folder.");
        }
    }

    /**
     * Thrown when tasklist in task manager cannot be written to file
     */
    public static class UnableToWriteFileException extends FileException {
        public UnableToWriteFileException() {
            super("Error while writing file.");
        }
    }

    /**
     * Thrown when there is any error in parsing file content
     */
    public static class FileCorruptedException extends FileException {
        public FileCorruptedException() {
            super("Tasklist file is corrupted.");
        }
    }
}
