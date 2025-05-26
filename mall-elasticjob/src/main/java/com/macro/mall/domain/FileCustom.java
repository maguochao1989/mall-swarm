package com.macro.mall.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FileCustom {
    private long id;
    private String name;
    private String type;
    private String content;
    private Boolean backedUp = false;

}