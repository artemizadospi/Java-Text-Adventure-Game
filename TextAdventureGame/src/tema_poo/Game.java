package tema_poo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Game {
    private static Game instance = null;
    public ArrayList<Account> players_list = new ArrayList<>();
    public Map<Cell.board_cell, ArrayList<String>> dictionary = new HashMap<>();
    Character current_character;
    Cell current_cell;
    Shop get_potion = new Shop();
    String display;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    void run() throws InformationIncompleteException, NullPointerException {
        // parte a metodei run create pentru interfata grafica
        if (display.equals("G")) {
            try {
                // se completeaza lista de conturi cu informatii din fisierul json
                Object obj = new JSONParser().parse(new FileReader("D:\\tema_POO\\POO\\accounts.json"));
                JSONObject jobj = (JSONObject) obj;
                JSONArray jarray = (JSONArray) jobj.get("accounts");
                for (Object o : jarray) {
                    JSONObject object = (JSONObject) o;
                    JSONObject a = (JSONObject) object.get("credentials");
                    Account account_player = new Account();
                    account_player.number_played_games = Integer.parseInt((String) object.get("maps_completed"));
                    SortedSet<String> fave_games = new TreeSet<>();
                    JSONArray b = (JSONArray) object.get("favorite_games");
                    for (Object games : b) {
                        fave_games.add((String) games);
                    }
                    Credentials C = new Credentials();
                    try {
                        account_player.info_players = new Account.Information.Builder()
                                .setCredentials(C, (String) a.get("email"), (String) a.get("password"))
                                .setFaveGames(fave_games).setName((String) object.get("name"))
                                .setCountry((String) object.get("country"))
                                .build();
                    } catch (InformationIncompleteException ex) {
                        System.out.println("Information Incomplete!");
                        System.exit(1);
                    }
                    JSONArray c = (JSONArray) object.get("characters");
                    for (Object character : c) {
                        JSONObject detail = (JSONObject) character;
                        String profession = (String) detail.get("profession");
                        Character ch = CharacterFactory.getCharacter(profession);
                        try {
                            if (ch == null)
                                throw new NullPointerException("The object is null!");
                            account_player.characters_of_account.add(ch);
                            ch.name = (String) detail.get("name");
                            ch.level = Integer.parseInt((String) detail.get("level"));
                            ch.current_experience = (Long) detail.get("experience");
                            ch.initial_experience = ch.current_experience;
                            ch.potions_list.nr_coins = 100;
                        } catch (NullPointerException e) {
                            System.out.println("Null pointer exception!");
                            System.exit(1);
                        }
                    }
                    players_list.add(account_player);
                }
                // prima pagina din interfata unde poti alege contul cu care vrei sa joci
                DefaultListModel<Account> players_jlist = new DefaultListModel<>();
                for (Account a : players_list) {
                    players_jlist.addElement(a);
                }
                JFrame frame = new JFrame();
                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
                JPanel main_frame = new JPanel();
                JPanel main_frame2 = new JPanel(new GridBagLayout());
                GridBagConstraints position = new GridBagConstraints();
                position.gridx = 0;
                position.gridy = 0;
                main_frame.setBackground(Color.lightGray);
                main_frame2.setBackground(Color.lightGray);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 600);
                JLabel instructions = new JLabel("Select an email and insert the password!", SwingConstants.CENTER);
                instructions.setFont(new Font("Serif", Font.BOLD, 20));
                main_frame2.add(instructions, position);
                position.gridy++;
                position.gridx = 0;
                JLabel email = new JLabel("Email:", SwingConstants.CENTER);
                email.setFont(new Font("Serif", Font.BOLD, 20));
                TextField text_email = new TextField();
                text_email.setPreferredSize(new Dimension(200, 20));
                JLabel password = new JLabel("Password:", SwingConstants.CENTER);
                password.setFont(new Font("Serif", Font.BOLD, 20));
                JPasswordField text_password = new JPasswordField();
                text_password.setPreferredSize(new Dimension(200, 20));
                JList<Account> list = new JList<>(players_jlist);
                list.setFixedCellHeight(60);
                list.setFixedCellWidth(500);
                list.setFont(new Font("Serif", Font.BOLD, 15));
                list.setBackground(Color.pink);
                list.setBorder(new LineBorder(Color.black));
                ListSelectionListener l = new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (list.isSelectionEmpty())
                            return;
                        int i = list.getSelectedIndex();
                        text_email.setText(players_list.get(i).info_players.getCredentials().getEmail());
                        text_password.setText(players_list.get(i).info_players.getCredentials().getPassword());
                    }
                };
                list.addListSelectionListener(l);
                JButton b = new JButton("Submit");
                b.setBounds(850, 400, 150, 40);
                JButton add_account = new JButton("Add new account");
                add_account.setBounds(850, 460, 150, 40);
                frame.add(b);
                frame.add(add_account);
                main_frame.add(list);
                main_frame2.add(email, position);
                position.gridx++;
                main_frame2.add(text_email, position);
                position.gridy++;
                position.gridx = 0;
                main_frame2.add(password, position);
                position.gridx++;
                main_frame2.add(text_password, position);
                container.add(main_frame);
                container.add(main_frame2);
                frame.add(container);
                frame.setVisible(true);
                // la apasarea butonului "Add new account" se afiseaza o noua pagina unde se pot completa informatii
                add_account.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                        JFrame frame_add_account = new JFrame();
                        JPanel container = new JPanel();
                        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
                        JPanel main_frame = new JPanel();
                        JPanel main_frame2 = new JPanel(new GridBagLayout());
                        GridBagConstraints position = new GridBagConstraints();
                        position.gridx = 0;
                        position.gridy = 0;
                        main_frame.setBackground(Color.lightGray);
                        main_frame2.setBackground(Color.lightGray);
                        frame_add_account.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame_add_account.setSize(1200, 600);
                        JLabel instructions = new JLabel("Insert the email address and the password!", SwingConstants.CENTER);
                        instructions.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        main_frame2.add(instructions, position);
                        position.gridy++;
                        position.gridx = 0;
                        JLabel email = new JLabel("Email:", SwingConstants.CENTER);
                        email.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_email = new TextField();
                        text_email.setPreferredSize(new Dimension(200, 20));
                        JLabel password = new JLabel("Password:", SwingConstants.CENTER);
                        password.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_password = new TextField();
                        text_password.setPreferredSize(new Dimension(200, 20));
                        JLabel name = new JLabel("Name:", SwingConstants.CENTER);
                        name.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_name = new TextField();
                        text_name.setPreferredSize(new Dimension(200, 20));
                        JLabel country = new JLabel("Country:", SwingConstants.CENTER);
                        country.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_country = new TextField();
                        text_country.setPreferredSize(new Dimension(200, 20));
                        JLabel character_name = new JLabel("Character's name:", SwingConstants.CENTER);
                        character_name.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_character_name = new TextField();
                        text_character_name.setPreferredSize(new Dimension(200, 20));
                        JLabel character_profession = new JLabel("Character's profession:", SwingConstants.CENTER);
                        character_profession.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_character_profession = new TextField();
                        text_character_profession.setPreferredSize(new Dimension(200, 20));
                        JLabel fave_game = new JLabel("Favorite game:", SwingConstants.CENTER);
                        fave_game.setFont(new Font("Serif", Font.BOLD, 20));
                        TextField text_fave_game = new TextField();
                        text_fave_game.setPreferredSize(new Dimension(200, 20));
                        JButton b = new JButton("Submit");
                        b.setBounds(850, 400, 150, 40);
                        frame_add_account.add(b);
                        main_frame.add(instructions);
                        main_frame2.add(email, position);
                        position.gridx++;
                        main_frame2.add(text_email, position);
                        position.gridy++;
                        position.gridx = 0;
                        main_frame2.add(password, position);
                        position.gridx++;
                        main_frame2.add(text_password, position);
                        position.gridy++;
                        position.gridx = 0;
                        main_frame2.add(name, position);
                        position.gridx++;
                        main_frame2.add(text_name, position);
                        position.gridy++;
                        position.gridx = 0;
                        main_frame2.add(country, position);
                        position.gridx++;
                        main_frame2.add(text_country, position);
                        position.gridy++;
                        position.gridx = 0;
                        main_frame2.add(character_name, position);
                        position.gridx++;
                        main_frame2.add(text_character_name, position);
                        position.gridy++;
                        position.gridx = 0;
                        main_frame2.add(character_profession, position);
                        position.gridx++;
                        main_frame2.add(text_character_profession, position);
                        position.gridy++;
                        position.gridx = 0;
                        main_frame2.add(fave_game, position);
                        position.gridx++;
                        main_frame2.add(text_fave_game, position);
                        container.add(main_frame);
                        container.add(main_frame2);
                        frame_add_account.add(container);
                        frame_add_account.setVisible(true);
                        // la apasarea butonului "Submit" se adauga informatiile in fisier si se ajunge iar
                        // la pagina de autentificare
                        b.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ac) {
                                Map<Object, Object> jsonMap = new HashMap<>();
                                Map<Object, Object> credentials_map = new HashMap<>();
                                Map<Object, Object> characters_map = new HashMap<>();
                                JSONArray characters = new JSONArray();
                                characters_map.put("name", text_character_name.getText());
                                characters_map.put("profession", text_character_profession.getText());
                                characters_map.put("level", "1");
                                characters_map.put("experience", 0);
                                JSONObject char_of_account = new JSONObject(characters_map);
                                characters.add(char_of_account);
                                credentials_map.put("email", text_email.getText());
                                credentials_map.put("password", text_password.getText());
                                JSONObject credentials = new JSONObject(credentials_map);
                                jsonMap.put("credentials", credentials);
                                jsonMap.put("name", text_name.getText());
                                jsonMap.put("country", text_country.getText());
                                jsonMap.put("maps_completed", "0");
                                JSONArray fave_games = new JSONArray();
                                fave_games.add(text_fave_game.getText());
                                jsonMap.put("favorite_games", fave_games);
                                jsonMap.put("characters", characters);
                                Map<Object, Object> map_array = new HashMap<>();
                                try {
                                    FileWriter file = new FileWriter("D:\\tema_POO\\POO\\accounts.json");
                                    jarray.add(jsonMap);
                                    map_array.put("accounts", jarray);
                                    JSONObject obj_map = new JSONObject(map_array);
                                    file.write(obj_map.toString());
                                    file.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                frame_add_account.setVisible(false);
                                frame.setVisible(true);
                            }
                        });
                    }
                });
                // la apasarea butonului "Submit" de pe pagina de autentificare
                // se verifica daca contul introdus se gaseste printre cele existente
                // si se afiseaza o noua fereastra de unde se poate selecta caracterul dorit
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ac) {
                        for (Account player : players_list) {
                            if (text_email.getText().equals(player.info_players.getCredentials().getEmail())) {
                                if (String.valueOf(text_password.getPassword()).equals(player.info_players.getCredentials().getPassword())) {
                                    frame.setVisible(false);
                                    DefaultListModel<Character> characters = new DefaultListModel<>();
                                    for (Character ch : player.characters_of_account) {
                                        characters.addElement(ch);
                                    }
                                    JFrame character_frame = new JFrame();
                                    JPanel container = new JPanel();
                                    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
                                    JPanel main_frame = new JPanel();
                                    JPanel main_frame2 = new JPanel(new GridBagLayout());
                                    GridBagConstraints position = new GridBagConstraints();
                                    position.gridx = 0;
                                    position.gridy = 0;
                                    main_frame.setBackground(Color.lightGray);
                                    main_frame2.setBackground(Color.lightGray);
                                    character_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    character_frame.setSize(1200, 600);
                                    JLabel instructions = new JLabel("Select your character!", SwingConstants.CENTER);
                                    instructions.setFont(new Font("Serif", Font.BOLD, 20));
                                    main_frame2.add(instructions, position);
                                    position.gridy++;
                                    position.gridx = 0;
                                    JLabel character = new JLabel("Your character:", SwingConstants.CENTER);
                                    character.setFont(new Font("Serif", Font.BOLD, 20));
                                    TextField text_ch = new TextField();
                                    text_ch.setPreferredSize(new Dimension(200, 20));
                                    JList<Character> list = new JList<>(characters);
                                    list.setFixedCellHeight(60);
                                    list.setFixedCellWidth(500);
                                    list.setFont(new Font("Serif", Font.BOLD, 15));
                                    list.setBackground(Color.pink);
                                    list.setBorder(new LineBorder(Color.black));
                                    ListSelectionListener l = new ListSelectionListener() {
                                        @Override
                                        public void valueChanged(ListSelectionEvent e) {
                                            if (list.isSelectionEmpty())
                                                return;
                                            int i = list.getSelectedIndex();
                                            text_ch.setText(player.characters_of_account.get(i).name);
                                        }
                                    };
                                    list.addListSelectionListener(l);
                                    JButton b = new JButton("Select");
                                    b.setBounds(850, 400, 150, 40);
                                    character_frame.add(b);
                                    main_frame.add(list);
                                    main_frame2.add(character, position);
                                    position.gridx++;
                                    main_frame2.add(text_ch, position);
                                    container.add(main_frame);
                                    container.add(main_frame2);
                                    character_frame.add(container);
                                    character_frame.setVisible(true);
                                    // la apasarea butonului "Select" se selecteaza caracterul
                                    // si se initializeaza atributele acestuia
                                    b.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String character_name = text_ch.getText();
                                            for (Character Ch : player.characters_of_account) {
                                                if (Ch.name.equals(character_name)) {
                                                    current_character = Ch;
                                                    current_character.potions_list.nr_coins = 100;
                                                }
                                            }
                                            current_character.current_life = 100;
                                            current_character.current_manna = 210;
                                            current_character.max_life = 100;
                                            current_character.max_manna = 210;
                                            // abilitatile sunt setate random
                                            Random rand = new Random();
                                            int nr_abilities = rand.nextInt(3) + 2;
                                            for (int i = 1; i <= nr_abilities; i++) {
                                                int ability_index = rand.nextInt(3);
                                                if (ability_index == 0) {
                                                    Fire ability = new Fire();
                                                    current_character.abilities.add(ability);
                                                }
                                                if (ability_index == 1) {
                                                    Ice ability = new Ice();
                                                    current_character.abilities.add(ability);
                                                }
                                                if (ability_index == 2) {
                                                    Earth ability = new Earth();
                                                    current_character.abilities.add(ability);
                                                }
                                            }
                                            character_frame.setVisible(false);
                                            // se iau povestile din fisierul json si se pun in "dictionary"
                                            try {
                                                Object stories = new JSONParser().parse(new FileReader("D:\\tema_POO\\POO\\stories.json"));
                                                JSONObject aux = (JSONObject) stories;
                                                JSONArray aux1 = (JSONArray) aux.get("stories");
                                                ArrayList<String> empty = new ArrayList<>();
                                                ArrayList<String> enemy = new ArrayList<>();
                                                ArrayList<String> shop = new ArrayList<>();
                                                ArrayList<String> finish = new ArrayList<>();
                                                for (Object o : aux1) {
                                                    JSONObject object = (JSONObject) o;
                                                    if ((object.get("type")).equals("EMPTY")) {
                                                        empty.add((String) object.get("value"));
                                                    }
                                                    if ((object.get("type")).equals("ENEMY")) {
                                                        enemy.add((String) object.get("value"));
                                                    }
                                                    if ((object.get("type")).equals("SHOP")) {
                                                        shop.add((String) object.get("value"));
                                                    }
                                                    if ((object.get("type")).equals("FINISH")) {
                                                        finish.add((String) object.get("value"));
                                                    }
                                                }
                                                dictionary.put(Cell.board_cell.EMPTY, empty);
                                                dictionary.put(Cell.board_cell.ENEMY, enemy);
                                                dictionary.put(Cell.board_cell.SHOP, shop);
                                                dictionary.put(Cell.board_cell.FINISH, finish);
                                            } catch (IOException | ParseException ex) {
                                                ex.printStackTrace();
                                                System.out.println("The file \"stories.json\" cannot be open/parsed!");
                                            }
                                            // se afiseaza o noua fereastra cu harta si pozitia caracterului
                                            JFrame map_frame = new JFrame();
                                            map_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                            JPanel container = new JPanel();
                                            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
                                            JPanel main_frame = new JPanel(new GridBagLayout());
                                            JPanel main_frame2 = new JPanel();
                                            GridBagConstraints position = new GridBagConstraints();
                                            main_frame.setBackground(Color.pink);
                                            main_frame2.setBackground(Color.pink);
                                            map_frame.setSize(1200, 600);
                                            Grid map = Grid.init(5, 5);
                                            Grid.board = Grid.getMapGui();
                                            current_cell = Grid.board.get(0).get(0);
                                            current_cell.review = 'P';
                                            map.current_cell = current_cell;
                                            current_character.x = map.current_cell.x;
                                            current_character.y = map.current_cell.y;
                                            map.current_character = current_character;
                                            Scanner keyboard = new Scanner(System.in);
                                            position.gridy = 0;
                                            position.gridx = 0;
                                            position.insets = new Insets(15, 15, 15, 15);
                                            JLabel message = new JLabel("The map and your position:", SwingConstants.CENTER);
                                            message.setFont(new Font("Times New Roman", Font.BOLD, 20));
                                            for (int i = 0; i < Grid.length; i++) {
                                                for (int j = 0; j < Grid.width; j++) {
                                                    JButton cell = new JButton(String.valueOf(Grid.board.get(i).get(j).review));
                                                    cell.setBounds(850, 400, 150, 40);
                                                    main_frame.add(cell, position);
                                                    position.gridx++;
                                                }
                                                position.gridx = 0;
                                                position.gridy++;
                                            }
                                            JButton b = new JButton("I finished watching the map!");
                                            b.setBounds(850, 500, 300, 40);
                                            map_frame.add(b);
                                            main_frame2.add(message);
                                            container.add(main_frame2);
                                            container.add(main_frame);
                                            map_frame.add(container);
                                            map_frame.setVisible(true);
                                            // la apasarea butonului "I finished watchinf the map!" se afiseaza
                                            // povestea si optiunile pentru celula curenta in terminal
                                            // se asteapta introducerea in terminal a comenzii de miscare pe harta
                                            b.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    map_frame.setVisible(false);
                                                    story(current_cell);
                                                    try {
                                                        options(current_cell);
                                                    } catch (InvalidCommandException ex) {
                                                        System.out.println("Invalid Command!");
                                                    }
                                                    if (current_cell.type_cell != Cell.board_cell.FINISH && current_character.current_life > 0) {
                                                        System.out.println("Please, insert U/D/L/R if you want to move and right after, insert the distance: ");
                                                        String move = keyboard.next();
                                                        String distance = keyboard.next();
                                                        try {
                                                            moveOnMap(move, distance, map);
                                                        } catch (InvalidCommandException ex) {
                                                            System.out.println("Invalid Command!");
                                                        }
                                                        current_cell.review = current_cell.current_character_from_cell.toCharacter();
                                                        current_character.x = map.current_cell.x;
                                                        current_character.y = map.current_cell.y;
                                                        map.current_character.x = map.current_cell.x;
                                                        map.current_character.y = map.current_cell.y;
                                                        current_cell = map.current_cell;
                                                        current_cell.review = 'P';
                                                        Component[] comp = main_frame.getComponents();
                                                        int i = 0;
                                                        for (int j = 0; j < Grid.length; j++) {
                                                            for (int k = 0; k < Grid.width; k++) {
                                                                ((JButton) comp[i]).setText(String.valueOf(Grid.board.get(j).get(k).review));
                                                                i++;
                                                            }
                                                        }
                                                        map_frame.setVisible(true);
                                                    } else {
                                                        finalPage();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                System.out.println("The file \"accounts.json\" cannot be open/parsed!");
            }
        }
        // parte a metodei run create pentru terminal
        if (display.equals("T")) {
            // la fel, se creeaza conturile si se adauga in lista
            try {
                Object obj = new JSONParser().parse(new FileReader("D:\\tema_POO\\POO\\accounts.json"));
                JSONObject jo = (JSONObject) obj;
                JSONArray ja = (JSONArray) jo.get("accounts");
                for (Object o : ja) {
                    JSONObject object = (JSONObject) o;
                    JSONObject a = (JSONObject) object.get("credentials");
                    Account account_player = new Account();
                    account_player.number_played_games = Integer.parseInt((String) object.get("maps_completed"));
                    SortedSet<String> fave_games = new TreeSet<>();
                    JSONArray b = (JSONArray) object.get("favorite_games");
                    for (Object games : b) {
                        fave_games.add((String) games);
                    }
                    Credentials C = new Credentials();
                    try {
                        account_player.info_players = new Account.Information.Builder()
                                .setCredentials(C, (String) a.get("email"), (String) a.get("password"))
                                .setFaveGames(fave_games).setName((String) object.get("name"))
                                .setCountry((String) object.get("country"))
                                .build();
                    } catch (InformationIncompleteException ex) {
                        System.out.println("Incomplete Information!");
                        System.exit(1);
                    }
                    JSONArray c = (JSONArray) object.get("characters");
                    for (Object character : c) {
                        JSONObject detail = (JSONObject) character;
                        String profession = (String) detail.get("profession");
                        Character ch = CharacterFactory.getCharacter(profession);
                        try {
                            if (ch == null)
                                throw new NullPointerException("The object is null!");
                            account_player.characters_of_account.add(ch);
                            ch.name = (String) detail.get("name");
                            ch.level = Integer.parseInt((String) detail.get("level"));
                            ch.current_experience = (Long) detail.get("experience");
                            ch.initial_experience = ch.current_experience;
                            ch.potions_list.nr_coins = 100;
                        } catch (NullPointerException e) {
                            System.out.println("Null pointer exception!");
                            System.exit(1);
                        }
                    }
                    players_list.add(account_player);
                    System.out.println(account_player.info_players.getCredentials().getEmail());
                }
                // se introduce emailul pentru contul dorit si parola
                System.out.print("Please, insert your email: ");
                Scanner keyboard = new Scanner(System.in);
                String inserted_email = keyboard.nextLine();
                int password = 0;
                int email = 0;
                while (email == 0) {
                    for (Account player : players_list) {
                        if (inserted_email.equals(player.info_players.getCredentials().getEmail())) {
                            email = 1;
                            System.out.print("Please, insert your password: ");
                            String inserted_password = keyboard.nextLine();
                            while (password == 0) {
                                if (inserted_password.equals(player.info_players.getCredentials().getPassword())) {
                                    password = 1;
                                    System.out.println("You got in!");
                                    for (Character Ch : player.characters_of_account) {
                                        System.out.println(Ch.name);
                                    }
                                    // daca parola a fost introdusa corect, se alege caracterul
                                    System.out.print("Choose your current character: ");
                                    String character_name = keyboard.nextLine();
                                    for (Character Ch : player.characters_of_account) {
                                        if (Ch.name.equals(character_name)) {
                                            current_character = Ch;
                                            current_character.potions_list.nr_coins = 100;
                                        }
                                    }
                                    current_character.current_life = 100;
                                    current_character.current_manna = 210;
                                    current_character.max_life = 100;
                                    current_character.max_manna = 210;
                                    Fire ability = new Fire();
                                    current_character.abilities.add(ability);
                                    Ice ability1 = new Ice();
                                    current_character.abilities.add(ability1);
                                    Earth ability2 = new Earth();
                                    current_character.abilities.add(ability2);
                                } else {
                                    System.out.println("Wrong password =((");
                                    System.out.println("Insert P if you want to exit!");
                                    System.out.println("Try again!");
                                    inserted_password = keyboard.nextLine();
                                    if (inserted_password.equals("P"))
                                        System.exit(1);
                                }
                            }
                            break;
                        }
                    }
                    if (email == 0) {
                        System.out.println("If you want to exit, insert P!");
                        System.out.println("Wrong email =((! Please, try again!");
                        inserted_email = keyboard.nextLine();
                        if (inserted_email.equals("P"))
                            System.exit(1);
                    }
                }
                // se introduc povestile pentru fiecare tip de celula in "dictionary"
                try {
                    Object stories = new JSONParser().parse(new FileReader("D:\\tema_POO\\POO\\stories.json"));
                    JSONObject aux = (JSONObject) stories;
                    JSONArray aux1 = (JSONArray) aux.get("stories");
                    ArrayList<String> empty = new ArrayList<>();
                    ArrayList<String> enemy = new ArrayList<>();
                    ArrayList<String> shop = new ArrayList<>();
                    ArrayList<String> finish = new ArrayList<>();
                    for (Object o : aux1) {
                        JSONObject object = (JSONObject) o;
                        if ((object.get("type")).equals("EMPTY")) {
                            empty.add((String) object.get("value"));
                        }
                        if ((object.get("type")).equals("ENEMY")) {
                            enemy.add((String) object.get("value"));
                        }
                        if ((object.get("type")).equals("SHOP")) {
                            shop.add((String) object.get("value"));
                        }
                        if ((object.get("type")).equals("FINISH")) {
                            finish.add((String) object.get("value"));
                        }
                    }
                    dictionary.put(Cell.board_cell.EMPTY, empty);
                    dictionary.put(Cell.board_cell.ENEMY, enemy);
                    dictionary.put(Cell.board_cell.SHOP, shop);
                    dictionary.put(Cell.board_cell.FINISH, finish);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                    System.out.println("The file \"stories.json\" cannot be open/parsed!");
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                System.out.println("The file \"accounts.json\" cannot be open/parsed!");
            }
        }
    }

    // metoda care, in functie de tipul celulei, afiseaza un mesaj corespunzator
    void story(Cell current_cell) {
        Random rand = new Random();
        if (!current_cell.visited) {
            if (current_cell.type_cell.equals(Cell.board_cell.EMPTY)) {
                int nr_story = rand.nextInt(dictionary.get(Cell.board_cell.EMPTY).size());
                System.out.println(dictionary.get(Cell.board_cell.EMPTY).get(nr_story));
                int nr_coins = current_cell.getPossibleFoundCoins();
                current_character.potions_list.nr_coins += nr_coins;
                if (nr_coins > 0) {
                    System.out.println("How sweet of you to be here! Get " + nr_coins + " as a gift!");
                    current_character.won_coins += nr_coins;
                }
                current_cell.visited = true;
            }
            if (current_cell.type_cell.equals(Cell.board_cell.ENEMY)) {
                int nr_story = rand.nextInt(dictionary.get(Cell.board_cell.ENEMY).size());
                System.out.println(dictionary.get(Cell.board_cell.ENEMY).get(nr_story));
            }
            if (current_cell.type_cell.equals(Cell.board_cell.SHOP)) {
                int nr_story = rand.nextInt(dictionary.get(Cell.board_cell.SHOP).size());
                System.out.println(dictionary.get(Cell.board_cell.SHOP).get(nr_story));
            }
            if (current_cell.type_cell.equals(Cell.board_cell.FINISH)) {
                int nr_story = rand.nextInt(dictionary.get(Cell.board_cell.FINISH).size());
                System.out.println(dictionary.get(Cell.board_cell.FINISH).get(nr_story));
                int nr_coins = current_cell.getPossibleFoundCoins();
                current_character.potions_list.nr_coins += nr_coins;
                if (nr_coins > 0) {
                    System.out.println("How sweet of you to be here! Get " + nr_coins + " as a gift!");
                    current_character.won_coins += nr_coins;
                }
                current_cell.visited = true;
            }
        }
    }

    // metoda care, in functie de tipul celulei, afiseaza optiunile personajului (folosita pentru interfata grafica)
    void options(Cell current_cell) throws InvalidCommandException {
        if (!current_cell.visited) {
            if (current_cell.type_cell.equals(Cell.board_cell.SHOP)) {
                int nr_coins = current_cell.getPossibleFoundCoins();
                current_character.potions_list.nr_coins += nr_coins;
                if (nr_coins > 0) {
                    System.out.println("How sweet of you to be here! Get " + nr_coins + " as a gift!");
                    current_character.won_coins += nr_coins;
                }
                if (get_potion.potions.size() == 0)
                    System.out.println("We have no potions =((!");
                else {
                    System.out.println("These are the potions I can help you with!");
                    System.out.println("You can choose which one you like! In exchange you have to pay me!");
                    System.out.println("This is your money: " + current_character.potions_list.nr_coins);
                    System.out.println("This is your storage space: " + current_character.potions_list.getActualWeight());
                    String potion;
                    String index;
                    int index_aux;
                    for (int i = 0; i < get_potion.potions.size(); i++) {
                        if (get_potion.potions.get(i) instanceof ManaPotion)
                            System.out.println(i + "." + "ManaPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                        else
                            System.out.println(i + "." + "HealthPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                    }
                    System.out.println("Insert the letter M/H and the index right after, if you want to buy a potion!");
                    System.out.print("Insert S if you want to stop!");
                    Scanner keyboard = new Scanner(System.in);
                    potion = keyboard.next();
                    if (!potion.equals("S") && !potion.equals("M") && !potion.equals("H"))
                        throw new InvalidCommandException("Invalid Command!");
                    while (!potion.equals("S") && get_potion.potions.size() > 0) {
                        index = keyboard.next();
                        try {
                            index_aux = Integer.parseInt(index);
                        } catch (NumberFormatException e) {
                            throw new InvalidCommandException("Invalid Command!");
                        }
                        if (index_aux < 0 || index_aux > get_potion.potions.size() - 1)
                            throw new InvalidCommandException("Invalid Command!");
                        if (potion.equals("M")) {
                            if (current_character.buyPotion(get_potion.potions.get(index_aux))) {
                                current_character.potions_list.nr_coins -= get_potion.potions.get(index_aux).getPrice();
                                current_character.potions_list.addPotion(get_potion.selectPotion(index_aux));
                                if (get_potion.potions.size() > 0) {
                                    System.out.println("The new list of potions: ");
                                    for (int i = 0; i < get_potion.potions.size(); i++) {
                                        if (get_potion.potions.get(i) instanceof ManaPotion)
                                            System.out.println(i + "." + "ManaPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                                        else
                                            System.out.println(i + "." + "HealthPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                                    }
                                }
                            } else
                                System.out.print("Not enough space or money to buy something anymore!");
                        }
                        if (potion.equals("H")) {
                            if (current_character.buyPotion(get_potion.potions.get(index_aux))) {
                                current_character.potions_list.nr_coins -= get_potion.potions.get(index_aux).getPrice();
                                current_character.potions_list.addPotion(get_potion.selectPotion(index_aux));
                                if (get_potion.potions.size() > 0) {
                                    System.out.println("The new list of potions: ");
                                    for (int i = 0; i < get_potion.potions.size(); i++) {
                                        if (get_potion.potions.get(i) instanceof ManaPotion)
                                            System.out.println(i + "." + "ManaPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                                        else
                                            System.out.println(i + "." + "HealthPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                                    }
                                }
                            } else
                                System.out.print("Not enough space or money to buy something anymore!Press S!");
                        }
                        System.out.println("This is your money: " + current_character.potions_list.nr_coins);
                        System.out.println("This is your storage space: " + current_character.potions_list.getActualWeight());
                        if (get_potion.potions.size() == 0)
                            System.out.println("We have no potions =((! Insert S to leave!");
                        potion = keyboard.next();
                        if (!potion.equals("S") && !potion.equals("M") && !potion.equals("H"))
                            throw new InvalidCommandException("Invalid Command!");
                    }
                }
                current_cell.visited = true;
            }
            if (current_cell.type_cell.equals(Cell.board_cell.ENEMY)) {
                int nr_coins = current_cell.getPossibleFoundCoins();
                current_character.potions_list.nr_coins += nr_coins;
                if (nr_coins > 0) {
                    System.out.println("How sweet of you to be here! Get " + nr_coins + " as a gift!");
                    current_character.won_coins += nr_coins;
                }
                Enemy e = new Enemy();
                Scanner keyboard = new Scanner(System.in);
                String instruction;
                System.out.println("Please, insert A if you want to attack the enemy!");
                System.out.println("Please, insert U if you want to use any ability!");
                System.out.println("Please, insert P if you want to use any potion!");
                while (current_character.current_life > 0 && e.current_life > 0) {
                    System.out.println("Current life:" + current_character.current_life);
                    System.out.println("Current manna:" + current_character.current_manna);
                    System.out.println("Enemy's current life:" + e.current_life);
                    System.out.println("Your turn: ");
                    instruction = keyboard.next();
                    if (!instruction.equals("A") && !instruction.equals("U") && !instruction.equals("P"))
                        throw new InvalidCommandException("Invalid Command!");
                    if (instruction.equals("A")) {
                        int damage = current_character.getDamage();
                        e.receiveDamage(damage);
                    }
                    if (instruction.equals("U")) {
                        System.out.println("These are your abilities: ");
                        for (int i = 0; i < current_character.abilities.size(); i++) {
                            System.out.println(i + "." + current_character.abilities.get(i).getName() + current_character.abilities.get(i).cost_manna + "$");
                        }
                        System.out.print("Please, insert the index of ability you want to use: ");
                        int index = keyboard.nextInt();
                        current_character.useAbility(e, current_character.abilities.get(index));
                    }
                    if (instruction.equals("P")) {
                        System.out.println("These are your potions: ");
                        for (int i = 0; i < current_character.potions_list.potions.size(); i++) {
                            System.out.println(i + "." + current_character.potions_list.potions.get(i).getName());
                        }
                        System.out.print("Please, insert the index of the potion you want to use: ");
                        int index = keyboard.nextInt();
                        current_character.potions_list.potions.get(index).usePotion(current_character);
                        current_character.potions_list.potions.remove(index);
                    }
                    if (e.current_life <= 0) {
                        System.out.println("Congrats, you win this round! See you next time!");
                        current_character.defeated_enemies++;
                        int nr_coins_enemy = e.getPossibleFoundCoins();
                        current_character.potions_list.nr_coins += nr_coins_enemy;
                        if (nr_coins_enemy > 0) {
                            System.out.println("The enemy gave you " + nr_coins_enemy + " coins for your victory!");
                            current_character.won_coins += nr_coins;
                        }
                        current_character.current_experience += 10;
                        current_character.won_experience += 10;
                        if (current_character.current_experience - current_character.initial_experience >= 20) {
                            current_character.initial_experience = current_character.current_experience;
                            current_character.level++;
                            System.out.println("Congrats, you leveled up to level " + current_character.level + "!");
                        }
                        break;
                    }
                    System.out.println("End of your turn!");
                    System.out.println("Enemy's turn: ");
                    Random rand = new Random();
                    int chances = rand.nextInt(3);
                    if (chances == 0) {
                        if (e.abilities.size() > 0) {
                            int ability = rand.nextInt(e.abilities.size());
                            if (!e.useAbility(current_character, e.abilities.get(ability))) {
                                int damage = e.getDamage();
                                current_character.receiveDamage(damage);
                                //se elimina din lista abilitarea folosita
                                e.abilities.remove(ability);
                            }
                        } else {
                            System.out.println("No more abilities! Enemy attacks normal!");
                            int damage = e.getDamage();
                            current_character.receiveDamage(damage);
                        }
                    } else {
                        System.out.println("Enemy chose to attack normal!");
                        int damage = e.getDamage();
                        current_character.receiveDamage(damage);
                    }
                    System.out.println("End of enemy's turn!");
                    if (current_character.current_life <= 0)
                        System.out.println("Sorry, you lost!");
                }
                current_cell.visited = true;
            }
        }
    }

    // metoda care, in functie de celula curenta, afiseaza optiunile personajului (folosita pentru terminal)
    void optionsT(Cell current_cell) throws InvalidCommandException {
        if (!current_cell.visited) {
            if (current_cell.type_cell.equals(Cell.board_cell.SHOP)) {
                int nr_coins = current_cell.getPossibleFoundCoins();
                current_character.potions_list.nr_coins += nr_coins;
                if (nr_coins > 0) {
                    System.out.println("How sweet of you to be here! Get " + nr_coins + " as a gift!");
                    current_character.won_coins += nr_coins;
                }
                if (get_potion.potions.size() > 0) {
                    System.out.println("These are the potions I can help you with!");
                    System.out.println("You can choose which one you like! In exchange you have to pay me!");
                    System.out.println("This is your money: " + current_character.potions_list.nr_coins);
                    System.out.println("This is your storage space: " + current_character.potions_list.getActualWeight());
                    String potion;
                    for (int i = 0; i < get_potion.potions.size(); i++) {
                        if (get_potion.potions.get(i) instanceof ManaPotion)
                            System.out.println(i + "." + "ManaPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                        else
                            System.out.println(i + "." + "HealthPotion :" + get_potion.potions.get(i).getPrice() + "$" + " storage space: " + get_potion.potions.get(i).getWeight());
                    }
                    System.out.println("Insert the letter P to buy the potions!");
                    Scanner keyboard = new Scanner(System.in);
                    potion = keyboard.next();
                    if (!potion.equals("P"))
                        throw new InvalidCommandException("Invalid Command!");
                    int nr_potions = 0;
                    for (int i = 0; i < get_potion.potions.size() && nr_potions == 0; i++) {
                        if (get_potion.potions.get(i) instanceof ManaPotion) {
                            current_character.potions_list.nr_coins -= get_potion.potions.get(i).getPrice();
                            current_character.potions_list.addPotion(get_potion.selectPotion(i));
                            System.out.println("You have just bought a ManaPotion!");
                            nr_potions++;
                        }
                    }
                    System.out.println("This is your money: " + current_character.potions_list.nr_coins);
                    System.out.println("This is your storage space: " + current_character.potions_list.getActualWeight());
                    for (int i = 0; i < get_potion.potions.size() && nr_potions == 1; i++) {
                        if (get_potion.potions.get(i) instanceof HealthPotion) {
                            current_character.potions_list.nr_coins -= get_potion.potions.get(i).getPrice();
                            current_character.potions_list.addPotion(get_potion.selectPotion(i));
                            System.out.println("You have just bought a HealthPotion!");
                        }
                        nr_potions++;
                    }
                    System.out.println("This is your money: " + current_character.potions_list.nr_coins);
                    System.out.println("This is your storage space: " + current_character.potions_list.getActualWeight());
                } else {
                    System.out.println("We have no potions =((! Insert P to leave!");
                    Scanner keyboard = new Scanner(System.in);
                    String instruction = keyboard.next();
                    if (!instruction.equals("P"))
                        throw new InvalidCommandException("Invalid Command!");
                    return;
                }
            }
            current_cell.visited = true;
        }
        if (current_cell.type_cell.equals(Cell.board_cell.ENEMY)) {
            int nr_coins = current_cell.getPossibleFoundCoins();
            current_character.potions_list.nr_coins += nr_coins;
            if (nr_coins > 0) {
                System.out.println("How sweet of you to be here! Get " + nr_coins + " as a gift!");
                current_character.won_coins += nr_coins;
            }
            Enemy e = new Enemy();
            Scanner keyboard = new Scanner(System.in);
            String command;
            System.out.println("Please, insert P if you want to fight!");
            command = keyboard.next();
            if (!command.equals("P"))
                throw new InvalidCommandException("Invalid Command!");
            int nr_abilities = 3;
            int nr_potions = 2;
            while (nr_abilities != 0) {
                System.out.println("Current life:" + current_character.current_life);
                System.out.println("Current manna:" + current_character.current_manna);
                System.out.println("Enemy's current life:" + e.current_life);
                System.out.println("Your turn: ");
                int index = nr_abilities - 1;
                System.out.println("You use the ability " + current_character.abilities.get(index).getName());
                current_character.useAbility(e, current_character.abilities.get(index));
                if (e.current_life <= 0) {
                    System.out.println("Congrats, you win this round! See you next time!");
                    current_character.defeated_enemies++;
                    int nr_coins_enemy = e.getPossibleFoundCoins();
                    current_character.potions_list.nr_coins += nr_coins_enemy;
                    if (nr_coins_enemy > 0) {
                        System.out.println("The enemy gave you " + nr_coins_enemy + " coins for your victory!");
                        current_character.won_coins += nr_coins;
                    }
                    current_character.current_experience += 10;
                    current_character.won_experience += 10;
                    if (current_character.current_experience - current_character.initial_experience >= 20) {
                        current_character.initial_experience = current_character.current_experience;
                        current_character.level++;
                        System.out.println("Congrats, you leveled up to level " + current_character.level + "!");
                    }
                    return;
                }
                System.out.println("End of your turn!");
                System.out.println("Enemy's turn: ");
                Random rand = new Random();
                int chances = rand.nextInt(3);
                if (chances == 0) {
                    if (e.abilities.size() > 0) {
                        int ability = rand.nextInt(e.abilities.size());
                        if (!e.useAbility(current_character, e.abilities.get(ability))) {
                            int damage = e.getDamage();
                            current_character.receiveDamage(damage);
                            //se elimina din lista abilitarea folosita
                            e.abilities.remove(ability);
                        }
                    } else {
                        System.out.println("No more abilities! Enemy attacks normal!");
                        int damage = e.getDamage();
                        current_character.receiveDamage(damage);
                    }
                } else {
                    System.out.println("Enemy chose to attack normal!");
                    int damage = e.getDamage();
                    current_character.receiveDamage(damage);
                }
                System.out.println("End of enemy's turn!");
                if (current_character.current_life <= 0)
                    System.out.println("Sorry, you lost!");
                nr_abilities--;
            }
            while (nr_potions != 0) {
                System.out.println("Current life:" + current_character.current_life);
                System.out.println("Current manna:" + current_character.current_manna);
                System.out.println("Enemy's current life:" + e.current_life);
                System.out.println("Your turn: ");
                System.out.println("These are your potions: ");
                for (int i = 0; i < current_character.potions_list.potions.size(); i++) {
                    System.out.println(i + "." + current_character.potions_list.potions.get(i).getName());
                }
                System.out.println("You are using the potion:" + current_character.potions_list.potions.get(0).getName());
                current_character.potions_list.potions.get(0).usePotion(current_character);
                current_character.potions_list.removePotion(current_character.potions_list.potions.get(0));
                if (e.current_life <= 0) {
                    System.out.println("Congrats, you win this round! See you next time!");
                    current_character.defeated_enemies++;
                    int nr_coins_enemy = e.getPossibleFoundCoins();
                    current_character.potions_list.nr_coins += nr_coins_enemy;
                    if (nr_coins_enemy > 0) {
                        System.out.println("The enemy gave you " + nr_coins_enemy + " coins for your victory!");
                        current_character.won_coins += nr_coins;
                    }
                    current_character.current_experience += 10;
                    current_character.won_experience += 10;
                    if (current_character.current_experience - current_character.initial_experience >= 20) {
                        current_character.initial_experience = current_character.current_experience;
                        current_character.level++;
                        System.out.println("Congrats, you leveled up to level " + current_character.level + "!");
                    }
                    return;
                }
                System.out.println("End of your turn!");
                System.out.println("Enemy's turn: ");
                Random rand = new Random();
                int chances = rand.nextInt(3);
                if (chances == 0) {
                    if (e.abilities.size() > 0) {
                        int ability = rand.nextInt(e.abilities.size());
                        if (!e.useAbility(current_character, e.abilities.get(ability))) {
                            int damage = e.getDamage();
                            current_character.receiveDamage(damage);
                            //se elimina din lista abilitarea folosita
                            e.abilities.remove(ability);
                        }
                    } else {
                        System.out.println("No more abilities! Enemy attacks normal!");
                        int damage = e.getDamage();
                        current_character.receiveDamage(damage);
                    }
                } else {
                    System.out.println("Enemy chose to attack normal!");
                    int damage = e.getDamage();
                    current_character.receiveDamage(damage);
                }
                System.out.println("End of enemy's turn!");
                if (current_character.current_life <= 0)
                    System.out.println("Sorry, you lost!");
                nr_potions--;
            }
            while (current_character.current_life > 0 && e.current_life > 0) {
                System.out.println("Current life:" + current_character.current_life);
                System.out.println("Current manna:" + current_character.current_manna);
                System.out.println("Enemy's current life:" + e.current_life);
                System.out.println("Your turn: ");
                System.out.println("You chose to attack normal!");
                int damage = current_character.getDamage();
                e.receiveDamage(damage);
                if (e.current_life <= 0) {
                    System.out.println("Congrats, you win this round! See you next time!");
                    current_character.defeated_enemies++;
                    int nr_coins_enemy = e.getPossibleFoundCoins();
                    current_character.potions_list.nr_coins += nr_coins_enemy;
                    if (nr_coins_enemy > 0) {
                        System.out.println("The enemy gave you " + nr_coins_enemy + " coins for your victory!");
                        current_character.won_coins += nr_coins;
                    }
                    current_character.current_experience += 10;
                    current_character.won_experience += 10;
                    if (current_character.current_experience - current_character.initial_experience >= 20) {
                        current_character.initial_experience = current_character.current_experience;
                        current_character.level++;
                        System.out.println("Congrats, you leveled up to level " + current_character.level + "!");
                    }
                    return;
                }
                System.out.println("End of your turn!");
                System.out.println("Enemy's turn: ");
                Random rand = new Random();
                int chances = rand.nextInt(3);
                if (chances == 0) {
                    if (e.abilities.size() > 0) {
                        int ability = rand.nextInt(e.abilities.size());
                        if (!e.useAbility(current_character, e.abilities.get(ability))) {
                            int damage_enemy = e.getDamage();
                            current_character.receiveDamage(damage_enemy);
                            //se elimina din lista abilitarea folosita
                            e.abilities.remove(ability);
                        }
                    } else {
                        System.out.println("No more abilities! Enemy attacks normal!");
                        int damage_enemy = e.getDamage();
                        current_character.receiveDamage(damage_enemy);
                    }
                } else {
                    System.out.println("Enemy chose to attack normal!");
                    int damage_enemy = e.getDamage();
                    current_character.receiveDamage(damage_enemy);
                }
                System.out.println("End of enemy's turn!");
                if (current_character.current_life <= 0)
                    System.out.println("Sorry, you lost!");
            }
            current_cell.visited = true;
        }
    }

    // metoda care modifica pozitia caracterului pe harta (folosita pentru interfata grafica)
    // verifica daca comanda introdusa este corecta
    void moveOnMap(String move, String distance, Grid map) throws InvalidCommandException {
        if (!move.equals("R") && !move.equals("L") && !move.equals("U") && !move.equals("D"))
            throw new InvalidCommandException("Invalid Command!");
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

    // metoda care afiseaza pagina de final (folosita pentru interfata grafica)
    void finalPage() {
        JFrame final_frame = new JFrame();
        JPanel main_frame = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        main_frame.setBackground(Color.lightGray);
        final_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final_frame.setSize(1200, 600);
        JLabel space = new JLabel(" ", SwingConstants.CENTER);
        space.setFont(new Font("Times New Roman", Font.BOLD, 50));
        JLabel game_over = new JLabel("GAME OVER", SwingConstants.CENTER);
        game_over.setFont(new Font("Times New Roman", Font.BOLD, 50));
        game_over.setForeground(Color.red);
        JLabel label_experience = new JLabel("Won experience:", SwingConstants.CENTER);
        label_experience.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel experience = new JLabel(current_character.current_experience.toString());
        experience.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel label_level = new JLabel("Reached level:", SwingConstants.CENTER);
        label_level.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel level = new JLabel(Integer.toString(current_character.level), SwingConstants.CENTER);
        level.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel label_enemy = new JLabel("Defeated enemies:", SwingConstants.CENTER);
        label_enemy.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel nr_enemies = new JLabel(Integer.toString(current_character.defeated_enemies), SwingConstants.CENTER);
        nr_enemies.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel label_coins = new JLabel("Won coins:", SwingConstants.CENTER);
        label_coins.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel coins = new JLabel(Integer.toString(current_character.won_coins), SwingConstants.CENTER);
        coins.setFont(new Font("Serif", Font.BOLD, 20));
        position.gridx = 0;
        position.gridy = 0;
        main_frame.add(game_over, position);
        position.gridy++;
        main_frame.add(space, position);
        position.gridy++;
        position.gridx++;
        main_frame.add(space, position);
        position.gridy++;
        position.gridx = 0;
        main_frame.add(label_experience, position);
        position.gridx++;
        main_frame.add(experience, position);
        position.gridx = 0;
        position.gridy++;
        main_frame.add(label_level, position);
        position.gridx++;
        main_frame.add(level, position);
        position.gridx = 0;
        position.gridy++;
        main_frame.add(label_enemy, position);
        position.gridx++;
        main_frame.add(nr_enemies, position);
        position.gridx = 0;
        position.gridy++;
        main_frame.add(label_coins, position);
        position.gridx++;
        main_frame.add(coins, position);
        JButton b = new JButton("Exit");
        b.setBounds(850, 400, 150, 40);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        final_frame.add(b);
        final_frame.add(main_frame);
        final_frame.setVisible(true);
    }
}
