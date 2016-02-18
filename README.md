AndroidSwipeableCardStack
=========================
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidSwipeableCardStack-green.svg?style=true)](https://android-arsenal.com/details/1/2724)


Change log:
Now it is compatible with api level 14
Investigating compatibility with api level 13



![image](https://raw.githubusercontent.com/wenchaojiang/AndroidSwipeableCardStack/master/pics/image2.png)



A tinder like swipeable card stack component. Provide "swipe to like" effects. Easy to customize card views.


See youtube demo : https://www.youtube.com/watch?v=YsMnLJeouf8&feature=youtu.be
A Demo App is also included in the source.


Installation
---
Use jitpack
```
repositories {
   maven { url "https://jitpack.io" }
}

dependencies {
   compile 'com.github.mattwaggs:AndroidSwipeableCardStack:0.1.8'
}
```
OR manually

1. Download released .aar file
[Download current release] (https://github.com/wenchaojiang/AndroidSwipeableCardStack/releases/)

2. put it into your project lib dir, "libs" for example.

3. put following lines to your gradle.build file
```
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile(name:'android-card-stack-0.1.0', ext:'aar')
}
```

Configuration
-----


Put CardStack in your layout file

```xml
 <com.wenchao.cardstack.CardStack
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="20dp"
        android:clipChildren="false"
        android:clipToPadding="false"
    />
```

Create your card view layout file.

Example: card_layout.xml, contain only a TextView
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:id="@+id/content"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
    />

</LinearLayout>
```

Implement your own adapter for the card stack. The CardStack will accept ArrayAdapter.
The Following example extends a simple ArrayAdapter<String>, overriding ```getView()``` to supply your customized card layout

```java
public class CardsDataAdapter extends ArrayAdapter<String> {

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        TextView v = (TextView)(contentView.findViewById(R.id.content));
        v.setText(getItem(position));
        return contentView;
    }

}
```
Get the CardStack instance in your activity

```java
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mCardStack = (CardStack)findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.card_content);
        mCardStack.setStackMargin(20);
        
  }
  ```
  
Finally, set the adapter 


```java
    mCardAdapter = new CardsDataAdapter(getApplicationContext(),0);
    mCardAdapter.add("test1");
    mCardAdapter.add("test2");
    mCardAdapter.add("test3");
    mCardAdapter.add("test4");
    mCardAdapter.add("test5");
    
    mCardStack.setAdapter(mCardAdapter);
```


Listening to card stack event 
----
implement CardStack.CardEventListener, and set it as listener ```mCardStack.setListener(yourListener);   ```

```java

public class YourListener implements CardStack.CardEventListener {

    private static final String TAG = "YourListener";

    @Override
    public void onSwipeLeft(int mIndex) {
        Log.d(TAG, "Card Swiped Left");
    }

    @Override
    public void onSwipeRight(int mIndex) {
        Log.d(TAG, "Card Swiped Right");
    }

    @Override
    public void onCardTapped(int mIndex) {
        Log.d(TAG, "Card Tapped");
    }
}


mCardStack.setListener(new YourListener());
```


TODO
----

1. deploy to maven central
2. compatibility with api level 13
