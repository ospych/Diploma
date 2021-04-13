package com.example.intourist.ui.favourites

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intourist.R
import com.example.intourist.adapter.AdapterFavourite
import com.example.intourist.room.RoomViewModel
import com.example.intourist.utils.log
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.view.*

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private lateinit var viewModel: RoomViewModel
    private val adapter by lazy { AdapterFavourite() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.show()

        viewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        setupRecyclerview()

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("favId", it.id)
                putString("favTitle", it.title)
            }
            findNavController().navigate(R.id.detailFragment, bundle)
        }

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