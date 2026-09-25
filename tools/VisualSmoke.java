import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/** Runs the real onboarding flow and renders screenshots without external UI automation. */
public class VisualSmoke extends Application {
    private static final Path OUT=Path.of("target/visual-qa");
    private Game game;
    private Stage primary;
    private int step;
    private Game preview;
    private GameMap previewMap;
    private GameLoop previewLoop;
    private long movementStarted;
    private int startX,startY;
    private Stage boardStage;
    private static Throwable failure;

    @Override public void start(Stage stage) {
        try {
            Files.createDirectories(OUT);
            primary=stage;
            game=new Game(20260925L);
            game.start(stage);
            later(this::advance);
        } catch(Throwable ex) { fail(ex); }
    }
    private void later(Runnable action) {
        PauseTransition pause=new PauseTransition(Duration.millis(650));
        pause.setOnFinished(e -> { try { action.run(); } catch(Throwable ex) { fail(ex); } });
        pause.play();
    }
    private static Stage visible() {
        return (Stage)Window.getWindows().stream().filter(Window::isShowing).reduce((a,b)->b).orElseThrow();
    }
    private static Button button(Scene scene,String prefix) {
        return scene.getRoot().lookupAll(".button").stream().filter(n -> n instanceof Button)
            .map(n -> (Button)n).filter(b -> b.getText().startsWith(prefix)).findFirst().orElseThrow();
    }
    private void advance() {
        try {
            if(step==0) {
                save(primary.getScene(),"01-welcome");
                button(primary.getScene(),"開始新遊戲").fire();
            } else if(step>=1 && step<=4) {
                Stage settings=visible();
                if(step==1) {
                    save(settings.getScene(),"02-characters");
                    long enabled=settings.getScene().getRoot().lookupAll(".toggle-button").stream().filter(n->!n.isDisabled()).count();
                    require(enabled==8,"All eight new characters must be available");
                }
                button(settings.getScene(),step==4?"開始冒險":"下一位玩家").fire();
            } else if(step==5) {
                Stage board=visible(); save(board.getScene(),"03-board");
                boardStage=board;
                assertBoardFits(board);
                require(game.btnNewButton.isVisible(),"Roll control missing");
                // Actual logical geometry must remain inside the viewport after resizing.
                board.setWidth(660); board.setHeight(690);
            } else if(step==6) {
                Stage board=visible(); save(board.getScene(),"04-small-board");
                assertBoardFits(board);
                game.btnPropertyButton.fire();
            } else if(step==7) {
                save(visible().getScene(),"05-property"); visible().hide();
                renderShowcase();
                previewLoop=new GameLoop(preview,new MapCanvas(previewMap,preview),previewMap,null);
                previewLoop.susp=true;
                DealDialog.show(preview,previewLoop,"興建飯店","投資 Tamsui 的下一階段。",2400,9,()->preview.p_money[0]-=2400);
            } else if(step==8) {
                save(visible().getScene(),"06-purchase");
                button(visible().getScene(),"確認交易").fire();
                require(preview.p_money[0]==27600 && !previewLoop.susp,"Transaction did not complete/resume");
                verifyAssetsAndGeometry();
                verifyTileJumps();
                java.util.Arrays.fill(game.p_type,0);
                startX=game.p_x_now[0]; startY=game.p_y_now[0];
                require(!game.btnNewButton.isDisabled(),"Human player cannot roll");
                game.btnNewButton.fire(); movementStarted=System.nanoTime();
                require(game.move_start,"Roll did not start movement");
            } else if(step>=9) {
                if(step==9) {
                    require(game.p_x_now[0]!=startX || game.p_y_now[0]!=startY,"Pawn is not moving");
                    save(boardStage.getScene(),"10-moving-board");
                }
                for(Window w:new ArrayList<>(Window.getWindows())) {
                    if(w instanceof Stage s && s.isShowing() && (s.getTitle().equals("購入土地") || s.getTitle().equals("升級房屋")))
                        button(s.getScene(),"稍後再說").fire();
                }
                require(game.p_x_now[0]>=20 && game.p_x_now[0]<=980 && game.p_y_now[0]>=20 && game.p_y_now[0]<=980,"Moving pawn outside board");
                if(game.turn!=0 && !game.move_start) {
                    save(boardStage.getScene(),"11-completed-turn");
                    System.out.println("VISUAL_SMOKE_OK: onboarding, eight characters, initial/resized bounds, property, transaction, 160 pawn slots, assets, 60000 dice rolls and complete moving turn");
                    Platform.exit(); return;
                }
                require(System.nanoTime()-movementStarted<35_000_000_000L,"Turn did not complete within 35 seconds");
            }
            step++; later(this::advance);
        } catch(Throwable ex) { fail(ex); }
    }
    private void renderShowcase() throws Exception {
        Canvas sheet=new Canvas(960,960); var g=sheet.getGraphicsContext2D();
        g.setFill(Color.web("#fff1d9")); g.fillRect(0,0,960,960);
        for(int i=0;i<16;i++) Art.draw(g,Art.sprite(i),(i%4)*240,(i/4)*240,240,240);
        save(sheet.snapshot(null,null),"07-sprite-contact");
        Canvas dice=new Canvas(720,160); var d=dice.getGraphicsContext2D();
        d.setFill(Theme.NAVY); d.fillRect(0,0,720,160);
        for(int i=1;i<=6;i++) Art.draw(d,Art.dice(i),(i-1)*120,10,120,140);
        save(dice.snapshot(null,null),"08-dice-contact");
        preview=new Game(98765L);
        preview.btnNewButton=Theme.button("Roll",true); preview.btnPropertyButton=Theme.button("Property",false);
        GameMap gm=new MainMap(preview).ini_gameMap(new GameMap()); previewMap=gm;
        for(int i=0;i<40;i++) if(gm.type[i]==0) { gm.owner[i]=(i%4)+1; gm.level[i]=(i%4)+1; }
        for(int p=0;p<4;p++) {
            preview.p_name[p]=game.p_name[p]; preview.p_money[p]=30000;
            preview.p_ic[p]=game.p_ic[p]; preview.p_pawn[p]=game.p_pawn[p];
            preview.pshow_sqmark[p]=true;
            var slot=MapCanvas.pawnSlot(20,p); preview.p_x_now[p]=(int)slot.getMinX(); preview.p_y_now[p]=(int)slot.getMinY();
        }
        MapCanvas showcase=new MapCanvas(gm,preview); showcase.draw(); save(showcase.snapshot(null,null),"09-developed-board");
    }
    private void assertBoardFits(Stage board) {
        var content=game.btnNewButton.getParent();
        var bounds=content.localToScene(content.getBoundsInLocal());
        require(bounds.getMinX()>=11 && bounds.getMinY()>=11,"Board has no safe outer margin");
        require(bounds.getMaxX()<=board.getScene().getWidth()-11 && bounds.getMaxY()<=board.getScene().getHeight()-11,"Board is clipped");
    }
    private void verifyTileJumps() throws Exception {
        long clock=1_000_000_000L;
        int player=0;
        // Walk through every corner and across 39 -> 0 without real-time sleeps.
        preview.p_id[player]=38;
        for(int tile=0;tile<40;tile++) for(int p=0;p<4;p++) {
            var slot=MapCanvas.pawnSlot(tile,p);
            previewMap.pX[p][tile]=(int)slot.getMinX(); previewMap.pY[p][tile]=(int)slot.getMinY();
        }
        preview.p_x_now[player]=previewMap.pX[player][38];
        preview.p_y_now[player]=previewMap.pY[player][38];
        for(int step=0;step<43;step++) {
            int old=preview.p_id[player], next=(old+1)%40;
            previewLoop.advancePawn(player,clock);
            PawnJump motion=preview.pawnJumps.get(player);
            require(motion!=null && motion.targetTile()==next,"A jump skipped a tile");
            previewLoop.advancePawn(player,clock+PawnJump.FLIGHT_NANOS/2);
            require(preview.p_id[player]==old,"Position changed before landing");
            previewLoop.advancePawn(player,clock+PawnJump.FLIGHT_NANOS);
            require(preview.p_id[player]==old,"Landing pause was skipped");
            clock+=PawnJump.FLIGHT_NANOS+PawnJump.LANDING_NANOS;
            previewLoop.advancePawn(player,clock);
            require(preview.p_id[player]==next && preview.pawnJumps.get(player)==null,"Jump did not land once");
            require(preview.p_x_now[player]==previewMap.pX[player][next] && preview.p_y_now[player]==previewMap.pY[player][next],"Pawn overshot landing position");
            clock+=10_000_000L;
        }
        // Render a lifted piece separately from its stationary ground shadow.
        var from=MapCanvas.pawnSlot(6,0); var to=MapCanvas.pawnSlot(7,0);
        preview.pawnJumps.set(0,new PawnJump(7,from.getMinX(),from.getMinY(),to.getMinX(),to.getMinY(),System.nanoTime()-PawnJump.FLIGHT_NANOS/2));
        MapCanvas jumpBoard=new MapCanvas(previewMap,preview); jumpBoard.draw();
        save(jumpBoard.snapshot(null,null),"12-jump-midair");
        preview.pawnJumps.set(0,null);
    }
    private void verifyAssetsAndGeometry() {
        for(int i=0;i<16;i++) {
            var image=Art.sprite(i); require(!image.isError(),"Broken sprite "+i);
            int w=(int)image.getWidth(),h=(int)image.getHeight(); var pixels=image.getPixelReader();
            for(int x=0;x<w;x++) require((pixels.getArgb(x,0)>>>24)==0 && (pixels.getArgb(x,h-1)>>>24)==0,"Missing sprite gutter");
            for(int y=0;y<h;y++) require((pixels.getArgb(0,y)>>>24)==0 && (pixels.getArgb(w-1,y)>>>24)==0,"Missing sprite gutter");
        }
        for(int i=0;i<40;i++) {
            var cell=MapCanvas.cell(i);
            require(cell.getMinX()>=20 && cell.getMinY()>=20 && cell.getMaxX()<=980 && cell.getMaxY()<=980,"Unsafe outer edge");
            for(int p=0;p<4;p++) {
                var slot=MapCanvas.pawnSlot(i,p); require(cell.contains(slot),"Pawn outside cell "+i);
                for(int q=p+1;q<4;q++) require(!slot.intersects(MapCanvas.pawnSlot(i,q)),"Pawn overlap");
            }
        }
        int[] counts=new int[6];
        for(int n=0;n<60000;n++) {
            game.dice.rollDice(); require(game.dice.count==game.dice.dice1+game.dice.dice2,"Dice sum mismatch");
            require(game.dice.idice1==Art.dice(game.dice.dice1) && game.dice.idice2==Art.dice(game.dice.dice2),"Artwork and dice result differ");
            counts[game.dice.dice1-1]++;
        }
        for(int count:counts) require(count>9000 && count<11000,"Biased dice distribution");
    }
    private static void require(boolean ok,String message) { if(!ok) throw new AssertionError(message); }
    private static void save(Scene scene,String name) throws Exception {
        scene.getRoot().applyCss(); scene.getRoot().layout(); save(scene.snapshot(null),name);
    }
    private static void save(WritableImage image,String name) throws Exception {
        int w=(int)image.getWidth(),h=(int)image.getHeight();
        BufferedImage out=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        for(int y=0;y<h;y++) for(int x=0;x<w;x++) out.setRGB(x,y,image.getPixelReader().getArgb(x,y));
        ImageIO.write(out,"png",OUT.resolve(name+".png").toFile());
    }
    private static void fail(Throwable ex) { failure=ex; ex.printStackTrace(); Platform.exit(); }
    public static void main(String[] args) { launch(args); if(failure!=null) System.exit(1); }
}
