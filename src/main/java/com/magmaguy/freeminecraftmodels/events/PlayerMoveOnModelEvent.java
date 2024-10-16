package com.magmaguy.freeminecraftmodels.events;

import com.magmaguy.freeminecraftmodels.MetadataHandler;
import com.magmaguy.freeminecraftmodels.customentity.MountingData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class PlayerMoveOnModelEvent implements Listener {

    private NamespacedKey namespace;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        this.namespace = NamespacedKey.fromString("dynamic_entity", MetadataHandler.PLUGIN);
        final var availableData = this.availableMountingData(player);

        if (availableData.isPresent() && availableData.get().isMounting()) {
            event.setCancelled(true);
        }
    }

    private Optional<MountingData> availableMountingData(Player player) {
        if (player.isInsideVehicle() && this.namespace != null) {
            player.sendMessage("Namespace exists and riding smth");
            final Entity entity = player.getVehicle();

            if (entity instanceof final Horse horse) {
                player.sendMessage("Riding a horse");
                PersistentDataContainer container = horse.getPersistentDataContainer();

                if (container.has(this.namespace)) {
                    player.sendMessage("Namespace exists in horse");
                    String data = container.get(this.namespace, PersistentDataType.STRING);

                    if (data == null) {
                        throw new IllegalStateException("Mounting data cannot be null.");
                    }

                    return Optional.of(new MountingData(data));
                }
            }
        }

        return Optional.empty();
    }

}
