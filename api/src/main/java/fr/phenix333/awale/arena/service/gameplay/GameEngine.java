package fr.phenix333.awale.arena.service.gameplay;

import org.springframework.stereotype.Component;

import fr.phenix333.awale.arena.model.Game;
import fr.phenix333.awale.arena.model.Move;

/**
 * Class for game engine logic.
 * 
 * @author Colin de Seroux
 */
@Component
public class GameEngine {

    /**
     * Distribute seeds according to the last move.
     * 
     * @param game     -> Game : the game
     * @param int      -> startIndex : the first index of the distribution
     * @param pitColor -> char : the pit color ('R', 'B', 'T')
     * @param step     -> int : the step size for distribution
     * 
     * @return int -> The last index after distribution
     */
    private int distributeSeedsByColor(Game game, int startIndex, char pitColor, int step) {
        int[] pitColors = game.getPitColors(pitColor);
        int pitIndex = game.getLastMove().getIndex();
        int nbSeeds = pitColors[pitIndex];
        pitColors[pitIndex] = 0;

        int lastIndex = startIndex;
        int distributed = 0;

        while (distributed < nbSeeds) {
            lastIndex = (lastIndex + step) % 16;

            // Pass over the starting pit
            if (lastIndex == pitIndex) {
                lastIndex = (lastIndex + step) % 16;
            }

            pitColors[lastIndex]++;
            distributed++;
        }

        return lastIndex;
    }

    /**
     * Distribute seeds according to the last move.
     * 
     * @param game -> Game : the game
     * 
     * @return int -> The last index after distribution
     */
    private int distributeSeeds(Game game) {
        Move move = game.getLastMove();
        int startIndex = (move.getIndex() - 1 + 16) % 16;
        char pitColor = move.getColor();
        int step = move.getColor() == 'R' ? 1 : 2;

        if (move.isTransparent()) {
            startIndex = this.distributeSeedsByColor(game, startIndex, 'T', step);
        }

        return this.distributeSeedsByColor(game, startIndex, pitColor, step);
    }

    /**
     * Capture seeds starting from the last index.
     * 
     * @param game      -> Game : the game
     * @param lastIndex -> int : the last index
     */
    private void captureSeeds(Game game, int lastIndex) {
        while (true) {
            int nbSeedsInPit = game.getRed()[lastIndex] + game.getBlue()[lastIndex] + game.getTransparent()[lastIndex];

            if (nbSeedsInPit == 2 || nbSeedsInPit == 3) {
                game.getScore()[game.getCurrentPlayer()] += nbSeedsInPit;
                game.getRed()[lastIndex] = 0;
                game.getBlue()[lastIndex] = 0;
                game.getTransparent()[lastIndex] = 0;
                lastIndex = (lastIndex - 1 + 16) % 16;
            } else {
                break;
            }
        }
    }

    /**
     * Count seeds for a player starting from a given index.
     * 
     * @param game       -> Game : the game
     * @param startIndex -> int : the starting index
     * 
     * @return int -> The number of seeds
     */
    private int countSeeds(Game game, int startIndex) {
        int count = 0;

        for (int i = startIndex; i < 16; i += 2) {
            count += game.getRed()[i] + game.getBlue()[i] + game.getTransparent()[i];
        }

        return count;
    }

    /**
     * Collect seeds for a player starting from a given index.
     * 
     * @param game   -> Game : the game
     * @param player -> int : the player number
     */
    private void collectSeeds(Game game, int player) {
        for (int i = player; i < 16; i += 2) {
            game.getScore()[player] += game.getRed()[i] + game.getBlue()[i] + game.getTransparent()[i];
            game.getRed()[i] = game.getBlue()[i] = game.getTransparent()[i] = 0;
        }
    }

    /**
     * Check for starving condition and collect seeds if necessary.
     * 
     * @param game -> Game : the game
     */
    private void starvingCheck(Game game) {
        if (this.countSeeds(game, game.getOpponentPlayer()) == 0) {
            this.collectSeeds(game, game.getCurrentPlayer());
        }
    }

    /**
     * Play a move in the game.
     * 
     * @param game -> Game : the game
     * @param move -> String : the move to play
     */
    public void playMove(Game game) {
        String move = game.getLastMoveString();

        if (!RuleChecker.isMoveSyntaxValid(move)) {
            game.addMove("MoveSyntaxInvalid");
            game.setFinished(true);
            game.setWinner(game.getOpponentPlayer());

            return;
        }

        Move parsedMove = Move.parse(move);
        game.setLastMove(parsedMove);

        if (!RuleChecker.isMoveLegal(game)) {
            game.addMove("MoveIllegal");
            game.setFinished(true);
            game.setWinner(game.getOpponentPlayer());

            return;
        }

        if (!RuleChecker.isWithinTimeLimit(game)) {
            game.setFinished(true);
            game.setWinner(game.getWinnerByScore());

            return;
        }

        int lastIndex = this.distributeSeeds(game);
        this.captureSeeds(game, lastIndex);
        this.starvingCheck(game);

        if (RuleChecker.isGameOver(game)) {
            game.addMove("GameOver");
            game.setFinished(true);
            game.setWinner(game.getWinnerByScore());
            return;
        }

        game.setCurrentPlayer(game.getOpponentPlayer());
    }

}
