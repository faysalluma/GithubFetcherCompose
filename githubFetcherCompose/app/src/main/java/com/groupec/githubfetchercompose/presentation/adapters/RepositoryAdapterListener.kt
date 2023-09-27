package com.groupec.githubfetchercompose.presentation.adapters

import com.groupec.githubfetchercompose.data.dto.RepositoryDTO

interface RepositoryAdapterListener {
    fun onRepositorySelected(repository: RepositoryDTO)
}