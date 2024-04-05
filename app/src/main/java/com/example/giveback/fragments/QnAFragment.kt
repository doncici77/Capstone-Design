package com.example.giveback.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.giveback.R
import com.example.giveback.databinding.FragmentQnaBinding

// QnA 페이지
class QnAFragment : Fragment() {

    private lateinit var binding: FragmentQnaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qna, container, false)

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_qnaFragment_to_tipFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_qnaFragment_to_homeFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_qnaFragment_to_talkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_qnaFragment_to_storeFragment)
        }

        return binding.root
    }
}