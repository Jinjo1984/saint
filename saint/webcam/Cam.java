package saint.webcam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tiagorlampert
 *
 * Source: https://github.com/sarxos/webcam-capture
 */
public class Cam {

    // Метод для захвата изображения с веб-камеры
    public static void Capture(String filePath, String fileName, int widthx, int heighty) throws IOException {
        // Получение веб-камеры по умолчанию
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            // Установка размера изображения
            webcam.setViewSize(new Dimension(widthx, heighty));
            // Открытие веб-камеры
            webcam.open();
            // Захват изображения и сохранение его в файл
            ImageIO.write(webcam.getImage(), "PNG", new File(filePath + fileName + ".png"));
            // Закрытие веб-камеры
            webcam.close();
        }
    }
}
