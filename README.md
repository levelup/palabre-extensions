# Create an extension for Palabre

Palabre is now extension friendly. By creating an extension you provide content to Palabre's app.

## How does it work?

The user installs your extension and enables it in the app. It opens your configuration activity where the user may have to log in / add content.
Then he can switch between your extension's content and the Palabre's one.

## Mandatory declarations

### Gradle

```groovy
repositories {
    maven {url "http://levelupstudio.com/maven2"}
}

dependencies{
    compile 'com.levelup.palabre.api:palabre-api:1.0'
}
```

### Manifest

The app will request the extension using a `ContentProvider`. You have to declare it this way (replace "com.example.extension" with your package name).

```xml
<provider
    android:name="com.levelup.palabre.provider.RSSProvider"
    android:authorities="com.example.extension.provider"
    android:exported="true" />
```

Palabre and the extension will communicate through a `Service`. You have to declare it this way.

```xml
<application ...>
...
    <service
        android:name=".MyExtension"
        android:label="@string/app_name"
        android:permission="com.levelup.palabre.permission.READ_EXTENSION_DATA">
        <intent-filter>
            <action android:name="com.levelup.palabre.Extension"/>
        </intent-filter>
    
        <meta-data
            android:name="protocolVersion"
            android:value="1"/>
        <meta-data
            android:name="worldReadable"
            android:value="true"/>
        <meta-data
            android:name="supportSendRead"
            android:value="true"/>
        <meta-data
            android:name="settingsActivity"
            android:value=".MainActivity"/>
        <meta-data
            android:name="authority"
            android:value="com.example.extension.provider"/>
        <meta-data
            android:name="extensionicon"
            android:resource="@drawable/my_extension_icon"/>
        <meta-data
            android:name="maxKeptItems"
            android:value="5000"/>
    </service>
</application>
```

This points to a class extending ```PalabreExtension``` [see below](#extension-service).

##### Meta data explanations 

`protocolVersion` will be used in the future to support older versions of the api

`worldReadable` indicate if your extension can share its data with other third party apps

`supportSendRead` indicate if your extension needs to know when an article has been read

`settingsActivity` points to the activity that will be launched when the user enables the extension

`authority` the `ContentProvider` activity you declared above

`extensionicon` : the icon that will be used to represent your extension's services. See [guidelines](#guidelines)

`maxKeptItems`: number of articles that will be kept in database (cannot be higher than 10,000). This is not mandatory. By default Palabre keeps 10,000 articles.

### Authority declaration

In order to work, your app should have a `PalabreApiProviderAuthority` in the ```com.levelup.palabre.api.provider``` package that declares your authority.

```java
package com.levelup.palabre.api.provider;

public class PalabreApiProviderAuthority {
    public static final String CONTENT_AUTHORITY = "com.example.extension.provider";
}
```

Notice the package. You will have to create the file in the right folder.

### First launch

When the first launch is complete (the user is authenticated for example), you can tell Palabre by sending this `Intent`

```java
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("palabre://extauth"));
startActivity(intent);
```

### Extension Service

This service is your extension endpoint to communicate with Palabre. It will be automatically launched when Palabre needs information.

```java
public class MyExtension extends PalabreExtension {
    @Override
    protected void onUpdateData(int reason, UpdateData updateData) {
    }
}
```

When Palabre needs some data update, the `onUpdateData` callback is launched.
You can send information to Palabre with theses 3 methods:

```java
void publishUpdate(ExtensionData data) //indicates that the update is finished

void publishUpdateStatus(ExtensionUpdateStatus data) //sends the update's status and progress

void publishAccountInfo(ExtensionAccountInfo data) //sends some information about the users identity (for example his avatar)
```

### Persist data

We created some useful helpers to help you persist the data.
Your data have to follow the following schema.
![UML diagram](http://levelupstudio.com/palabre/extension/img/palabre_api.png)

##### Examples

```java

// Save multiple sources
List<Source> sources = new ArrayList<Source>();
sources.add(new Source()
    .title(subscription.getTitle())
    .dataUrl(subscription.getHtmlUrl())
    .iconUrl(subscription.getIconUrl())
    .uniqueId(subscription.getId())
    .categories(categoryList));
Source.multipleSave(context, sources);

// Create and save an article
Article article = new Article()
    .author(author)
    .content(content)
    .title(title)
    .date(date)
    .image(image)
    .sourceId(source.getId())
    .uniqueId(uniqueId)
    .linkUrl(linkUrl)
    .saved(saved)
    .save(context);

// Delete this article
article.delete(context);

// Get all the sources
List<Source> sources = Source.getAll(context);

```

## Guidelines

### Name

Palabre launches a Play Store search to display the extension list. If you want to appear in this list, please make sure your app ends with "for Palabre".

### Icon



You have two icons to design for your app. 
- Your app icon should follow the Android and Palabre guidelines. Use this [PSD](https://github.com/levelup/palabre-extensions/raw/master/Resources/extensions_icons.psd) to create it.
- The one declared in the extensionicon metadata represents the service and should follow the [following guideline](https://github.com/levelup/palabre-extensions/raw/master/Resources/extensions_logo.psd).