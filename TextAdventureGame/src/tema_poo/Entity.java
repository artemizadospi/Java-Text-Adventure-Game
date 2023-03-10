package tema_poo;

import java.util.ArrayList;

public abstract class Entity implements Element {
    ArrayList<Spell> abilities = new ArrayList<>();
    int current_life;
    int max_life;
    int current_manna;
    int max_manna;
    boolean protection_fire = false;
    boolean protection_ice = false;
    boolean protection_earth = false;

    // metoda care regenereaza viata
    void getLife(int val) {
        if (val + current_life > max_life)
            current_life = max_life;
        else
            current_life += val;
    }

    // metoda care regenereaza mana
    void getMana(int val) {
        if (val + current_manna > max_manna)
            current_manna = max_manna;
        else
            current_manna += val;
    }

    // metoda care verifica daca abilitatea poate fi folosita si o foloseste
    boolean useAbility(Entity current_enemy, Spell ability) {
        if (current_manna - ability.cost_manna >= 0) {
            System.out.println("The ability " + ability.getName() + " is used!");
            current_enemy.accept(ability);
            current_manna -= ability.cost_manna;
            return true;
        } else {
            System.out.println("You can't use the ability " + ability.getName() + ", not enough mana!");
            return false;
        }
    }

    abstract void receiveDamage(int val);

    abstract int getDamage();
}
