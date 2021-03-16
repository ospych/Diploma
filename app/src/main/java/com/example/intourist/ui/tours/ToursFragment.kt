package com.example.intourist.ui.tours

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intourist.R
import com.example.intourist.adapter.Adapter
import com.example.intourist.retrofit.Repository
import com.example.intourist.retrofit.RetrofitViewModel
import com.example.intourist.retrofit.RetrofitViewModelFactory
import com.example.intourist.utils.isInternetAvailable
import com.example.intourist.utils.isNetworkAvailable
import kotlinx.android.synthetic.main.fragment_tours.*


class ToursFragment : Fragment(R.layout.fragment_tours) {

    private lateinit var retrofitViewModel: RetrofitViewModel
    private val adapter by lazy { Adapter() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerview()

        val isFiltered = arguments?.getBoolean("filtered")

        if (isNetworkAvailable(requireContext()) || isInternetAvailable()) {
            if (isFiltered == true){
                val category = arguments?.getString("category")
                if (category != null) {
                    getFilteredTours(category)
                }
            } else {
                getTours()
            }
        } else {
            Toast.makeText(requireContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
        }

        filterBT.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_tours_to_filtersFragment
            )
        }
    }

    private fun getFilteredTours(filter: String) {
        val repository = Repository()
        val tourViewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(requireActivity(), tourViewModelFactory)
            .get(RetrofitViewModel::class.java)
        retrofitViewModel.getFilteredTour(filter)
        retrofitViewModel.myResponseFiltered.observe(requireActivity(), { response ->
            if (response.isSuccessful) {
                response.body()?.let { adapter.setData(it) }
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTours() {
        val repository = Repository()
        val tourViewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(requireActivity(), tourViewModelFactory)
            .get(RetrofitViewModel::class.java)
        retrofitViewModel.getTours()
        retrofitViewModel.myResponse.observe(requireActivity(), { response ->
            if (response.isSuccessful) {
                response.body()?.let { adapter.setData(it) }
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerview() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}