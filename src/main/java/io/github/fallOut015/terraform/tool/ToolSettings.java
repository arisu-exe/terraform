package io.github.fallOut015.terraform.tool;

import java.util.Map;

public class ToolSettings {
    final Map<String, Object> types;

    public ToolSettings(Map<?, ?> settings) {
        this.types = (Map<String, Object>) settings;
    }

    public <T> T get(String key) {
        return (T) this.types.get(key);
    }
    public void set(String key, Object value) {
        this.types.replace(key, value);
    }
}