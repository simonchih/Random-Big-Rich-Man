## Fortune Edition artwork

The JavaFX interface uses newly generated original artwork inspired by the supplied
Richman reference: eight characters, buildings, landmark/event icons, six accurate
dice faces and a Taipei city illustration. All artwork is in `src/main/resources/Art`.
The 58 legacy image files have been retired. Sound retains its original attribution
to gtkmonop, kapitalist, monopolie and the original project contributors.

See [artwork sources, prompts and layout notes](design/ARTWORK.md).

## Install preconditions

* JDK 17 or newer (`JAVA_HOME` should point to the JDK)
* Maven 3.8 or newer

## Game Features

* Random map

## Game Rules

1. Monopoly-like game rule
2. Land Tax: each land $400
3. House Tax: each house $200, hotel is $800
4. Question Mark event:
	a. give player money
	b. pay money
	c. forward steps
	d. go to start point or CKS Memorial Hall
	e. stop once
	f. ...
5. Go to hospital will pay $1000
6. Toll is 20% of land value
7. Toll is double each house
8. Toll is 1600% if land have hotel
9. Player can build hotel if 3 houses on the land
10. If player buy all same color land, toll is double
11. Toll free if land owner is in jail
12. Player exceed start point will give $2000, but $0 if on the start point
13. Go to jail stop 3 turns

## How to build and run

Build the JAR and its runtime dependencies without producing a Windows app image:

```bat
mvn package -P!win-package
```

Run directly from source (loads JavaFX modules automatically):

```bat
mvn javafx:run
```

On Windows, run the built game with `run.cmd`. It prefers `JAVA_HOME`, includes
JavaFX modules and loads the dependencies beside the JAR. A plain `java -jar` does
not supply the JavaFX module path.

The Windows `win-package` profile is enabled by default for `mvn package`; it
produces an app image with a bundled runtime and the new icon under `target/dist`.
An existing app image at the same destination must be moved before packaging again.

The board resizes proportionally with a safe outer margin; all image boxes use
contain scaling. Character selection, transaction dialogs and the asset table use
the same navy/gold theme. Dice movement, balance feedback and scene fades are
presentation effects and do not change game results.

Board pieces use an 84×96 logical-pixel drawing box (previously 17×25) and may
overlap tile artwork. The active player's piece is drawn on top. Each move consists
of separate 360 ms jumps with a 38-pixel arc and 100 ms landing pauses; a tile is
committed only after landing. Ground shadows and a small landing squash reinforce
the feel of picking up and placing a physical piece.

After building, run the Windows visual/integration check:

```powershell
powershell -ExecutionPolicy Bypass -File tools/verify-visuals.ps1
```

This opens the actual JavaFX scenes, checks initial/resized board bounds, all 160
pawn landing anchors, 43 consecutive jumps including corners and wraparound, atlas gutters,
60,000 dice rolls, a transaction and a complete moving turn, and writes screenshots to
`target/visual-qa`. Run it in a desktop session.

