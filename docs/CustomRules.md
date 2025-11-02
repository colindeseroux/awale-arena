# AI Game Programming Awale

The goal of the game is to have the most seeds at the end of the game. This is a variation of the game Awale that you can find on the internet.

## Board

- There are 16 holes, 8 per player.
- Holes are number from 1 to 16.
- We turn clockwise: Hole 1 follows clockwise hole 16.
- The first player has the odd holes, the second player has the even holes.

## Seeds

- There are two main colors (red and blue) and one transparent color (transparent).
- At the beginning there are 2 red seeds, 2 blue seeds and 2 transparent seeds per hole.

## Goal

The goal of the game is to capture more seeds than one's opponent. Since there is an even number of seeds, it is possible for the game to end in a draw, where each player has captured 48.

## Sowing

Players take turns moving the seeds. On a turn, a player chooses one of the height holes under their control. The player removes seeds from that hole (see below for the color management), and distributes them, dropping one in holes clockwise (i.e. in non decreasing order) from this hole, in a process called sowing.

Moves are made according to colors. First a color is designed and all the seeds of this color are played:

- If the seeds are red, then they are distributed in each hole.
- If the seeds are blue, then they are distributed only in the opponent's holes.
- If transparent color is selected then the player has to decide if they are played like red or like blue. Then they are played with the other seeds of the same color. BE CAREFUL: transparent remains transparent and are distributed first (that is before the other seeds of the designated color).

Seeds are not distributed into the hole drawn from. The starting hole is always left empty for the selected color; if a clock turn is made then the initial hole is skipped, and the seed is placed in the next hole.

Thus, a move is expressed by NC if B or R seeds are played where N is the number of the hole, C is the color which is played. For instance, 3R means that we play the red seeds of hole 3 (and only the red). If a transparent color is played then the move is expressed by NTC. 4TR means that the transparent seeds are considered as red and they are played with the other reds.

## Capturing

- Capturing occurs only when a player brings the count of an hole to exactly two or three seeds (of any color). This always captures the seeds in the corresponding hole, and possibly more: if the previous-to-last seed also brought an hole to two or three seeeds, these are captured as well, and so on until a hole is reached which does not contain two or three seeds. The captured seeds are set aside.
- Be careful, it is allowed to take the seeds from its own hole and seeds are captured independently of their colors.
- Starving the opponent IS ALLOWED.
- Taking all the seeds of the opponent is allowed. In case of starving all the seeds are captured by the last player.

## Winning

The game is over when:

- A player does not respond within 1 second (the opponent win).
- Each player has taken 40 seeds (draw).
- There is only strictly less than 10 seeds that remain.
- One player has captured 49 or more seeds.
- Time of 3 minutes 30 seconds exceeded
- The winner is the player who has more seeds than his opponent.
