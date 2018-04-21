package vone.person.com.yuxue.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class BrandTextView extends android.support.v7.widget.AppCompatTextView {

      public BrandTextView(Context context, AttributeSet attrs, int defStyle) {
          super(context, attrs, defStyle);
      }
     public BrandTextView(Context context, AttributeSet attrs) {
          super(context, attrs);
      }
     public BrandTextView(Context context) {
          super(context);
     }
     public void setTypeface(Typeface tf, int style) {
//           if (style == Typeface.BOLD) {
//                super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YourCustomFont_Bold.ttf"));
//            } else {
//               super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YourCustomFont.ttf"));
//            }
               super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/pen.ttf"));
      }
 }