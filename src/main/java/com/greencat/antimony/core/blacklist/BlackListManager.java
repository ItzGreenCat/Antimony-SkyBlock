package com.greencat.antimony.core.blacklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.greencat.antimony.core.config.getConfigByFunctionName;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;

public class BlackListManager {

	private static Logger logger = LogManager.getLogger();
	
	public static volatile Map<String, BlackListPlayer> gaoNengs = new HashMap<String, BlackListPlayer>();
	public static volatile Map<String, BlackListPlayer> loading_gaoNengs = new HashMap<String, BlackListPlayer>();
	
	public static String[] url = new String[] { "https://api.scamlist.cn/rank.json",
	"https://black.maid.ink/rank.json" };

	public static void load() {
		try {
			loadBlackList();
			gaoNengs = loading_gaoNengs;
			loading_gaoNengs = new HashMap<String, BlackListPlayer>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BlackListPlayer getIfIsGaoNeng(EntityOtherPlayerMP entity) {
		if (gaoNengs.containsKey(getPlayerUUID(entity))) {
			return gaoNengs.get(getPlayerUUID(entity));
		}
		return null;
	}
	
	public static BlackListPlayer getIfIsGaoNeng(EntityPlayerSP entity) {
		if (gaoNengs.containsKey(EntityPlayerSP.getUUID(entity.getGameProfile()).toString().replaceAll("-", ""))) {
			return gaoNengs.get(EntityPlayerSP.getUUID(entity.getGameProfile()).toString().replaceAll("-", ""));
		}
		return null;
	}

	public static BlackListPlayer getIfIsGaoNeng(String uuid) {
		if (gaoNengs.containsKey(uuid)) {
			return gaoNengs.get(uuid);
		}
		return null;
	}

	public static void loadBlackList() throws IOException {
		String result = get(url[(Integer) getConfigByFunctionName.get("BlackList","api")]);
		if (result == null) {
			logger.error("Result cannot be 'null'.");
			return;
		}

		JsonObject object = (JsonObject) new JsonParser().parse(result);

		if (object == null)
			return;

		JsonObject all = object.getAsJsonObject();
		for (Map.Entry<String, JsonElement> playerObj : all.entrySet()) {
			String id = playerObj.getKey();
			BlackListPlayer gn = loading_gaoNengs.get(id);
			boolean notexisted = gn == null;

			if (notexisted) {
				gn = new BlackListPlayer(playerObj.getKey(), playerObj.getValue().getAsString());
				loading_gaoNengs.put(playerObj.getKey(), gn);
			}
		}
	}

	private static String getPlayerUUID(EntityOtherPlayerMP entity) {
		if (entity == null)
			return "none";
		String unformatted = EntityOtherPlayerMP.getUUID(entity.getGameProfile()).toString();
		String uuid = unformatted.replaceAll("-", "");
		return uuid;
	}
	
	public static String get(String url) throws IOException {
		String result = "";
		try {
			String urlNameString = url;
			URL realurl = new URL(urlNameString);
			HttpURLConnection httpUrlConn = (HttpURLConnection) realurl.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			InputStream input = httpUrlConn.getInputStream();
			InputStreamReader read = new InputStreamReader(input, "utf-8");
			BufferedReader br2 = new BufferedReader(read);
			String data = br2.readLine();
			while (data != null) {
				result = String.valueOf(String.valueOf(result)) + data + "\n";
				data = br2.readLine();
			}
			br2.close();
			read.close();
			input.close();
			httpUrlConn.disconnect();
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		return result;
	}

	public static class BlackListPlayer {
		private String rank;
		private String uuid;

		public BlackListPlayer(String uuid, String rank) {
			this.rank = rank;
			this.uuid = uuid;
		}

		public String getRank() {
			return rank;
		}

		public String getUuid() {
			return uuid;
		}
	}
}
