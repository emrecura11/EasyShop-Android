package com.emrecura.easyshop.screens.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.emrecura.easyshop.R
import com.emrecura.easyshop.adapters.ProductAdapter
import com.emrecura.easyshop.configs.RoomDB
import com.emrecura.easyshop.models.Product
import com.emrecura.easyshop.screens.activities.ProductDetailsActivity


class FavoriteProductFragment : Fragment() {

    private lateinit var favProductRecyclerView: RecyclerView
    private lateinit var db: RoomDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favProductRecyclerView = view.findViewById(R.id.favProductRecyclerView)
        favProductRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        db = Room.databaseBuilder(requireContext(), RoomDB::class.java, "favorite_products")
            .allowMainThreadQueries().build()

        fetchFavoriteProducts()

        return view
    }

    private fun fetchFavoriteProducts() {
        val productDao = db.productDao()
        val products = productDao.getAllFavorites()
        setAdapter(products)

    }

    private fun setAdapter(products : List<Product>) {
        favProductRecyclerView.adapter = ProductAdapter(products) { product ->
            val intent = Intent(activity, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
    }

}