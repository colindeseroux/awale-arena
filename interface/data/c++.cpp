#include <iostream>
#include <vector>
#include <string>
#include <cstdlib>
#include <ctime>
#include <algorithm>

class Game
{
public:
    std::vector<int> red;
    std::vector<int> blue;
    std::vector<int> transparent;
    std::vector<int> score;
    int current_player;

    Game() : red(16, 2), blue(16, 2), transparent(16, 2), score(2, 0), current_player(0) {}

    std::vector<int> &get_pit_colors(char pit)
    {
        if (pit == 'R')
        {
            return red;
        }

        if (pit == 'B')
        {
            return blue;
        }

        return transparent;
    }

    int distribute_seeds_by_color(int start_index, char pit_color, int step, int pit_index)
    {
        std::vector<int> &pit_colors = get_pit_colors(pit_color);
        int nb_seeds = pit_colors[pit_index];
        pit_colors[pit_index] = 0;

        int last_index = start_index;
        int distributed = 0;

        while (distributed < nb_seeds)
        {
            last_index = (last_index + step) % 16;

            if (last_index == pit_index)
            {
                last_index = (last_index + step) % 16;
            }

            pit_colors[last_index]++;
            distributed++;
        }
        return last_index;
    }

    void capture_seeds(int last_index)
    {
        while (true)
        {
            int nb_seeds_in_pit = red[last_index] + blue[last_index] + transparent[last_index];

            if (nb_seeds_in_pit > 1 && nb_seeds_in_pit < 4)
            {
                score[current_player] += nb_seeds_in_pit;
                red[last_index] = 0;
                blue[last_index] = 0;
                transparent[last_index] = 0;
                last_index = (last_index - 1 + 16) % 16;
            }
            else
            {
                break;
            }
        }
    }

    void apply_move(const std::string &move)
    {
        bool is_transparent = move.find('T') != std::string::npos;
        char pit_color = toupper(move.back());

        int pit_index;

        if (is_transparent)
        {
            pit_index = std::stoi(move.substr(0, move.size() - 2)) - 1;
        }
        else
        {
            pit_index = std::stoi(move.substr(0, move.size() - 1)) - 1;
        }

        int start_index = (pit_index - 1 + 16) % 16;
        int step = (pit_color == 'R') ? 1 : 2;

        if (is_transparent)
        {
            start_index = distribute_seeds_by_color(start_index, 'T', step, pit_index);
        }

        int last_index = distribute_seeds_by_color(start_index, pit_color, step, pit_index);

        capture_seeds(last_index);
        current_player = (current_player + 1) % 2;
    }

    std::vector<std::string>

    get_valid_moves(int player)
    {
        std::vector<std::string> valid_moves;
        int start_hole = (player == 0) ? 1 : 2;

        for (int hole = start_hole; hole <= 16; hole += 2)
        {
            if (red[hole - 1] > 0)
            {
                valid_moves.push_back(std::to_string(hole) + "R");
            }

            if (blue[hole - 1] > 0)
            {
                valid_moves.push_back(std::to_string(hole) + "B");
            }

            if (transparent[hole - 1] > 0)
            {
                valid_moves.push_back(std::to_string(hole) + "TR");
                valid_moves.push_back(std::to_string(hole) + "TB");
            }
        }

        return valid_moves;
    }
};

int main(int argc, char *argv[])
{
    if (argc < 2)
        return 1;

    int my_player = std::stoi(argv[1]) - 1;
    Game game;

    std::srand(std::time(nullptr));

    std::string line;

    while (std::getline(std::cin, line))
    {
        if (line == "END")
        {
            break;
        }

        if (line != "START")
        {
            game.apply_move(line);
        }

        auto valid_moves = game.get_valid_moves(my_player);

        std::string my_move;

        if (!valid_moves.empty())
        {
            my_move = valid_moves[std::rand() % valid_moves.size()];
        }
        else
        {
            my_move = "???";
        }

        game.apply_move(my_move);

        std::cout << my_move << std::endl;
    }

    return 0;
}
