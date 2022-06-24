package com.assesment2.estuff.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.assesment2.estuff.R
import com.assesment2.estuff.databinding.FragmentAboutBinding
import com.assesment2.estuff.model.AboutApp
import com.assesment2.estuff.network.LoadingIndicator
import com.bumptech.glide.Glide

class AboutFragment : Fragment() {
    private val viewModel: AboutViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AboutViewModel::class.java)
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentAboutBinding.inflate(layoutInflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl =  "https://i.ibb.co/G93LsbV/gambar.png"
        Glide.with(requireContext())
            .load(Uri.parse(imageUrl))
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.imageView)

        viewModel.getCopyRightText().observe(viewLifecycleOwner){
            getAbout(it)
        }
        viewModel.getStatus().observe(viewLifecycleOwner){
            updateProgressBar(it)
        }

    }

    private fun getAbout(it: AboutApp?) {
        if (it != null){
            binding.copyrightText.text = it.copyright
        }
    }

    private fun updateProgressBar(status: LoadingIndicator) {
        when (status) {
            LoadingIndicator.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            LoadingIndicator.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            LoadingIndicator.FAILED -> {
                binding.progressBar.visibility = View.GONE
                //belum
            }
        }
    }
}