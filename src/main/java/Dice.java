/* Copyright (C) 2017 Simon <ficstudio@yahoo.com.tw>
 * Licensed under GNU GPL v3 or later; see LICENSE. */
import javafx.scene.image.Image;

public class Dice {
    public Image idice1,idice2;
    public int dice1=1,dice2=2,count=3;
    public volatile long rolledAt;
    private final Game game;
    private final Image[] faces=new Image[6];
    Dice(Game game) {
        this.game=game;
        // Load before the game thread starts; no rendering or cache mutation in rollDice.
        for(int face=1;face<=6;face++) faces[face-1]=Art.dice(face);
        idice1=faces[0]; idice2=faces[1];
    }
    public int generateRandomNumber() { return game.getRandom().nextInt(6)+1; }
    public void rollDice() {
        dice1=generateRandomNumber(); dice2=generateRandomNumber(); count=dice1+dice2;
        idice1=faces[dice1-1]; idice2=faces[dice2-1]; rolledAt=System.nanoTime();
    }
}
