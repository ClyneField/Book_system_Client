package background.book;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {

        if (intent.getAction().equals("BookMessageAdapterSuccess")) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            Log.d("MessageReceiver", "onReceive: ");
        }
        else if (intent.getAction().equals("BookMessageAdapterFailed"))
            Toast.makeText(context, "该图书已存在", Toast.LENGTH_SHORT).show();
        else if (intent.getAction().equals("BookshelfAdapterSuccess"))
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
    }
}
