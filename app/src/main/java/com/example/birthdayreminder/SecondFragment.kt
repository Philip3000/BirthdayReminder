package com.example.birthdayreminder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.birthdayreminder.databinding.FragmentSecondBinding
import com.example.birthdayreminder.models.MyAdapter
import com.example.birthdayreminder.models.PersonViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val personViewModel: PersonViewModel by activityViewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Thread.sleep(2000)
        Log.d("OnPageLoad","${personViewModel.personsLiveData.value}")

        personViewModel.personsLiveData.observe(viewLifecycleOwner) {persons ->
            Log.d("APPLE", "observer $persons")
            if (persons == null) {
                Snackbar.make(binding.root, "Add friends to get started", Snackbar.LENGTH_LONG).show()
            }
            binding.recyclerView.visibility = if (persons == null) View.GONE else View.VISIBLE
            val adapter = MyAdapter(persons) { position ->
                val action = SecondFragmentDirections.actionSecondFragmentToFifthFragment(position)
                findNavController().navigate(action)
            }
            binding.recyclerView.adapter = adapter
        }
        binding.buttonSortAndFilter.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
        }
        binding.fab.setOnClickListener {
             findNavController().navigate(R.id.action_SecondFragment_to_FourthFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            personViewModel.reload()
            Snackbar.make(binding.root, "Refreshed", Snackbar.LENGTH_SHORT).show()
            binding.swiperefresh.isRefreshing = false
        }
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}