package tema_poo;

import java.util.Random;

public class Warrior extends Character {
    public Warrior() {
        protection_fire = true;
        potions_list.max_weight = 70;
        strength = level * 10;
        charisma = (level / 2) * 10;
        dexterity = (level / 5) * 10;
    }

    @Override
    void receiveDamage(int val) {
        Random rand = new Random();
        int value = rand.nextInt(level + 1);
        if ((charisma + dexterity + value * 10) / 10 >= level)
            current_life -= (val / 2);
        else
            current_life -= val;
    }

    @Override
    int getDamage() {
        Random rand = new Random();
        // genereaza damage ul in range ul 10-25
        int damage = rand.nextInt(16) + 10;
        int value = rand.nextInt(level + 1);
        if ((strength + value * 10) / 10 >= level * 3 / 2)
            damage *= 2;
        return damage;
    }

    @Override
    public void accept(Spell ability) {
        if (ability instanceof Ice || ability instanceof Earth)
            ability.visit(this);
        if (ability instanceof Fire)
            System.out.println("The ability didn't have any effect!=(");
    }
}
