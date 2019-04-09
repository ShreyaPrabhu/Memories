package com.example.shreya.makememories.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.shreya.makememories.R
import com.example.shreya.makememories.databinding.FragmentMemoryBinding
import com.example.shreya.makememories.room.MemoryEntity
import com.example.shreya.makememories.room.MemoryViewModel

class MemoryFragment : Fragment() {

    private lateinit var binding: FragmentMemoryBinding
    private lateinit var memoryEntity: MemoryEntity
    private lateinit var memoryViewModel: MemoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_memory, container, false)

        val memory_id = arguments?.getInt("memory_id")

        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel::class.java)

        if (memory_id != null) memoryEntity = memoryViewModel.getMemoryById(memory_id.toLong())

        binding.imageSelected.setImageBitmap(BitmapFactory.decodeFile(memoryEntity.imageReference))
        binding.imageCaption.text = memoryEntity.imageCaption
        binding.imageDescription.text = memoryEntity.imageDescription

        binding.fabShare.setOnClickListener{view: View->
            shareMemory(memoryEntity);
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.delete) {
            // Delete image here
            val memoryViewModel: MemoryViewModel
            memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel::class.java)
            memoryViewModel.deleteMemoryById(memoryEntity.id.toLong())
            Thread.sleep(100)
            fragmentManager!!.popBackStack()
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun shareMemory(memoryEntity: MemoryEntity){
        val pictureUri = Uri.parse(memoryEntity.imageReference.toString())
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, memoryEntity.imageCaption + " - " + memoryEntity.imageDescription)
        shareIntent.type = "image/png";
        startActivity(Intent.createChooser(shareIntent,"Share with"));
    }

}
