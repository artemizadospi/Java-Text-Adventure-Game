package tema_poo;

import java.util.Random;

public class Rogue extends Character {
    public Rogue() {
        protection_earth = true;
        potions_list.max_weight = 50;
        dexterity = level * 10;
        charisma = (level / 2) * 10;
        strength = (level / 5) * 10;
    }

    @Override
    void receiveDamage(int val) {
        Random rand = new Random();
        int value = rand.nextInt(level + 1);
        if ((charisma + strength + value * 10) / 10 >= level)
            current_life -= (val / 2);
        else
            current_life -= val;
    }

    @Override
    int getDamage() {
        Random rand = new Random();
        // genereaza damage ul in range ul 10-20
        int damage = rand.nextInt(11) + 10;
        int value = rand.nextInt(level + 1);
        if ((dexterity + value * 10) / 10 >= level * 3 / 2)
            damage *= 2;
        return damage;
    }

    @Override
    public void accept(Spell ability) {
        if (ability instanceof Ice || ability instanceof Fire)
            ability.visit(this);
        if (ability instanceof Earth)
            System.out.println("The ability didn't have any effect!=(");
    }
}
