# My-Calendar
## This project is my first library project
---
### Version: [![](https://jitpack.io/v/muminkayaaslan/My-Calendar.svg)](https://jitpack.io/#muminkayaaslan/My-Calendar)

---
# How to install?
## To download this library:
### Step 1: 
Go to "Settings.gradle.kts" and write this code:
````
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } //add line

    }
}
````

### Step 2: 
Then go to “build.gradle.kts(Module :app)” and add this line:
````
    implementation("com.github.muminkayaaslan:My-Calendar:<version>")
````
---
# In case of a problem [https://jitpack.io/](https://jitpack.io/) you can look here
