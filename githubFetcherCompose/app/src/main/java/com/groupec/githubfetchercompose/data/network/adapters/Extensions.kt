package com.groupec.githubfetchercompose.data.network.adapters

import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.data.dtole.BranchDTO
import com.groupec.githubfetchercompose.data.network.bodies.results.Branch
import com.groupec.githubfetchercompose.data.network.bodies.results.Contributor
import com.groupec.githubfetchercompose.data.network.bodies.results.Repository

fun Repository.asDto() = RepositoryDTO(id, full_name, description,  languages_url, stargazers_url, branches_url, contributors_url)
fun Branch.asDto() = BranchDTO(name)
fun Contributor.asDto() = ContributorDTO(login, avatar_url)