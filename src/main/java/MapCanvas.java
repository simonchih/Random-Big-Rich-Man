/* Copyright (C) 2017 Simon <ficstudio@yahoo.com.tw>
 * Licensed under GNU GPL v3 or later; see LICENSE. */
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/** Logical board coordinates and explicit image boxes, independent of source resolution. */
public class MapCanvas extends Canvas {
    public static final int SIZE=1000, MARGIN=20, CORNER=120, TILE=80;
    public static final int PAWN_WIDTH=84, PAWN_HEIGHT=96;
    public static final int PAWN_SLOT_WIDTH=17, PAWN_SLOT_HEIGHT=25;
    public final GameMap game_data;
    public final Game mygame;
    private final long[] lastMoney=new long[4], moneyDelta=new long[4], moneyAt=new long[4];
    private boolean initialized;
    private static final java.util.Map<String,Font> FONTS=new java.util.HashMap<>();
    private final DropShadow shadow=new DropShadow(12, Color.web("#00000070"));

    public MapCanvas(GameMap gm, Game game) {
        super(SIZE,SIZE); game_data=gm; mygame=game;
    }

    public static Rectangle2D cell(int i) {
        if(i<0 || i>=40) throw new IllegalArgumentException("Cell: "+i);
        if(i==0) return new Rectangle2D(860,860,120,120);
        if(i==10) return new Rectangle2D(20,860,120,120);
        if(i==20) return new Rectangle2D(20,20,120,120);
        if(i==30) return new Rectangle2D(860,20,120,120);
        if(i<10) return new Rectangle2D(860-i*80,860,80,120);
        if(i<20) return new Rectangle2D(20,860-(i-10)*80,120,80);
        if(i<30) return new Rectangle2D(140+(i-21)*80,20,80,120);
        return new Rectangle2D(860,140+(i-31)*80,120,80);
    }

