package com.example.intourist.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intourist.R
import com.example.intourist.adapter.AdapterFavourite
import com.example.intourist.room.RoomViewModel
import com.example.intourist.ui.SingleTourActivity
import com.example.intourist.utils.log
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.view.*

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private lateinit var viewModel: RoomViewModel
//    private lateinit var adapter: AdapterFavourite
    private val adapter by lazy { AdapterFavourite() }
    val isFragment: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        setupRecyclerview()

        adapter.setOnItemClickListener {
            val intent = Intent(requireContext(), SingleTourActivity::class.java)
            intent.putExtra("TourID", it.id)
            intent.putExtra("isFragment", isFragment)
            requireActivity().startActivity(intent)
        }

//        test.text = viewModel.testRR(1).toString()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val tour = adapter.differ.currentList[position]
                viewModel.deleteItems(tour)
                view.let {
                    Snackbar.make(it, "Удачно удалено", Snackbar.LENGTH_LONG).apply {
                        setAction("Вернуть") {
                            viewModel.addItems(tour)
                        }
                        show()
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(view.recyclerViewFav)
        }

        try {
            viewModel.readAllItems.observe(viewLifecycleOwner, { tours ->
                adapter.differ.submitList(tours)
            })
        } catch (e: Exception) {
            log("Error")
        }


    }

    private fun setupRecyclerview() {
        recyclerViewFav.adapter = adapter
        recyclerViewFav.layoutManager = LinearLayoutManager(requireContext())
        Log.v("Adapter", "AdapterSetting")
    }
}