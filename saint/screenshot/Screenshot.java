package saint.screenshot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tiagorlampert
 * 
 * Source: http://www.codejava.net/java-se/graphics/how-to-capture-screenshot-programmatically-in-java
 */
public class Screenshot {

    // Метод для создания скриншота
    public static void TakeScreenshot(String filePath, String fileName) {
        try {
            // Создание объекта Robot для захвата экрана
            Robot robot = new Robot();
            // Определение размера экрана
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            // Захват изображения экрана
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            // Сохранение изображения в файл
            ImageIO.write(screenFullImage, "jpg", new File(filePath + fileName + ".jpg"));
        } catch (AWTException | IOException ex) {
            // Вывод сообщения об ошибке
            System.out.println(ex.getMessage());
        }
    }
}
