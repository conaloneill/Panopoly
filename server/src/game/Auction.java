package game;

import game_models.Player;
import game_models.RentalProperty;
import org.json.JSONException;
import org.json.JSONObject;

import game_interfaces.JSONable;
import main.Main;
/*
 * This class contains all information about any current auction
 * */
public class Auction implements JSONable{

	private RentalProperty prop;
	private int price;
	private Player playerBuying;
	private Player playerSelling;
	private boolean auctionInProgress;

	public Auction(){
		this.prop = null;
		this.price = 0;
		this.playerBuying = null;
		this.playerSelling = null;
		this.auctionInProgress = false;
	}

	/*
	*When a player puts a property up for auction or when the bank auction a property
	*
	*/
	public void auction(RentalProperty prop, Player playerBuying, int price){
		this.auctionInProgress = true;
		this.price = price;
		this.prop = prop;
		this.playerBuying = playerBuying;
		this.playerSelling = prop.getOwner();
	}

	private void reset(){
		this.prop = null;
		this.price = 0;
		this.playerBuying = null;
		this.auctionInProgress = false;
	}
	
	/*
	 * Settle the auction once it finishes
	 * */
	public String finish(){

		String res = "";
		if(!(playerSelling == null)){
			if(playerBuying.equals(playerSelling))
				res = playerBuying.getCharName() + " keeps " + prop.getId() + ".";
			else{
				prop.setOwner(playerBuying);
				playerBuying.addNewPropertyBought(prop, price);
				playerSelling.removePropertySold(prop, price);
				res = playerBuying.getCharName() + " bought " + prop.getId() + " for " + this.price + " SHM.";
			}
		}
		else if(!(playerBuying == null)){
			prop.setOwner(playerBuying);
			playerBuying.addNewPropertyBought(prop, price);
			res = playerBuying.getCharName() + " bought " + prop.getId() + " for " + this.price + " SHM.";
		}
		else if(!(prop == null)){
			res = "No one bought " + prop.getId() + ". It remains unowned. ";
		}

		this.reset();
		//Check for any pending actions and execute them
		res += "\n" + Main.gameState.doPendingAction();
		return res;
	}

	public boolean auctionInProgress(){
		return this.auctionInProgress;
	}

	/*
	 * If a player makes a bid, update the current highest bidder
	 * 
	 * */
	public boolean update(Player player, int price){
		if(price>this.price){
			this.playerBuying = player;
			this.price = price;
			return true;
		}
		return false;
	}

	/*
	 * Get the auction information to send to the desktop
	 * */
	@Override
	public JSONObject getInfo() throws JSONException {
		JSONObject info = new JSONObject();
		if(this.playerSelling != null)
			info.put("player_selling", this.prop.getOwner().getID());
		else
			info.put("player_selling", 0);
		if(this.playerBuying != null)
			info.put("player_buying", this.playerBuying.getID());
		else
			info.put("player_buying", 0);
		info.put("price", this.price);
		info.put("location", this.prop.getLocation());
		return info;
	}

	public boolean isValidBid(Player player) {
		return !player.equals(playerSelling);
	}
}
