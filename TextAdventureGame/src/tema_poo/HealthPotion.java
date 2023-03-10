package tema_poo;

public class HealthPotion implements Potion {
    private final int price;
    private final int regeneration_value;
    private final int weight;

    public HealthPotion(int price, int regeneration_value, int weight) {
        this.price = price;
        this.regeneration_value = regeneration_value;
        this.weight = weight;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getRegenerationValue() {
        return regeneration_value;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void usePotion(Entity player) {
        player.getLife(getRegenerationValue());
    }

    public String getName() {
        return "HealthPotion";
    }
}
