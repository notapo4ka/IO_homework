import java.io.*;

public class FileManager {

    private static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void run() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String command;

        while (true) {
            System.out.println(currentDirectory.getAbsoluteFile() + " $ ");
            try {
                command = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (command.equals("exit")) {
                break;
            }

            String[] keys = command.split("\\s+");

            switch (keys[0]) {
                case "cd":
                    changeDirectory(keys);
                    break;
                case "cp":
                    copyFile(keys);
                    break;
                case "ls":
                    localStorage(keys);
                    break;
                case "pwd":
                    printWorkingDirectory(keys);
                    break;
                default:
                    System.out.println("The wrong command is entered");
                    break;
            }
        }
    }

    private static void changeDirectory(String[] keys) {
        if (keys.length == 1) {
            System.out.println("Specify the directory");
        }

        String targetDirectory = keys[1];

        if (targetDirectory.equals("..")) {
            File parentDirectory = currentDirectory.getParentFile();
            if (parentDirectory == null) {
                System.out.println("You are already in the root directory");
            }
            currentDirectory = parentDirectory;
        } else if (targetDirectory.startsWith("/")) {
            currentDirectory = new File(targetDirectory);
        } else {
            currentDirectory = new File(currentDirectory, targetDirectory);
        }

        if (!currentDirectory.exists()) {
            System.out.println("Directory does not exist");
            currentDirectory = currentDirectory.getParentFile();
        }
    }

    private static void copyFile(String[] keys) {
        if (keys.length < 3) {
            System.out.println("Please use this type form: cp [source] [target]");
        }

        File sourceFile = new File(keys[1]);
        File targetFile = new File(keys[2]);

        if (!sourceFile.exists()) {
            System.out.println("Source file does not exist");
        }

        if (targetFile.isDirectory()) {
            targetFile = new File(targetFile, sourceFile.getName());
        }

        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void localStorage(String[] keys) {
        File[] files = currentDirectory.listFiles();
        if (files == null) {
            System.out.println("Error list files");
        }

        for(File file : files) {
            System.out.println(file);
        }
    }

    private static void printWorkingDirectory(String[] keys) {
        System.out.println(currentDirectory);
    }
}