# Popular Movies (UDACITY Android Developer Nanodegree)

![](https://imgur.com/t2eJ75U.gif)

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE.txt)[![Platform Android](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)

## Project Overview

In this Popular Movies project, the app was built in two stages. 
In Stage 1, 
* Upon launch, the user is presented with an grid arrangement of movie posters.
* Allowed the user to change sort order via a setting:
   * The sort order can be by most popular, or by top rated
* Allowed the user to tap on a movie poster and transition to a details screen with additional information such as:
   * original title
   * movie poster image thumbnail
   * a plot synopsis (called overview in the api)
   * user rating (called vote_average in the api)
   * release date

In Stage 2, 
Additional functionality was added to the app that was built in Stage 1.

More information added to the movie details view:
* Users allowed to view and play trailers ( either in the youtube app or a web browser).
* Users allowed to read reviews of a selected movie.
* Users allowed to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that is maintained and does not require an API request.
* Modified the existing sorting criteria for the main view to include an additional pivot to show the user's favorites collection.

## Rubric

[Stage 1](documentation/PopularMoviesStage1Rubric.pdf)

[Stage 2](documentation/PopularMoviesStage2Rubric.pdf)


## How to build the app

1. Clone this repository on your local machine:

```
https://github.com/douggharvey/PopularMovies.git
```

2. Open Android Studio and open the project from `File > Open...`

3. Obtain a developer API key from [The Movie DB API](https://www.themoviedb.org/documentation/api).

4. Enter the API key in the project `gradle.properties` file with syntax:
`API_KEY="your api key"`

## Languages, libraries and tools used

* [Java](https://docs.oracle.com/javase/8/)
* Android Support Libraries
* [Picasso](https://github.com/square/picasso)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [MaterialFavoriteButton](https://github.com/IvBaranov/MaterialFavoriteButton)
## Requirements

* JDK 1.8
* [Android SDK](https://developer.android.com/studio/index.html)
* Android O ([API 27](https://developer.android.com/about/versions/oreo/))


## License

[MIT license](LICENSE.txt)
Copyright 2018 Douglas Gordon Harvey
