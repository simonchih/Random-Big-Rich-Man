import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

/** Export the generated wealth-god sprite into a PNG-backed Windows icon. */
public class ExportIcon extends Application {
    @Override public void start(Stage unused) throws Exception {
        Canvas canvas=new Canvas(256,256);
        Art.draw(canvas.getGraphicsContext2D(),Art.sprite(0),0,0,256,256);
        SnapshotParameters parameters=new SnapshotParameters(); parameters.setFill(Color.TRANSPARENT);
        var image=canvas.snapshot(parameters,null);
        BufferedImage out=new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
        for(int y=0;y<256;y++) for(int x=0;x<256;x++) out.setRGB(x,y,image.getPixelReader().getArgb(x,y));
        ByteArrayOutputStream png=new ByteArrayOutputStream(); ImageIO.write(out,"png",png);
        byte[] data=png.toByteArray();
        Files.write(Path.of("src/main/resources/Art/richman.png"),data);
        ByteBuffer ico=ByteBuffer.allocate(22+data.length).order(ByteOrder.LITTLE_ENDIAN);
        ico.putShort((short)0).putShort((short)1).putShort((short)1);
        ico.put((byte)0).put((byte)0).put((byte)0).put((byte)0);
        ico.putShort((short)1).putShort((short)32).putInt(data.length).putInt(22).put(data);
        Files.write(Path.of("src/main/resources/Art/richman.ico"),ico.array());
        Platform.exit();
    }
    public static void main(String[] args) { launch(args); }
}
