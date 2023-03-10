package tema_poo;

public class Fire extends Spell {
    public Fire() {
        damage = 20;
        cost_manna = 90;
    }

    @Override
    public void visit(Entity entity) {
        entity.receiveDamage(damage);
    }

    @Override
    String getName() {
        return "Fire";
    }
}
