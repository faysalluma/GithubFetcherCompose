package com.groupec.githubfetchercompose.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.databinding.FragmentHomeItemBinding

class RepositoryAdapter(private val listener: RepositoryAdapterListener) :
    RecyclerView.Adapter<RepositoryAdapter.MainViewHolder>() {
    private var repositoryList = mutableListOf<RepositoryDTO>()

    fun setRepositories(repositoryList: List<RepositoryDTO>) {
        this.repositoryList = repositoryList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentHomeItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val repository = repositoryList[position]
        holder.binding.title.text = repository.full_name
        holder.binding.description.text = repository.description
       /* holder.binding.language.text =repository.languages_url
        holder.binding.starCount.text = repository.stargazers_url*/

        // Onclick item
        holder.itemView.setOnClickListener {
            listener.onRepositorySelected(repositoryList[position])
        }
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    fun filterList(filterdRepositories: List<RepositoryDTO>) {
        this.repositoryList = filterdRepositories.toMutableList()
        notifyDataSetChanged()
    }

    class MainViewHolder(val binding: FragmentHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}