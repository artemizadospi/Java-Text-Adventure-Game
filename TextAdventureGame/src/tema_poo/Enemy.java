package tema_poo;

import java.util.Random;

public class Enemy extends Entity implements CellElement {
    // metoda care returneaza numarul de monede castigate de personaj in urma infrangerii unui inamic
    public int getPossibleFoundCoins() {
        int possible_found_coins;
        Random rand = new Random();
        int index = rand.nextInt(5);
        if (index == 0)
            possible_found_coins = rand.nextInt(6) + 15;
        else
            possible_found_coins = 0;
        return possible_found_coins;
    }

    public Enemy() {
        current_life = 100;
        current_manna = 210;
        Random rand2 = new Random();
        int aux1 = rand2.nextInt(3) + 3;
        if (aux1 % 3 == 0)
            protection_earth = true;

        if (aux1 % 4 == 0)
            protection_fire = true;

        if (aux1 % 5 == 0)
            protection_ice = true;

        // genereaza numarul de abilitati in range ul 2-4
        Random rand3 = new Random();
        int nr_abilities = rand3.nextInt(3) + 2;
        for (int i = 1; i <= nr_abilities; i++) {
            int ability_index = rand2.nextInt(3);
            if (ability_index == 0) {
                Fire ability = new Fire();
                abilities.add(ability);
            }
            if (ability_index == 1) {
                Ice ability = new Ice();
                abilities.add(ability);
            }
            if (ability_index == 2) {
                Earth ability = new Earth();
                abilities.add(ability);
            }
        }
    }

    @Override
    void receiveDamage(int val) {
        Random rand = new Random();
        int avoid_damage = rand.nextInt(2);
        if (avoid_damage == 0)
            current_life -= val;
        else
            System.out.println("The enemy avoided the damage!");
    }

    @Override
    int getDamage() {
        // alege o valoare din intervalul 10-15
        Random rand = new Random();
        int damage = rand.nextInt(6) + 10;
        int double_damage = rand.nextInt(2);
        if (double_damage == 1)
            return 2 * damage;
        return damage;
    }

    @Override
    public char toCharacter() {
        return 'E';
    }

    @Override
    public void accept(Spell ability) {
        if (ability instanceof Fire && !this.protection_fire) {
            ability.visit(this);
        }
        if (ability instanceof Fire && this.protection_fire) {
            System.out.println("The ability didn't have any effect!=(");
        }
        if (ability instanceof Ice && !this.protection_ice) {
            ability.visit(this);
        }
        if (ability instanceof Ice && this.protection_ice) {
            System.out.println("The ability didn't have any effect!=(");
        }
        if (ability instanceof Earth && !this.protection_earth) {
            ability.visit(this);
        }
        if (ability instanceof Earth && this.protection_earth) {
            System.out.println("The ability didn't have any effect!=(");
        }
    }
}
