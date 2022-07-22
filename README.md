Original App Design Project - README Template
===

# Meloday 🎶

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Meloday is a music-based social media app that asks users to choose a song to post once a day. New users are prompted to create a playlist and each daily song will be auto-added to the playlist. The post is shared online, where other users can like, comment, or share. Meloday is meant to encourage people to  create daily memories through music and view their progression throughout the year.

### App Evaluation
- **Category:** Music / Social Media / Entertainment
- **Mobile:** This app is primarily suited for mobile because of the flexibility and quicker access to user's phones. Since Meloday is meant to be used daily and the user receives daily notifications, user convenience is important.
- **Story:** Everyone listens to music - 270 million people in the US listen to music daily. Meloday personalizes this experience by culminating a year's worth of music into one playlist and creates a social environment with others. 
- **Market:** user - any individual who uses Spotify (the world's most popular audio streaming subscription service with 422 million users)
- **Habit:** Meloday is designed to be used at least once a day. A push notification is sent to users to remind them to post their song before the end of the day.
- **Scope:** Meloday would have the core feature of users being able to search for a song and select it as their song of the day. Using the Spotify API, I am able to leverage Spotify's library of over 82 million songs. The daily posts are stored in an external database that can be accessed from any device with Internet. In this case, I am using Parse, a platform that allows developers to build mobile apps with shared data.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

[x] User accounts
   [x] Users are able to login via Spotify authentication/logout from Meloday without having to sign out of the Spotify as well
   [x] Meloday remembers if the user is currently logged in
   [x] New users can create a Meloday playlist
[x] Users are able to search for a song
[x] Users are able to post the searched song to the timeline
[x] The song is auto-added to their Meloday playlist
[x] Users are able to view their Meloday playlist within the app
[x] Timeline displays other user's daily posts

**Planned Problems**
[x] Set daily notifications that remind user to post their song. Allow users to customize the set daily reminder to notify at a certain time of the day and save this data. 
[x] Play song snippets from within the app when you click on a song cover from the home timeline. Animate the song snippet with a smooth waveform using the audio bytes.

**Optional Nice-to-have Stories**
[x] UI/UX features, such as establishing a color theme and design consistency throughout app
[x] Design an original app logo
[x] Users can like another user's post, can double-tap to like
[x] Users can comment on another user's post
[x] Users can see other users' playlist
[x] Users can share a post to Instagram stories

### 2. Screen Archetypes
* Login screen
   * Users are able to login via Spotify
* Home Timeline screen
   * Display the song posts of different users
   * Play a song snippet when a song cover image is clicked
   * Pop up of a user's Meloday playlist when their profile is clicked
   * Can like, comment, and share each post
* Search screen
   * Search for a song, album, artist
   * Display search results
* Add Song screen
   * When a searched track is selected, open Add Song screen
   * Display song details (title, album cover image, artist)
   * Post button
      * Navigates to home timeline
* Playlist screen
   * View your own Meloday playlist
   * Display songs, playlist cover, name, description, and the date of when each song was added
* Profile screen
   * Notifications - can toggle notifications on/off, view current set time for daily reminders, edit reminder time  

### 3. Navigation

**Flow Navigation** (Screen to Screen)
* Splash screen
* Login screen

**Tab Navigation** (Tab to Screen)
* Home Timeline
   * Opening tab after login
   * View posts
* Search
   * Search for songs
   * Navigates to Add Song screen
* Playlist
   * View your Meloday playlist
* Profile
   * View notification settings
   * Logout
      * Navigates back to login screen 


## Wireframes
<img src="https://user-images.githubusercontent.com/54146286/173664939-bd2f1685-2668-44f0-a1eb-b63e31143b2d.jpg" width=600>

## Schema 
### Models
#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username      | String   | unique username |
   | password      | String   | user's password |
   | profilePic    | String   | profile image URL |
   | accessToken   | String   | current Spotify auth token for user |
   | alarmTime     | String   | daily reminder time (default 11:00 PM) |
   | notifOn       | Boolean  | notifications on/off |
   | createdAt     | DateTime | date when user is created (default field) |
   | updatedAt     | DateTime | date when user is last updated (default field) |

#### ParsePlaylist

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | user          | Pointer to User| post author |
   | playlistId    | String   | user's Meloday playlist ID |
   | createdAt     | DateTime | date when playlist is created (default field) |
   | updatedAt     | DateTime | date when playlist is last updated (default field) |
   
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | user          | Pointer to User| post author |
   | trackId       | String   | song ID |
   | trackName     | String   | song name |
   | trackArtists  | String   | song artists |
   | trackImageUrl | String   | album cover image URL |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
  
   
#### Like

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the like (default field) |
   | user      | Pointer to User| like author |
   | post          | Pointer to Post| post that is liked |
   | createdAt     | DateTime | date when like is created (default field) |
   | updatedAt     | DateTime | date when like is last updated (default field) |
   
 #### Comment

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the comment (default field) |
   | user          | Pointer to User| comment author |
   | post          | Pointer to Post| post that is commented on |
   | message       | String   | comment message |
   | createdAt     | DateTime | date when comment is created (default field) |
   | updatedAt     | DateTime | date when comment is last updated (default field) |


### Networking
#### List of network requests by screen
   - Home Timeline query example
      - (Read/GET) Query all posts
         ```java
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(10);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                posts.addAll(queryPosts);
                adapter.notifyDataSetChanged();
            }
        });
         ```
      - (Create/POST) Create a new post object
      - (Delete) Delete existing post

#### APIs/Services
- Spotify
- Parse
- Instagram
