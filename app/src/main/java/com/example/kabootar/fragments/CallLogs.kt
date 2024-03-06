package com.example.kabootar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabootar.DataModel
import com.example.kabootar.R
import com.example.kabootar.adapters.CallRecyclerViewAdapter
import com.example.kabootar.databinding.FragmentCallLogsBinding

class CallLogs : Fragment() {

    lateinit var arr : MutableList<DataModel>
    lateinit var callAdapter: CallRecyclerViewAdapter
    lateinit var fragmentBind : FragmentCallLogsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBind = FragmentCallLogsBinding.inflate(inflater, container, false)
        return fragmentBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arr = mutableListOf(DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[....1", "Yash"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Yash Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Yash Surendra Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Manisha"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Manisha Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Manisha Surendra Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Surendra"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Surendra Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Surendra GorakhNath Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Karan"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[","Karan Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Karan Surendra Jadhav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Saurav"),DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[......n", "Shubham"))

        callAdapter = CallRecyclerViewAdapter(arr,requireContext())

        fragmentBind.rvCallLogRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = callAdapter
        }
    }

}