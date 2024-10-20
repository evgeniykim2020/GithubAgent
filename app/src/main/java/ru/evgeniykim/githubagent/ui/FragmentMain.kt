package ru.evgeniykim.githubagent.ui

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.evgeniykim.githubagent.R
import ru.evgeniykim.githubagent.data.Repos
import ru.evgeniykim.githubagent.data.ReposDB
import ru.evgeniykim.githubagent.databinding.FragmentMainBinding
import ru.evgeniykim.githubagent.network.api.State
import ru.evgeniykim.githubagent.ui.adapter.AdapterListener
import ru.evgeniykim.githubagent.ui.adapter.MyAdapter
import ru.evgeniykim.githubagent.viewmodel.MyViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class FragmentMain: Fragment(R.layout.fragment_main), AdapterListener {
    private lateinit var binding: FragmentMainBinding
    private var navController: NavController? = null
    private val myViewModel by viewModels<MyViewModel>()
    private var adapter: MyAdapter? = null
    private var repoDB: ReposDB? = null
    private var _owner = ""
    private var _repoName = ""
    private var _repoUrl = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        navController = findNavController()

        setupDB()
        setupAdapter()
        setupSearchView()
        setupViewModel()
    }

    private fun setupDB() {
        repoDB = ReposDB.getDatabase(requireActivity())
    }

    private fun setupAdapter() {
        adapter = MyAdapter(this)
        binding.listRepos.adapter = adapter
    }

    private fun setupViewModel() {
        myViewModel.gainedData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is State.Loading -> { showProgress() }
                is State.Success -> {
                    hideProgress()
                    adapter?.differ?.submitList(it.data)
                }
                is State.Error -> {
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupSearchView() {
        binding.searchFrame.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    myViewModel.getRepos(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
    }

    private fun showProgress() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgress() {
        binding.progressBar.isGone = true
    }

    private fun startPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                downloadFile()
            } else {
                Toast.makeText(requireContext(), "Для загрузки нужно разрешения", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
        }

    private fun onDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadFile()
        }else{
            startPermissionRequest()
        }
    }

    private fun downloadFile(){
        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(_repoUrl))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(_repoName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,_repoName)
        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)
    }

    override fun openRepo(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun loadRepo(owner: String, repoName: String, url: String) {

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                _owner = owner
                _repoName = repoName
                _repoUrl = "https://api.github.com/repos/$owner/$repoName/zipball/master"
            }
            if (!_repoName.isNullOrEmpty()) {
                withContext(Dispatchers.IO) {
                    onDownload()
                }
                repoDB?.repoDao()?.insert(Repos(userName = _owner, repoName = _repoName))
            }
            println("Link $_repoUrl")
        }
        println("Download clicked")
    }

}
