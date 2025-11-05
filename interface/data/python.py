import sys
import random

class Game:
    def __init__(self):
        self.red = [2] * 16
        self.blue = [2] * 16
        self.transparent = [2] * 16
        self.score = [0, 0]
        self.current_player = 0

    def get_pit_colors(self, pit: str):
        if pit.upper() == 'R':
            return self.red
        
        if pit.upper() == 'B':
            return self.blue

        return self.transparent

    def distribute_seeds_by_color(self, start_index: int, pit_color: str, step: int, pit_index: int):
        pit_colors = self.get_pit_colors(pit_color)
        nb_seeds = pit_colors[pit_index]
        pit_colors[pit_index] = 0

        last_index = start_index
        distributed = 0

        while distributed < nb_seeds:
            last_index = (last_index + step) % 16

            if last_index == pit_index:
                last_index = (last_index + step) % 16

            pit_colors[last_index] += 1
            distributed += 1

        return last_index

    def apply_move(self, move: str):
        is_transparent = 'T' in move or 't' in move
        pit_color = move[-1].upper()

        if is_transparent:
            pit_index = int(move[:-2]) - 1
        else:
            pit_index = int(move[:-1]) - 1

        start_index = (pit_index - 1 + 16) % 16
        step = 1 if pit_color == 'R' else 2

        if is_transparent:
            start_index = self.distribute_seeds_by_color(start_index, 'T', step, pit_index)

        last_index = self.distribute_seeds_by_color(start_index, pit_color, step, pit_index)

        self.capture_seeds(last_index)
        self.current_player = (self.current_player + 1) % 2

    def capture_seeds(self, last_index: int):
        while True:
            nb_seeds_in_pit = self.red[last_index] + self.blue[last_index] + self.transparent[last_index]
            
            if 1 < nb_seeds_in_pit < 4:
                self.score[self.current_player] += nb_seeds_in_pit
                self.red[last_index] = 0
                self.blue[last_index] = 0
                self.transparent[last_index] = 0
                last_index = (last_index - 1 + 16) % 16
            else:
                break

    def get_valid_moves(self, player: int):
        valid_moves = []
        start_hole = 1 if player == 0 else 2

        for hole in range(start_hole, 17, 2):
            if self.red[hole - 1] > 0:
                valid_moves.append(f"{hole}R")
                
            if self.blue[hole - 1] > 0:
                valid_moves.append(f"{hole}B")
                
            if self.transparent[hole - 1] > 0:
                valid_moves.append(f"{hole}TR")
                valid_moves.append(f"{hole}TB")

        return valid_moves

def main():
    my_player = int(sys.argv[1]) - 1

    game = Game()

    for line in sys.stdin:
        move = line.strip()
        
        if move == "END":
            break
        
        if move != "START":
            game.apply_move(move)
        
        valid_moves = game.get_valid_moves(my_player)
        
        if valid_moves:
            my_move = random.choice(valid_moves)
        else:
            my_move = "???"
            
        game.apply_move(my_move)
        
        sys.stdout.write(my_move + "\n")
        sys.stdout.flush()

if __name__ == "__main__":
    main()
