package tema_poo;

import java.util.ArrayList;

public class Inventory {
    ArrayList<Potion> potions = new ArrayList<>();
    int max_weight;
    int nr_coins;

    void addPotion(Potion p) {
        potions.add(p);
    }

    void removePotion(Potion p) {
        potions.remove(p);
    }

    // metoda care returneaza spatiul ramas in inventar
    int getActualWeight() {
        int actual_weight = max_weight;
        for (Potion potion : potions) {
            actual_weight = actual_weight - potion.getWeight();
        }
        return actual_weight;
    }
}
