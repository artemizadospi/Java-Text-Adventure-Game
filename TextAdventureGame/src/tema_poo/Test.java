package tema_poo;

import java.util.*;



public class Test {

    public static void main(String[] args) {
        Game NewGame = Game.getInstance();
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Insert T if you want to continue the game in terminal mode!");
        System.out.println("Insert G if you want to continue the game in graphic window mode!");
        NewGame.display = keyboard.next();
        try {
            if (!NewGame.display.equals("T") && !NewGame.display.equals("G"))
                throw new InvalidCommandException("Invalid Command!");
            if (NewGame.display.equals("T")) {
                Grid map = Grid.init(5, 5);
                Grid.board = Grid.getMap();
                NewGame.current_cell = Grid.board.get(0).get(0);
                NewGame.current_cell.review = 'P';
                try {
                    NewGame.run();
                } catch (InformationIncompleteException ex) {
                    System.out.println("Incomplete Information!");
                }
                showMap();
                NewGame.story(NewGame.current_cell);
                try {
                    NewGame.optionsT(NewGame.current_cell);
                } catch (InvalidCommandException ex) {
                    System.out.println("Invalid Command!");
                }
                map.current_cell = NewGame.current_cell;
                NewGame.current_character.x = map.current_cell.x;
                NewGame.current_character.y = map.current_cell.y;
                map.current_character = NewGame.current_character;
                try {
                    System.out.println("Please, insert P if you want to move: ");
                    String command = keyboard.next();
                    if (!command.equals("P"))
                        throw new InvalidCommandException("Invalid Command!");
                    try {
                        String move = "R";
                        String distance = "3";
                        moveOnMap(move, distance, map);
                        NewGame.current_cell.review = NewGame.current_cell.current_character_from_cell.toCharacter();
                        NewGame.current_character.x = map.current_cell.x;
                        NewGame.current_character.y = map.current_cell.y;
                        map.current_character.x = map.current_cell.x;
                        map.current_character.y = map.current_cell.y;
                        NewGame.current_cell = map.current_cell;
                        NewGame.current_cell.review = 'P';
                        showMap();
                        NewGame.story(NewGame.current_cell);
                        NewGame.optionsT(NewGame.current_cell);
                        map.current_cell = NewGame.current_cell;
                    } catch (InvalidCommandException ex) {
                        System.out.println("Invalid Command!");
                    }
                    if (NewGame.current_cell.type_cell == Cell.board_cell.FINISH || NewGame.current_character.current_life <= 0)
                        System.exit(0);
                    try {
                        String move = "R";
                        String distance = "1";
                        moveOnMap(move, distance, map);
                        NewGame.current_cell.review = NewGame.current_cell.current_character_from_cell.toCharacter();
                        NewGame.current_character.x = map.current_cell.x;
                        NewGame.current_character.y = map.current_cell.y;
                        map.current_character.x = map.current_cell.x;
                        map.current_character.y = map.current_cell.y;
                        NewGame.current_cell = map.current_cell;
                        NewGame.current_cell.review = 'P';
                        showMap();
                        NewGame.story(NewGame.current_cell);
                        NewGame.optionsT(NewGame.current_cell);
                        map.current_cell = NewGame.current_cell;
                    } catch (InvalidCommandException ex) {
                        System.out.println("Invalid Command!");
                    }
                    if (NewGame.current_cell.type_cell == Cell.board_cell.FINISH || NewGame.current_character.current_life <= 0)
                        System.exit(0);
                    try {
                        String move = "D";
                        String distance = "3";
                        moveOnMap(move, distance, map);
                        NewGame.current_cell.review = NewGame.current_cell.current_character_from_cell.toCharacter();
                        NewGame.current_character.x = map.current_cell.x;
                        NewGame.current_character.y = map.current_cell.y;
                        map.current_character.x = map.current_cell.x;
                        map.current_character.y = map.current_cell.y;
                        NewGame.current_cell = map.current_cell;
                        NewGame.current_cell.review = 'P';
                        showMap();
                        NewGame.story(NewGame.current_cell);
                        NewGame.optionsT(NewGame.current_cell);
                        map.current_cell = NewGame.current_cell;
                    } catch (InvalidCommandException ex) {
                        System.out.println("Invalid Command!");
                    }
                    if (NewGame.current_cell.type_cell == Cell.board_cell.FINISH || NewGame.current_character.current_life <= 0)
                        System.exit(0);
                    try {
                        String move = "D";
                        String distance = "1";
                        moveOnMap(move, distance, map);
                        NewGame.current_cell.review = NewGame.current_cell.current_character_from_cell.toCharacter();
                        NewGame.current_character.x = map.current_cell.x;
                        NewGame.current_character.y = map.current_cell.y;
                        map.current_character.x = map.current_cell.x;
                        map.current_character.y = map.current_cell.y;
                        NewGame.current_cell = map.current_cell;
                        NewGame.current_cell.review = 'P';
                        showMap();
                        NewGame.story(NewGame.current_cell);
                        NewGame.optionsT(NewGame.current_cell);
                        map.current_cell = NewGame.current_cell;
                    } catch (InvalidCommandException ex) {
                        System.out.println("Invalid Command!");
                    }
                    if (NewGame.current_cell.type_cell == Cell.board_cell.FINISH || NewGame.current_character.current_life <= 0)
                        System.exit(0);
                } catch (InvalidCommandException e) {
                    System.out.println("Invalid Command!");
                }
            }
            if (NewGame.display.equals("G")) {
                try {
                    NewGame.run();
                } catch (InformationIncompleteException ex) {
                    System.out.println("Incomplete Information!");
                }
            }
        } catch (InvalidCommandException e) {
            System.out.println("Invalid Command!");
        }
    }

    // metoda care afiseaza harta
    static void showMap() {
        for (int i = 0; i < Grid.length; i++) {
            for (int j = 0; j < Grid.width; j++)
                System.out.print(Grid.board.get(i).get(j).review + " ");
            System.out.print("\n");
        }
    }

    // metoda care modifica celula curenta in functie de input-ul primit
    // metoda care traverseaza harta
    static void moveOnMap(String move, String distance, Grid map) throws InvalidCommandException {
        if (!move.equals("R") && !move.equals("L") && !move.equals("U") && !move.equals("D")) {
            throw new InvalidCommandException("Invalid Command!");
        }
        int distance_aux;
        try {
            distance_aux = Integer.parseInt(distance);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Invalid Command!");
        }
        if (move.equals("R"))
            map.goRight(distance_aux);
        if (move.equals("L"))
            map.goLeft(distance_aux);
        if (move.equals("U"))
            map.goUp(distance_aux);
        if (move.equals("D"))
            map.goDown(distance_aux);
    }
}
