package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.util.DBOpenHelper;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(!isTaskRoot()){
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if(intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)){
                finish();
            }
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = View.inflate(this,R.layout.activity_splash_screen,null);
        setContentView(view);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f,1.0f);
        alphaAnimation.setDuration(2000);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void redirectTo() {
//        DBOpenHelper helper = new DBOpenHelper(SplashScreenActivity.this, "db_referee", null, 1);
//        SQLiteDatabase database = helper.getReadableDatabase();
//        Cursor cursor = database.query("user", new String[]{"nick_name"}, null, null, null, null, null);
//        int count = cursor.getCount();
//        if (count > 0) {
//            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//            startActivity(intent);
//        }else{
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
//        }
        finish();
    }
}
