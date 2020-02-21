package me.Duppy.TemplarWar.Guilds.Guilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.Duppy.TemplarWar.Main.ConfigManager;
import me.Duppy.TemplarWar.Teams.TeamManager;

public class GuildManager {
	private static ArrayList<Guild> guildList = new ArrayList<Guild>();
	private static ConfigManager cfgm = new ConfigManager();
	
	public static void setupGuilds() {
		//Cycles through config file and sets up the object ArrayList of guilds
		for(String guild : cfgm.getGuilds().getKeys(false)) {
			Guild g = new Guild();
			g.setName(guild);
			g.setLives(cfgm.getGuilds().getInt(guild + ".lives"));
			g.setTeam(TeamManager.getTeam(guild + ".team"));
			//Create Home Location
			if(cfgm.getGuilds().getConfigurationSection(guild + ".home") != null)
				g.setHome(new Location(Bukkit.getServer().getWorld(cfgm.getGuilds().getString(guild + ".home.world")),
					cfgm.getGuilds().getInt(guild + ".home.x"),
					cfgm.getGuilds().getInt(guild + ".home.y"),
					cfgm.getGuilds().getInt(guild + ".home.z")));
			
			//Setup members
			HashMap<UUID, String> map = new HashMap<>();
			for (String s : cfgm.getGuilds().getConfigurationSection(guild + ".members").getKeys(false)) {
			   String value =  cfgm.getGuilds().getString(guild + ".members."+s);
			   map.put(UUID.fromString(s),value);
			}
			
			g.setGuildMap(map);
			
			//Add guild to guild manager list
			GuildManager.addGuild(g);
			
		}
	}
	
	public static void saveGuilds() {
		//Clears the old config file
		for(String key : cfgm.getGuilds().getKeys(false))
			cfgm.getGuilds().set(key, null);
		
		//Saves guilds from object array to config
		for(Guild g : guildList) {
			cfgm.getGuilds().createSection(g.toString() + ".members", g.getGuildMap());
			cfgm.getGuilds().set(g.toString() + ".lives",g.getLives());
			
			if(g.getHome() != null) {
				Location loc = g.getHome();
				cfgm.getGuilds().set(g.toString() + ".home.x",loc.getBlockX());
				cfgm.getGuilds().set(g.toString() + ".home.y",loc.getBlockY());
				cfgm.getGuilds().set(g.toString() + ".home.z",loc.getBlockZ());
				cfgm.getGuilds().set(g.toString() + ".home.world",loc.getWorld().getName());
			}
		}
	}
	//Removes a guild from the guild list
	public static ArrayList<Guild> getGuildList() {
		return guildList;
	}
	
	//Returns a guild object that has the given name
	public static Guild getGuildFromGuildName(String guildName) {
		for(Guild g : guildList)
			if(g.toString().equalsIgnoreCase(guildName))
				return g;
		return null;
	}
	
	public static Guild getGuildFromPlayerUUID(UUID uuid) {
		for(Guild g : guildList) {
			if(g.getGuildMap().containsKey(uuid))
				return g;
		}
		return null;
	}
	
	//Adds a guild to the guild list
	public static void addGuild(Guild guild) {
		if(!(guildList.contains(guild)))
			guildList.add(guild);
	}
	
	//Removes a guild from the guild list
	public static void removeGuild(Guild guild) {
		if(guildList.contains(guild))
			guildList.remove(guild);
	}
	
}