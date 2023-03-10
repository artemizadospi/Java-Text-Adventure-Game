package tema_poo;

public class ManaPotion implements Potion {
    private final int price;
    private final int regeneration_value;
    private final int weight;

    public ManaPotion(int price, int regeneration_value, int weight) {
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
        player.getMana(regeneration_value);
    }

    public String getName() {
        return "ManaPotion";
    }
}
