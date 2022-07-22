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
[x] Timeline displays other user's daily posts

    * See their year playlist
    * Be able to like & comment other people's songs
*  Be able to notify user daily to add a song
    * Maybe have the user be able to add a song any time of the day but have a notify reminder at end of day if user did not add a song?

**Planned Problems**
[x] Set daily notifications that remind user to post their song. Allow users to customize the set daily reminder to notify at a certain time of the day and save this data. 
[x] Play song snippets from within the app when you click on a song cover from the home timeline. Animate the audio with a smooth waveform.

**Optional Nice-to-have Stories**
[x] UI/UX features, such as establishing a color theme and design consistency throughout app
[x] Users can like another user's post, can double-tap to like
[x] Users can comment on another user's post
[x] Users can see other users' playlist
[x] Users can share a post to Instagram stories

### 2. Screen Archetypes
* Login
   * Individual user accounts
       * Username, password, profile picture
       * Playlist
       * Friends
       * Settings for notifications
* Timeline
   * See other users' posts
       * Username, profile picture
       * Song post
       * Like/comment on others' posts
* Post
   * Search for song

### 3. Navigation

**Flow/Tab Navigation** (Tab/Screen to Screen)

* Login
   * Opening screen
* Home timeline
   * From login screen to home timeline fragment
   * When click on home timeline tab on bottom navigation bar
* Post
   * To post fragment
   * When click on post tab on bottom navigation bar
* Playlist
   * Display current playlist
* Profile
   * To profile fragment
   * When click on post tab on bottom navigation bar
* Settings
    * From profile fragment to settings fragment
    * Notification settings
    * Logout

## Wireframes
<img src="https://user-images.githubusercontent.com/54146286/173664939-bd2f1685-2668-44f0-a1eb-b63e31143b2d.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username      | String   | unique username |
   | password      | String   | user's password |
   | profilePic    | File     | user's profile image |
   | playlist      | JSONObject? | user's playlist |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | user          | Pointer to User| post author |
   | song          | JSONObject? | song that user posts |
   | caption       | String   | post caption by author |
   | commentsCount | Number   | number of comments on post |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   
#### Comments

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the comment (default field) |
   | post          | Pointer to Post| post that is commented on |
   | commentUser   | Pointer to User| comment author |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   
#### Likes

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the like (default field) |
   | post          | Pointer to Post| post that is commented on |
   | likeUser      | Pointer to User| like author |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
  
#### Friends

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the friendship (default field) |
   | friend1       | Pointer to User| first user in friendship |
   | friend2       | Pointer to User| second user in friendship |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |


### Networking
#### List of network requests by screen
   - Home Timeline Screen
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

#### APIs
- Login
    - Google
    - Facebook
- Timeline
    - Spotify
- Map
    - Google Maps
- Share song
    - Instagram (specifically opening an IG story)
    - Text
- Song recognition
    - Shazam
