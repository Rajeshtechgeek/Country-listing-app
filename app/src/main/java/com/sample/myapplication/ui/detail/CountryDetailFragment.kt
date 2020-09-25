package com.sample.myapplication.ui.detail

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.Fade
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sample.myapplication.DetailsTransition
import com.sample.myapplication.R
import com.sample.myapplication.api.response.CountryResponseItem
import com.sample.myapplication.databinding.CountryDetailFragmentBinding
import com.sample.myapplication.utils.ImageUtils

class CountryDetailFragment : Fragment() {

    private var data: CountryResponseItem? = null
    private var _binding: CountryDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: CountryDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup enter transition on second fragment
        sharedElementEnterTransition = DetailsTransition()
        enterTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CountryDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        data = args.CountryDetail
        data?.let {
            loadImage(it)
            binding.toolbarLayout.title = it.name
            binding.textView1.text = String.format(getString(R.string.native_name, it.nativeName))
            binding.textView2.text = String.format(getString(R.string.capital, it.capital))
            binding.textView3.text = String.format(getString(R.string.area, it.area.toString()))
            binding.textView4.text = String.format(getString(R.string.numeric_code, it.numericCode))
            binding.textView5.text = String.format(getString(R.string.population, it.population))
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun loadImage(it: CountryResponseItem) {
        ViewCompat.setTransitionName(binding.detailImage, data?.name)
        context?.let { it1 ->
            ImageUtils.loadSVGImage(
                it1,
                it.flag,
                binding.detailImage,
                R.drawable.drawable_splash, object : RequestListener<PictureDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<PictureDrawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: PictureDrawable?,
                        model: Any?,
                        target: Target<PictureDrawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        startPostponedEnterTransition()
                        return false
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}