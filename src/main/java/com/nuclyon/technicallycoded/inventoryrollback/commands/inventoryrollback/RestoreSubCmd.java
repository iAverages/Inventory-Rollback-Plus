package com.nuclyon.technicallycoded.inventoryrollback.commands.inventoryrollback;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import com.nuclyon.technicallycoded.inventoryrollback.commands.IRPCommand;
import me.danjono.inventoryrollback.InventoryRollback;
import me.danjono.inventoryrollback.config.ConfigData;
import me.danjono.inventoryrollback.config.MessageData;
import me.danjono.inventoryrollback.gui.menu.MainMenu;
import me.danjono.inventoryrollback.gui.menu.PlayerMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestoreSubCmd extends IRPCommand {

    public RestoreSubCmd(InventoryRollbackPlus mainIn) {
        super(mainIn);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("inventoryrollbackplus.restore") || sender.hasPermission("inventoryrollback.restore")) {
                if (!ConfigData.isEnabled()) {
                    sender.sendMessage(MessageData.getPluginPrefix() + MessageData.getPluginDisabled());
                    return;
                }
                Player staff = (Player) sender;
                openBackupMenu(sender, staff, args);
            } else {
                sender.sendMessage(MessageData.getPluginPrefix() + MessageData.getNoPermission());
            }
        } else {
            sender.sendMessage(MessageData.getPluginPrefix() + MessageData.getPlayerOnlyError());
        }
    }

    private void openBackupMenu(CommandSender sender, Player staff, String[] args) {
        if (args.length <= 0 || args.length == 1) {
            try {
                openMainMenu(staff);
            } catch (NullPointerException e) {}
        } else if(args.length == 2) {
            @SuppressWarnings("deprecation")
            OfflinePlayer rollbackPlayer = Bukkit.getOfflinePlayer(args[1]);

            try {
                openPlayerMenu(staff, rollbackPlayer);
            } catch (NullPointerException e) {}
        } else {
            sender.sendMessage(MessageData.getPluginPrefix() + MessageData.getError());
        }
    }

    private void openMainMenu(Player staff) {
        MainMenu menu = new MainMenu(staff, 1);

        staff.openInventory(menu.getInventory());
        Bukkit.getScheduler().runTaskAsynchronously(InventoryRollback.getInstance(), menu::getMainMenu);
    }

    private void openPlayerMenu(Player staff, OfflinePlayer offlinePlayer) {
        PlayerMenu menu = new PlayerMenu(staff, offlinePlayer);

        staff.openInventory(menu.getInventory());
        Bukkit.getScheduler().runTaskAsynchronously(InventoryRollback.getInstance(), menu::getPlayerMenu);
    }

}
