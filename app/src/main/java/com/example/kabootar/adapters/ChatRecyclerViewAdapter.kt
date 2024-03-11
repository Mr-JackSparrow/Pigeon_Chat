package com.example.kabootar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

                Glide
                    .with(context)
                    .load(this.img)
                    .into(bind.ivCallProfileImg)

                bind.ivCallProfileImg.setOnClickListener {
                    Toast.makeText(context, " Profile Image", Toast.LENGTH_LONG).show()
                }

                bind.mainChat.setOnClickListener {
                    Toast.makeText(context, " chat", Toast.LENGTH_LONG).show()
                }



            }
        }
    }
}