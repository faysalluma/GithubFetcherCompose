package com.groupec.githubfetchercompose.data.dto

// fun RepositoryDTO.asEntity() = com.groupec.githubfetchercompose.data.database.entities.Repository(id.toLong(), full_name, description, languages_url, stargazers_url, branches_url, contributors_url)
fun RepositoryDTO.asBody() = com.groupec.githubfetchercompose.data.network.bodies.results.Repository(id, full_name, description, languages_url, stargazers_url, branches_url, contributors_url)