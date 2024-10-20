package ru.evgeniykim.githubagent.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.evgeniykim.githubagent.R
import ru.evgeniykim.githubagent.data.ReposDB
import ru.evgeniykim.githubagent.databinding.FragmentArchiveBinding
import ru.evgeniykim.githubagent.ui.adapter.ArchiveAdapter

class FragmentArchive: Fragment(R.layout.fragment_archive) {
    private lateinit var binding: FragmentArchiveBinding
    private var navController: NavController? = null
    private var repoDB: ReposDB? = null
    private var adapter: ArchiveAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoDB = ReposDB.getDatabase(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArchiveBinding.bind(view)
        navController = findNavController()

        setupAdapter()
        getRepos()
    }

    private fun setupAdapter() {
        adapter = ArchiveAdapter()
        binding.itemsList.adapter = adapter
    }

    private fun getRepos() {
        val allRepos = repoDB?.repoDao()?.getAllRepos()
        allRepos?.observe(viewLifecycleOwner, Observer {
            adapter?.differ?.submitList(it)
        })
    }
}