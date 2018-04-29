package game_interfaces;

public interface Ownable extends Identifiable {

    // all ownable tiles have an owner
    Playable getOwner();
    void setOwner(Playable player);

    // all ownable tiles have a price
    int getPrice();
    void setPrice(int price);

    // bool to check is tile owned or not
    boolean isOwned();

    // all game components have a type
    // type of location = property/jail/station/utility/tax etc
    String getType();
    void setType(String type);
}
