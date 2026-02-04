Image or sound from below project or Internet:
gtkmonop-0.3.0
kapitalist-0.4
monopolie-0.9.7

Install precondition:
Install java 1.8.0

FAQ:
Q: I alreay install Java but when I try to execute random-big-rich-man-*.exe it shows Java not found error.
How do I do?
A: You need to set the JAVA_HOME environment variable to your JDK or JRE base path, 
e.g. C:\Program Files\Java\jdk1.8.0_91. For this, go to 
Control Panel -> System -> Advanced System Settings -> Advanced -> Environment Variables -> System Variables -> New.

Game Feature:
Random map
The land name is real TAIWAN Taipei/New Taipei location name

Game Rule:
1. Monopoly-like game rule
2. Land Tax: each land $400
3. House Tax: each house $200, hotel is $800
4. Question Mark event:
a. give player money
b. pay money
c. forward steps
d. go to start point or CKS Memorial Hall
e. stop once
...
5. Go to hospital will pay $1000
6. Toll is 20% of land value
7. Toll is double each house
8. Toll is 1600% if land have hotel
9. Player can build hotel if 3 houses on the land
10. If player buy all same color land, toll is double
11. Toll free if land owner is in jail
12. Player exceed start point will give $2000, but $0 if on the start point
13. Go to jail stop 3 turns

Following is Chinese user guide:
遊戲名稱:瑞德大富翁
遊戲平台：windows 8和其它支援Java的平台
緣起：
大富翁是我小時候很喜歡的桌遊，
2014年底，我發現有一款VB寫的Monopolie的project，
下載回來執行後，發現在Windows 8底下會有fatal error.
因此興起我想另外寫一款JAVA的大富翁遊戲。

遊戲特色：
隨機地圖
土地名稱是真實台北/新北英文地名

遊戲規則：
1. 按Roll Dice這個Button來投骰子
2. 買全同色的土地，過路費兩倍
3. 在監獄中，不能收過路費
4. 在0.1.1b版中，過路費是土地價格的20%，
每蓋一間房子，過路費變為兩倍，
蓋一間飯店，過路費為16倍。
5. 蓋完三間房子，才可以蓋一間飯店。
6. 土地税(Land Tax): 每筆$400
7. 房屋税(House Tax): 每間$200, 飯店是$800
8. 問號事件包括:
a. 獲得金錢 (在0.1.1b中，是100~3000)
b. 付錢 (在0.1.1b中，是100~3000)
c. 收所得税，大約是玩家金錢的10%，
如果玩家錢太少，就不收錢。
d. 前進1~12步
e. 前進到起點
f. 前進到中正紀念堂
g. 暫停一個回合
...(其它事件可能在0.1.1b以後的版本加入)

9. 超過起點可以得$2000，但是剛好走到起點不會有錢。
10. 進到醫院要付$1000，但走到醫院的大格子不會進入醫院(免錢)
11. 進入監獄，玩家暫停三回合
12. 其它規則，大致跟大富翁遊戲一樣

Q&A.
Q:我已經安裝好Java了，但是執行random-big-rich-man-*.exe, 它還是說找不到Java, 要怎麼辦？
A: 你需要設定環境變數 JAVA_HOME 為JDK或JRE的路徑， 
e.g. C:\Program Files\Java\jdk1.8.0_91. 要這麼做，請到以下位子去設定 
控制台 -> 系統 -> 進階系統設定 -> 進階 -> 環境變數 -> 系統變數 -> 新增.

Q:為什麼在Player Setting，買地或買房屋時，按"X"來關閉視窗沒反應？
A:那邊是故意把X關閉功能disable，以避免誤按，請改用當時視窗的button來決定動作。

Q:這個遊戲在Linux效果如何？
A:這個遊戲的0.1.1 executable jar file，在Linux套件Fedora 20下測過(需手動安裝Java 1.8.0)，是可以玩，
但是效果有點差強人意…建議可能的話，在Windows下玩。

Q:這個遊戲何時會出正式版？
A:正式版沒有預定何時會出，甚至最後可能沒有正式版，這也是很有可能。