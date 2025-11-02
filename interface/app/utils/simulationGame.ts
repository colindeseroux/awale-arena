class SimulationGame {
    red: number[];
    blue: number[];
    transparent: number[];
    score: number[];
    currentPlayer: number;
    history: {
        red: number[];
        blue: number[];
        transparent: number[];
        score: number[];
        currentPlayer: number;
    }[];

    constructor() {
        this.red = Array(16).fill(2);
        this.blue = Array(16).fill(2);
        this.transparent = Array(16).fill(2);
        this.score = [0, 0];
        this.currentPlayer = 0;
        this.history = [];
    }

    getPitColors(pit: string): number[] {
        const pitUpper = pit.toUpperCase();

        if (pitUpper === "R") {
            return this.red;
        }

        if (pitUpper === "B") {
            return this.blue;
        }

        return this.transparent;
    }

    distributeSeedsByColor(
        startIndex: number,
        pitColor: string,
        step: number,
        pitIndex: number,
    ): number {
        const pitColors = this.getPitColors(pitColor);
        let nbSeeds = pitColors[pitIndex]!;
        pitColors[pitIndex] = 0;

        let lastIndex = startIndex;
        let distributed = 0;

        while (distributed < nbSeeds) {
            lastIndex = (lastIndex + step) % 16;

            if (lastIndex === pitIndex) {
                lastIndex = (lastIndex + step) % 16;
            }

            pitColors[lastIndex]! += 1;
            distributed += 1;
        }

        return lastIndex;
    }

    applyMove(move: string) {
        if (
            move === "GameOver" ||
            move === "TimeOut" ||
            move === "MoveSyntaxInvalid" ||
            move === "MoveIllegal"
        ) {
            return;
        }

        this.history.push({
            red: [...this.red],
            blue: [...this.blue],
            transparent: [...this.transparent],
            score: [...this.score],
            currentPlayer: this.currentPlayer,
        });

        const isTransparent = move.toUpperCase().includes("T");
        const pitColor = move.slice(-1).toUpperCase();

        let pitIndex: number;

        if (isTransparent) {
            pitIndex = parseInt(move.slice(0, -2)) - 1;
        } else {
            pitIndex = parseInt(move.slice(0, -1)) - 1;
        }

        let startIndex = (pitIndex - 1 + 16) % 16;
        const step = pitColor === "R" ? 1 : 2;

        if (isTransparent) {
            startIndex = this.distributeSeedsByColor(
                startIndex,
                "T",
                step,
                pitIndex,
            );
        }

        const lastIndex = this.distributeSeedsByColor(
            startIndex,
            pitColor,
            step,
            pitIndex,
        );

        this.captureSeeds(lastIndex);
        this.currentPlayer = (this.currentPlayer + 1) % 2;
    }

    captureSeeds(lastIndex: number) {
        let index = lastIndex;

        while (true) {
            const nbSeedsInPit =
                this.red[index]! + this.blue[index]! + this.transparent[index]!;

            if (nbSeedsInPit > 1 && nbSeedsInPit < 4) {
                this.score[this.currentPlayer]! += nbSeedsInPit;
                this.red[index] = 0;
                this.blue[index] = 0;
                this.transparent[index] = 0;
                index = (index - 1 + 16) % 16;
            } else {
                break;
            }
        }
    }

    previousMove() {
        if (this.history.length === 0) {
            return;
        }

        const lastState = this.history.pop()!;
        this.red = lastState.red;
        this.blue = lastState.blue;
        this.transparent = lastState.transparent;
        this.score = lastState.score;
        this.currentPlayer = lastState.currentPlayer;
    }
}

export default SimulationGame;
