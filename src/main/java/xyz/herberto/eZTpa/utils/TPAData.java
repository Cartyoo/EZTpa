package xyz.herberto.eZTpa.utils;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TPAData {

    private final UUID target;
    private final long sentTime;

    public TPAData(UUID target) {
        this.target = target;
        this.sentTime = System.currentTimeMillis();
    }

}
