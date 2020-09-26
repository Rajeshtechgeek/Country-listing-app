package com.sample.myapplication.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.myapplication.R
import com.sample.myapplication.api.country.CountryResponseItem
import com.sample.myapplication.databinding.ItemCountryBinding
import com.sample.myapplication.ui.main.CountryAdapter.CountryViewHolder
import com.sample.myapplication.utils.ImageUtils.loadSVGImage

class CountryAdapter(private val countries: MutableList<CountryResponseItem>) :
    RecyclerView.Adapter<CountryViewHolder>() {

    var onItemClick: ((CountryResponseItem, ItemCountryBinding) -> Unit)? = null

    fun updateListData(countries: List<CountryResponseItem>) {
        this.countries.clear()
        this.countries.addAll(countries)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemCountryBinding.inflate(inflater, parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class CountryViewHolder(val binding: ItemCountryBinding) : ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(countries[bindingAdapterPosition], binding)
            }
        }

        fun bind(countryResponseItem: CountryResponseItem) {
            ViewCompat.setTransitionName(binding.imageView, countryResponseItem.name)
            binding.textView.text = countryResponseItem.name
            loadSVGImage(
                binding.root.context, countryResponseItem.flag, binding.imageView,
                R.drawable.ic_splash
            )
        }
    }
}