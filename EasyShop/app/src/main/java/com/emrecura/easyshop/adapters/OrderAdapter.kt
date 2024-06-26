package com.emrecura.easyshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emrecura.easyshop.R
import com.emrecura.easyshop.models.CartProduct

class OrderAdapter(private val products: List<CartProduct>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.o_image)
        val title: TextView = itemView.findViewById(R.id.o_title)
        val discount: TextView = itemView.findViewById(R.id.o_discount)
        val price: TextView = itemView.findViewById(R.id.o_price)
        val quantity: TextView = itemView.findViewById(R.id.o_quantity)
        val discountedTotal: TextView = itemView.findViewById(R.id.o_discountedTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_row, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = products[position]
        holder.title.text = product.title
        holder.discount.text = "${product.discountPercentage}% Discount"
        holder.price.text = "${product.price} "
        holder.quantity.text = "Quantity: ${product.quantity}"
        holder.discountedTotal.text = "Discounted Total: ${product.discountedTotal} TL"
        Glide.with(holder.image.context).load(product.thumbnail).into(holder.image)
    }

    override fun getItemCount() = products.size
}
