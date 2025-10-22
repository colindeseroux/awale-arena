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

    public Game(Bot bot1, Bot bot2) {
        this.bot1 = bot1;
        this.bot2 = bot2;
    }

}
