package noc_db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Clothes_noc {

	private final String determiner;
	private final String clothing;
	private String covers;
	
	public Clothes_noc(String[] info){
		this.determiner = info[0];
		this.clothing = info[1];
		if(info.length>2)
		this.covers = info[2];
	}
	
	public String getDeterminer(){
		return this.determiner.trim();
	}
	
	public String getClothes(){
		String aff[] = this.clothing.split(", ");
		Random random = new Random();
		return aff[random .nextInt(aff.length)].trim();
	}

	public String getCovering() {
		String aff[] = this.covers.split(", ");
		Random random = new Random();
		return aff[random .nextInt(aff.length)].trim();
	}
	
	public List<String> getClothings() {
		List<String> l = new ArrayList<String>();
		Collections.addAll(l, this.clothing.split(", "));
		return l;
	}
}