    public static Rectangle2D pawnSlot(int cell, int player) {
        Rectangle2D r=cell(cell);
        // Logical landing anchors stay separate; the large figures intentionally overlap tile art.
        return new Rectangle2D(r.getMinX()+(r.getWidth()-72)/2+player*18,
            r.getMaxY()-29,PAWN_SLOT_WIDTH,PAWN_SLOT_HEIGHT);
    }
    private static Font font(double size, boolean bold) {
        return FONTS.computeIfAbsent(size+":"+bold,k -> Font.font("Microsoft JhengHei",bold?FontWeight.BOLD:FontWeight.NORMAL,size));
    }
    public boolean hasAnimations(long now) {
        for(int p=0;p<Game.maxPSize;p++) if(mygame.pawnJumps.get(p)!=null) return true;
        if(now-mygame.dice.rolledAt<600_000_000L) return true;
        for(long at:moneyAt) if(at!=0 && now-at<1_600_000_000L) return true;
        return false;
    }
    private static void text(GraphicsContext g,String s,double x,double y,double size,Color color,double width) {
        g.setFont(font(size,false)); g.setFill(color); g.setTextAlign(TextAlignment.LEFT);
        g.fillText(s==null?"":s,x,y,width);
    }
    private static void center(GraphicsContext g,String s,double x,double y,double size,Color color,double width) {
        g.setFont(font(size,true)); g.setFill(color); g.setTextAlign(TextAlignment.CENTER);
        g.fillText(s==null?"":s,x,y,width); g.setTextAlign(TextAlignment.LEFT);
    }
    public void draw() {
        GraphicsContext g=getGraphicsContext2D();
        g.setImageSmoothing(true);
        g.setFill(new LinearGradient(0,0,1,1,true,CycleMethod.NO_CYCLE,
            new Stop(0,Color.web("#283b56")),new Stop(1,Color.web("#0a1528"))));
        g.fillRoundRect(0,0,SIZE,SIZE,32,32);
        g.setStroke(Theme.GOLD); g.setLineWidth(2); g.strokeRoundRect(8,8,984,984,26,26);
        paintConstantField(g); paintVariableThings(g);
    }
    public void paintConstantField(GraphicsContext g) {
        for(int i=0;i<40;i++) paintCell(g,i);
        g.setFill(Color.web("#112139")); g.fillRoundRect(151,151,698,698,22,22);
        Art.draw(g,Art.load("taipei"),418,165,416,278);
        text(g,"TAIPEI · FORTUNE EDITION",175,191,12,Theme.GOLD,232);
        text(g,"瑞德",175,250,47,Theme.GOLD,230);
        text(g,"大富翁",175,308,47,Theme.GOLD,232);
        text(g,"R I C H M A N",178,343,19,Color.WHITE,228);
        text(g,"四位玩家，一場城市冒險。",177,382,14,Color.web("#b5c8d9"),235);
        text(g,"買地 · 建設 · 探索你的好運",177,407,13,Color.web("#879fb9"),235);
        text(g,"玩家資產  /  PLAYERS",177,467,13,Theme.GOLD,300);
        text(g,"金色外框標示目前回合",641,467,12,Color.web("#afc0d4"),188);
        g.setFill(Color.web("#0b172a")); g.fillRoundRect(175,708,650,76,16,16);
        text(g,"命運之骰",195,738,18,Theme.GOLD,180);
        text(g,"擲出下一段旅程",195,762,12,Color.web("#afc0d4"),180);
    }
    private void paintCell(GraphicsContext g,int i) {
        Rectangle2D r=cell(i);
        double x=r.getMinX()+2,y=r.getMinY()+2,w=r.getWidth()-4,h=r.getHeight()-4;
        boolean tall=r.getHeight()>r.getWidth(),corner=i%10==0;
        g.setFill(Color.web(corner?"#f4d99d":"#fff1d9")); g.fillRoundRect(x,y,w,h,9,9);
        g.setStroke(Color.web("#c9a35c")); g.setLineWidth(1); g.strokeRoundRect(x+0.5,y+0.5,w-1,h-1,9,9);
        if(game_data.type[i]==0 && i!=0) {
            g.setFill(game_data.color[i].deriveColor(0,0.65,0.88,1)); g.fillRoundRect(x+3,y+3,w-6,15,6,6);
            boolean sideBuilding=!tall && game_data.level[i]>0;
            double labelX=sideBuilding?x+76:x+w/2, labelWidth=sideBuilding?w-46:w-8;
            center(g,game_data.name[i],labelX,y+33,11,Theme.INK,labelWidth);
            center(g,"$"+game_data.value[i],labelX,y+49,10,Color.web("#806044"),labelWidth);
            if(tall) {
                int level=game_data.level[i];
                if(level>0) {
                    Art.draw(g,level==4?mygame.ihotel:mygame.ihouse,x+8,y+53,33,30);
                    text(g,level==4?"HOTEL":"×"+level,x+43,y+73,10,Theme.INK,w-46);
                } else if(game_data.owner[i]>0) Art.draw(g,mygame.p_ic[game_data.owner[i]-1],x+w/2-15,y+52,30,30);
            } else if(game_data.level[i]>0) {
                Art.draw(g,game_data.level[i]==4?mygame.ihotel:mygame.ihouse,x+4,y+19,38,32);
                center(g,game_data.level[i]==4?"HOTEL":"LEVEL "+game_data.level[i],x+w/2,y+14,9,Theme.INK,w-10);
            }
            if(game_data.owner[i]>0) {
                g.setStroke(Theme.PLAYERS[game_data.owner[i]-1]); g.setLineWidth(3);
                g.strokeRoundRect(x+2,y+2,w-4,h-4,8,8);
            }
        } else {
            Image art; String label;
            if(i==0) { art=mygame.iarrow; label="起點  +$2,000"; }
            else if(game_data.type[i]==2) { art=mygame.iquestionmark; label="機會"; }
            else {
                switch(game_data.id[i]) {
                    case 25: art=mygame.ijail; label="監獄"; break;
                    case 26: art=mygame.ickshall; label="中正紀念堂"; break;
                    case 27: art=mygame.ihospital; label="醫院"; break;
                    case 36: art=mygame.ijail; label="前往監獄"; break;
                    case 37: art=mygame.ihospital; label="前往醫院"; break;
                    case 38: art=mygame.ihouse; label="土地稅"; break;
                    case 39: art=mygame.ihotel; label="房屋稅"; break;
                    default: art=mygame.iarrow; label="起點";
                }
            }
            if(!tall && !corner) {
                Art.draw(g,art,x+3,y+1,43,43); center(g,label,x+79,y+26,12,Theme.INK,w-53);
            } else {
                Art.draw(g,art,x+8,y+2,w-16,h-48); center(g,label,x+w/2,y+h-32,12,Theme.INK,w-8);
            }
        }
    }
    public void paintVariableThings(GraphicsContext g) {
        long now=System.nanoTime();
        for(int p=0;p<4;p++) {
            if(initialized && lastMoney[p]!=mygame.p_money[p]) {
                moneyDelta[p]=mygame.p_money[p]-lastMoney[p]; moneyAt[p]=now;
            }
            lastMoney[p]=mygame.p_money[p];
            double x=175+(p%2)*333,y=484+(p/2)*108;
            g.setFill(Color.web(p==mygame.turn?"#304459":"#1c304a")); g.fillRoundRect(x,y,317,96,13,13);
            g.setStroke(p==mygame.turn?Theme.GOLD:Color.web("#3c5068")); g.setLineWidth(p==mygame.turn?2:1);
            g.strokeRoundRect(x,y,317,96,13,13);
            if(mygame.p_ic[p]!=null) Art.draw(g,mygame.p_ic[p],x+8,y+9,68,76);
            text(g,mygame.p_name[p]+(p==mygame.turn?"  •  回合中":""),x+85,y+24,14,Theme.PLAYERS[p],mygame.pshow_sqmark[p]?196:218);
            if(mygame.pshow_sqmark[p]) Art.draw(g,mygame.isqmark,x+289,y+5,20,20);
            text(g,String.format("$%,d",mygame.p_money[p]),x+85,y+52,23,Color.web("#fff0cf"),218);
            String status=mygame.p_status[p];
            if(status==null || status.equals("0")) status=mygame.p_type[p]==0?"準備出發 · 手動玩家":"準備出發 · 電腦玩家";
            text(g,mygame.p_type[p]==9?"已破產":status,x+85,y+77,11,Color.web("#b5c8d9"),218);
            double age=(now-moneyAt[p])/1e9;
            if(moneyAt[p]!=0 && age<1.5) {
                g.save(); g.setGlobalAlpha(1-age/1.5);
                text(g,(moneyDelta[p]>0?"+":"")+String.format("%,d",moneyDelta[p]),x+185,y+48-age*18,16,
                    moneyDelta[p]>0?Color.web("#80ebbf"):Color.web("#ff9382"),120); g.restore();
            }
        }
        initialized=true;
        // Paint the active player last so the jumping figure stays above all other pieces.
        for(int order=0;order<4;order++) {
            int p=(mygame.turn+1+order)%4;
            if(mygame.p_type[p]==9 || mygame.p_pawn[p]==null) continue;
            PawnJump jump=mygame.pawnJumps.get(p);
            PawnJump.Pose pose=jump==null
                ?new PawnJump.Pose(mygame.p_x_now[p],mygame.p_y_now[p],0,1,1):jump.pose(now);
            double footX=pose.x()+PAWN_SLOT_WIDTH/2.0, footY=pose.y()+PAWN_SLOT_HEIGHT;
            double shadowWidth=48*(1-0.35*pose.lift()/PawnJump.HEIGHT);
            g.setFill(Color.color(0,0,0,0.22));
            g.fillOval(footX-shadowWidth/2,footY-9,shadowWidth,12);
            g.setStroke(Theme.PLAYERS[p]); g.setLineWidth(p==mygame.turn?3:2);
            g.strokeOval(footX-shadowWidth/2,footY-9,shadowWidth,12);
            double width=PAWN_WIDTH*pose.scaleX(), height=PAWN_HEIGHT*pose.scaleY();
            // Allow overlap with neighboring artwork, but keep the full figure inside the canvas.
            double x=Math.max(8,Math.min(SIZE-8-width,footX-width/2));
            double y=Math.max(8,Math.min(SIZE-8-height,footY-height-pose.lift()));
            g.save(); g.setEffect(shadow); g.translate(x,y); g.scale(pose.scaleX(),pose.scaleY());
            Art.draw(g,mygame.p_pawn[p],0,0,PAWN_WIDTH,PAWN_HEIGHT);
            g.restore();
        }
        // Animation changes only presentation; the dice result is never altered by rendering.
        double elapsed=(now-mygame.dice.rolledAt)/1e9;
        for(int d=0;d<2;d++) {
            double x=442+d*84,y=715;
            double angle=elapsed>=0 && elapsed<0.48?Math.sin(elapsed*48+d)*14*(1-elapsed/0.48):0;
            g.save(); g.translate(x+30,y+30); g.rotate(angle); g.setEffect(shadow);
            Art.draw(g,d==0?mygame.dice.idice1:mygame.dice.idice2,-30,-30,60,60); g.restore();
        }
        text(g,"前進 "+mygame.dice.count+" 格",638,754,19,Theme.GOLD,168);
    }
}
