package tema_poo;

import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList<Cell> {
    static ArrayList<ArrayList<Cell>> board = new ArrayList<>();
    static int length;
    static int width;
    Character current_character;
    Cell current_cell;

    private Grid(int length, int width) {
        Grid.length = length;
        Grid.width = width;
    }

    public static Grid init(int length, int width) {
        return new Grid(length, width);
    }

    // metoda care returneaza harta (folosita pentru terminal)
    static ArrayList<ArrayList<Cell>> getMap() {
        ArrayList<Cell> aux = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cell position0 = new Cell(0, i, Cell.board_cell.EMPTY, false, 'N');
            aux.add(position0);
        }
        Cell position0 = new Cell(0, 3, Cell.board_cell.SHOP, false, 'N');
        aux.add(position0);
        Cell position1 = new Cell(0, 4, Cell.board_cell.EMPTY, false, 'N');
        aux.add(position1);
        board.add(aux);
        ArrayList<Cell> aux1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cell position2 = new Cell(1, i, Cell.board_cell.EMPTY, false, 'N');
            aux1.add(position2);
        }
        Cell position3 = new Cell(1, 3, Cell.board_cell.SHOP, false, 'N');
        aux1.add(position3);
        Cell position4 = new Cell(1, 4, Cell.board_cell.EMPTY, false, 'N');
        aux1.add(position4);
        board.add(aux1);
        ArrayList<Cell> aux2 = new ArrayList<>();
        Cell position5 = new Cell(2, 0, Cell.board_cell.SHOP, false, 'N');
        aux2.add(position5);
        for (int i = 1; i <= 4; i++) {
            Cell position6 = new Cell(2, i, Cell.board_cell.EMPTY, false, 'N');
            aux2.add(position6);
        }
        board.add(aux2);
        ArrayList<Cell> aux3 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Cell position7 = new Cell(3, i, Cell.board_cell.EMPTY, false, 'N');
            aux3.add(position7);
        }
        Cell position8 = new Cell(3, 4, Cell.board_cell.ENEMY, false, 'N');
        aux3.add(position8);
        board.add(aux3);
        ArrayList<Cell> aux4 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Cell position9 = new Cell(4, i, Cell.board_cell.EMPTY, false, 'N');
            aux4.add(position9);
        }
        Cell position10 = new Cell(4, 4, Cell.board_cell.FINISH, false, 'N');
        aux4.add(position10);
        board.add(aux4);
        return board;
    }

    // metoda care returneaza harta (folosita pentru interfata grafica)
    static ArrayList<ArrayList<Cell>> getMapGui() {
        Random rand = new Random();
        ArrayList<ArrayList<Cell>> board_aux = new ArrayList<>();
        for (int i = 0; i < Grid.length; i++) {
            ArrayList<Cell> list = new ArrayList<>();
            for (int j = 0; j < Grid.width; j++) {
                Cell empty = new Cell(0, 0, Cell.board_cell.EMPTY, false, 'N');
                list.add(empty);
            }
            board_aux.add(list);
        }
        for (int i = 0; i < 2; i++) {
            int poz_x = rand.nextInt(Grid.length);
            int poz_y = rand.nextInt(Grid.width);
            while (board_aux.get(poz_x).get(poz_y).x != 0 && board_aux.get(poz_x).get(poz_y).y != 0) {
                poz_x = rand.nextInt(Grid.length);
                poz_y = rand.nextInt(Grid.width);
            }
            Cell element = new Cell(poz_x, poz_y, Cell.board_cell.SHOP, false, 'N');
            board_aux.get(poz_x).set(poz_y, element);
        }
        for (int i = 0; i < 4; i++) {
            int poz_x = rand.nextInt(Grid.length);
            int poz_y = rand.nextInt(Grid.width);
            while (board_aux.get(poz_x).get(poz_y).x != 0 && board_aux.get(poz_x).get(poz_y).y != 0) {
                poz_x = rand.nextInt(Grid.length);
                poz_y = rand.nextInt(Grid.width);
            }
            Cell element = new Cell(poz_x, poz_y, Cell.board_cell.ENEMY, false, 'N');
            board_aux.get(poz_x).set(poz_y, element);
        }
        int poz_x = rand.nextInt(Grid.length);
        int poz_y = rand.nextInt(Grid.width);
        while (board_aux.get(poz_x).get(poz_y).x != 0 && board_aux.get(poz_x).get(poz_y).y != 0 && poz_y == 0 && poz_x == 0) {
            poz_x = rand.nextInt(Grid.length);
            poz_y = rand.nextInt(Grid.width);
        }
        Cell element = new Cell(poz_x, poz_y, Cell.board_cell.FINISH, false, 'N');
        board_aux.get(poz_x).set(poz_y, element);
        int nr_shops = 2;
        int nr_enemies = 4;
        int aux = 1;
        for (int i = 0; i < Grid.length; i++) {
            for (int j = aux; j < Grid.width; j++) {
                int index = rand.nextInt(3);
                if (index == 0) {
                    if (board_aux.get(i).get(j).x == 0 && board_aux.get(i).get(j).y == 0) {
                        Cell element_aux = new Cell(i, j, Cell.board_cell.EMPTY, false, 'N');
                        board_aux.get(i).set(j, element_aux);
                    }
                }
                if (index == 1) {
                    if (board_aux.get(i).get(j).x == 0 && board_aux.get(i).get(j).y == 0) {
                        if (nr_shops <= Grid.length * Grid.width / 5) {
                            Cell element_aux = new Cell(i, j, Cell.board_cell.SHOP, false, 'N');
                            board_aux.get(i).set(j, element_aux);
                            nr_shops++;
                        } else {
                            Cell element_aux = new Cell(i, j, Cell.board_cell.EMPTY, false, 'N');
                            board_aux.get(i).set(j, element_aux);
                        }
                    }
                }
                if (index == 2) {
                    if (board_aux.get(i).get(j).x == 0 && board_aux.get(i).get(j).y == 0) {
                        if (nr_enemies <= Grid.width * Grid.length / 4) {
                            Cell element_aux = new Cell(i, j, Cell.board_cell.ENEMY, false, 'N');
                            board_aux.get(i).set(j, element_aux);
                            nr_enemies++;
                        } else {
                            Cell element_aux = new Cell(i, j, Cell.board_cell.EMPTY, false, 'N');
                            board_aux.get(i).set(j, element_aux);
                        }
                    }
                }
            }
            aux = 0;
        }
        //prima celula va fi mereu de tip empty
        Cell first_cell = new Cell(0, 0, Cell.board_cell.EMPTY, false, 'N');
        board_aux.get(0).set(0, first_cell);
        return board_aux;
    }

    // metode care modifica celula curenta
    void goUp(int distance) {
        int x = current_cell.x;
        int y = current_cell.y;
        current_cell.visited = true;
        if (x - distance < 0)
            System.out.println("You cannot go that way!");
        else
            current_cell = board.get(x - distance).get(y);
    }

    void goDown(int distance) {
        int x = current_cell.x;
        int y = current_cell.y;
        current_cell.visited = true;
        if (x + distance >= length)
            System.out.println("You cannot go that way!");
        else
            current_cell = board.get(x + distance).get(y);
    }

    void goLeft(int distance) {
        int x = current_cell.x;
        int y = current_cell.y;
        current_cell.visited = true;
        if (y - distance < 0)
            System.out.println("You cannot go that way!");
        else
            current_cell = board.get(x).get(y - distance);
    }

    void goRight(int distance) {
        int x = current_cell.x;
        int y = current_cell.y;
        current_cell.visited = true;
        if (y + distance >= width)
            System.out.println("You cannot go that way!");
        else
            current_cell = board.get(x).get(y + distance);
    }
}
