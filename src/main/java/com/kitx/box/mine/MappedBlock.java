package com.kitx.box.mine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class MappedBlock implements Serializable {
    private final SerializedLocation location;
    private final String material;
}
