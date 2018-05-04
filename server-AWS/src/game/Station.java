package game;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import game_interfaces.JSONable;

public class Station extends RentalProperty implements JSONable{

	public Station(String name, int price, int[] rentAmounts) {
		super(name, price);
		super.setRentAmounts(rentAmounts);
		super.setType("Station");
	}

	public int getRentalAmount() {
		if (!this.isMortgaged() && this.isOwned()) {
			ArrayList<RentalProperty> properties = this.getOwner().getOwnedProperties();

			int numStationsOwned = -1;
			for (RentalProperty p : properties) {
				if (p.getOwner()  == this.getOwner()) {
					numStationsOwned++;
				}
			}
			if(numStationsOwned >= 0) {
				return this.getAllRentAmounts()[numStationsOwned];
			} else {
				return 0;
			}
		} else {
			System.out.println("Cant claim rent on station that is mortgaged");
			return 0;
		}
	}
	
	@Override
	public JSONObject getInfo() throws JSONException {
		JSONObject info =  super.getInfo();
		info.put("rent", this.getRentalAmount());
		return info;
	}
	
}
