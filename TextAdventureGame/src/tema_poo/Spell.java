package tema_poo;

public abstract class Spell implements Visitor {
    int damage;
    int cost_manna;

    abstract String getName();
}
