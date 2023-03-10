package tema_poo;

import java.util.Random;

public class Cell {
    int x;
    int y;
    boolean visited;
    char review;

    // metoda care returneaza nr de monede gasite pe celula curenta
    public int getPossibleFoundCoins() {
        int possible_found_coins;
        Random rand = new Random();
        int index = rand.nextInt(5);
        if (index == 0)
            possible_found_coins = rand.nextInt(6) + 15;
        else
            possible_found_coins = 0;
        return possible_found_coins;
    }

    enum board_cell {
        EMPTY,
        ENEMY,
        SHOP,
        FINISH
    }

    board_cell type_cell;

    public Cell(int x, int y, board_cell type_cell, boolean visited, char review) {
        this.x = x;
        this.y = y;
        this.type_cell = type_cell;
        this.visited = visited;
        this.review = review;
    }

    CellElement current_character_from_cell = new CellElement() {
        @Override
        public char toCharacter() {
            if (type_cell == board_cell.ENEMY) {
                return 'E';
            }
            if (type_cell == board_cell.SHOP) {
                return 'S';
            }
            if (type_cell == board_cell.FINISH) {
                return 'F';
            }
            return 'N';
        }
    };
}
