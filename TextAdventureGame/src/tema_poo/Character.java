package tema_poo;

public abstract class Character extends Entity {
    String name = "";
    int x;
    int y;
    int won_experience = 0;
    int won_coins = 0;
    int defeated_enemies = 0;
    Inventory potions_list = new Inventory();
    Long current_experience;
    Long initial_experience;
    int level;
    int strength;
    int charisma;
    int dexterity;

    // metoda care verifica daca exista suficient spatiu si suficienti bani pentru a cumpara o potiune
    boolean buyPotion(Potion potion) {
        int ok = 0;
        if (potions_list.nr_coins - potion.getPrice() >= 0)
            ok++;
        if (potions_list.getActualWeight() - potion.getWeight() >= 0)
            ok++;
        return ok == 2;
    }

    public String toString() {
        return "name:" + this.name;
    }
}
