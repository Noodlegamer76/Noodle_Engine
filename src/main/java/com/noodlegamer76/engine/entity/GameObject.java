package com.noodlegamer76.engine.entity;

import com.noodlegamer76.engine.core.ComponentManager;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.ComponentType;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.network.GameObjectPayload.ComponentPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class GameObject extends Entity {
    private static final EntityDataAccessor<Vector3f> SCALE = SynchedEntityData.defineId(GameObject.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<CompoundTag> COMPONENT_UPDATE_DATA = SynchedEntityData.defineId(GameObject.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<CompoundTag> COMPONENT_DELETE_DATA = SynchedEntityData.defineId(GameObject.class, EntityDataSerializers.COMPOUND_TAG);
    private final ComponentManager componentManager = new ComponentManager();
    private int nextId = 0;

    public GameObject(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder dataBuilder) {
        dataBuilder.define(SCALE, new Vector3f(1, 1, 1));
        dataBuilder.define(COMPONENT_UPDATE_DATA, new CompoundTag());
        dataBuilder.define(COMPONENT_DELETE_DATA, new CompoundTag());
    }

    public void addComponent(Component component) {
        componentManager.addComponent(component);
    }

    public void removeComponent(Component component) {
        componentManager.removeComponent(component);
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
    }

    @Override
    public void onClientRemoval() {
        super.onClientRemoval();

        for (Component component: componentManager.getComponents().values()) {
            removeComponent(component);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        byte[] state = componentManager.getLastFullState();
        if (state == null) return;
        if (state.length > 0) {
            ComponentPayload payload = new ComponentPayload(getId(), state);
            serverPlayer.connection.send(payload);
        }
    }

    @Override
    public void tick() {
        super.tick();

        componentManager.tick(this);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        CompoundTag noodleEngineTag = tag.getCompound("noodleengine");
        nextId = noodleEngineTag.getInt("nextId");
        Vector3f scale = new Vector3f(noodleEngineTag.getFloat("scaleX"), noodleEngineTag.getFloat("scaleY"), noodleEngineTag.getFloat("scaleZ"));
        entityData.set(SCALE, scale);

        CompoundTag componentTag = noodleEngineTag.getCompound("components");
        componentTag.getAllKeys().forEach(key -> {
            int id = Integer.parseInt(key);
            CompoundTag componentTagData = componentTag.getCompound(key);
            Component component;
            if (!componentManager.getComponents().containsKey(id)) {
                ResourceLocation type = ResourceLocation.tryParse(componentTagData.getString("type"));
                component = InitComponents.getComponentType(type).create(this);
                component.setId(id);
                addComponent(component);
            }
            else {
                component = componentManager.getComponents().get(id);
            }
            component.load(componentTagData);
            if (componentManager.getComponents().containsKey(component.getId())) {
                componentManager.getComponents().replace(component.getId(), component);
            }
            component.onAdded(level());
        });
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        CompoundTag noodleEngineTag = new CompoundTag();
        noodleEngineTag.putInt("nextId", nextId);
        Vector3f scale = entityData.get(SCALE);
        noodleEngineTag.putFloat("scaleX", scale.x);
        noodleEngineTag.putFloat("scaleY", scale.y);
        noodleEngineTag.putFloat("scaleZ", scale.z);

        CompoundTag componentTag = new CompoundTag();
        for (Component component : componentManager.getComponents().values()) {
            component.save(componentTag);
        }
        noodleEngineTag.put("components", componentTag);
        tag.put("noodleengine", noodleEngineTag);
    }

    public Vector3f getScale() {
        return entityData.get(SCALE);
    }

    public void setScale(Vector3f scale) {
        entityData.set(SCALE, scale);
    }

    public int nextId() {
        return nextId++;
    }

    public ComponentManager getComponentManager() {
        return componentManager;
    }

    public static EntityDataAccessor<CompoundTag> getComponentDeleteData() {
        return COMPONENT_DELETE_DATA;
    }

    public static EntityDataAccessor<CompoundTag> getComponentUpdateData() {
        return COMPONENT_UPDATE_DATA;
    }
}
