Original App Design Project - README Template
===

# 📝 App Name in Progress...

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Create a playlist of the year by notifying you daily to add a song to the playlist. There will be a home timeline where you can follow people and see their song of the day, like and comment on the post.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** music, social
- **Mobile:** yes
- **Story:** yes - creating a story of the year through song choices
- **Market:** social
- **Habit:** daily
- **Scope:**

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Individual user accounts
    * Username, display name, profile picture
    * Remember user's login
* Add daily song to playlist
    * Be able to search for songs
    * Add caption to song post
* Timeline to see other people's songs
    * See their year playlist
    * Be able to like & comment other people's songs
*  Be able to notify user daily to add a song
    * Maybe have the user be able to add a song any time of the day but have a notify reminder at end of day if user did not add a song?
* Modern UI with color theme
* Suggest friends to follow based on contacts list, Facebook friends, Spotify friends
* Play song snippets when you click on song cover

**Planned Problems**
* Filter timeline to only see songs from a certain date range

**Optional Nice-to-have Stories**

* Map of where you are when you added song
    * Google Maps API
    * Can see other friends locations also
    * Global map feature to see people around world
* Use Shazam (or some music recognition software) to recognize songs and ask user if they want to add it to playlist
* Be able to login via Spotify, Google, Facebook.
* Share daily song post via text message, post to IG story
* Categorize songs into their respective categories of music (pop, indie, rap, etc.) and create a visualization of your accumulated songs
    * Pie chart

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
