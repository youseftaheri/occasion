package com.iranwebyar.occasions.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {
    var dataList: List<T>? = null
        protected set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return onCreateViewHolderBase(parent, viewType)
    }

    abstract fun onCreateViewHolderBase(parent: ViewGroup?, viewType: Int): BaseViewHolder
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindViewHolderBase(holder, position)
    }

    abstract fun onBindViewHolderBase(holder: BaseViewHolder?, position: Int)
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if (dataList != null && dataList!!.isNotEmpty()) dataList!!.size else 0
    }

    operator fun get(position: Int): T {
        return dataList!![position]
    }

}