package org.app.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.app.common.Constants
import org.app.demo.android.R
import org.app.demo.android.databinding.ActivityDetailBinding
import org.app.extensions.getStringResources
import org.app.models.ProductImage
import org.app.ui.detail.adapter.ProductImageAdapter

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail) }
    private val viewModelDetail by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }
    private val productSKU by lazy { intent.getStringExtra(Constants.PRODUCT_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        super.onCreate(savedInstanceState)
        getProductDetail()
    }

    private fun getProductDetail() {
        viewModelDetail.getProductDetailById(this, productSKU, binding)
        viewModelDetail.productDetail.observe(this, Observer {
            binding.productDetailViewModel = it
            binding.productImagePage.adapter =
                ProductImageAdapter(it.images as ArrayList<ProductImage>)

                TabLayoutMediator(binding.tabProductImagePage as TabLayout, binding.productImagePage,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    }).attach()
            var price = 0.0f
            it.price?.supplierSalePrice?.let {
                price = it.toFloat()
            }

            binding.footerView.totalPrice = ( price * viewModelDetail.numberPay.value!!.toFloat()).toString()
        })

        binding.viewpager.adapter = InfoPagerAdapter(this)
        TabLayoutMediator(binding.tabs as TabLayout, binding.viewpager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                val tabItems = MessagesTabItem.values()
                tab.text = getStringResources(tabItems[position].titleRes)
            }).attach()

        // Listener scroll appBarLayout
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener(

            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarTitle = viewModelDetail.productDetail.value?.name
                    binding.toolbarPrice = viewModelDetail.productDetail.value?.price?.supplierSalePrice
                } else {
                    binding.toolbarTitle = " "
                    binding.toolbarPrice = " "
                }
            })

        binding.footerView.viewModel = viewModelDetail
    }

    inner class InfoPagerAdapter(@NonNull fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        private val tabItems = MessagesTabItem.values()
        @NonNull
        override fun createFragment(position: Int): Fragment {
            return tabItems[position].fragment
        }

        override fun getItemCount(): Int {
            return tabItems.size
        }
    }

    enum class MessagesTabItem(@StringRes val titleRes: Int, val fragment: Fragment) {
        DESCRIPTION(R.string.tab_product_description, ProductInfoFragment.newInstance(0)),
        INFO(R.string.tab_product_info, ProductInfoFragment.newInstance(1)),
        PRICE(R.string.tab_product_price, ProductInfoFragment.newInstance(2));
    }

    companion object {
        fun intent(context: Context?) = Intent(context, DetailActivity::class.java)
    }
}
