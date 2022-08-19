package com.gmail.necnionch.myplugin.ceteamsetprefix.bukkit;

import com.gmail.necnionch.myplugin.sideboardperplayer.bukkit.SideboardPlugin;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TeamArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public final class CETeamSetPrefixPlugin extends JavaPlugin {
    public static final String PERMS_COMMAND = "teamsetprefix.command.";

    @Override
    public void onEnable() {
        new CommandAPICommand("ceteamsetprefix")
                .withPermission(PERMS_COMMAND + ".setprefix")
                .withArguments(new TeamArgument("team"))
                .withArguments(new GreedyStringArgument("jsonText"))
                .executesNative(this::execSetPrefix)
                .register();

        new CommandAPICommand("ceteamsetsuffix")
                .withPermission(PERMS_COMMAND + ".setsuffix")
                .withArguments(new TeamArgument("team"))
                .withArguments(new GreedyStringArgument("jsonText"))
                .executesNative(this::execSetSuffix)
                .register();

    }

    private int execSetPrefix(NativeProxyCommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        String teamName = ((String) args[0]);
        String json = (String) args[1];

        @SuppressWarnings("ConstantConditions")
        Team team = getServer().getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (team == null)
            return 0;

        String text;
        Entity target;
        if (sender.getCallee() instanceof Entity) {
            target = (Entity) sender.getCallee();
        } else if (sender.getCaller() instanceof Entity) {
            target = (Entity) sender.getCaller();
        } else {
            throw new WrapperCommandSyntaxException(new CommandSyntaxException(new CommandExceptionType() {}, () -> "NOT ENTITY TARGET"));
        }

        try {
            text = SideboardPlugin.convertJsonToString(sender.getCaller(), target, json);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            throw new WrapperCommandSyntaxException(e);
        }

        team.setPrefix(text);
        sender.sendMessage("チームPrefixを変更: " + text);
        return 0;
    }

    private int execSetSuffix(NativeProxyCommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        String teamName = ((String) args[0]);
        String json = (String) args[1];

        @SuppressWarnings("ConstantConditions")
        Team team = getServer().getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (team == null)
            return 0;

        String text;
        Entity target;
        if (sender.getCallee() instanceof Entity) {
            target = (Entity) sender.getCallee();
        } else if (sender.getCaller() instanceof Entity) {
            target = (Entity) sender.getCaller();
        } else {
            throw new WrapperCommandSyntaxException(new CommandSyntaxException(new CommandExceptionType() {}, () -> "NOT ENTITY TARGET"));
        }

        try {
            text = SideboardPlugin.convertJsonToString(sender.getCaller(), target, json);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            throw new WrapperCommandSyntaxException(e);
        }

        team.setSuffix(text);
        sender.sendMessage("チームSuffixを変更: " + text);
        return 0;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
