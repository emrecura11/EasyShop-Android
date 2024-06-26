package com.emrecura.easyshop.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

class HomeFragment : Fragment() {

    private lateinit var productRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        productRecyclerView = view.findViewById(R.id.productRecyclerView)
        productRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        arguments?.getString("categorySlug")?.let { categorySlug ->
            fetchProductsByCategory(categorySlug)
        } ?: run {
            fetchProducts()
        }

        return view
    }

    private fun fetchProducts() {
        val productService = ApiClient.getClient().create(ProductService::class.java)
        val call = productService.getProducts(30)

        call.enqueue(object : Callback<Products> {

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products
                    if (products != null) {
                        setAdapter(products)
                    }
                } else {
                    Toast.makeText(context, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Log.d("HomeFragment", "Request failed: ${t.message}")
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchProductsByCategory(categorySlug: String) {
        val productService = ApiClient.getClient().create(ProductService::class.java)
        val call = productService.getProductsByCategory(categorySlug)

        call.enqueue(object : Callback<Products> {

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products
                    if (products != null) {
                        setAdapter(products)
                    }
                } else {
                    Toast.makeText(context, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(products: List<Product>){
        productRecyclerView.adapter = ProductAdapter(products) { product ->
            val intent = Intent(activity, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
    }


}
