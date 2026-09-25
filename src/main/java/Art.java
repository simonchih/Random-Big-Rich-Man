import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import java.util.HashMap;
import java.util.Map;

/** Original generated artwork; atlas regions include complete silhouettes and shadows. */
public final class Art {
    private Art() { }
    private static final Map<String, Image> CACHE = new HashMap<>();
    private static final int[][] REGIONS = {
        {0,0,320,354}, {320,0,314,352}, {635,0,306,350}, {945,0,309,352},
        {0,354,320,340}, {320,351,313,343}, {635,346,307,348}, {945,354,309,340},
        {0,697,320,269}, {320,694,313,273}, {636,694,307,275}, {945,698,309,269},
        {0,973,320,269}, {320,972,313,270}, {636,970,307,272}, {945,972,309,270}
    };
    public static final String[] NAMES = {"財神", "玉龍", "藍寶", "招財貓", "金虎", "探險家", "幸運兔", "銀河機器人"};

    public static Image load(String name) {
        return CACHE.computeIfAbsent(name, key -> {
            var url = Art.class.getResource("/Art/" + key + ".png");
            if (url == null) throw new IllegalStateException("Missing artwork: " + key);
            Image image = new Image(url.toExternalForm());
            if (image.isError()) throw new IllegalStateException("Invalid artwork: " + key, image.getException());
            return image;
        });
    }

    public static Image sprite(int index) {
        Image atlas = load("characters-atlas");
        return CACHE.computeIfAbsent("sprite-" + index, key -> {
            int[] r = REGIONS[index];
            return paddedRegion(atlas, r[0], r[1], r[2], r[3]);
        });
    }

    public static Image dice(int face) {
        if (face < 1 || face > 6) throw new IllegalArgumentException("Dice face: " + face);
        Image atlas = load("dice-atlas");
        return CACHE.computeIfAbsent("dice-" + face, key -> {
            int col = (face - 1) % 3, row = (face - 1) / 3;
            return paddedRegion(atlas, col * 418, row == 0 ? 195 : 694, 418, 370);
        });
    }

    private static Image paddedRegion(Image source, int x, int y, int w, int h) {
        // Keep a transparent gutter even when the source silhouette approaches an atlas edge.
        int side = Math.max(w, h) + 32;
        WritableImage image = new WritableImage(side, side);
        image.getPixelWriter().setPixels((side-w)/2, (side-h)/2, w, h, source.getPixelReader(), x, y);
        return image;
    }

    /** Contain, never cover: every call preserves aspect ratio and a safe inner margin. */
    public static Rectangle2D fit(Image image, double x, double y, double w, double h, double padding) {
        double scale = Math.min(Math.max(0, w-2*padding)/image.getWidth(), Math.max(0, h-2*padding)/image.getHeight());
        double dw = image.getWidth()*scale, dh = image.getHeight()*scale;
        return new Rectangle2D(x+(w-dw)/2, y+(h-dh)/2, dw, dh);
    }
    public static void draw(GraphicsContext g, Image image, double x, double y, double w, double h) {
        Rectangle2D r = fit(image, x, y, w, h, 2);
        g.drawImage(image, r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
    }
    public static ImageView view(Image image, double w, double h) {
        ImageView view = new ImageView(image);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        view.setFitWidth(w);
        view.setFitHeight(h);
        return view;
    }
}
