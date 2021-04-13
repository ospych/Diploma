package com.example.intourist.ui.filters

//import com.example.intourist.adapter.AdapterCategory
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.intourist.R
import com.example.intourist.retrofit.*
import com.example.intourist.retrofit.model.Category
import com.example.intourist.ui.filters.data.CategoryName
import com.example.intourist.utils.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FiltersFragment : Fragment(R.layout.fragment_filters) {

    private val calendar = Calendar.getInstance()
    private lateinit var mService : TourApi
    var category = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true)

        mService = RetrofitInstance.api
        if (isNetworkAvailable(requireContext()) || isInternetAvailable()) {
//            categories()
//            getDate()
        } else {
            toast(requireContext(), "Нет интернет соединения")
        }

        chooseBT.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("filtered", true)
                putString("category", category)
                putString("minDate", dateFrom.text.toString())
            }
            findNavController().navigate(
                    R.id.action_filtersFragment_to_navigation_tours, bundle
            )
        }
    }

    private fun categories() {
        mService.getCategory().enqueue(object : Callback<MutableList<Category>> {
            override fun onResponse(call: Call<MutableList<Category>>, response: Response<MutableList<Category>>) {
                val categoriesArray: ArrayList<CategoryName> = ArrayList()
                categoriesArray.add(CategoryName("Все"))
                response.body()?.forEach {
                    categoriesArray.add(CategoryName(it.name))
                }
                try {
                    spinnerCategory(requireContext(), categoriesArray, categoryFilter)
                } catch (e: Exception) {
                    log(e.toString())
                }


                categoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                    ) {
                        val categoryName: CategoryName = parent.selectedItem as CategoryName
                        category = if (categoryName.name == "Все") {
                            ""
                        } else {
                            categoryName.name
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<Category>>, t: Throwable) {

            }

        })
    }

    private fun getDate() = CoroutineScope(Dispatchers.Main).launch {
        dateFrom.setOnClickListener {
            DatePickerDialog(
                    requireContext(),
                    date(dateFrom),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}