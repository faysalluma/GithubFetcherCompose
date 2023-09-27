package com.groupec.githubfetchercompose.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.databinding.FragmentContributorItemBinding

class ContributorAdapter(private val context: Context) : RecyclerView.Adapter<ContributorAdapter.MainViewHolder>() {
    private var contributorList = mutableListOf<ContributorDTO>()

    fun setContributors(contributorList: List<ContributorDTO>) {
        this.contributorList = contributorList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentContributorItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val contributor = contributorList[position]
        holder.binding.login.text = contributor.login
        Glide.with(context)
            .load(contributor.avatar_url)
            .into(holder.binding.avatarUrl)

    }

    override fun getItemCount(): Int {
        return contributorList.size
    }

    class MainViewHolder(val binding: FragmentContributorItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}