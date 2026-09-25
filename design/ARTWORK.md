# Richman — Fortune Edition

Generated with the built-in imagegen tool on 2026-09-25. The user-supplied
`Richman_icon.png` was used as a style reference, not as a runtime asset.
No external API key or fallback CLI was used.

## Runtime assets

| File in `src/main/resources/Art` | Use |
| --- | --- |
| `characters-atlas.png` | Eight full-body characters; house, hotel, memorial hall, hospital, jail, start arrow, chance envelope and decorative dice pair |
| `dice-atlas.png` | Six dice faces, visually checked for exact pip count |
| `taipei.png` | Welcome and board-center key art |
| `richman.png`, `richman.ico` | Export of the new wealth-god sprite, with transparent padding; Windows package and window icon |
| `theme.css` | Navy, ivory, vermilion and gold control styling |

The original 58 images are replaced by these atlases. Former rotated house/hotel
and chance variants now share upright artwork, fitted into each tile's own image
box. Former 24 dice orientations now use six unambiguous face textures; each value
is still equally likely. Player portraits and pawns share the same matching sprite.
The eighth character is selectable; the previous duplicate-color exclusion is removed.

`Art.java` contains the measured atlas regions, adds a transparent gutter, caches
images and centralizes aspect-preserving contain scaling. The generated sheet did
not follow a perfectly uniform grid, so measured regions are used rather than equal
quadrants. No image is stretched to fill a differently proportioned box.

`MapCanvas.cell` defines the 40 tile rectangles; `pawnSlot` reserves four separate
landing anchors per tile. At the user's request, pieces are now much larger (84×96
logical pixels) and may overlap tile artwork. The active player is drawn last.
`PawnJump` defines a separate arc per square with pickup, flight, landing squash and
a short pause; logical positions update only after each landing.
The board is 1000 logical pixels, has a 20-pixel internal margin and
an additional 12-pixel viewport margin. The entire board and controls scale together.
Tile images, labels, dice and action buttons have separate regions; large player
pieces can cover neighboring artwork while remaining within the canvas. Resizing is applied
in layout, including the first frame. Visual effects include scene fades, button
hover highlights, active-player outlines, dice wobble and fading balance deltas.

## Final generation prompts

### Character and city-object atlas

Use case: stylized-concept. Create ONE production game sprite atlas asset for a
JavaFX Taiwanese Richman board game. The attached reference is STYLE ONLY: polished
dimensional cartoon, rich red and gold, dark navy outlines, luminous beveled gold,
friendly charming Taiwanese board game aesthetic. Atlas is a precisely aligned 4
columns by 4 rows uniform grid, square 2048x2048, transparent background, no grid
lines, no captions, no text except question mark. Each sprite fully contained within
its cell with 18% clear padding on EVERY edge, no overlapping cells. Row 1: smiling
red/gold wealth god with white beard holding ingot; jade green dragon mascot;
sapphire blue businesswoman; purple lucky cat. Row 2: orange tiger mascot; turquoise
explorer girl; pink rabbit mascot; silver robot. All eight full-body collectible
miniature game pieces on tiny gold bases, equally sized. Row 3: red-roof jade
townhouse; luxury gold/red hotel; blue-roof Chiang Kai-shek memorial hall; friendly
cream hospital with green medical cross. Row 4: whimsical blue-gray jail with barred
window; curled gold GO arrow pointing left on red round medallion (no letters);
large gold question mark on red lucky envelope; a pair of ivory and gold casino
dice with dark navy pips. Clean alpha edges, high quality game illustration,
coherent perspective, crisp at small sizes.

### Taipei key art

Use case: stylized-concept. One premium landscape background illustration for the
center and welcome screen of a Taiwanese Richman JavaFX board game, 1536x1024.
Reference image is style only. New original composition: miniature Taipei waterfront
at golden hour, teal Taipei 101 and white cable-stayed bridge in middle distance,
small jewel-colored townhouses and trees bordering a winding river, a cheerful
red/gold wealth god holding gold ingot situated on the right quarter. Elegant navy
shadows, radiant warm gold, rich vermilion, ivory clouds, clean polished dimensional
cartoon illustration with crisp dark outlines, professional game key art. Framing:
full skyline and mascot contained with at least 8% edge safety; top left and center
sky has calm navy-to-teal open space for UI headings that will be rendered separately.
No letters, no words, no logos, no dice, no UI, no border. Rich detail around perimeter
but uncluttered center. Broad cinematic lighting, tasteful gold sparkle accents.

### Dice face atlas

Use case: stylized-concept. Production sprite sheet texture for a premium Taiwanese
board game. ONE square transparent PNG containing exactly SIX dice faces in a strict
uniform 3-column 2-row grid. Each cell has one identical FRONT-FACING square ivory
dice tile, small bevelled gold frame, softly rounded corners, navy recessed circular
pips. Almost orthographic front view, NOT isometric. Tile width and height 65% of
cell so extremely generous empty margins; each completely isolated. Top row left to
right shows EXACTLY 1 pip (center), 2 pips (top left and bottom right), 3 pips (top left,
center, bottom right). Bottom row shows EXACTLY 4 pips (four corners), 5 pips (four
corners and center), 6 pips (two vertical columns of three). Identical size, cream
enamel, luxurious polished gold, soft dimensional highlight top-left and small shadow.
No text, no labels, no other objects. Transparent background. Accurate pip count is
essential for gameplay.

## Reproducible verification

Build with `mvn package -P!win-package`, then run `tools/verify-visuals.ps1`.
Screenshots are generated under `target/visual-qa`. `tools/ExportIcon.java` recreates
the PNG-backed ICO from the actual cached sprite using JavaFX, not a separate drawing.
The visual checker requires a desktop JavaFX session; it is not a headless browser test.
