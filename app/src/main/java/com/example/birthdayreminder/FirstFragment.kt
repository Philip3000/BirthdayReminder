package com.example.birthdayreminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.birthdayreminder.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth



/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser

        if (currentUser != null) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editEmail.error = "No email"
                return@setOnClickListener
            }
            val password = binding.editPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editPassword.error = "No password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        binding.responseMessage.text = "Successfull operations"
                        val user = auth.currentUser
                        Thread.sleep(1000)
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    } else {
                        binding.responseMessage.text = "Authentication failed:" + task.exception?.message
                    }
            }
        }
        binding.registerButton.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editEmail.error = "No email"
                return@setOnClickListener
            }
            val password = binding.editPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editPassword.error = "No password"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        binding.responseMessage.text = "Sign up successfull"
                        val user = auth.currentUser
                        Thread.sleep(2000)
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    } else {
                        binding.responseMessage.text = "Registration failed: " + task.exception?.message
                    }
                }
        }
        binding.forgotPasswordButton.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editEmail.error = "No email"
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(requireActivity()) {task ->
                    if (task.isSuccessful) {
                        binding.responseMessage.text = "Email Sent"
                    }
                    else {
                        binding.responseMessage.text = "Something went wrong: " + task.exception?.message
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}