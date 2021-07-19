package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_gmail.sharedPref.SharedPreferences
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentHistoryBinding
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.presentation.adapters.QueriesHistoryRecyclerAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.sharedPref.QUERY
import com.example.gorbachev_wallpapers.viewmodels.QueriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment(R.layout.fragment_history),
	QueriesHistoryRecyclerAdapter.OnItemClick,
	QueriesHistoryRecyclerAdapter.OnLikeClick {
	
	private val viewModel by viewModels<QueriesViewModel>()
	
	private var adapter = QueriesHistoryRecyclerAdapter(this, this)
	
	private var _binding: FragmentHistoryBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var SP: SharedPreferences
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		val recyclerView: RecyclerView = requireView().findViewById(R.id.queriesHistoryContainer)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = adapter
		
		SP = SharedPreferences(requireContext())
		
		_binding = FragmentHistoryBinding.bind(view)
		showQueries()
	}
	
	private fun showQueries() {
		viewModel.allData.observe(viewLifecycleOwner, {
			adapter.submitList(it)
		})
	}
	
	override fun onItemClick(query: Queries) {
		SP.setPref(QUERY, query.query)
		val action = HistoryFragmentDirections.actionHistoryFrToSearchFr()
		findNavController().navigate(action)
	}
	
	override fun onLikeClick(query: Queries) {
	
	}
	
}