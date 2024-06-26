package com.emrecura.easyshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emrecura.easyshop.R
import com.emrecura.easyshop.models.Product

class ProductAdapter(private val productList: List<Product> , private val onItemClicked: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pImage: ImageView = itemView.findViewById(R.id.p_image)
        val pTitle: TextView = itemView.findViewById(R.id.p_title)
        val pPrice: TextView = itemView.findViewById(R.id.p_price)
        val pDiscount: TextView = itemView.findViewById(R.id.p_discount)
        val pBrand: TextView = itemView.findViewById(R.id.p_brand)
        val pDescription: TextView = itemView.findViewById(R.id.p_description)

        fun bind(product: Product, onItemClicked: (Product) -> Unit) {
            itemView.setOnClickListener {
                onItemClicked(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_row, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        var productDescr = ""
        holder.bind(productList[position], onItemClicked)


        productDescr = if (product.description.length > 45){
            "${product.description.substring(0,45)}..."
        }else{
            product.description
        }

        Glide.with(holder.itemView.context).load(product.images[0]).into(holder.pImage)
        holder.pTitle.text = product.title
        holder.pPrice.text = "${product.price}$"
        holder.pDiscount.text = "${product.discountPercentage}% Discount!"
        holder.pBrand.text = product.brand
        holder.pDescription.text = productDescr
    }

    override fun getItemCount() = productList.size
}
