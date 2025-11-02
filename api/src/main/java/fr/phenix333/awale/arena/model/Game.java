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
     * Get last move string.
     * 
     * @return String -> The last move string
     */
    public String getLastMoveString() {
        if (this.moves.isEmpty()) {
            return null;
        }

        return this.moves.get(this.moves.size() - 1);
    }

    /**
     * Get pit colors array.
     * 
     * @param pit -> char : the pit color ('R', 'B', 'T')
     * 
     * @return int[] -> The pit colors array
     */
    public int[] getPitColors(char pit) {
        return pit == 'R' ? this.red : (pit == 'B' ? this.blue : this.transparent);
    }

    /**
     * Get the opponent player's index.
     * 
     * @param player -> int : the current player's index
     * 
     * @return int -> The opponent player's index
     */
    public int getOpponentPlayer() {
        return (this.currentPlayer + 1) % 2;
    }

    /**
     * Determine the winner based on scores.
     * 
     * @param game -> Game : the game to evaluate
     * 
     * @return int -> 0 for draw, 1 for player 1 win, 2 for player 2 win
     */
    public int getWinnerByScore() {
        return Integer.compare(this.score[0], this.score[1]) == 0 ? 0
                : (this.score[0] > this.score[1] ? 1 : 2);
    }

}
