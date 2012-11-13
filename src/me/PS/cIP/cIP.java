/////////////////////////////////////////////////////
//#################################################//
//##                                             ##//
//##                                             ##//
//##	Developed and creted by P_S 2012         ##//
//##	www.ps-bred02.eu                         ##//
//##	psb@ps-bred02.eu                         ##//
//##                                             ##//
//##                                             ##//
//#################################################//
/////////////////////////////////////////////////////


package me.PS.cIP;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class cIP extends JavaPlugin implements Listener {
	public static cIP plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	
	////////////////////////////////////
	////////////////////////////////////
	//////         Events		  //////
	////////////////////////////////////
	////////////////////////////////////
	
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
        this.saveDefaultConfig();
        getConfig().options().copyHeader(true);
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.addPermission(new cIPPermissions().maincIP);
		getServer().getPluginManager().registerEvents(this, this);
				
		this.logger.info("[cIP] " + pdfFile.getName() + " (" + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + ") Has been enabled ...");		

		String alphaVer = pdfFile.getVersion();
		String betaVer = getConfig().getString("version");		
		
		if(alphaVer.equals(betaVer)) {
			this.logger.info("[cIP] {I} plugin config is actual ...");
			this.logger.info("[cIP] {I} server v-" + getConfig().getString("version") + " & plugin v-" + pdfFile.getVersion() + " ...");
			
		} else {
			this.logger.info("[cIP] {W} plugin config is not actual ...");
			this.logger.info("[cIP] {I} server v-" + getConfig().getString("version") + " & plugin v-" + pdfFile.getVersion() + " ...");
			this.logger.info("[cIP] {W} updating plugin config ...");

			File config = new File(getDataFolder(), "config.yml");
			config.renameTo(new File(getDataFolder(), "oldConfigV" + betaVer + ".yml"));
			config.delete();
	        this.saveDefaultConfig();
	        getConfig().options().copyHeader(true);
			
			
			this.logger.info("[cIP] {W} update complete ...");
			if(alphaVer.equals(betaVer)) {
				this.logger.info("[cIP] {D} internal error config update failed ...");
				this.logger.info("[cIP] {D} try remove config and run plugin again ...");				
			} else {
				this.logger.info("[cIP] {I} update was succesfull ...");
			}
		}
		
		
		if(getConfig().getBoolean("numericIP") == true) {
			this.logger.info("[cIP] - Numeric IP replace enabled ...");
		} else {
			this.logger.info("[cIP] - Numeric IP replace is disabled ...");
		}
		
		
	}	
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[cIP] " + pdfFile.getName() + " (" + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + ") Has been disabled ...");
		getServer().getPluginManager().removePermission(new cIPPermissions().maincIP);
		
		
	}

	@EventHandler	
	public void onChat(PlayerChatEvent event) {
		String msgText = event.getMessage();
		String replacement = getConfig().getString("chatReplacement");
		String msgEditOne;
		String msgEditTwo;
		
		
		if(getConfig().getBoolean("numericIP") == true) {
			msgEditOne = msgText.replaceAll("\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?", ChatColor.translateAlternateColorCodes('&', replacement));
			
		} else {
			msgEditOne = msgText;
			
		}
		
		if(getConfig().getBoolean("domainIP") == true) {
			msgEditTwo = msgEditOne.replaceAll("", ChatColor.translateAlternateColorCodes('&', replacement));
			
		} else {
			msgEditTwo = msgEditOne;
			
		}
		
		String msgEdited = msgEditTwo;
		
		event.setMessage(msgEdited);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		PluginDescriptionFile pdfFile = this.getDescription();
		
		if(commandLabel.equalsIgnoreCase("cIP")) {
			if(sender.hasPermission(new cIPPermissions().maincIP)) {
				if(args.length == 0) {
					this.logger.info("[PSB] Player '" + sender.getName() + "' used command /cIP");	
					sender.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "About cIP Plugin" + ChatColor.WHITE + "]");
					sender.sendMessage(ChatColor.GREEN + "------------------------------------------------");
					sender.sendMessage(ChatColor.GREEN + "  Author: " + ChatColor.WHITE + pdfFile.getAuthors() +"");
					sender.sendMessage(ChatColor.GREEN + "  WWW: " + ChatColor.WHITE + pdfFile.getWebsite() +"");
					sender.sendMessage(ChatColor.GREEN + "  Version: " + ChatColor.WHITE + pdfFile.getVersion() +"");
					sender.sendMessage(ChatColor.GREEN + "------------------------------------------------");
					sender.sendMessage(ChatColor.GOLD + "  /cIP" + ChatColor.WHITE + " - Info about plugin");
					sender.sendMessage(ChatColor.GOLD + "  /cIP reload" + ChatColor.WHITE + " - Reload plugin");
					sender.sendMessage(ChatColor.GREEN + "------------------------------------------------");
					
				}
				if(args[0].equals("reload")) {
					String plugVer = pdfFile.getVersion();
					String chatPrefix = getConfig().getString("chatPrefix");
					
					this.logger.info("[cIP] Player '" + sender.getName() + "' used command /cIP reload");
					this.logger.info("------------------------------------------------");
					sender.sendMessage(ChatColor.GREEN + "------------------------------------------------");	
					this.logger.info("[cIP] Reloading cIP plugin v-" + plugVer + "");	
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', chatPrefix) + ChatColor.GREEN + " Reloading cIP plugin v-" + plugVer);					
			
					// reload start script	
			
					this.reloadConfig();
					
					if(getConfig().getBoolean("numericIP") == true) {
						this.logger.info("[cIP] - Debug System is enabled ...");
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', chatPrefix) + " - Numeric IP is enabled ...");
					} else {
						this.logger.info("[cIP] - Debug System is disabled ...");
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', chatPrefix) + " - Numeric IP is disabled ...");
					}
					
					// reload end script
					
					
					this.logger.info("[cIP] Reload complete");	
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', chatPrefix) + ChatColor.GREEN + " Reload complete" + ChatColor.WHITE + "");
					this.logger.info("------------------------------------------------");
					sender.sendMessage(ChatColor.GREEN + "------------------------------------------------");
					
				}
			} else {
				this.logger.info("[cIP] Player " + sender.getName() + " tried use command /cIP <arg>");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("chatPrefix")) + " You do not have acces to this command !!");
				
			}
		}
		
		return false;
	}
	
}
