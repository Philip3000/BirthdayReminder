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
import com.example.birthdayreminder.databinding.FragmentThirdBinding
import com.example.birthdayreminder.models.PersonViewModel
import com.example.birthdayreminder.repository.PersonsRepository

class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    val personViewModel: PersonViewModel by activityViewModels()
    var upOrDown: String = "Asc"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personViewModel.reload()

        binding.buttonAscending.setOnClickListener {
            upOrDown = "Asc"
        }
        binding.buttonDescending.setOnClickListener {
            upOrDown = "Desc"
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.sortName.setOnClickListener {
            Log.d("Computer", "${personViewModel.personsLiveData.value}")
            if (upOrDown == "Desc") {
                personViewModel.sortByNameDescending()
            } else {
                personViewModel.sortByName()
            }
        }
        binding.sortAge.setOnClickListener {
            Log.d("Computer", "${personViewModel.personsLiveData.value}")
            if (upOrDown == "Desc") {
                personViewModel.sortByAgeDescending()
            } else {
                personViewModel.sortByAge()
            }
        }
        binding.sortBirth.setOnClickListener {
            if (upOrDown == "Desc") {
                personViewModel.sortByCountdownDescending()
            } else {
            personViewModel.sortByCountdown()
            }
        }
        binding.goButton.setOnClickListener {
            val filter = binding.filterString.text.toString()
            if (filter.isNotEmpty()) {
                personViewModel.filterByName(filter)
            }
            val filterAge = binding.filterAge.text.toString()
            if (filterAge.isNotEmpty()) {
                personViewModel.filterByAge(filterAge.toInt())
            }
            findNavController().popBackStack()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}