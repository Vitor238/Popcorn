<h1 align="center"> 🍿 Popcorn </h1>

<p align="center">
<img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/Vitor238/Popcorn">

<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/Vitor238/Popcorn">

<img alt="GitHub issues" src="https://img.shields.io/github/issues/Vitor238/Popcorn">

</p>

<p align="center">
 <a href="./README-PT-BR.md">Readme em Português </a> •
 <a href="#information_source-about">About</a> •
 <a href="#iphone-download-app">Download app</a> •
 <a href="#hammer-building-the-project">Building the project</a> •
 <a href="#rocket-technologies-and-resources">Technologies and resources</a> •
 <a href="#memo--license">License</a>
</p>

<p align="center">
<img src="./screenshots/home_en_us.png" alt="Home" 
width="180">
<img src="./screenshots/info_en_us.png" 
alt="Info" width="180" hspace="4">
<img src="screenshots/now_playing_en_us.png" alt="Now playing" 
width="180">
<img src="./screenshots/favorites_en_us.png" alt="Favorites" 
width="180">
</p>

## :information_source: About

This application was created with the aim of studying more about the MVVM architecture, Retrofit, Coroutines, Firebase and Material Design.

With this app you can search for movies and series, see which ones are trending, and save to your favorites list. You can also see which movies are playing in your country’s cinemas. The application is available in Portuguese and English and its content is available in [supported languages](https://developers.themoviedb.org/3/configuration/get-languages) through the TMDB API.

The application also has a light and dark theme that can be changed in the settings.

## :iphone: Download app

You can download it by clicking on this [link](https://github.com/Vitor238/Popcorn/releases/latest/download/popcorn.apk).

## :hammer: Building the project

1. Clone the project by copying this command to your terminal:

   ```bash
   git clone https://github.com/Vitor238/Popcorn.git
   ```

2. Open the project in Android Studio

3. Change the name of the project packages. See how [here](https://stackoverflow.com/a/29092698/9729980).

4. Create a new project on [Firebase](https://console.firebase.google.com/) and add the app. See how [here](https://firebase.google.com/docs/android/setup).

5. Create a new account and generate an API key in [TMDB](https://developers.themoviedb.org/3/getting-started/introduction)

6. Add the TMDB API keys and [Firebase server client ID](https://firebase.google.com/docs/auth/android/google-signin#authenticate_with_firebase) to the "local.properties" file:

```groovy
TMDB_API_KEY=1234567890abcd
DEFAULT_WEB_CLIENT_ID=123456example.apps.googleusercontent.com
```

7. Run the app

## :rocket: Technologies and resources

- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore/quickstart)
- [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android)
- [Firebase Email Auth](https://firebase.google.com/docs/auth/android/password-auth)
- [Firebase Google Auth](https://firebase.google.com/docs/auth/android/google-signin)
- [Firebase Storage](https://firebase.google.com/docs/storage/android/start)
- [Glide](https://github.com/bumptech/glide)
- [Moshi](https://github.com/square/moshi)
- [Preferences](https://developer.android.com/jetpack/androidx/releases/preference)
- [Retrofit](https://github.com/square/retrofit)
- [Secrets Gradle Plugin](https://github.com/google/secrets-gradle-plugin)
- [The Movie Database API](https://www.themoviedb.org/documentation/api)


## :memo:  License

This project is licensed under the [Apache 2 License](https://www.apache.org/licenses/LICENSE-2.0https://www.apache.org/licenses/LICENSE-2.0) - see the [LICENSE](LICENSE) file for details.
