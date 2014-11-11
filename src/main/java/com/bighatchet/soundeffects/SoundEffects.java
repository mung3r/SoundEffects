package com.bighatchet.soundeffects;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

public class SoundEffects extends JavaPlugin
{
    private static final int PAGE_SIZE = 10;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean result = true;

        if (sender instanceof Player && "sound".equals(command.getName())) {
            Player player = (Player) sender;

            if (args.length < 1) {
                return false;
            }

            switch (args[0]) {
                case "list":
                    List<Sound> sounds = Lists.newArrayList(Sound.values());
                    Collections.sort(sounds, new Comparator<Sound>() {

                        @Override
                        public int compare(Sound a, Sound b)
                        {
                            return a.name().compareToIgnoreCase(b.name());
                        }
                    });

                    int page = 1;
                    int pages = (int) Math.ceil(sounds.size() / PAGE_SIZE);
                    if (args.length > 1) {
                        try {
                            page = Integer.valueOf(args[1]);
                            if (page > pages) {
                                page = 1;
                            }
                        }
                        catch (Exception ignored) {
                        }
                    }

                    int i = page * PAGE_SIZE;
                    int end = (page + 1) * PAGE_SIZE;
                    player.sendMessage("Page " + page + " of " + pages);
                    while (i < end && i < sounds.size()) {
                        player.sendMessage(sounds.get(i++).name().toLowerCase());
                    }
                    result = true;
                    break;
                case "play":
                    Sound sound = null;
                    float pitch;
                    float volume;
                    try {
                        sound = Sound.valueOf(args[1].toUpperCase());
                        volume = Float.valueOf(args[2]);
                        pitch = Float.valueOf(args[3]);
                        player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
                    }
                    catch (Exception e) {
                        player.sendMessage("Invalid soundeffect command.");
                    }
                    result = true;
                    break;
                default:
                    result = super.onCommand(sender, command, label, args);
            }
        }

        return result;
    }
}
