package com.magmaguy.freeminecraftmodels.customentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class MountingData {

    private final String modelId;
    private final UUID ownerUUID;
    private final boolean isMounting;

    public MountingData(String nsValue) {
        String[] components = nsValue.split(":");

        if (components.length != 3) {
            throw new IllegalArgumentException("Malformed mounting data String: " + nsValue);
        }

        this.modelId = components[0];
        this.ownerUUID = UUID.fromString(components[1]);
        this.isMounting = Boolean.parseBoolean(components[2]);
    }

    @Override
    public String toString() {
        return this.modelId + ":" + this.ownerUUID.toString() + ":" + this.isMounting;
    }

}
