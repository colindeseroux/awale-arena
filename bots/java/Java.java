import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Java {

    static class Game {
        private int[] red;
        private int[] blue;
        private int[] transparent;
        private int[] score;
        private int currentPlayer;

        public Game() {
            this.red = new int[16];
            this.blue = new int[16];
            this.transparent = new int[16];
            this.score = new int[2];
            this.currentPlayer = 0;

            for (int i = 0; i < 16; i++) {
                this.red[i] = 2;
                this.blue[i] = 2;
                this.transparent[i] = 2;
            }
        }

        public void applyMove(String move) {
            boolean isTransparent = move.contains("T") || move.contains("t");
            int pitIndex;
            char pitColor;

            if (isTransparent) {
                pitIndex = Integer.parseInt(move.substring(0, move.length() - 2)) - 1;
                pitColor = move.charAt(move.length() - 1);
            } else {
                pitIndex = Integer.parseInt(move.substring(0, move.length() - 1)) - 1;
                pitColor = move.charAt(move.length() - 1);
            }

            int[] pitColors;

            if (isTransparent) {
                pitColors = this.transparent;
            } else if (Character.toUpperCase(pitColor) == 'R') {
                pitColors = this.red;
            } else {
                pitColors = this.blue;
            }

            int nbSeeds = pitColors[pitIndex];
            pitColors[pitIndex] = 0;

            int lastIndex;

            if (Character.toUpperCase(pitColor) == 'R') {
                for (int i = 1; i <= nbSeeds; i++) {
                    pitColors[(pitIndex + i) % 16]++;
                }

                lastIndex = (pitIndex + nbSeeds) % 16;
            } else {
                for (int i = 1; i < nbSeeds * 2; i += 2) {
                    pitColors[(pitIndex + i) % 16]++;
                }

                lastIndex = (pitIndex + nbSeeds * 2) % 16;
            }

            this.captureSeeds(lastIndex);
            this.currentPlayer = (this.currentPlayer + 1) % 2;
        }

        private void captureSeeds(int lastIndex) {
            while (true) {
                int nbSeedsInPit = this.red[lastIndex] + this.blue[lastIndex] + this.transparent[lastIndex];

                if (nbSeedsInPit > 1 && nbSeedsInPit < 4) {
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

        public List<String> getValidMoves(int player) {
            List<String> validMoves = new ArrayList<>();
            int startHole = (player == 0) ? 1 : 2;

            for (int hole = startHole; hole <= 16; hole += 2) {
                int index = hole - 1;

                if (this.red[index] > 0) {
                    validMoves.add(hole + "R");
                }

                if (this.blue[index] > 0) {
                    validMoves.add(hole + "B");
                }

                if (this.transparent[index] > 0) {
                    validMoves.add(hole + "TR");
                    validMoves.add(hole + "TB");
                }
            }

            return validMoves;
        }
    }

    public static void main(String[] args) throws IOException {
        int myPlayer = Integer.parseInt(args[0]) - 1;
        Game game = new Game();
        Random random = new Random();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = reader.readLine()) != null) {
            String move = line.trim();

            if (move.equals("END")) {
                break;
            }

            if (!move.equals("START")) {
                game.applyMove(move);
            }

            List<String> validMoves = game.getValidMoves(myPlayer);

            String myMove;

            if (!validMoves.isEmpty()) {
                myMove = validMoves.get(random.nextInt(validMoves.size()));
            } else {
                myMove = "???";
            }

            game.applyMove(myMove);

            System.out.println(myMove);
            System.out.flush();
        }
    }

}