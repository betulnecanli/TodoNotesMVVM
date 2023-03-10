#  <h1 align="center">📝 TodoNotesMVVM</h1>
# 
<p align="center">  ToDo and Notes App is a simple Android app for managing notes and to-do lists. With ToDo and Notes App, you can quickly add, delete, and update notes, as well as check off completed tasks in Kotlin with  MVVM architecture.</>

#  <h1 align="center">🖼 Preview</h1>


#  <h1 align="center">TODO 📝</h1>
 Add&Delete  | Search&Edit  | Hide Completed
------------- | ------------- | -------------
![](https://github.com/betulnecanli/TodoNotesMVVM/blob/master/ScreenGifs/addtodo.gif?raw=true)  | ![](https://github.com/betulnecanli/TodoNotesMVVM/blob/master/ScreenGifs/searchedittodo.gif?raw=true)  | ![](https://github.com/betulnecanli/TodoNotesMVVM/blob/master/ScreenGifs/hidecompleted.gif?raw=true)



#  <h1 align="center">NOTES 📒</h1>
 Add&Delete  | Search&Edit  | Sort by Name or Date
------------- | ------------- | -------------
![](https://github.com/betulnecanli/TodoNotesMVVM/blob/master/ScreenGifs/addnote.gif?raw=true)  | ![](https://github.com/betulnecanli/TodoNotesMVVM/blob/master/ScreenGifs/searcheditnotes.gif?raw=true)  | ![](https://github.com/betulnecanli/TodoNotesMVVM/blob/master/ScreenGifs/sortnotes.gif?raw=true)


<h2 align="center">Features⭐</h2>

- Add, delete and update your todo list.
- Check the box if you done the task.
- Add, delete and update your your notes.

<h2 align="center">Architecture ☁</h2>

This app follows the MVVM (Model-View-ViewModel) architecture pattern. The components of the app are organized as follows:

- Model: The data source for the app is the PokeAPI, which provides information about Pokemon characters in JSON format. The app uses Retrofit to make network requests to the PokeAPI and Gson to deserialize the JSON responses into Java objects.

- View: The views in the app are implemented using Android's XML layout files. The main activity (MainActivity) contains a RecyclerView that displays a list of Pokemon characters, and a search bar that allows users to filter the list by name. Clicking on a character in the list navigates the user to the CharacterDetailActivity, which displays detailed information about the selected character.

- ViewModel: The CharacterViewModel class acts as an intermediary between the model and the view. It retrieves data from the model and exposes it to the view through observable data fields. It also provides methods for filtering the character list based on user input.


<h2 align="center">Getting Started 🚀</h2>

To run this app, you'll need to have Android Studio installed. Follow these steps to get started:

 - Clone this repository: git clone https://github.com/betulnecanli/RickandMortyMVVM.git
 - Open the project in Android Studio.
 - Build and run the app.



#  <h1 align="center">📚 Libraries and Tools Used </h1>

<p align="center">

- ViewBinding
- Flows
- Coil
- Jetpack Datastore
- Dagger Hilt
- Coroutines

</p>


# License
```xml
Designed and developed by 2022 Betül Necanlı 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


