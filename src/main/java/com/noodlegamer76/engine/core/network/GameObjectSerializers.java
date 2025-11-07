package com.noodlegamer76.engine.core.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

public class GameObjectSerializers {
    public static final GameObjectSerializer<String> STRING = GameObjectSerializer.create(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    public static final GameObjectSerializer<Integer> INTEGER = GameObjectSerializer.create(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt);
    public static final GameObjectSerializer<Integer> VAR_INT = GameObjectSerializer.create(FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);
    public static final GameObjectSerializer<Float> FLOAT = GameObjectSerializer.create(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
    public static final GameObjectSerializer<Boolean> BOOLEAN = GameObjectSerializer.create(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    public static final GameObjectSerializer<Double> DOUBLE = GameObjectSerializer.create(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
    public static final GameObjectSerializer<Long> LONG = GameObjectSerializer.create(FriendlyByteBuf::writeLong, FriendlyByteBuf::readLong);
    public static final GameObjectSerializer<ResourceLocation> RESOURCE_LOCATION = GameObjectSerializer.create(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation);
    public static final GameObjectSerializer<Vector3f> VECTOR3F = GameObjectSerializer.create((
            (buf, value) -> buf.writeVector3f(value)),
            (buf -> buf.readVector3f()));
    public static final GameObjectSerializer<Quaternionf> QUATERNION = GameObjectSerializer.create((
                    (buf, value) -> buf.writeQuaternion(value)),
            (buf -> buf.readQuaternion()));
    public static final GameObjectSerializer<UUID> UUID = GameObjectSerializer.create((
                    (buf, value) -> buf.writeUUID(value)),
            (buf -> buf.readUUID()));
}
