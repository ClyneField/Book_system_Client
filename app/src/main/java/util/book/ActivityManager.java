package util.book;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ActivityManager extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityManager activityManager;

    public ActivityManager(){
    }

    public static ActivityManager getInstance(){
        if(null == activityManager){
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    //MyApplication.getInstance().addActivity(this);每打开个Activity就掉用
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void exit(){

        for(Activity a : activityList){
            a.finish();
        }
    }
}