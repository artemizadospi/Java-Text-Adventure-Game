package tema_poo;

public interface Potion {
    void usePotion(Entity player);

    int getPrice();

    int getRegenerationValue();

    int getWeight();

    String getName();
}
