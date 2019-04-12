package com.example.shreya.makememories.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shreya.makememories.R
import com.example.shreya.makememories.adapters.RecyclerAdapter
import com.example.shreya.makememories.databinding.FragmentMainBinding
import com.example.shreya.makememories.room.MemoryEntity
import com.example.shreya.makememories.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val REQUEST_CODE = 101

    private lateinit var mainViewModel: MainViewModel
    private val adapter = RecyclerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setupPermissions()

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main, container, false)

        val recyclerView: RecyclerView = binding.recyclerView

        binding.fab.setOnClickListener{view: View->
            view.findNavController().navigate(R.id.action_mainFragment2_to_addMemoryFragment2)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        Timber.i("%s %s", "FragmentSize: ", mainViewModel.getSize())
        mainViewModel.getAllMemos().observe(this,
                Observer<List<MemoryEntity>> { t -> adapter.setMemos(t!!) })
        adapter.setOnItemClickListener { it ->
            val bundle = Bundle()
            bundle.putInt("memory_id", it.id)
            this.findNavController().navigate(R.id.action_mainFragment2_to_memoryFragment, bundle)
        }

        MainViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                val snackbar = Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.memory_deleted),
                        Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.undo, View.OnClickListener() {
                    val memoryEntity = MainViewModel.getMemoryEntityToUndo
                    mainViewModel.insert(memoryEntity)
                })
                snackbar.show()
                MainViewModel.doneShowingSnackBarEvent()
            }
        })

        return binding.root
    }

    private fun setupPermissions() {
        val permissionCamera = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val permissionReadStorage = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWriteStorage = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCamera != PackageManager.PERMISSION_GRANTED || permissionReadStorage != PackageManager.PERMISSION_GRANTED || permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Permission has been denied by user")
                } else {
                    Timber.i( "Permission has been granted by user")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
        || super.onOptionsItemSelected(item)
    }
}
