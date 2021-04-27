# Client Reddit for Android

A simple Reddit client that shows the top 50 entries from Reddit. The client consumes the Reddit API, specifically /r/subreddit/top endpoint.


<img src="/redditLandscape.PNG" width="300"/> <img src="/RedditPortrait.PNG" width="300"/>


## Set up

Follow the instructions here [Getting started](https://github.com/reddit-archive/reddit/wiki/OAuth2) in order to get a client ID (note: the app uses an application Only OAuth).
After you get your cliend-id, you need to add this value to the project. 


## Adding your cliend id to the project


1. Add a file in the root app called apiKey.properties
2. In the file add the follow: CLIENT_ID="ID"

Insted of put "ID" you should use your reddit cliend id.

## App

-The app uses the repository pattern. The app checks if there are no posts in the database (using for cache), it calles the endpoints to get the top 50 from Reddit and saves theses into the database. 
-The operations read and dismiss update the records in the database.
-Refresh operation will replace the records in the database (restoring the values for read and dismiss to false)

![Diagram class](/class.PNG "Diagram class")

## Features

- Pagination for RecyclerView with 10 elements per page
- Pull to refresh the list
- Save pictures in the gallery
- Dismiss a post or dismiss all the posts
- Support for landscape (Marter-detail pattern) and portrait (Using a drawerLayout)
