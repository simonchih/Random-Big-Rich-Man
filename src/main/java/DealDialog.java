import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Shared purchase/build layout; illustration and wrapping text occupy separate columns. */
public final class DealDialog {
    private DealDialog() { }
    public static void show(Game game,GameLoop loop,String title,String detail,long price,int sprite,Runnable purchase) {
        Stage stage=new Stage(); stage.setTitle(title); Theme.decorate(stage);
        var description=Theme.label(detail,"subtitle"); description.setWrapText(true); description.setMaxWidth(305);
        var balance=Theme.label(String.format("可用資金  $%,d",game.p_money[game.turn]),"muted");
        var copy=new VBox(14,Theme.label(title,"heading"),description,
            Theme.label(String.format("$%,d",price),"title"),balance);
        copy.setPrefWidth(305);
        HBox content=new HBox(24,Art.view(Art.sprite(sprite),166,180),copy);
        content.setAlignment(Pos.CENTER_LEFT);
        Button cancel=Theme.button("稍後再說",false),accept=Theme.button("確認交易",true);
        cancel.setOnAction(e -> stage.close());
        accept.setDisable(price>game.p_money[game.turn]);
        accept.setOnAction(e -> { purchase.run(); stage.close(); });
        HBox actions=new HBox(14,cancel,accept); actions.setAlignment(Pos.CENTER_RIGHT);
        var notice=Theme.label(price>game.p_money[game.turn]?"資金不足，暫時無法進行這筆交易。":"每一次投資，都讓城市更靠近你。",price>game.p_money[game.turn]?"error":"muted");
        VBox root=new VBox(22,Theme.label("CITY INVESTMENT  /  城市投資","eyebrow"),content,notice,actions);
        root.setPadding(new Insets(30));
        stage.setScene(Theme.scene(root,570,390)); stage.setResizable(false);
        stage.setOnHidden(e -> loop.susp=false);
        stage.show(); stage.toFront(); Theme.reveal(root);
    }
}
