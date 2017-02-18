package com.three38inc.app.picsmash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Jobith on 8/12/2015.
 */
public class IntroActivity extends AppIntro2 {
    @Override
    public void init(Bundle bundle) {
        // Instead of fragments, you can also use our default slide
        
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Picsmash - Greet the World!", "", R.drawable.web_icon, Color.parseColor("#34495e")));
        addSlide(AppIntroFragment.newInstance("Explore your favourite picsmash", "", R.drawable.searchbar, Color.parseColor("#e74c3c")));
        addSlide(AppIntroFragment.newInstance("Choose the perfect picsmash", "", R.drawable.choose, Color.parseColor("#475867")));
        addSlide(AppIntroFragment.newInstance("Share it with the world!", "", R.drawable.sharing, Color.parseColor("#27ae61")));

        showDoneButton(true);
        setCustomTransformer(new PageFadeTransformer());
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class PageFadeTransformer implements ViewPager.PageTransformer {

        public void transformPage(View view, float position) {
            view.setTranslationX(view.getWidth() * -position);

            if(position <= -1.0F || position >= 1.0F) {
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setAlpha(1.0F - Math.abs(position));
            }
        }

    }

}
