package tema_poo;

import java.util.Random;

public class Mage extends Character {
    public Mage() {
        protection_ice = true;
        potions_list.max_weight = 30;
        charisma = level * 10;
        strength = (level / 2) * 10;
        dexterity = (level / 5) * 10;
    }

    @Override
    void receiveDamage(int val) {
        Random rand = new Random();
        int value = rand.nextInt(level + 1);
        if ((dexterity + strength + value * 10) / 10 >= level)
            current_life -= (val / 2);
        else
            current_life -= val;
    }

    @Override
    int getDamage() {
        Random rand = new Random();
        // genereaza damage ul in range ul 10-15
        int damage = rand.nextInt(6) + 10;
        int value = rand.nextInt(level + 1);
        if ((charisma + value * 10) / 10 >= level * 3 / 2)
            damage *= 2;
        return damage;
    }

    @Override
    public void accept(Spell ability) {
        if (ability instanceof Fire || ability instanceof Earth)
            ability.visit(this);
        if (ability instanceof Ice)
            System.out.println("The ability didn't have any effect!=(");
    }
}
