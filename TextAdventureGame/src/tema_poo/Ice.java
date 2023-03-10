package tema_poo;

public class Ice extends Spell {
    public Ice() {
        damage = 15;
        cost_manna = 70;
    }

    @Override
    public void visit(Entity entity) {
        entity.receiveDamage(damage);
    }

    @Override
    String getName() {
        return "Ice";
    }
}
