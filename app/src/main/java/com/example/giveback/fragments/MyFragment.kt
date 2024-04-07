package com.example.giveback.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.giveback.R
import com.example.giveback.auth.LoginActivity
import com.example.giveback.databinding.FragmentMyBinding
import com.google.firebase.auth.FirebaseAuth

// 사용자 페이지
class MyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentMyBinding

    private lateinit var auth: FirebaseAuth

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)

        auth = FirebaseAuth.getInstance()

        binding.email.setText("사용자: ${email}")



        // 로그아웃 버튼을 클릭했을 때 로그아웃이 진행되고 로그인 페이지로 이동
        binding.logoutBtn.setOnClickListener {
            auth.signOut()

            // LoginActivity로 화면 이동
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // SIGNOUT 버튼을 클릭했을 때 회원탈퇴가 진행되고 로그인 페이지로 이동
        binding.signoutBtn.setOnClickListener {

            auth.currentUser?.delete()

            // LoginActivity로 화면 이동
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            
        }


        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myFragment_to_talkFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myFragment_to_bookmarkFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_getFragment_to_storeFragment)
        }

        return binding.root
    }
}