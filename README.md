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
