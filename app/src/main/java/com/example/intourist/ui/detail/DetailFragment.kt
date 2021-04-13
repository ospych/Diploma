package com.example.intourist.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.intourist.R
import com.example.intourist.retrofit.*
import com.example.intourist.retrofit.model.TourDetail
import com.example.intourist.room.RoomViewModel
import com.example.intourist.room.data.TourRoom
import com.example.intourist.ui.register.RegisterFragment
import com.example.intourist.utils.isInternetAvailable
import com.example.intourist.utils.isNetworkAvailable
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var retrofitViewModel: RetrofitViewModel
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var mApi: TourApi
    private var menu: Menu? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.show()
        progress_barSingleActivity.visibility = View.VISIBLE

        val repository = Repository()
        val homeViewModelFactory = RetrofitViewModelFactory(repository)

        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        retrofitViewModel = ViewModelProvider(this, homeViewModelFactory).get(RetrofitViewModel::class.java)
        mApi = RetrofitInstance.api

        val id = arguments?.getInt("id")
        val title = arguments?.getString("title")
        val isTour = arguments?.getBoolean("isTour")
        val favId = arguments?.getInt("favId")
        val favTitle = arguments?.getString("favTitle")

        if (isNetworkAvailable(requireContext()) || isInternetAvailable()) {
            getSingleTour()
            if (isTour == true) {
                if (id != null) {
                    retrofitViewModel.getSingleTour(id)
                }
            } else {
                if (favId != null) {
                    retrofitViewModel.getSingleTour(favId)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
            progress_barSingleActivity.visibility = View.GONE
        }

        textSingle.setOnClickListener {
            if (paymentSingleLayout.visibility == View.GONE) {
                paymentSingleLayout.visibility = View.VISIBLE
                open.visibility = View.GONE
                close.visibility = View.VISIBLE
            } else {
                paymentSingleLayout.visibility = View.GONE
                open.visibility = View.VISIBLE
                close.visibility = View.GONE
            }
        }

        if (isTour == true) {
            toolbar?.title = title
        } else {
            toolbar?.title = favTitle
        }
    }

    private fun deleteTour(id: Int) {
        roomViewModel.deleteById(id)
    }

    private fun saveToDb(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        try {
            mApi.getTour(id).enqueue(object : Callback<TourDetail> {
                override fun onResponse(call: Call<TourDetail>, response: Response<TourDetail>) {
                    val idRoom = response.body()?.id!!.toInt()
                    val title = response.body()?.title.toString()
                    val price = response.body()?.price!!.toInt()
                    val places = response.body()?.places.toString()
                    val date = response.body()?.date.toString()
                    val image = response.body()?.image.toString()

                    val tours = TourRoom(idRoom, title, price, date, image, places)
                    roomViewModel.addItems(tours)
                }
                override fun onFailure(call: Call<TourDetail>, t: Throwable) {
                    Toast.makeText(requireContext(), "Ошибка с интернет соединением", Toast.LENGTH_SHORT).show()
                }

            })
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getSingleTour() {
        retrofitViewModel.myResponseSingle.observe(requireActivity(), { res ->
            if (res.isSuccessful) {
                singleMain.visibility = View.VISIBLE
                val dateStr = res.body()?.date
                var sdf = SimpleDateFormat("yyyy-MM-dd")
                val date: Date? = sdf.parse(dateStr!!)
                sdf = SimpleDateFormat("dd.MM.yyyy")

                titleSingle.text = res.body()?.title
                placesSingle.text = res.body()?.places
                priceSingle.text = res.body()?.price.toString()
                dateSingle.text = sdf.format(date!!)
                descSingle.text = res.body()?.desc
                paymentSingle.text = res.body()?.payment
                progress_barSingleActivity.visibility = View.GONE

                val sampleImages = res.body()?.shots
                val arrayImage : MutableList<String> = ArrayList()
                arrayImage.clear()
                sampleImages?.forEach {
                    arrayImage.add(it.image)
                }

                val carouselView = carouselView as CarouselView
                val imageListener = ImageListener { pos, imageView ->
                    try {
                        Glide.with(requireContext()).load(arrayImage[pos])
                            .into(imageView)
                    } catch (e: Exception) {
                        Log.v("Error in Glide", "$e")
                    }
                }
                carouselView.setImageListener(imageListener)
                carouselView.pageCount = res.body()?.shots?.size!!
//                arrayImage.clear()

                register.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("TourTitle", res.body()?.title)
                    }
                    findNavController().navigate(R.id.registerFragment, bundle)
                }
                progress_barSingleActivity.visibility = View.GONE
            } else {
                progress_barSingleActivity.visibility = View.GONE
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val isTour = arguments?.getBoolean("isTour")
        if (isTour == true) {
            inflater.inflate(R.menu.add_fav, menu)
            this.menu = menu
        } else {
            inflater.inflate(R.menu.delete_from_fav, menu)
            this.menu = menu
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isTour = arguments?.getBoolean("isTour")
        if (isTour == false) {
            when (item.itemId) {
                R.id.delFav -> {
                    val favId = arguments?.getInt("favId")
                    deleteTour(favId!!)
                    findNavController().navigate(R.id.navigation_favourites)
                    Toast.makeText(requireContext(), "Удалено из Избранных", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            when (item.itemId) {
                R.id.addFav -> {
                    val id = arguments?.getInt("id")
                    CoroutineScope(Dispatchers.IO).launch {
                        if (id != null) {
                            saveToDb(id)
                        }
                    }
                    Toast.makeText(requireContext(), "Добавлено в Избранное", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }

//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onResume() {
//        val id = arguments?.getInt("id")
//        val isTour = arguments?.getBoolean("isTour")
//        val favId = arguments?.getInt("favId")
//
//        if (isNetworkAvailable(requireContext()) || isInternetAvailable()) {
//            getSingleTour()
//            if (isTour == true) {
//                if (id != null) {
//                    retrofitViewModel.getSingleTour(id)
//                }
//            } else {
//                if (favId != null) {
//                    retrofitViewModel.getSingleTour(favId)
//                }
//            }
//        } else {
//            Toast.makeText(requireContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
//            progress_barSingleActivity.visibility = View.GONE
//        }
//        super.onResume()
//    }
}