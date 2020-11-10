package com.akarinti.sapoe.view.main.other.gallery.adapter

import com.akarinti.sapoe.R
import com.akarinti.sapoe.extension.dateFormat
import com.akarinti.sapoe.model.dummy.GallerySection
import com.akarinti.sapoe.utils.GlideApp
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_photo.view.*
import java.util.*

class GalleryAdapter constructor(data: List<GallerySection>) :
    BaseSectionQuickAdapter<GallerySection, BaseViewHolder>(
        R.layout.item_photo,
        R.layout.section_photo,
        data
    ) {

    override fun convertHead(helper: BaseViewHolder?, item: GallerySection?) {
        helper?.setText(R.id.tvSectionTitle, item?.header)
    }

    override fun convert(helper: BaseViewHolder?, item: GallerySection?) {
        helper?.let { h ->
            item?.t?.let { data ->
                GlideApp.with(mContext).load(data.image64).into(h.itemView.ivPhoto)
                h.itemView.tvDesc.text = "${ if(data.status == "inprogress") "unsent" else "sent"} - ${(data.dateTime*1000L).dateFormat("dd/MM/yy kk:mm", Locale("id, ","ID"))}"
                h.itemView.tvInfo.text = "${data.agentName} - ${data.locationName}\nFotoID: ${data.imageName}"
            }
        }
    }
}