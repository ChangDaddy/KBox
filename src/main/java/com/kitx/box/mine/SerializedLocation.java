package com.kitx.box.mine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class SerializedLocation implements Serializable {
    private final String world;
    private final int x, y, z;
}
