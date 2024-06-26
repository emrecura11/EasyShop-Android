package com.emrecura.easyshop.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrecura.easyshop.R
import com.emrecura.easyshop.adapters.ProductAdapter
import com.emrecura.easyshop.configs.ApiClient
import com.emrecura.easyshop.models.Product
import com.emrecura.easyshop.models.Products
import com.emrecura.easyshop.screens.activities.ProductDetailsActivity
import com.emrecura.easyshop.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchText: EditText
    private lateinit var searchButton: Button
    private lateinit var termTextView: TextView
    private lateinit var logoImageView : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchRecyclerView = view.findViewById(R.id.searchRecyclerView)
        searchText = view.findViewById(R.id.searchText)
        searchButton = view.findViewById(R.id.search_button)
        termTextView = view.findViewById(R.id.term_text_view)
        logoImageView = view.findViewById(R.id.search_logo_image)

        searchRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        searchButton.setOnClickListener {
            val query = searchText.text.toString()
            if (query.isNotEmpty()) {
                searchProducts(query)
            } else {
                Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun searchProducts(query: String) {
        val productService = ApiClient.getClient().create(ProductService::class.java)
        val call = productService.searchProducts(query)

        call.enqueue(object : Callback<Products> {

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products
                    if (products != null) {
                        setAdapter(products)
                        termTextView.visibility = View.INVISIBLE
                        logoImageView.visibility = View.INVISIBLE

                    } else {
                        Toast.makeText(context, "No products found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to search products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setAdapter(products: List<Product>){
        searchRecyclerView.adapter = ProductAdapter(products) { product ->
            val intent = Intent(activity, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }

    }
}
