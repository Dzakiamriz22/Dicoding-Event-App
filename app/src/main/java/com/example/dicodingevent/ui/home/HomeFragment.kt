package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.finished.FinishedEventAdapter
import com.example.dicodingevent.ui.upcoming.UpcomingAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels<HomeViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireActivity())

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)

                    homeViewModel.fetchSearchResult(searchView.text.toString())
                    homeViewModel.isLoading3.observe(viewLifecycleOwner) {
                        showLoading3(it)
                    }

                    binding.rvSearchResult.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager(requireActivity()).orientation))

                    false
                }
        }

        homeViewModel.listEvent.observe(viewLifecycleOwner) { eventList ->
            setEventData(eventList)
        }

        homeViewModel.listFinishedEvent.observe(viewLifecycleOwner) { finishedEventList ->
            setFinishedEventData(finishedEventList)
        }

        homeViewModel.searchResult.observe(viewLifecycleOwner) { eventList ->
            setSearchResultData(eventList)
            if (eventList?.isEmpty() == true) {
                binding.tvTextEmptyResult.text = getString(R.string.empty_search)
            } else {
                binding.tvTextResult.text = getString(R.string.search_result)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.isLoading2.observe(viewLifecycleOwner) {
            showLoading2(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showLoading2(isLoading2: Boolean) {
        if (isLoading2) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }

    private fun showLoading3(isLoading3: Boolean) {
        if (isLoading3) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    private fun setEventData(eventList: List<ListEventsItem?>?) {
        val adapter = UpcomingAdapter()
        adapter.submitList(eventList)
        binding.rvEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : UpcomingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEventItem(data)
            }
        })
    }

    private fun setFinishedEventData(eventList: List<ListEventsItem?>?) {
        val adapter = FinishedEventAdapter()
        adapter.submitList(eventList)
        binding.rvFinishedEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : FinishedEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEventItem(data)
            }
        })
    }

    private fun setSearchResultData(eventList: List<ListEventsItem?>?) {
        val adapter = SearchBarAdapter()
        adapter.submitList(eventList)
        binding.rvSearchResult.adapter = adapter

        adapter.setOnItemClickCallback(object : SearchBarAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSearchEventItem(data)
            }
        })
    }

    private fun showSelectedEventItem(event: ListEventsItem) {
        val toEventDetailActivity = HomeFragmentDirections.actionNavigationHomeToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }

    private fun showSearchEventItem(event: ListEventsItem) {
        val toEventDetailActivity = HomeFragmentDirections.actionNavigationHomeToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }

}