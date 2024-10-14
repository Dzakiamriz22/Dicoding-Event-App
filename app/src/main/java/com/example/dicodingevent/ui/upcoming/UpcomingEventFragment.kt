package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentUpcomingEventBinding

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: UpcomingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity())

        eventViewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            setEventData(eventList)
        }

        eventViewModel.isLoadingData.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        eventViewModel.isDataLoadedSuccessfully.observe(viewLifecycleOwner) { success ->
            if (!success) {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    private fun showSelectedEventItem(event: ListEventsItem) {
        val toEventDetailActivity = UpcomingEventFragmentDirections.actionNavigationActiveEventToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }
}
