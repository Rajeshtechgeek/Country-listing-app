package com.sample.myapplication.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.Fade
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sample.myapplication.DetailsTransition
import com.sample.myapplication.R
import com.sample.myapplication.databinding.MainFragmentBinding
import com.sample.myapplication.utils.TAG
import com.sample.myapplication.utils.isNetConnected
import com.sample.myapplication.utils.toast

class MainFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    /**
     * Country list view adapter
     */
    private val listAdapter = CountryAdapter(ArrayList())

    private var isTextChanged: Boolean = false

    //will be used if permission not granted
    val defaultLat = "13.067439"
    val defaultLong = "80.237617"

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(TAG, "Permission granted")
                // Permission is granted. Continue the action or workflow in your app.
                getLastLocation()
            } else {
                Log.d(TAG, "Permission denied")
                requireContext().toast(getString(R.string.warning_location_permission))
                if (requireContext().isNetConnected()) {
                    viewModel.getWeather(defaultLat, defaultLong)
                }
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    if (requireContext().isNetConnected()) {
                        viewModel.getWeather(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                } else {
                    Log.d(TAG, "No Last location found")
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        initViews()
        loadData()
        setObservers()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkPermissions()
    }

    private fun loadData() {
        if (requireContext().isNetConnected()) {
            viewModel.getCountryList(false)
        } else {
            binding.shimmerViewContainer.startShimmer()
            requireContext().toast(getString(R.string.error_internet))
        }
    }

    private fun setObservers() {
        viewModel.countryList.observe(viewLifecycleOwner, {
            Log.d(TAG, "country list size: ${it.size}")
            binding.countryRecyclerView.visibility = View.VISIBLE
            binding.shimmerViewContainer.visibility = View.GONE
            listAdapter.updateListData(it)
            listAdapter.notifyDataSetChanged()
            binding.shimmerViewContainer.stopShimmer()
            // Start the transition once all views have been
            // measured and laid out
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })

        viewModel.weatherData.observe(viewLifecycleOwner, {
            Log.d(TAG, "Weather data observed")
            binding.textViewTitle.text = it.name
            binding.textViewTemperature.text =
                getString(R.string.temperature, it.main?.temp.toString())
        })
    }

    private fun initViews() {
        binding.shimmerViewContainer.startShimmer()
        binding.countryRecyclerView.apply {
            setItemViewCacheSize(10)
            adapter = listAdapter
        }
        binding.searchView.setOnQueryTextListener(this)
        listAdapter.onItemClick = { countryResponseItem, binding ->
            // Setup exit transition on first fragment
            sharedElementReturnTransition = DetailsTransition()
            exitTransition = Fade()
            val view = binding.imageView
            val extras = FragmentNavigatorExtras(view to view.transitionName)
            val action =
                MainFragmentDirections.actionMainFragmentToCountryDetailFragment(countryResponseItem)
            binding.root.findNavController().navigate(action, extras)
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isNotEmpty()) {
            isTextChanged = newText.isNotEmpty()
        }
        if (isTextChanged) {
            viewModel.filter(newText)
        }
        return true
    }

    private fun checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLastLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    requireContext().toast(getString(R.string.warning_location_permission))
                    if (requireContext().isNetConnected()) {
                        viewModel.getWeather(defaultLat, defaultLong)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
        }
    }

}