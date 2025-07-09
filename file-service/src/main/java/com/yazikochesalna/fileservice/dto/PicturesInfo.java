package com.yazikochesalna.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PicturesInfo {

    BaseFileInfo baseMeta;
    int width;
    int height;
}
