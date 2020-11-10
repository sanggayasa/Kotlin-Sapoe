package com.akarinti.sapoe.model.dummy

import com.chad.library.adapter.base.entity.SectionEntity

class GallerySection : SectionEntity<Photo> {
    constructor(isHeader: Boolean, header: String) : super(isHeader, header)
    constructor(t: Photo) : super(t)
}