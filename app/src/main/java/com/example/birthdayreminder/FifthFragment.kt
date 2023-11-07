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
import com.example.birthdayreminder.databinding.FragmentFifthBinding
import com.example.birthdayreminder.databinding.FragmentFourthBinding
import com.example.birthdayreminder.models.Person
import com.example.birthdayreminder.models.PersonViewModel
import com.example.birthdayreminder.repository.PersonsRepository
import com.google.android.material.snackbar.Snackbar
import java.time.Year


class FifthFragment : Fragment() {
    private var _binding: FragmentFifthBinding? = null
    private var person: Person? = null
    private val personViewModel: PersonViewModel by activityViewModels()
    private var personID: Int = 0

    var validYear: Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFifthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validYear = Year.now().value

        val bundle = requireArguments()
        val fifthFragmentArgs: FifthFragmentArgs = FifthFragmentArgs.fromBundle(bundle)
        val position = fifthFragmentArgs.position
        personViewModel.personsLiveData.observe(viewLifecycleOwner) { persons ->
            if (persons != null) {
                person = persons[position]
                personID = person!!.id
                person?.let { safePerson ->
                    displayPersonDetails(safePerson)
                }
            } else {
                Log.d("person", "Doesn't exist at position: $position")
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.updateButton.setOnClickListener {
            if (binding.newYear.text.toString().length != 4 || binding.newYear.text.toString()
                    .toInt() > validYear || binding.newYear.text.toString().toInt() < 1900
            ) {
                binding.newYear.error = "4 numbers! and valid year"
            } else if (binding.edittextName.text.toString().isEmpty()) {
                binding.newMonth.error = "Please enter a name"
            } else if (binding.newMonth.text.toString().length != 2 || binding.newMonth.text.toString()
                    .toInt() > 12
            ) {
                binding.newMonth.error = "2 numbers! must be under 12"
            } else if (binding.newDay.text.toString().length != 2 || binding.newDay.text.toString()
                    .toInt() > 31
            ) {
                binding.newDay.error = "2 numbers! must be under 31"
            } else {
                var remarks = binding.remarks.text.toString()
                var newYear = binding.newYear.text.toString()
                var newMonth = binding.newMonth.text.toString()
                var newDay = binding.newDay.text.toString()
                val person = Person(
                    id = personID,
                    name = binding.edittextName.text.toString(),
                    birthYear = newYear.toInt(),
                    birthMonth = newMonth.toInt(),
                    birthDayOfMonth = newDay.toInt(),
                    age = 0,
                    countdown = 0
                )
                person.remarks = remarks
                person.countdown = person.calculateDays()
                person.age =
                    person.calculateAge(person.birthYear, person.birthMonth, person.birthDayOfMonth)
                try {
                    personViewModel.update(person)
                    Log.d("Banana", "${personViewModel.personsLiveData.value}")
                }
                catch (exception: Exception) {
                    Log.d("Water", "$exception")
                }
                Log.d("WaterCup", "$person")

            }
            findNavController().popBackStack()
        }
        binding.deleteButton.setOnClickListener {
            if (person == null) {
                Log.d("person", "person is null")
            } else {
                Log.d("test", "${person!!.id}")
                personViewModel.delete(person!!.id)

                Thread.sleep(1000)
                findNavController().popBackStack()
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayPersonDetails(person: Person) {
        binding.edittextName.setText(person.name)
        binding.newYear.setText(person.birthYear.toString())
        binding.newMonth.setText(person.birthMonth.toString())
        binding.newDay.setText(person.birthDayOfMonth.toString())
        binding.remarks.setText(person.remarks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}