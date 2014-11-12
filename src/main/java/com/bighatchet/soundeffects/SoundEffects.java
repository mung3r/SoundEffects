package com.bighatchet.soundeffects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

public class SoundEffects extends JavaPlugin
{
    private static final List<String> COMMANDS = Lists.newArrayList("help", "list", "play");
    private static final List<Sound> SOUNDS;
    private static final int PAGE_SIZE = 10;
    private static final int NUM_PAGES;
    static {
        SOUNDS = Lists.newArrayList(Sound.values());
        Collections.sort(SOUNDS, new Comparator<Sound>() {

            @Override
            public int compare(Sound a, Sound b)
            {
                return a.name().compareToIgnoreCase(b.name());
            }
        });

        NUM_PAGES = SOUNDS.size() / PAGE_SIZE;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player && "sound".equals(command.getName())) {
            Player player = (Player) sender;

            if (args.length > 0) {
                switch (args[0]) {
                case "list":
                    int page = 1;
                    if (args.length > 1) {
                        try {
                            page = Integer.valueOf(args[1]);
                            if (page > NUM_PAGES) {
                                page = 1;
                            }
                        }
                        catch (Exception ignored) {
                        }
                    }

                    int i = page * PAGE_SIZE;
                    int end = (page + 1) * PAGE_SIZE;
                    player.sendMessage(ChatColor.GRAY + "Page " + page + " of " + NUM_PAGES);
                    while (i < end && i < SOUNDS.size()) {
                        player.sendMessage(ChatColor.GRAY + SOUNDS.get(i++).name().toLowerCase());
                    }
                    break;
                case "play":
                    Sound sound = null;
                    float pitch;
                    float volume;
                    try {
                        sound = Sound.valueOf(args[1].toUpperCase());
                        volume = args.length > 2 ? Float.valueOf(args[2]) : 1.0F;
                        pitch = args.length > 3 ? Float.valueOf(args[3]) : 1.0F;
                        player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
                    }
                    catch (Exception e) {
                        player.sendMessage(ChatColor.GRAY + "Invalid play arguments. Type /sound help for play command syntax.");
                    }
                    break;
                case "help":
                default:
                    player.sendMessage(ChatColor.GRAY + "Usage");
                    player.sendMessage(ChatColor.GRAY + "/<command> list [page] - list sound names");
                    player.sendMessage(ChatColor.GRAY + "/<command> play <sound_name> [volume] [pitch] - play a sound");
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> matches = new ArrayList<String>();

        if (sender instanceof Player && "sound".equals(command.getName())) {

            switch (args[0]) {
            case "play":
                if (args.length > 1 && StringUtils.isNotEmpty(args[1])) {
                    for (Sound sound : SOUNDS) {
                        if (sound.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                            matches.add(sound.name().toLowerCase());
                        }
                    }
                }
                break;
            default:
                if (StringUtils.isNotEmpty(args[0])) {
                    for (String cmd : COMMANDS) {
                        if (cmd.toLowerCase().startsWith(args[0])) {
                            matches.add(cmd);
                        }
                    }
                }
            }
        }

        return matches;
    }
}
