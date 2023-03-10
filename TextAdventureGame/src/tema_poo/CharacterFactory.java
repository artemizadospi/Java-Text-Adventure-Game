package tema_poo;

public class CharacterFactory {
    public static Character getCharacter(String characterType) {
        if (characterType.equals("Warrior")) {
            return new Warrior();
        } else if (characterType.equals("Rogue")) {
            return new Rogue();
        } else if (characterType.equals("Mage")) {
            return new Mage();
        }
        return null;
    }
}
