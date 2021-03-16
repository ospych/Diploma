package com.example.intourist.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.intourist.R
import com.example.intourist.retrofit.*
import com.example.intourist.retrofit.model.TourDetail
import com.example.intourist.room.RoomViewModel
import com.example.intourist.room.data.TourRoom
import com.example.intourist.ui.favourites.FavouriteFragment
import com.example.intourist.ui.register.RegisterFragment
import com.example.intourist.utils.isInternetAvailable
import com.example.intourist.utils.isNetworkAvailable
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_single_tour.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleTourActivity : AppCompatActivity() {
    private lateinit var retrofitViewModel: RetrofitViewModel
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var mApi: TourApi

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_tour)
        progress_barSingleActivity.visibility = View.VISIBLE

        val repository = Repository()
        val homeViewModelFactory = RetrofitViewModelFactory(repository)

        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        retrofitViewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(RetrofitViewModel::class.java)
        mApi = RetrofitInstance.api


        val actionBar = supportActionBar

        val intent = intent
        val id = intent.getIntExtra("id", 1)
        val idFragment = intent.getIntExtra("TourID", 1)
        val isFragment = intent.getBooleanExtra("isFragment", false)


        if (isNetworkAvailable(this) || isInternetAvailable()) {
            getSingleTour()
            if (isFragment) {
                retrofitViewModel.getSingleTour(idFragment)
            } else {
                retrofitViewModel.getSingleTour(id)
            }
        } else {
            Toast.makeText(this, "Нет интернет соединения", Toast.LENGTH_SHORT).show()
            progress_barSingleActivity.visibility = View.GONE
            actionBar?.hide()
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


        actionBar!!.title = title
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val intent = intent
        val isFragment = intent.getBooleanExtra("isFragment", false)
        if (isFragment) {
            menuInflater.inflate(R.menu.delete_from_fav, menu)
        } else {
            menuInflater.inflate(R.menu.add_fav, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = intent
        val isFragment = intent.getBooleanExtra("isFragment", false)
        if (isFragment) {
            when (item.itemId) {
                R.id.delFav -> {
                    val id = intent.getIntExtra("id", 1)
                    deleteTour(id)
                    Toast.makeText(this@SingleTourActivity, "Удалено из Избранных", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            when (item.itemId) {
                R.id.addFav -> {
                    val id = intent.getIntExtra("id", 1)
                    CoroutineScope(Dispatchers.IO).launch {
                        saveToDb(id)
                    }
                    Toast.makeText(this@SingleTourActivity, "Добавлено в Избранное", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return false
    }

    private fun deleteTour(id: Int) {
        roomViewModel.deleteById(id)
        onBackPressed()
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
                    Toast.makeText(this@SingleTourActivity, "Ошибка с интернет соединением", Toast.LENGTH_SHORT).show()
                }

            })
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

    private fun getSingleTour() {
        retrofitViewModel.myResponseSingle.observe(this, { res ->
            if (res.isSuccessful) {
                titleSingle.text = res.body()?.title
                placesSingle.text = res.body()?.places
                priceSingle.text = res.body()?.price.toString()
                dateSingle.text = res.body()?.date
                descSingle.text = res.body()?.desc
                paymentSingle.text = res.body()?.payment
                progress_barSingleActivity.visibility = View.GONE

                paymentSingle.visibility = View.VISIBLE
                imageViewPrice.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                textSingle.visibility = View.VISIBLE
                cardView2.visibility = View.VISIBLE
                register.visibility = View.VISIBLE

                val sampleImages = res.body()?.shots
                val arrayImage : MutableList<String> = ArrayList()
                if (sampleImages != null) {
                    for (i in sampleImages){
                        val ss = i.image
                        arrayImage.add(ss)
                    }
                }

                val carouselView = carouselView as CarouselView
                val imageListener = ImageListener { pos, imageView ->
                    try {
                        Glide.with(applicationContext).load(arrayImage[pos])
                            .into(imageView)
                    } catch (e: Exception) {
                        Log.v("Error in Glide", "$e")
                    }
                }
                carouselView.setImageListener(imageListener)
                carouselView.pageCount = res.body()?.shots?.size!!
                arrayImage.clear()

                register.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("TourTitle", res.body()?.title)
                    val fragment: Fragment = RegisterFragment()
                    fragment.arguments = bundle
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.singleContainer, fragment)
                    transaction.commit()
                    singleMain.visibility = View.GONE
                }
            } else {
                progress_barSingleActivity.visibility = View.GONE
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}