package tema_poo;

public class Earth extends Spell {
    public Earth() {
        damage = 10;
        cost_manna = 50;
    }

    @Override
    public void visit(Entity entity) {
        entity.receiveDamage(damage);
    }

    @Override
    String getName() {
        return "Earth";
    }
}
