# IconDotTextView
Convenient to add a View which contains icon, text and red dot.

## Why this View?

Many app has a view which cotains an icon, some text. And it will show a red dot when the app wants to notify the user there are something which are needed to be checked.

Normally, we will do that with a `FrameLayout` contains a `TextView` and a `View`(sometimes another `TextView` when there are text in the dot). And with this view, you can get rid of all those nested views.

## Usage

simplely add this to your `build.gradle`.
```
compile 'com.assistne.android:icon-dot-text-view:1.0'
```

and then you can use it in your layout like:
```
<com.assistne.icondottextview.IconDotTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:text="Category"
    app:icon="@drawable/ic_shopping_cart_black_48dp"
    app:dot_visible="true"
    app:dot_text="400"
    />
```

that's it.

## Preview
![Demo](/demo.gif)

## Supported Attribute
1. `icon` : set the icon.
2. `icon_size` : set the size of the icon. Default it will use the intrinsic size of the drawable.
3. `icon_width` and `icon_height` : set the width and the height seperately, which will override `icon_size`.
4. `spacing` : the spacing between the icon and the text.
5. `direction` : determine the position of the icon and the text. It can be set to `row`, `row_reserve`, `column` and `column_reserve`. Default it is `column` which means the icon is above the text.
6. `text`, `textSize` and `textColor` : just like the `TextView`.
7. `dot_visible` : is the dot visible. Default is not visible.
8. `dot_size' : the size of the dot.
9. `dot_color` : color of the dot. Default is Red.
10. `dot_text`, `dot_textSize` and `dot_textColor` : the text in the dot.
11. `dot_alignToIcon` : the dot can align to the icon or the boarder of the view. Default it aligns to the icon.
12. `dot_alignTo` : determine the position of the dot. Default the dot is at the right-top.
13. `dot_marginTop`, `dot_marginRight`, `dot_marginBottom`, `dot_marginLeft` : the margin of the dot.

Feel free to push an issue to let me know if there are any bugs.
