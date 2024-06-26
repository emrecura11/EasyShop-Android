package com.emrecura.easyshop.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrecura.easyshop.R
import com.emrecura.easyshop.adapters.CategoryAdapter
import com.emrecura.easyshop.configs.ApiClient
import com.emrecura.easyshop.models.Category
import com.emrecura.easyshop.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesFragment : Fragment() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var productService: ProductService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productService = ApiClient.getClient().create(ProductService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        categoriesRecyclerView = view.findViewById(R.id.recycler_view_categories)
        categoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        loadCategories()
        return view
    }

    private fun loadCategories() {
        productService.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()
                    categoriesRecyclerView.adapter = CategoryAdapter(categories) { category ->

                        // Kategoriye tıklandığında HomeFragment'e geçiş yap
                        val fragment = HomeFragment()
                        val bundle = Bundle()
                        bundle.putString("categorySlug", category.slug)
                        fragment.arguments = bundle

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
