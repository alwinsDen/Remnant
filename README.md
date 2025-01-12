# Remnant - Built with Kotlin Multiplatform

This is a Kotlin Multiplatform project targeting Android, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here
  too.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

### Get SHA-1 Key

```shell
cd ~{jresource}/bin
./keytool -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore
```

### Creds

1. Add `google-services.json` to `Remnant/composeApp`
2. add `client_secret_desktop.json` to `Remnant/composeApp`
3. add `ktor-firebase-auth-firebase-adminsdk.json` to
   `Remnant/server/src/main/resources/ktor-firebase-auth-firebase-adminsdk.json`

### Environment variables
To run **Remnant server** add the .env variables in intellij/AS run configuration section.
![image](https://github.com/user-attachments/assets/07690b74-0d0a-42d8-a072-e9e4d99c5602)


### Ktor docker image and deployment

#### 1. build the server's `.jar` build artifact.

```shell
./gradlew :server:build
```

#### 2. build and deploy docker image on local machine.

```shell
docker compose up --build
```

### fix for gradle error: `zip END header not found`(for unix)

```shell
#remove global gradle install
rm -rf ~/.gradle/wrapper/dists/gradle-8.7-bin
./glaflew clean
rm -rf ~/.gradle/caches
#run android studio/intellij
./gradlew build
```
### fix for SDK location not found even if you have sdk installed(for unix)
Open/create `local.properties` file and add
```shell
sdk.dir=/home/{username}/Android/Sdk
```

### Stackoverflow refs

1. https://stackoverflow.com/questions/36919313/android-studio-issue-missing-missing-debug-keystore
2. https://stackoverflow.com/questions/27037194/keystore-file-doesnt-exist

### Docs

1. https://github.com/JetBrains/compose-multiplatform/blob/master/experimental/components/VideoPlayer
2. https://github.com/ktorio/ktor-documentation/tree/3.0.0/codeSnippets/snippets/auth-jwt-rs256
3. https://kb.vander.host/security/how-to-generate-rsa-public-and-private-key-pair-in-pkcs8-format/
4. https://funkymuse.dev/posts/create-data-store-kmp/ [@FunkyMuse](https://github.com/FunkyMuse)

### Reqs

#### _JVM DESKTOP_

1. requires ```libvlc```/```vlccore``` with VLC 3.x.x installed on ```linux```/```OSx``` respctively.
