# The meal: Recipe Application
## Project Description
This project is the solution for the Showoff Android Technical Test, a simple recipe application developed in Kotlin for the Android platform.

The goal is to consume a public recipe API (TheMealDB) and present a three-screen user experience, as specified in the test:

1. Recipe Categories: Displays a list of recipe categories.
2. Recipe List: Displays a list of recipes for the selected category.
3. Recipe Details: Displays the full details of the selected recipe.

The User Interface (UI) was designed and implemented focusing on usability and clean design, utilizing the creative freedom granted.

## Technologies Used
- Language: Kotlin
- UI Framework: Jetpack Compose
- Architecture: MVVM
- API Consumption: Retrofit
- Image Loading: Coil
- Dependency Injection: Hilt

## How to Build and Run the Project
To build and run this project, you will need Android Studio installed with the Android SDK configured.

#### Prerequisites
- Android Studio (Recommended version: latest stable version)
- Android SDK (API Level: 36)

#### Steps to Run
1. Clone the Repository:

```bash
git clone https://github.com/danielvilha/kotlin-the-meal.git
```

2. Open in Android Studio:
- Open Android Studio.
- Select File > Open... and navigate to the kotlin-the-meal folder.

3. Sync and Compile:
- Wait for Gradle to sync the dependencies. If necessary, click Sync Project with Gradle Files (the elephant icon).
- Select an emulator or physical device to run the application.
- Click the Run button (the green play icon).

The project will be compiled and installed on the selected device.

## API Endpoints
- List Categories: https://www.themealdb.com/api/json/v1/1/categories.php
- Filter Recipes by Category: https://www.themealdb.com/api/json/v1/1/filter.php?c={category}
- Look up Recipe Details by ID: https://www.themealdb.com/api/json/v1/1/lookup.php?i={recipe_id}

## Application Screenshots
|![Categories List](/images/categories_list.png)|![Recipes List](/images/recipes_list.png)|![Recipe Details](/images/recipe_details.png)|

## Author
- Daniel Vilha
- https://github.com/danielvilha

## License

Copyright 2024 Daniel Vilha

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
