package us.infinitydev.mass_renamer;

import java.util.HashMap;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.User;

public class BotCore {
	
	static HashMap<Long, HashMap<Long, String>> server_map = new HashMap<Long, HashMap<Long, String>>();
	static final String nick = "Super Awesome Nickname";
	
	public static void main(String[] args) {
		String token = "token";
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
		System.out.println("MassRenamer has started.");
			
	    api.addMessageCreateListener(event ->{
	    	if(event.getMessage().getContent().equalsIgnoreCase("A-establish")) {
	    		if(!event.getMessage().getAuthor().isBotOwner()) {
	    			return;
	    		}else {
	    			event.getMessage().delete();
	    			
	    			System.out.println("Started renaming process.");
	    			
	    			for(User u : event.getServer().get().getMembers()) {
	    				if(u.getNickname(event.getServer().get()).isPresent()) {
	    					HashMap<Long, String> map;
	    					if(server_map.containsKey(event.getServer().get().getId())) {
	    						map = server_map.get(event.getServer().get().getId());
	    					}else {
	    						map = new HashMap<Long, String>();
	    					}
	    					map.put(u.getId(), u.getNickname(event.getServer().get()).get());
	    					server_map.remove(event.getServer().get().getId());
	    					server_map.put(event.getServer().get().getId(), map);
	    				}
	    				u.updateNickname(event.getMessage().getServer().get(), nick);
	    			}
	    			System.out.println("Done with renaming process");
	    		}
	    	}
	    	
	    	if(event.getMessage().getContent().equalsIgnoreCase("A-revert")) {
	    		if(!event.getMessage().getAuthor().isBotOwner()) {
	    			return;
	    		}else {
	    			event.getMessage().delete();
	    			
	    			System.out.println("Starting renaming process.");
	    			
	    			for(User u : event.getServer().get().getMembers()) {
	    				if(server_map.keySet().contains(event.getServer().get().getId())) {
	    					if(server_map.get(event.getServer().get().getId()).containsKey(u.getId())) {
	    					    u.updateNickname(event.getServer().get(), server_map.get(event.getServer().get().getId()).get(u.getId()));
	    					}else {
		    					u.resetNickname(event.getServer().get());
		    				}
	    				}
	    			}
	    			
	    			System.out.println("Done with renaming process");
	    		}
	    	}
				
		});
	}

}
