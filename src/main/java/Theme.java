import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class Theme {
    private Theme() { }
    public static final Color NAVY = Color.web("#101e36");
    public static final Color GOLD = Color.web("#f5ca73");
    public static final Color INK = Color.web("#273449");
    public static final Color[] PLAYERS = {Color.web("#ef7067"), Color.web("#55c9ab"), Color.web("#69b8f4"), Color.web("#d997e7")};

    public static Scene scene(javafx.scene.Parent root, double w, double h) {
        Scene scene = new Scene(root, w, h, NAVY);
        scene.getStylesheets().add(Theme.class.getResource("/Art/theme.css").toExternalForm());
        return scene;
    }
    public static void decorate(Stage stage) { stage.getIcons().setAll(Art.sprite(0)); }

    /** Scale the entire fixed logical board, including controls; keep it inside visual screen bounds. */
    public static Scene boardScene(Pane content) {
        content.setMinSize(1000, 1000);
        content.setPrefSize(1000, 1000);
        content.setMaxSize(1000, 1000);
        content.resize(1000, 1000);
        Group group = new Group(content);
        group.setManaged(false);
        javafx.scene.transform.Scale transform = new javafx.scene.transform.Scale(1, 1, 0, 0);
        group.getTransforms().add(transform);
        Pane holder = new Pane(group) {
            @Override protected void layoutChildren() {
                double scale = Math.max(0.1, Math.min((getWidth()-24)/1000, (getHeight()-24)/1000));
                transform.setX(scale);
                transform.setY(scale);
                group.setLayoutX((getWidth()-1000*scale)/2);
                group.setLayoutY((getHeight()-1000*scale)/2);
                content.resize(1000, 1000);
                content.layout();
            }
        };
        var screen = Screen.getPrimary().getVisualBounds();
        double side = Math.min(1024, Math.min(screen.getWidth()-60, screen.getHeight()-90));
        Scene scene = scene(holder, side, side);
        return scene;
    }
    public static Label label(String text, String style) {
        Label label = new Label(text);
        label.getStyleClass().add(style);
        return label;
    }
    public static Button button(String text, boolean primary) {
        Button button = new Button(text);
        if (primary) button.getStyleClass().add("primary");
        button.setMinHeight(40);
        return button;
    }
    public static void reveal(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(420), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}
