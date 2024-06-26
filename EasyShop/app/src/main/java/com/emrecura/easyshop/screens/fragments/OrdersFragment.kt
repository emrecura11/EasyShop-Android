package com.emrecura.easyshop.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrecura.easyshop.R
import com.emrecura.easyshop.adapters.OrderAdapter
import com.emrecura.easyshop.configs.ApiClient
import com.emrecura.easyshop.models.Cart
import com.emrecura.easyshop.models.CartProduct
import com.emrecura.easyshop.services.CartService
import com.emrecura.easyshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: OrderAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var totalAmountTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_orders, container, false)

        sessionManager = SessionManager(requireContext())
        cartRecyclerView = view.findViewById(R.id.orderRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        totalAmountTextView = view.findViewById(R.id.totalAmountTextView)

        fetchCartData()

        return view
    }

    private fun fetchCartData() {
        val token = sessionManager.fetchAuthToken()
        val userId = sessionManager.fetchUserId()

        if (token != null) {
            val cartService = ApiClient.getClient().create(CartService::class.java)
            val call = cartService.getUserCarts(userId = userId, token = "Bearer $token")

            call.enqueue(object : Callback<Cart> {
                override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            setupRecyclerView(it.carts[0].products)
                            updateTotalAmount(it.carts[0].total)
                        }
                    }
                }

                override fun onFailure(call: Call<Cart>, t: Throwable) {
                    Toast.makeText(context, "Failed to load orders", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setupRecyclerView(cartList: List<CartProduct>) {
        cartAdapter = OrderAdapter(cartList)
        cartRecyclerView.adapter = cartAdapter
    }

    private fun updateTotalAmount(total: Double) {
        totalAmountTextView.text = "Total: $$total"
    }
}
