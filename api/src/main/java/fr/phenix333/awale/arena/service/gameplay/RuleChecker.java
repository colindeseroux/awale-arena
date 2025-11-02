package fr.phenix333.awale.arena.service.gameplay;

import fr.phenix333.awale.arena.model.Game;
import fr.phenix333.awale.arena.model.Move;

/**
 * Class for checking game rules.
 * 
 * @author Colin de Seroux
 */
public class RuleChecker {

    /**
     * Check move syntax.
     * 
     * @param move -> String : the move to check
     * 
     * @return boolean -> true if the move is valid, false if the move is invalid
     */
    public static boolean isMoveSyntaxValid(String move) {
        return move.matches("(?:1[0-6]|[1-9])[RBrb]|(?:1[0-6]|[1-9])[Tt][RBrb]");
    }

    /**
     * Check move legality.
     * 
     * @return boolean -> true if the move is legal, false if the move is illegal
     */
    public static boolean isMoveLegal(Game game) {
        Move move = game.getLastMove();
        int moveIndex = move.getIndex();

        if ((moveIndex % 2 == 0 && game.getCurrentPlayer() == 1)
                || (moveIndex % 2 == 1 && game.getCurrentPlayer() == 0)) {
            return false;
        }

        int[] pits = game.getPitColors(move.isTransparent() ? 'T' : move.getColor());

        return pits[moveIndex] != 0;
    }

    /**
     * Check if the game is within the time limit.
     * 
     * @param game -> Game : the game to check
     * 
     * @return boolean -> true if within time limit, false otherwise
     */
    public static boolean isWithinTimeLimit(Game game) {
        return System.currentTimeMillis() - game.getStartTime() <= 210000;
    }

    /**
     * Check if the game is over.
     * 
     * @return boolean -> true if the game is over, false otherwise
     */
    public static boolean isGameOver(Game game) {
        int[] score = game.getScore();
        int total = score[0] + score[1];

        // One player has captured 49 or more seeds.
        if (score[0] >= 49 || score[1] >= 49) {
            return true;
        }

        // Each player has taken 40 seeds (draw).
        if (score[0] >= 40 && score[1] >= 40) {
            return true;
        }

        // There is only strictly less than 10 seeds that remain.
        if (total > 86) {
            return true;
        }

        return false;
    }

}
