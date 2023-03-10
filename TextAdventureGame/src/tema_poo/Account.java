package tema_poo;

import java.util.ArrayList;
import java.util.SortedSet;

public class Account {
    Information info_players;
    ArrayList<Character> characters_of_account = new ArrayList<>();
    int number_played_games;

    // clasa "Information" implementata cu pattern-ul Builder
    public static class Information {
        private final Credentials credentials_of_player;
        private final SortedSet<String> fave_games;
        private final String name;
        private final String country;

        private Information(Builder information) {
            this.credentials_of_player = information.credentials_of_player;
            this.fave_games = information.fave_games;
            this.name = information.name;
            this.country = information.country;
        }

        public Credentials getCredentials() {
            return this.credentials_of_player;
        }

        public SortedSet<String> getFaveGames() {
            return this.fave_games;
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return this.country;
        }

        public static class Builder {
            private Credentials credentials_of_player = null;
            private SortedSet<String> fave_games = null;
            private String name = null;
            private String country = null;

            public Builder setCredentials(Credentials C, String email, String password) throws InformationIncompleteException {
                if (email == null || password == null)
                    throw new InformationIncompleteException("Incomplete Information!");
                this.credentials_of_player = C;
                this.credentials_of_player.setEmail(email);
                this.credentials_of_player.setPassword(password);
                return this;
            }

            public Builder setFaveGames(SortedSet<String> fave_games) {
                this.fave_games = fave_games;
                return this;
            }

            public Builder setName(String name) throws InformationIncompleteException {
                if (name == null)
                    throw new InformationIncompleteException("Incomplete Information!");
                this.name = name;
                return this;
            }

            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }

            Information build() {
                return new Information(this);
            }
        }
    }

    public String toString() {
        return this.info_players.getCredentials().getEmail();
    }
}
