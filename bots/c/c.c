#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <ctype.h>

typedef struct
{
    int red[16];
    int blue[16];
    int transparent[16];
    int score[2];
    int current_player;
} Game;

void init_game(Game *game)
{
    for (int i = 0; i < 16; i++)
    {
        game->red[i] = 2;
        game->blue[i] = 2;
        game->transparent[i] = 2;
    }

    game->score[0] = 0;
    game->score[1] = 0;
    game->current_player = 0;
}

int *get_pit_colors(Game *game, char pit)
{
    if (pit == 'R')
    {
        return game->red;
    }

    if (pit == 'B')
    {
        return game->blue;
    }

    return game->transparent;
}

int distribute_seeds_by_color(Game *game, int start_index, char pit_color, int step, int pit_index)
{
    int *pit_colors = get_pit_colors(game, pit_color);
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

void capture_seeds(Game *game, int last_index)
{
    while (1)
    {
        int nb_seeds_in_pit = game->red[last_index] + game->blue[last_index] + game->transparent[last_index];

        if (nb_seeds_in_pit > 1 && nb_seeds_in_pit < 4)
        {
            game->score[game->current_player] += nb_seeds_in_pit;
            game->red[last_index] = 0;
            game->blue[last_index] = 0;
            game->transparent[last_index] = 0;
            last_index = (last_index - 1 + 16) % 16;
        }
        else
        {
            break;
        }
    }
}

void apply_move(Game *game, const char *move)
{
    int is_transparent = strchr(move, 'T') != NULL;
    char pit_color = toupper(move[strlen(move) - 1]);

    int pit_index;

    if (is_transparent)
    {
        char tmp[16];
        strncpy(tmp, move, strlen(move) - 2);
        tmp[strlen(move) - 2] = '\0';
        pit_index = atoi(tmp) - 1;
    }
    else
    {
        char tmp[16];
        strncpy(tmp, move, strlen(move) - 1);
        tmp[strlen(move) - 1] = '\0';
        pit_index = atoi(tmp) - 1;
    }

    int start_index = (pit_index - 1 + 16) % 16;
    int step = (pit_color == 'R') ? 1 : 2;

    if (is_transparent)
    {
        start_index = distribute_seeds_by_color(game, start_index, 'T', step, pit_index);
    }

    int last_index = distribute_seeds_by_color(game, start_index, pit_color, step, pit_index);

    capture_seeds(game, last_index);
    game->current_player = (game->current_player + 1) % 2;
}

void get_valid_moves(Game *game, int player, char moves[][8], int *count)
{
    *count = 0;
    int start_hole = (player == 0) ? 1 : 2;

    for (int hole = start_hole; hole <= 16; hole += 2)
    {
        if (game->red[hole - 1] > 0)
        {
            sprintf(moves[(*count)++], "%dR", hole);
        }

        if (game->blue[hole - 1] > 0)
        {
            sprintf(moves[(*count)++], "%dB", hole);
        }

        if (game->transparent[hole - 1] > 0)
        {
            sprintf(moves[(*count)++], "%dTR", hole);
            sprintf(moves[(*count)++], "%dTB", hole);
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc < 2)
    {
        return 1;
    }

    int my_player = atoi(argv[1]) - 1;

    Game game;
    init_game(&game);
    srand(time(NULL));

    char line[128];

    while (fgets(line, sizeof(line), stdin))
    {
        line[strcspn(line, "\n")] = '\0';

        if (strcmp(line, "END") == 0)
        {
            break;
        }

        if (strcmp(line, "START") != 0)
        {
            apply_move(&game, line);
        }

        char valid_moves[64][8];
        int count = 0;
        get_valid_moves(&game, my_player, valid_moves, &count);

        char my_move[8];

        if (count > 0)
        {
            strcpy(my_move, valid_moves[rand() % count]);
        }
        else
        {
            strcpy(my_move, "???");
        }

        apply_move(&game, my_move);
        printf("%s\n", my_move);
        fflush(stdout);
    }

    return 0;
}
