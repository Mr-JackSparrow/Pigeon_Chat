package com.example.kabootar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kabootar.DataModel
import com.example.kabootar.databinding.ChatlistBinding

class ChatRecyclerViewAdapter(var arr : MutableList<DataModel>, var context : Context)
    : RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder>()
{

        lateinit var bind : ChatlistBinding

        inner class MyViewHolder(view: ChatlistBinding) : RecyclerView.ViewHolder(view.root){
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        bind = ChatlistBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(bind)
    }

    override fun getItemCount() = arr.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(arr[position]){
                bind.tvProfileName.text = this.name
                bind.tvLastMsg.text = this.msg
            }
        }
    }
}