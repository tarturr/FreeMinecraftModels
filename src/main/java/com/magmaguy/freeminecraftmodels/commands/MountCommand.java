package com.magmaguy.freeminecraftmodels.commands;

import com.magmaguy.freeminecraftmodels.MetadataHandler;
import com.magmaguy.freeminecraftmodels.customentity.DynamicEntity;
import com.magmaguy.freeminecraftmodels.dataconverter.FileModelConverter;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MountCommand extends AdvancedCommand {

    List<String> entityIDs = new ArrayList<>();

    public MountCommand() {
        super(List.of("mount"));
        setDescription("Mounts a model (experimental!)");
        setPermission("freeminecraftmodels.*");
        setDescription("/fmm mount <modelID>");
        setUsage("/fmm mount <modelID>");
        setSenderType(SenderType.PLAYER);
        entityIDs = new ArrayList<>();
        FileModelConverter.getConvertedFileModels().values().forEach(fileModelConverter -> entityIDs.add(fileModelConverter.getID()));
        addArgument("models", entityIDs);
    }

    @Override
    public void execute(CommandData commandData) {
        if (!entityIDs.contains(commandData.getStringArgument("models"))) {
            Logger.sendMessage(commandData.getCommandSender(), "Invalid entity ID!");
            return;
        }

        final Player player = commandData.getPlayerSender();

        DynamicEntity dynamicEntity = DynamicEntity.create(
                commandData.getStringArgument("models"),
                player.getUniqueId(),
                true,
                (LivingEntity) commandData.getPlayerSender().getWorld().spawnEntity((commandData.getPlayerSender()).getLocation(), EntityType.HORSE)
        );

        if (dynamicEntity == null) {
            Logger.sendMessage(commandData.getCommandSender(), "The provided modelID was not found.");
            return;
        }

        final Horse horse = (Horse) dynamicEntity.getLivingEntity();
        horse.setOwner(player);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.addPassenger(player);

        player.sendMessage(Component.text("You are now riding your model!").color(TextColor.color(0x00ff00)));
    }
}
