package com.smilieideas.xmooth.utils;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
public class CloudDB
{
    public static final String STATUS_LIKED_TAG = "wall_post_likes";
    private static final String TAG = "CloudDB";
    public static final String USERT_ID = "user_id";
    public static final String USERT_NAME = "user_name";
    public static final String USERT_EMAIL = "user_email";
    public static final String USERT_IMAGE = "user_image";
    public static final String ACCESS_TOKEN = "access_token";


    public static final String USERS = "users_kdc_accounts_";
    public static final String DRAWINGS = "drawings_kdc_";
    public static final String DRAWINGS_SKETCH = "drawings_kdc_sketches_";
    public static final String DRAWINGS_LIKES = "drawings_kdc_likes_ignored";
    public static final String USER_FOLLOWER_STATS = "followers_user_stats";
    public static final String DRAWINGS_HEARTS_DT = "drawings_kdc_hearts";
    public static final String CLOUD_IMAGE_DIR = "images";
    public static final String CLOUD_PROFILE_DIR = "profiles";
    public static final String TV_USERS = "users";
    public static final String CONNECTED_TV_USERS = "tv_connected_users";
    public static final String STATUS_TAG = "tv_users_status_wall";
    public static final String NEW_STATUS_TAG = "tv_users_status_wall";
    public static final String NEW_STORY_TAG = "tv_users_story_wall";

    public static final String COMMUNITY_BAN = "tv_users_community_ban";
    public static final String USERS_NOTIFICATIONS = "tv_users_notifications";
    public static final String FOLLOWING = "tv_following_list";
    public static final String POST_LIKES_TAG = "wall_post_likes";
    public static final String STATUS_COMMENT_TAG = "status_wall_comments";
    public static final String SOCIAL_HANDLES = "tv_users_social_handles";
    public static final String DIAMOND_USERS = "tv_diamond_users";
    public static final String PROFILE_SETTINGS = "tv_users_profile_settings";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String USER_PREFS = "user_prefs";

    public static final String ART_VIDEO = "art-vid";
    public static final String ART_IMAGE = "art-img";
    public static final String ARTIST_IMAGE = "artist-img";
    public static final String ARTIST_VIDEO = "artist-vid";

    public static final String BEAUTY_VIDEO = "beautiful-vid";
    public static final String CREATIVE_VIDEO = "creative-vid";
    public static final String DESIGN_VIDEO = "design-vid";
    public static final String BEAUTY_IMAGE = "beautiful-img";
    public static final String CREATIVE_IMAGE = "creative-img";
    public static final String DESIGN_IMAGE = "design-img";

    public static final String EVENING_VIDEO = "evening-vid";
    public static final String FLOWERS_VIDEO = "flowers-vid";
    public static final String FOREST_VIDEO = "forest-vid";
    public static final String EVENING_IMAGE = "evening-img";
    public static final String FLOWERS_IMAGE = "flowers-img";
    public static final String FOREST_IMAGE = "forest-img";

    public static final String LIGHT_IMG = "light-img";
    public static final String MUSIC_IMG = "music-img";
    public static final String NATURE_IMG = "nature-img";
    public static final String LIGHT_VID = "light-vid";
    public static final String MUSIC_VID = "music-vid";
    public static final String NATURE_VID = "nature-vid";

    public static final String NIGHT_VID = "night-vid";
    public static final String PAINTING_VID = "painting-vid";
    public static final String SUNSET_VID = "sunset-vid";
    public static final String TECH_VID = "technology-vid";

    public static final String NIGHT_IMG = "night-img";
    public static final String PAINTING_IMG = "painting-img";
    public static final String SUNSET_IMG = "sunset-img";
    public static final String TECH_IMG = "technology-img";

    public static boolean is_tv_device = false;

    public static boolean checkIfTV(Context context){
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true;
        } else {
            return false;
        }
    }

    public static final String[] data_list = {
            ART_VIDEO,ART_IMAGE,ARTIST_IMAGE,ARTIST_VIDEO,BEAUTY_VIDEO,BEAUTY_IMAGE,CREATIVE_IMAGE,CREATIVE_VIDEO
            ,DESIGN_IMAGE,DESIGN_VIDEO,EVENING_IMAGE,EVENING_VIDEO,FLOWERS_IMAGE,FLOWERS_VIDEO,FOREST_IMAGE,FOREST_VIDEO
            ,LIGHT_IMG,LIGHT_VID,MUSIC_IMG,MUSIC_VID,NATURE_IMG,NATURE_VID,NIGHT_IMG,NIGHT_VID,PAINTING_IMG,PAINTING_VID
            ,SUNSET_IMG,SUNSET_VID,TECH_IMG,TECH_VID
    };

    private static Uri getmageToShare(Activity activity,Bitmap bitmap) {
        File imagefolder = new File(activity.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            // uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID +".provider", file);
            uri = FileProvider.getUriForFile(Objects.requireNonNull(activity),
                    "com.smilieideas.xmooth.provider", file);
        } catch (Exception e) {
            Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    public static String SaveImage(final Activity context,Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();

        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
// Tell the media scanner about the new file so that it is
// immediately available to the user.
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);


                        Toast.makeText(context,"Photo Saved to Gallery",Toast.LENGTH_LONG).show();
                    }
                });


        Log.e(TAG, "SaveImage: "+file.toString());
      //  shareImage(context, Uri.parse(file.toString()));
        return file.toString();


    }

    public static void shareImage(Activity activity,Uri uri) {

        Intent intent = new Intent(Intent.ACTION_SEND);

        String link = Uri.parse("http://play.google.com/store/apps/details?id="+activity.getPackageName()+"").toString();
        String text = "Download Media for Google Photos  \n\n "+link+"";
        // putting uri of image to be shared

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, text);

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, text);

        // setting type to image
        intent.setType("image/png");

        /*FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);*/
        // calling startactivity() to share
        activity.startActivity(Intent.createChooser(intent, "Share Via"));
    }

    public static void shareImageandText(Activity activity, Bitmap bitmap) {
        Uri uri = getmageToShare(activity,bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        String link = "";//Uri.parse("http://play.google.com/store/apps/details?id="+activity.getPackageName()+"").toString();
        String text = "Download Xmooth for Instagram  \n\n "+link+"";
        // putting uri of image to be shared

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, text);

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, text);

        // setting type to image
        intent.setType("image/*");

        /*FileProvider.getUriForFile(Objects.requireNonNull(activity),
                BuildConfig.APPLICATION_ID + ".provider", file);*/
        // calling startactivity() to share
        activity.startActivity(Intent.createChooser(intent, "Share Via"));
    }

    public static String getFormatedDate(String date){

        String formdate = date;
        try{
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = spf.parse(date);
            spf= new SimpleDateFormat("dd MMM yyyy");
            formdate = spf.format(newDate);

            return formdate;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return formdate;
    }

    public static String getFormattedImageName(String name){
        return CLOUD_IMAGE_DIR + "/"+ name+".jpg";
    }

    public static String getCloudTVUserUniqueCode(){
        Random rand = new Random();
        int min = 100000;
        int max = 999999;

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum1 = rand.nextInt((max - min) + 1) + min;

        min = 28539;
        max = 78900;
        int randomNum2 = rand.nextInt((max - min) + 1) + min;

        min = 9009;
        max = 898900;
        int randomNum3 = rand.nextInt((max - min) + 1) + min;


       // String uniqueId = ""+randomNum1+"tv"+randomNum2;
        Log.e(TAG, "getCloudTVUserUniqueCode:  "+randomNum1);

        return randomNum1+"";
    }




}
