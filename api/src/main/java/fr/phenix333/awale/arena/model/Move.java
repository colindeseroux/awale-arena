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
    private String color;

}
