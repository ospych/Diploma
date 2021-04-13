package com.example.intourist.ui.tours

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intourist.R
import com.example.intourist.adapter.Adapter
import com.example.intourist.retrofit.*
import com.example.intourist.retrofit.model.Category
import com.example.intourist.ui.filters.data.CategoryName
import com.example.intourist.utils.*
import kotlinx.android.synthetic.main.bottom_sheet_menu.*
import kotlinx.android.synthetic.main.bottom_sheet_menu.categoryFilter
import kotlinx.android.synthetic.main.bottom_sheet_menu.view.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.fragment_tours.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ToursFragment : Fragment(R.layout.fragment_tours) {

    private lateinit var retrofitViewModel: RetrofitViewModel
    private val adapter by lazy { Adapter() }

    private val calendar = Calendar.getInstance()
    private lateinit var mService : TourApi
    var category = ""
    private var menu: Menu? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = RetrofitInstance.api

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.show()
        setHasOptionsMenu(true)

        setupRecyclerview()

        val isFiltered = arguments?.getBoolean("filtered")

        if (isNetworkAvailable(requireContext()) || isInternetAvailable()) {
            if (isFiltered == true) {
                val category = arguments?.getString("category")

                val minDate = arguments?.getString("minDate")
                if (minDate != null) {
                    getFilteredTours(category.toString(), minDate)
                }
            } else {
                getTours()
            }
        } else {
            Toast.makeText(requireContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
            tourPB.visibility = View.INVISIBLE
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFilteredTours(category: String, date: String) {
        val repository = Repository()
        val tourViewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(requireActivity(), tourViewModelFactory)
            .get(RetrofitViewModel::class.java)
        retrofitViewModel.getFilteredTour(category, date)
        retrofitViewModel.myResponseFiltered.observe(requireActivity(), { response ->
            if (response.isSuccessful) {
                response.body()?.let { adapter.setData(it) }
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "response.code()", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTours() {
        tourPB.visibility = View.VISIBLE
        val repository = Repository()
        val tourViewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(requireActivity(), tourViewModelFactory)
            .get(RetrofitViewModel::class.java)
        retrofitViewModel.getTours()
        retrofitViewModel.myResponse.observe(requireActivity(), { response ->
            if (response.isSuccessful) {
                response.body()?.let { adapter.setData(it) }
                adapter.notifyDataSetChanged()
                if (tourPB != null) {
                    tourPB.visibility = View.INVISIBLE
                } else {
                    log("error")
                }
            } else {
                tourPB.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun categories(spinner: Spinner) {
        mService.getCategory().enqueue(object : Callback<MutableList<Category>> {
            override fun onResponse(call: Call<MutableList<Category>>, response: Response<MutableList<Category>>) {
                val categoriesArray: ArrayList<CategoryName> = ArrayList()
                categoriesArray.add(CategoryName("Все"))
                response.body()?.forEach {
                    categoriesArray.add(CategoryName(it.name))
                }
                spinnerCategory(requireContext(), categoriesArray, spinner)

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            override fun onFailure(call: Call<MutableList<Category>>, t: Throwable) {}
        })
    }

    private fun checkDate(textView: TextView) : String {
        return if (textView.text == null) {
            ""
        } else {
            textView.text.toString()
        }
    }



    private fun setupRecyclerview() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filters, menu)
        this.menu = menu
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.openFilters -> {
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu,null)
                val mBuilder = context?.let { it1 ->
                    AlertDialog.Builder(it1)
                            .setView(mDialogView)
                }
                val  mAlertDialog = mBuilder?.show()
                categories(mDialogView.categoryFilter)
                mDialogView.dateFrom.setOnClickListener {
                    DatePickerDialog(
                            requireContext(),
                            date(mDialogView.dateFrom),
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
                mDialogView.closeDialog.setOnClickListener {
                    mAlertDialog?.dismiss()
                }
                mDialogView.chooseBT.setOnClickListener{
                    getFilteredTours(category, checkDate(mDialogView.dateFrom))
                    mAlertDialog?.dismiss()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}