/* Copyright (C) 2017 Simon <ficstudio@yahoo.com.tw>
 * Licensed under GNU GPL v3 or later; see LICENSE. */
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

public class PlayerSettings {
    private final int playerIdx;
    public PlayerSettings(int playerIdx) { this.playerIdx=playerIdx; }

    public void show(Stage previousStage,Game game) {
        Stage stage=new Stage();
        stage.setTitle("玩家 "+(playerIdx+1)+" · 選擇角色");
        Theme.decorate(stage);
        Set<Integer> used=new HashSet<>();
        for(int p=0;p<playerIdx;p++) used.add(game.p_icon[p]);

        TextField name=new TextField(game.p_name[playerIdx]==null?"Player"+(playerIdx+1):game.p_name[playerIdx]);
        name.setPrefColumnCount(14);
        CheckBox ai=new CheckBox("由電腦自動遊玩");
        ai.setSelected(playerIdx!=0);
        Spinner<Integer> money=new Spinner<>(0,Integer.MAX_VALUE,30000,100);
        money.setEditable(true); money.setPrefWidth(180);
        GridPane form=new GridPane(); form.setHgap(20); form.setVgap(16);
        form.addRow(0,Theme.label("玩家名稱",""),name,ai);
        form.addRow(1,Theme.label("起始資金",""),money,Theme.label("預設 $30,000","muted"));

        ToggleGroup choices=new ToggleGroup();
        TilePane roster=new TilePane(12,12); roster.setPrefColumns(4);
        roster.setPrefTileWidth(176); roster.setPrefTileHeight(142);
        for(int i=0;i<8;i++) {
            VBox graphic=new VBox(4,Art.view(Art.sprite(i),92,92),Theme.label(Art.NAMES[i],""));
            graphic.setAlignment(Pos.CENTER);
            ToggleButton choice=new ToggleButton(); choice.setGraphic(graphic);
            choice.setPrefSize(176,142); choice.setToggleGroup(choices); choice.setUserData(i);
            choice.setDisable(used.contains(i));
            choice.setAccessibleText(Art.NAMES[i]);
            roster.getChildren().add(choice);
            if(!choice.isDisabled() && choices.getSelectedToggle()==null) choice.setSelected(true);
        }
        Label error=Theme.label("","error"); error.setMinHeight(22);
        Button back=Theme.button("← 上一步",false),next=Theme.button(playerIdx==3?"開始冒險 →":"下一位玩家 →",true);
        back.setOnAction(e -> { stage.close(); if(previousStage!=null) previousStage.show(); });
        next.setOnAction(e -> {
            if(name.getText()==null || name.getText().trim().isEmpty()) { error.setText("請輸入玩家名稱。"); return; }
            int startingMoney;
            try { startingMoney=Integer.parseInt(money.getEditor().getText().trim()); }
            catch(NumberFormatException ex) { error.setText("起始資金必須是 0 到 2,147,483,647 的整數。"); return; }
            if(startingMoney<0) { error.setText("起始資金不能小於 0。"); return; }
            if(choices.getSelectedToggle()==null) { error.setText("請選擇一位角色。"); return; }
            int selected=(Integer)choices.getSelectedToggle().getUserData();
            game.p_name[playerIdx]=name.getText().trim(); game.p_money[playerIdx]=startingMoney;
            game.p_type[playerIdx]=ai.isSelected()?1:0; game.p_icon[playerIdx]=selected;
            game.p_ic[playerIdx]=Art.sprite(selected); game.p_pawn[playerIdx]=Art.sprite(selected);
            stage.hide();
            if(playerIdx==3) new MainMap(game).generate_map(game);
            else new PlayerSettings(playerIdx+1).show(stage,game);
        });
        Region spacer=new Region(); HBox.setHgrow(spacer,Priority.ALWAYS);
        HBox actions=new HBox(16,back,spacer,next); actions.setAlignment(Pos.CENTER);
        VBox root=new VBox(16,Theme.label("NEW JOURNEY  /  "+(playerIdx+1)+" OF 4","eyebrow"),
            Theme.label("選擇你的幸運夥伴","heading"),form,roster,error,actions);
        root.setPadding(new Insets(28));
        stage.setScene(Theme.scene(root,796,630)); stage.setResizable(false);
        stage.setOnCloseRequest(e -> { if(previousStage!=null) previousStage.show(); });
        if(previousStage!=null) previousStage.hide();
        stage.show(); Theme.reveal(root);
    }
}
