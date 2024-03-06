package com.example.kabootar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kabootar.DataModel
import com.example.kabootar.databinding.CalllistBinding

class CallRecyclerViewAdapter
    (var arr : MutableList<DataModel>, var context : Context)
    : RecyclerView.Adapter<CallRecyclerViewAdapter.CallViewHolder>()
{
        lateinit var callBinding : CalllistBinding

        inner class CallViewHolder
            (view : CalllistBinding)
            :RecyclerView.ViewHolder(view.root)
        {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        callBinding = CalllistBinding.inflate(LayoutInflater.from(context), parent, false)

        return CallViewHolder(callBinding)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        with(holder){
            with(arr[position]){
                callBinding.tvProfileName.text = this.name
            }
        }
    }
}