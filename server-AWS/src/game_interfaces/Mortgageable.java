package game_interfaces;

public interface Mortgageable extends Ownable {
    // get and set Mortgage amount
    int getMortgageAmount();
    void setMortgageAmount(int mortgageAmount);

    // boolean to track mortgage status of property
    boolean isMortgaged = false;
}