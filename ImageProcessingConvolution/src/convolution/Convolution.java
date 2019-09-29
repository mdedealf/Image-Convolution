package convolution;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Convolution {

    public static void citraGrayscale(String path) throws IOException {
        BufferedImage img = null;
        File f = null;

        f = new File(path);
        img = ImageIO.read(f);

        int height = img.getHeight();
        int width = img.getWidth();

        for (int i = 0 ; i < height ; i++) {
            for (int j = 0 ; j < width ; j++) {
                int p = img.getRGB(i,j);

                int a = p >> 24 & 0xff;
                int r = p >> 16 & 0xff;
                int g = p >> 8 & 0xff;
                int b = p & 0xff;

                // calculate rgb
                int gray = (r+g+b) /3;

                // set new rgb
                p = a << 24 | gray << 16 | gray << 8 | gray;
                img.setRGB(i,j,p);
            }
        }

        // write new image after being convert into biner image
        f = new File("./assets/grayscale.jpg");
        ImageIO.write(img , "jpg" , f);
    }

    public static void showImage(String imgpath) throws IOException {
        File path = new File((imgpath));
        BufferedImage img = ImageIO.read(path);
        ImageIcon image = new ImageIcon();
        image.setImage(img);
        UIManager.put("OptionPane.minimumSize", new Dimension(500,500));
        JOptionPane.showMessageDialog(null, image);
    }

    public static BufferedImage paddingImage(String path, int padding) throws IOException {
        BufferedImage img = null;
        File f = null;
        f = new File(path);
        img = ImageIO.read(f);

        BufferedImage bi = new BufferedImage(img.getWidth() + padding , img.getHeight() + padding, img.getType());

        for (int i = 0 ; i < img.getHeight() ; i++) {
            for (int j = 0 ; j < img.getWidth() ; j++) {
                int p = img.getRGB(i,j);
                int a = p >> 24 & 0xff;
                int r = p >> 16 & 0xff;
                int g = p >> 8 & 0xff;
                int b = p & 0xff;

                int avg = (r+g+b)/3;
                p = p << 24 | avg << 16 | avg << 8 | avg;

                bi.setRGB(i+1,j+1,p);
            }
        }
        return bi;
    }

    public static void filterImage(float array[][],String path) throws IOException {
        BufferedImage img = null;
        File f = null;

        f = new File(path);
        img = ImageIO.read(f);

        int padding = array.length -1;
        BufferedImage pimg = paddingImage(path, padding);

        for (int i = 0 ; i < pimg.getHeight() - padding ; i++) {
            for (int j = 0 ; j < pimg.getWidth() - padding ; j++) {
                int temp = 0;
                int p ;
                int a = 0;
                int r ;
                int g ;
                int b ;

                for (int k = 0 ; k < array.length ; k++){
                    for (int l = 0 ; l < array.length ; l++){
                        p = pimg.getRGB(i+k,j+l);
                        a = p >> 24 & 0xff;
                        r = p >> 16 & 0xff;
                        g = p >> 8 & 0xff;
                        b = p & 0xff;

                        int avg = (r+g+b)/3;
                        temp += array[k][l] * avg;
                    }
                }

                if (temp > 255){
                    temp = 255;
                }else if (temp < 0){
                    temp = 0;
                }else temp = temp;

                p = a << 24 | temp << 16 | temp << 8 | temp;
                pimg.setRGB(i+1 , j+1 , p);
            }
        }
        ImageIO.write(pimg, "JPG" , new File("./assets/Convolution.jpg"));
    }


    public static void main(String[] args) throws IOException {
        String path = "./assets/Korean.jpg";
        citraGrayscale(path);
        showImage(path);

        float arrayFilter[][] = {
                {0 , -1 , 0},
                {-1,  8 , -1},
                {0 , -1 , 0}
        };

        String imgGrascale = "./assets/grayscale.jpg";
        filterImage(arrayFilter , imgGrascale);
        showImage("./assets/Convolution.jpg");
    }
}
