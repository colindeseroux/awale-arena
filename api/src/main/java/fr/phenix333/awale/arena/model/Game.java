package fr.phenix333.awale.arena.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.phenix333.awale.arena.converter.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "bot_1", nullable = false)
    private Bot bot1;

    @ManyToOne
    @JoinColumn(name = "bot_2", nullable = false)
    private Bot bot2;

    @Convert(converter = StringListConverter.class)
    @Column(name = "moves", columnDefinition = "text[]")
    private List<String> moves = new ArrayList<>();

    private int winner;

    @Transient
    private long startTime = System.currentTimeMillis();

    @Transient
    private int currentPlayer = 0;

    @Transient
    private boolean isFinished = false;

    @Transient
    private Move lastMove;

    @Transient
    private int[] red = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

    @Transient
    private int[] blue = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

    @Transient
    private int[] transparent = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

    @Transient
    private int[] score = new int[] { 0, 0 };

    public Game(Bot bot1, Bot bot2) {
        this.bot1 = bot1;
        this.bot2 = bot2;
    }

    /**
     * Add a move to the game.
     * 
     * @param move -> String : the move to add
     */
    public void addMove(String move) {
        this.moves.add(move);
    }

    /**
     * Check move syntax.
     * 
     * @param move -> String : the move to check
     * 
     * @return boolean -> true if the move is valid, false if the move is invalid
     */
    private boolean checkMoveSyntax(String move) {
        if (!move.matches("(?:1[0-6]|[1-9])[RBrb]|(?:1[0-6]|[1-9])[Tt][RBrb]")) {
            this.isFinished = true;
            this.winner = (this.currentPlayer + 1) % 2 + 1;

            return false;
        }

        return true;
    }

    /**
     * Break down a move string into its components.
     * 
     * @param move -> String : the move to break down
     */
    private void breakDownMovement(String move) {
        boolean isTransparent = move.contains("T") || move.contains("t");
        int pitIndex = Integer.parseInt(move.substring(0, move.length() - (isTransparent ? 2 : 1))) - 1;
        String pitColor = String.valueOf(move.charAt(move.length() - 1)).toUpperCase();

        this.lastMove = new Move(pitIndex, isTransparent, pitColor);
    }

    /**
     * Get pit colors array.
     */
    private int[] getPitColors() {
        if (this.lastMove.isTransparent()) {
            return this.transparent;
        }

        if (this.lastMove.getColor().equals("R")) {
            return this.red;
        }

        return this.blue;
    }

    /**
     * Check move legality.
     * 
     * @return boolean -> true if the move is legal, false if the move is illegal
     */
    private boolean checkMoveLegality() {
        if ((this.lastMove.getIndex() % 2 == 0 && this.currentPlayer == 1)
                || (this.lastMove.getIndex() % 2 == 1 && this.currentPlayer == 0)) {
            this.isFinished = true;
            this.winner = (this.currentPlayer + 1) % 2 + 1;

            return false;
        }

        if (this.getPitColors()[this.lastMove.getIndex()] == 0) {
            this.isFinished = true;
            this.winner = (this.currentPlayer + 1) % 2 + 1;

            return false;
        }

        return true;
    }

    /**
     * Check max time for a game (3 minutes and 30 seconds).
     * 
     * @return boolean -> true if the game can continue, false if the game is over
     */
    private boolean checkMaxTime() {
        if (System.currentTimeMillis() - this.startTime > 210000) {
            this.isFinished = true;
            this.winner = this.score[0] > this.score[1] ? 1 : this.score[0] < this.score[1] ? 2 : 0;

            return false;
        }

        return true;
    }

    /**
     * Capture seeds starting from the last index.
     * 
     * @param lastIndex -> int : the last index
     */
    private void captureSeeds(int lastIndex) {
        while (true) {
            int nbSeedsInPit = this.red[lastIndex] + this.blue[lastIndex] + this.transparent[lastIndex];

            if (nbSeedsInPit == 2 || nbSeedsInPit == 3) {
                this.score[this.currentPlayer] += nbSeedsInPit;
                this.red[lastIndex] = 0;
                this.blue[lastIndex] = 0;
                this.transparent[lastIndex] = 0;
                lastIndex = (lastIndex - 1 + 16) % 16;
            } else {
                break;
            }
        }
    }

    /**
     * Distribute seeds according to the last move.
     * 
     * @return int -> The last index after distribution
     */
    private int distributeSeeds() {
        int[] pitColors = this.getPitColors();
        int startIndex = this.lastMove.getIndex();
        int nbSeeds = pitColors[startIndex];
        pitColors[startIndex] = 0;

        int lastIndex = startIndex;
        int step = this.lastMove.getColor().equals("R") ? 1 : 2;
        int distributed = 0;

        while (distributed < nbSeeds) {
            lastIndex = (lastIndex + step) % 16;

            // Pass over the starting pit
            if (lastIndex == startIndex) {
                lastIndex = (lastIndex + step) % 16;
            }

            pitColors[lastIndex]++;
            distributed++;
        }

        return lastIndex;
    }

    /**
     * Count seeds for a player starting from a given index.
     * 
     * @param startIndex -> int : the starting index
     * 
     * @return int -> The number of seeds
     */
    private int countSeeds(int startIndex) {
        int count = 0;

        for (int i = startIndex; i < 16; i += 2) {
            count += red[i] + blue[i] + transparent[i];
        }

        return count;
    }

    /**
     * Collect seeds for a player starting from a given index.
     * 
     * @param player -> int : the player number
     */
    private void collectSeeds(int player) {
        for (int i = player; i < 16; i += 2) {
            score[player] += red[i] + blue[i] + transparent[i];
            red[i] = blue[i] = transparent[i] = 0;
        }
    }

    /**
     * Check for starving condition and collect seeds if necessary.
     */
    private void starvingCheck() {
        if (this.countSeeds((this.currentPlayer + 1) % 2) == 0) {
            this.collectSeeds(this.currentPlayer);
        }
    }

    /**
     * Play the last move.
     */
    private void playMove() {
        int lastIndex = this.distributeSeeds();

        this.captureSeeds(lastIndex);

        this.starvingCheck();
    }

    /**
     * Check if the game is over.
     * 
     * @return boolean -> true if the game is over, false otherwise
     */
    private boolean checkGameOver() {
        if (this.score[0] >= 49) {
            this.isFinished = true;
            this.winner = 1;

            return true;
        }

        if (this.score[1] >= 49) {
            this.isFinished = true;
            this.winner = 2;

            return true;
        }

        if (this.score[0] >= 40 && this.score[1] >= 40) {
            this.isFinished = true;
            this.winner = 0;

            return true;
        }

        if (this.score[0] + this.score[1] > 86) {
            this.isFinished = true;
            this.winner = this.score[0] > this.score[1] ? 1 : this.score[0] < this.score[1] ? 2 : 0;

            return true;
        }

        return false;
    }

    /**
     * Simulate the last move played.
     */
    public void simulateLastMove() {
        String lastMove = this.moves.get(this.moves.size() - 1);

        if (!this.checkMoveSyntax(lastMove)) {
            return;
        }

        this.breakDownMovement(lastMove);

        if (!this.checkMoveLegality()) {
            return;
        }

        if (!this.checkMaxTime()) {
            return;
        }

        this.playMove();

        if (this.checkGameOver()) {
            return;
        }

        this.currentPlayer = (this.currentPlayer + 1) % 2;
    }

}
