package fr.phenix333.awale.arena.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Move {

    private int index;
    private boolean isTransparent;
    private char color;

    /**
     * Break down a move string into its components.
     * 
     * @param move -> String : the move to break down
     * 
     * @return Move -> the parsed Move object
     */
    public static Move parse(String move) {
        boolean isTransparent = move.contains("T") || move.contains("t");
        int pitIndex = Integer.parseInt(move.substring(0, move.length() - (isTransparent ? 2 : 1))) - 1;
        char pitColor = Character.toUpperCase(move.charAt(move.length() - 1));

        return new Move(pitIndex, isTransparent, pitColor);
    }

}
