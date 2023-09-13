package com.smilieideas.searchwithinstagram.utils;

import android.util.Log;
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

    public static final String[] data_list = {
            ART_VIDEO,ART_IMAGE,ARTIST_IMAGE,ARTIST_VIDEO,BEAUTY_VIDEO,BEAUTY_IMAGE,CREATIVE_IMAGE,CREATIVE_VIDEO
            ,DESIGN_IMAGE,DESIGN_VIDEO,EVENING_IMAGE,EVENING_VIDEO,FLOWERS_IMAGE,FLOWERS_VIDEO,FOREST_IMAGE,FOREST_VIDEO
            ,LIGHT_IMG,LIGHT_VID,MUSIC_IMG,MUSIC_VID,NATURE_IMG,NATURE_VID,NIGHT_IMG,NIGHT_VID,PAINTING_IMG,PAINTING_VID
            ,SUNSET_IMG,SUNSET_VID,TECH_IMG,TECH_VID
    };


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
