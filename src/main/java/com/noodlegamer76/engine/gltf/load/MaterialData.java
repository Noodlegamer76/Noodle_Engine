package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.material.McMaterial;
import de.javagl.jgltf.model.MaterialModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialData {
    private final List<McMaterial> materials = new ArrayList<>();
    private final Map<MaterialModel, McMaterial> materialModelToMaterial = new HashMap<>();

    public List<McMaterial> getMaterials() {
        return materials;
    }

    public void addMaterial(McMaterial material) {
        materials.add(material);
        materialModelToMaterial.put(material.getMaterialModel(), material);
    }

    public Map<MaterialModel, McMaterial> getMaterialModelToMaterial() {
        return materialModelToMaterial;
    }
}
