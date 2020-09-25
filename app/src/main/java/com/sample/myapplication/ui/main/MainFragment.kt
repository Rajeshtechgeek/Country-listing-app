package com.sample.myapplication.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.sample.myapplication.DetailsTransition
import com.sample.myapplication.databinding.MainFragmentBinding
import com.sample.myapplication.utils.TAG

class MainFragment : Fragment(), SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    /**
     * Country list view adapter
     */
    private val listAdapter = CountryAdapter(ArrayList())

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
        viewModel.getCountryList(false)
        setObservers()
    }

    private fun setObservers() {
        viewModel.countryList.observe(viewLifecycleOwner, {
            Log.d(TAG, "country list size: ${it.size}")
            listAdapter.updateListData(it)
            listAdapter.notifyDataSetChanged()
            binding.progressLoading.hide()
            // Start the transition once all views have been
            // measured and laid out
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })
    }

    private fun initViews() {
        binding.progressLoading.show()
        binding.countryRecyclerView.apply {
            setItemViewCacheSize(10)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.filter(newText)
        return true
    }

}