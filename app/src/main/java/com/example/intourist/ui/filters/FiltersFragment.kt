package com.example.intourist.ui.filters

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intourist.R
import com.example.intourist.adapter.AdapterCategory
//import com.example.intourist.adapter.AdapterCategory
import com.example.intourist.retrofit.Repository
import com.example.intourist.retrofit.RetrofitViewModel
import com.example.intourist.retrofit.RetrofitViewModelFactory
import com.example.intourist.utils.isInternetAvailable
import com.example.intourist.utils.isNetworkAvailable
import com.example.intourist.utils.toast
import kotlinx.android.synthetic.main.fragment_filters.*


class FiltersFragment : Fragment(R.layout.fragment_filters) {

    private lateinit var retrofitViewModel: RetrofitViewModel
    private val adapter by lazy { AdapterCategory() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerview()

        if (isNetworkAvailable(requireContext()) || isInternetAvailable()) {
            getCategories()
        } else {
            toast(requireContext(), "Нет интернет соединения")
        }

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putBoolean("filtered", true)
                putString("category", it.name)
            }
            chooseBT.setOnClickListener {
                findNavController().navigate(
                    R.id.action_filtersFragment_to_navigation_tours,
                    bundle
                )
            }
        }

        categoryAll.setOnClickListener {
            chooseBT.setOnClickListener {
                findNavController().navigate(
                    R.id.action_filtersFragment_to_navigation_tours
                )
            }
        }
    }

    private fun getCategories() {
        val repository = Repository()
        val categoryViewModel = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(requireActivity(), categoryViewModel)
            .get(RetrofitViewModel::class.java)
        retrofitViewModel.getCategories()
        retrofitViewModel.myResponseCategory.observe(requireActivity(), {response ->
            if (response.isSuccessful) {
                response.body().let {
                    if (it != null) {
                        adapter.differ.submitList(it)
                        adapter.notifyDataSetChanged()
                    } else {
                        toast(requireContext(), "null")
                    }
                }
            } else {
                toast(requireContext(), "Error")
            }
        })
    }

    private fun setupRecyclerview() {
        recyclerViewCategory.adapter = adapter
        recyclerViewCategory.layoutManager = LinearLayoutManager(requireContext())
    }
}