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
import com.example.birthdayreminder.databinding.FragmentFourthBinding
import com.example.birthdayreminder.models.Person
import com.example.birthdayreminder.models.PersonViewModel
import com.example.birthdayreminder.repository.PersonsRepository
import com.google.android.material.snackbar.Snackbar
import java.time.Year

class FourthFragment : Fragment() {

    private var _binding: FragmentFourthBinding? = null
    //private var person: Person? = null
    private val personViewModel: PersonViewModel by activityViewModels()

    var validYear: Int = 0
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validYear = Year.now().value

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createButton.setOnClickListener {
            if (binding.newYear.text.toString().length != 4 || binding.newYear.text.toString().toInt() > validYear || binding.newYear.text.toString().toInt() < 1900) {
                binding.newYear.error = "4 numbers! and valid year"
            } else if (binding.edittextName.text.toString().isEmpty()) {
                binding.newMonth.error = "Please enter a name"
            } else if (binding.newMonth.text.toString().length != 2 || binding.newMonth.text.toString().toInt() > 12) {
                binding.newMonth.error = "2 numbers! must be under 12"
            } else if (binding.newDay.text.toString().length != 2 || binding.newDay.text.toString().toInt() > 31) {
                binding.newDay.error = "2 numbers! must be under 31"
            } else {
                var remarksText = binding.remarkField.text.toString()
                var newYear = binding.newYear.text.toString()
                var newMonth = binding.newMonth.text.toString()
                var newDay = binding.newDay.text.toString()
                val person = Person(
                    id = 1,
                    name = binding.edittextName.text.toString(),
                    birthYear = newYear.toInt(),
                    birthMonth = newMonth.toInt(),
                    birthDayOfMonth = newDay.toInt(),
                    age = 33,
                    countdown = 20
                )
                person.remarks = remarksText

                person.countdown = person.calculateDays()
                person.age =
                    person.calculateAge(person.birthYear, person.birthMonth, person.birthDayOfMonth)

                Log.d("personcheck", "before Add")

                personViewModel.add(person)
                Log.d("personcheck", "$person")

                findNavController().navigate(R.id.action_FourthFragment_to_SecondFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}