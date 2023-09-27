package com.groupec.githubfetchercompose.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupec.githubfetchercompose.data.dtole.BranchDTO
import com.groupec.githubfetchercompose.databinding.FragmentBranchItemBinding

class BranchAdapter() : RecyclerView.Adapter<BranchAdapter.MainViewHolder>() {
    private var branchList = mutableListOf<BranchDTO>()

    fun setBranches(branchList: List<BranchDTO>) {
        this.branchList = branchList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentBranchItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val branch = branchList[position]
        holder.binding.name.text = branch.name
    }

    override fun getItemCount(): Int {
        return branchList.size
    }

    class MainViewHolder(val binding: FragmentBranchItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}