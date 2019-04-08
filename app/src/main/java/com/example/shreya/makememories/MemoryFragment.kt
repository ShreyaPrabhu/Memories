package com.example.shreya.makememories

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.shreya.makememories.databinding.FragmentMemoryBinding
import com.example.shreya.makememories.room.MemoryEntity
import com.example.shreya.makememories.room.MemoryViewModel

class MemoryFragment : Fragment() {

    private lateinit var binding: FragmentMemoryBinding
    private var memoryEntity: MemoryEntity? = null
    private lateinit var memoryViewModel: MemoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_memory, container, false)

        val memory_id = arguments?.getInt("memory_id")

        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel::class.java)

        if (memory_id != null) memoryEntity = memoryViewModel.getMemoryById(memory_id.toLong())

        binding.imageSelected.setImageBitmap(BitmapFactory.decodeFile(memoryEntity!!.imageReference))
        binding.imageCaption.text = memoryEntity!!.imageCaption
        binding.imageDescription.text = memoryEntity!!.imageDescription

        return binding.root
    }
}
