package com.emrecura.easyshop.screens.activities

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.emrecura.easyshop.R
import com.emrecura.easyshop.configs.ApiClient
import com.emrecura.easyshop.configs.RoomDB
import com.emrecura.easyshop.databinding.ActivityProductDetailsBinding
import com.emrecura.easyshop.models.AddCartRequest
import com.emrecura.easyshop.models.Cart
import com.emrecura.easyshop.models.Product
import com.emrecura.easyshop.models.ProductQuantity
import com.emrecura.easyshop.services.CartService
import com.emrecura.easyshop.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var product: Product
    private lateinit var toolbar: Toolbar
    private lateinit var seassionManager: SessionManager
    private lateinit var db: RoomDB
    private lateinit var favoriteMenuItem: MenuItem
    private lateinit var cartService: CartService
    private var quantity = 1
    private var total = 0.0
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        seassionManager = SessionManager(this)
        product = intent.getSerializableExtra("product") as Product

        db = Room.databaseBuilder(this, RoomDB::class.java, "favorite_products")
            .allowMainThreadQueries().build()
        cartService = ApiClient.getClient().create(CartService::class.java)
        toolbar = findViewById(R.id.detail_toolbar)

        total = product.price.toDouble()
        setToolbar()
        setView()
        productQuantity()

        binding.detailToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_favorite -> {
                    toggleFavoriteStatus()
                    true
                }
                else -> false
            }
        }

        binding.addCartButton.setOnClickListener {
            addCart()
        }

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        binding.productDiscount.startAnimation(blinkAnimation)

    }

    private fun addCart() {
        val userId = seassionManager.fetchUserId()
        val addCartRequest = AddCartRequest(
            userId = userId,
            products = listOf(
                ProductQuantity(id = product.id.toInt(), quantity = quantity),
            )
        )

        cartService.addCart(addCartRequest).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    val msg = "${product.title} added to cart"
                    toastMsg(msg)
                } else {
                    val msg = "Something went wrong"
                    toastMsg(msg)
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                // Handle request failure
                println("Error: ${t.message}")
            }
        })
    }

    private fun productQuantity() {

        binding.increaseQuantity.setOnClickListener {
            quantity++
            binding.quantityText.text = quantity.toString()
            total = quantity*product.price.toDouble()
            binding.addCartText.text = "Add Cart : $$total"
        }

        binding.decreaseQuantitity.setOnClickListener {
            if (quantity > 1){
                quantity--
                binding.quantityText.text = quantity.toString()
                total = quantity*product.price.toDouble()
                binding.addCartText.text = "Add Cart : $$total"
            }else{
                val msg = "Quantity must be at least 1"
                toastMsg(msg)
            }
        }


    }

    private fun setView() {


        Glide.with(this).load(product.images.get(0)).into(binding.dProductImage)
        binding.productTitle.text = product.title

        binding.ratingBar.rating = product.rating.toFloat()
        binding.ratingBar.progressDrawable.setColorFilter(
            resources.getColor(R.color.orange, theme),
            PorterDuff.Mode.SRC_ATOP
        )
        binding.productDiscount.text = "${product.discountPercentage}% Discount"
        binding.productBrand.text = product.brand
        binding.productPrice.text = "${product.price}$"
        binding.productDescripton.text = product.description
        binding.addCartText.text = "Add Cart : $$total"


    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        binding.detailToolbar.title = product.title
        toolbar.setBackgroundColor(resources.getColor(R.color.orange))
        // Geri tuşunu etkinleştirme
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        // Geri tuşu tıklama olayını dinleme
        toolbar.setNavigationOnClickListener { view: View? -> onBackPressed() }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_toolbar, menu)
        favoriteMenuItem = menu!!.findItem(R.id.add_favorite)
        updateFavoriteIcon()
        return true
    }

    private fun isFavorite() : Boolean{
        val productDao = db.productDao()
        val existingProduct = productDao.getAllFavorites().find { it.id == product.id }
        return if (existingProduct == null) {
            false
        } else {
            true
        }
    }

    private fun toggleFavoriteStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            val productDao = db.productDao()
            val existingProduct = isFavorite()

            if (!existingProduct) {
                // Ürün favorilerde yoksa ekle
               val status = productDao.insert(product)
                if (status > 0){
                    withContext(Dispatchers.Main) {
                        val msg = "${product.title} added to favorites"
                        toastMsg(msg)
                    }
                }
            } else {

                // Ürün favorilerde varsa çıkar
                val status = productDao.delete(product)
                if (status > 0){
                    withContext(Dispatchers.Main) {
                        val msg = "${product.title} removed from favorites"
                        toastMsg(msg)
                    }
                }

            }
            withContext(Dispatchers.Main) {
                updateFavoriteIcon()
            }
        }
    }
    private fun updateFavoriteIcon() {
        if (isFavorite()) {
            favoriteMenuItem.icon?.setColorFilter(
                ContextCompat.getColor(this, R.color.red),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            favoriteMenuItem.icon?.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_IN
            )

        }
    }

    private fun toastMsg(msg: String) {
        Toast.makeText(this@ProductDetailsActivity, msg, Toast.LENGTH_SHORT).show()

    }
}