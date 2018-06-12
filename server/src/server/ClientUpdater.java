package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import game_models.Player;
import org.json.JSONException;
import org.json.JSONObject;

import main.Main;

/*
 * This class runs on its own thread and is used to hold open a connection between the Server and the desktop application.
 * It may send and receive messages from the desktop app.
 * 
 * */

@SuppressWarnings("ConstantConditions")
public class ClientUpdater extends Thread {

	private Socket socket;

	public void setup(int port) throws IOException {
		System.out.println("Connecting to Desktop...");
		ServerSocket server = new ServerSocket(port);
		socket = server.accept();
		server.close();
		System.out.println("Connected!");
	}

	public void updateDesktopBoard() {
		JSONObject output = new JSONObject();
		PrintWriter out = null;
		try {
			output = Main.gameState.getInfoBoard();
			output.put("action_info", Main.gameState.getActionInfo());
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		//Output to desktop
		out.println(output.toString());
	}

	public void updateDesktopAuction() {
		JSONObject output = new JSONObject();
		PrintWriter out = null;
		try {
			output.put("auction", Main.gameState.getAuctionInfo());
			output.put("action_info", Main.gameState.getActionInfo());
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		//Output to desktop
		out.println(output.toString());
	}

	public void updateDesktopBoardWithWinner(Player player) {
		JSONObject output = new JSONObject();
		PrintWriter out = null;
		try {
			output = Main.gameState.getInfoBoard();
			output.put("action_info", Main.gameState.getActionInfo());
			output.put("winner", player.getID());
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		//Output to desktop
		out.println(output.toString());
	}

	public void updateDesktopPlayers() {
		JSONObject output = new JSONObject();
		PrintWriter out = null;
		try {
			output = Main.gameState.getInfoPlayers();
			output.put("action_info", Main.gameState.getActionInfo());
			output.put("dice_values", Main.dice.getDiceValues());
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		//Output to desktop
		out.println(output.toString());
	}

	public void updateDesktopAll() {
		JSONObject output = new JSONObject();
		PrintWriter out = null;
		try {
			output = Main.gameState.getInfo();
			output.put("action_info", Main.gameState.getActionInfo());
			output.put("dice_values", Main.dice.getDiceValues());
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		//Output to desktop
		out.println(output.toString());
	}

	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (Main.isActive) {
			synchronized (Main.gameState) {
				try {
					//Input from desktop
					if (reader.ready()) {
						String line = reader.readLine();
						if (!line.isEmpty()) {
							System.out.println("Message from Desktop: " + line);
						}

						//Parse JSONObject from input
						JSONObject obj = new JSONObject(line);
						int id = obj.getInt("id");
						if (id == 0) {
							String action = obj.getString("action");
							if (action.equals("start")) {
								if (Main.gameState.numberOfPlayers() >=2) {
									Main.gameState.startGame();
									Main.gameState.updateActionInfo("\nGame has started! Good Luck!\n");
									Main.portAllocator.updatePlayers();
									Main.clientUpdater.updateDesktopPlayers();
								} else {
									Main.gameState.updateActionInfo("Must have at least 2 players to start the game!");
									Main.clientUpdater.updateDesktopPlayers();
								}
							}
							if (action.equals("end")) {
								Main.gameState.endGame();
							}
							if (action.equals("auction_over")) {
								Main.gameState.finishAuction();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
