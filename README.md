# XYZ Reader
Demos the application of Material Design principles to a feed reader UI. Built as part of the Udacity Android Course.

## Prerequisites
- Android SDK v24
- Android Build Tools v23.0.3
- Android Support Library v24.0.0

## Supported OS Versions
API level 16 and above

## Implementation Notes

### Feed List Screen
The app bar has been implemented using the `AppBar` and `CollapsingToolbar` widgets from the Design support library. The toolbar is pinned to the top when the app bar collapses. 

One of the challenges was to add elevation to the app bar in pre-Lollipop devices. This was achieved by creating a gradient drawable and adding it as the background of the `AppBar` widget. The drawable has been given a bottom padding to make it visible. Due to this padding the height of the app bar has been increased to match the Material Design spec (see the `list_appbar_height` dimension in [dimens.xml](XYZReader/src/main/res/values/dimens.xml)).

![App Bar shadow for pre-Lollipop devices](/screenshots/listpage-appbar-shadow.png)

Just adding the shadow wasn’t enough. The effect of the list going under the shadow had to be achieved as well. For this the swipe layout’s translationY property has been updated and some top padding has been added to the recycler view (see `ListPageSwipeLayout` and `ListPageRecyclerView`  styles in [styles.xml](XYZReader/src/main/res/values/styles.xml)).

The feeds have been implemented using `CardViews` and laid out using the `StaggeredGridLayoutManager`. The height of the card varies according to the aspect ratio of the image. The number of columns shown is two by default but becomes three in landscape mode for tablets.

![Card grid on phone](/screenshots/listpage-phone.png)

![Card grid on tablet in landscape mode](/screenshots/listpage-tablet-landscape.png)

*Roboto-black* font was used for the app bar title.

### Feed Detail Screen
In the feed detail screen the app bar consists of just a `Toolbar` widget. Its background is a layer list drawable consisting of shadow and color layers for pre-Lollipop devices and just a color layer for Lollipop and above.

The content view is a `ViewPager` allowing the user to swipe between feeds. On the phone, the detail view of the feed consists of an image shown at the top while the title, author and body shown are below. This layout changes in the landscape mode for tablets whose shortest width is greater than or equal to 600 dips. In this latter scenario the text container is centered horizontally and its width is restricted to a certain value for optimal reading. The image becomes full-bleed, occupying the whole background and covering any whitespace.

![Detail page on phone](/screenshots/detailpage-phone.png)

![Detail page on tablet](/screenshots/detailpage-tablet-landscape.png)

A FAB for sharing the current feed is shown at the bottom-right corner.

Both the FAB and the toolbar go out of view when the text is scrolled down and are brought back into view when scrolled up. Their appearance can also be toggled by a simple tap anywhere in the screen. This tap gesture is detected using the GestureDetector built-in class (see [FeedDetailFragment.java](XYZReader/src/main/java/com/example/xyzreader/ui/FeedDetailFragment.java)).

In the default layout wherein the image is stacked on top of the text, parallax scrolling has been implemented to create the effect of text scrolling over the image. For achieving this a custom `ScrollView` called `ObservableScrollView` was used to hold the text. Changes in the top y-coordinate of the scrollview is observed and the image is translated accordingly.
