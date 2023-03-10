package tema_poo;

import java.util.ArrayList;
import java.util.Random;

public class Shop implements CellElement {

    ArrayList<Potion> potions = new ArrayList<>();

    // se adauga cel putin o potiune de mana si una de health
    public Shop() {
        Random rand = new Random();
        int potion_type;
        int nr_potions = rand.nextInt(3) + 2;
        int nr_health_potions = 0;
        int nr_mana_potions = 0;
        for (int i = 0; i < nr_potions; i++) {
            potion_type = rand.nextInt(2);
            if (nr_health_potions == 0) {
                while (potion_type != 0) {
                    potion_type = rand.nextInt(2);
                }
                HealthPotion h = new HealthPotion(15, 30, 10);
                potions.add(h);
                nr_health_potions++;
                continue;
            }
            if (nr_mana_potions == 0) {
                while (potion_type != 1) {
                    potion_type = rand.nextInt(2);
                }
                ManaPotion m = new ManaPotion(13, 25, 5);
                potions.add(m);
                nr_mana_potions++;
                continue;
            }
            if (potion_type == 0) {
                HealthPotion h = new HealthPotion(15, 30, 10);
                potions.add(h);
            }
            if (potion_type == 1) {
                ManaPotion m = new ManaPotion(13, 25, 5);
                potions.add(m);
            }
        }
    }

    Potion selectPotion(int index) {
        Potion ret;
        ret = potions.get(index);
        potions.remove(ret);
        return ret;
    }

    @Override
    public char toCharacter() {
        return 'S';
    }
}
