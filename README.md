# GeolocationAPIandMap
1) Get api key from https://console.developers.google.com/
  - create project
  - Google Maps Android API
  - credentials/ create credentials  
  (keytool/SHA not needed)

2) Add API key to manifest in meta-data before the end of application
    
            android:name="com.google.android.geo.API_KEY"
            android:value="AIz...."/>"
    
    
3) Import google play services   
  in build.gradle:
    apply plugin: 'com.android.application'
    ...

    dependencies {
        compile 'com.google.android.gms:play-services:8.4.0'
    }
    
4) Add map fragment to layout

    <fragment xmlns:android="http://schemas.android.com/apk/res/android"
        android:name="com.google.android.gms.maps.MapFragment"
        android:id="@+id/map"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
        
5) Implement OnMapReadyCallback and override method

    @Override
    public void onMapReady(GoogleMap map) {
        this.map=map;
        map.getUiSettings().setZoomControlsEnabled(true);

    }
        
