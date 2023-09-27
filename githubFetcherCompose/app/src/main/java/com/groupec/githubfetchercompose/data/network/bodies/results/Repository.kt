package com.groupec.githubfetchercompose.data.network.bodies.results

data class Repository(val id: Int, val full_name: String, val description: String?, val languages_url: String,
                      val stargazers_url: String, val branches_url : String, val contributors_url: String)