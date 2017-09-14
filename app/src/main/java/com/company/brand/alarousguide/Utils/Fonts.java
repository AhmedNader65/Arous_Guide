package com.company.brand.alarousguide.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by waheed_manii on 5/27/2017.
 */

public class Fonts {



    public static Typeface mSetupFontLight(Context context){
       Typeface DIN_LIGHT = Typeface.createFromAsset(context.getAssets() , "Fonts/DinNextLight.ttf");
        return DIN_LIGHT;
    }

    public static Typeface mSetupFontRegular(Context context){
       Typeface DIN_REGULAR = Typeface.createFromAsset(context.getAssets() , "Fonts/DinNextRegular.ttf");
        return DIN_REGULAR;
    }

    public static Typeface mSetupFontMedium(Context context){
       Typeface DIN_MEDIUM = Typeface.createFromAsset(context.getAssets() , "Fonts/DinNextMedium.ttf");
        return DIN_MEDIUM;
    }

    public static Typeface mSetupFontBold(Context context){
        Typeface DIN_BOLD = Typeface.createFromAsset(context.getAssets() , "Fonts/DinNextBold.ttf");
        return DIN_BOLD;
    }

}
