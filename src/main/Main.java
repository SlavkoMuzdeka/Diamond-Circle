package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import models.Map;
import services.PlayerService;

public class Main {
	
	public static void main(String[] args){
		try {
			PlayerService playerService = new PlayerService();
			new Map(playerService);
		}catch(Exception ex) {
			Logger.getLogger(PlayerService.class.getName()).log(Level.WARNING,ex.fillInStackTrace().toString());
		}
	}

}
