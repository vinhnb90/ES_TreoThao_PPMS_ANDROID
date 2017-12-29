package es.vinhnb.ttht.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import static android.support.design.widget.BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_SWIPE;

/**
 * Created by VinhNB on 11/11/2017.
 */

public abstract class TthtHnBaseActivity extends AppCompatActivity implements IBaseView {
    public static final String BUNDLE_LOGIN = "BUNDLE_LOGIN";
    public static final String BUNDLE_MA_NVIEN = "BUNDLE_MA_NVIEN";
    public static final String BUNDLE_TAG_MENU = "BUNDLE_TAG_MENU";
    public static final String BUNDLE_ID_BBAN_TRTH = "BUNDLE_ID_BBAN_TRTH";
    public static final String BUNDLE_ID_BBAN_TUTI = "BUNDLE_ID_BBAN_TUTI";

    public static final String BUNDLE_POS = "BUNDLE_POS";
    public static final String BUNDLE_PRE_CTO = "BUNDLE_PRE_CTO";
    public static final String BUNDLE_NEXT_CTO = "BUNDLE_NEXT_CTO";
    public static final String BUNDLE_MA_BDONG = "BUNDLE_MA_BDONG";
    public static final String BUNDLE_TYPE_TOPMENU = "BUNDLE_TYPE_TOPMENU";

    public static final String BUNDLE_TYPE_SEARCH_STORE = "BUNDLE_TYPE_SEARCH_STORE";
    public static final String BUNDLE_MESSAGE_SEARCH_STORE = "BUNDLE_TYPE_MESSAGE_SEARCH";

    protected Snackbar snackbar;
    protected CoordinatorLayout coordinatorLayout;
    protected Bundle savedInstanceState;
    protected ArrayList<SnackQueue> snackbarQueues = new ArrayList<>();


    class SnackQueue {
        public String message;
        public @Nullable
        String content;
        public @Nullable
        ISnackbarIteractions actionOK;

        public SnackQueue(String message, String content, ISnackbarIteractions actionOK) {
            this.message = message;
            this.content = content;
            this.actionOK = actionOK;
        }
    }

    protected void setupFullScreen() {
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.e("TthtHnBaseActivity", "setupFullScreen: " + e.getMessage());
        }
    }

    public void showSnackBar(String message, @Nullable String content, @Nullable final ISnackbarIteractions actionOK) {
        try {
            //check
            if (coordinatorLayout == null)
                throw new RuntimeException("Be must set view CoordinatorLayout!");


            snackbar = Snackbar
                    .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });


            //add vào hàng đợi
            SnackQueue queue = new SnackQueue(message, content, actionOK);
            snackbarQueues.add(queue);

            showSbar(queue);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSbar(final SnackQueue snackQueue) {
        final Snackbar.Callback snackCallback = new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                for (Iterator<SnackQueue> iter = snackbarQueues.listIterator(); iter.hasNext(); ) {
                    SnackQueue a = iter.next();
                    if (snackQueue == a) {
                        iter.remove();
                    }
                }

//                snackbarQueues.remove(snackQueue);
                if (snackbarQueues.size() > 0) {
                    showSbar(snackbarQueues.get(0));
                } else {
//                    snackbar.removeCallback((BaseTransientBottomBar.BaseCallback<Snackbar>)snackCallback);
                    snackbar.addCallback(new Snackbar.Callback());
                    snackbar.dismiss();
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        };
        snackbar.addCallback(snackCallback);


        if (snackbarQueues.size() == 1) {
            // Hide the text and set width full screen
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            ((TextView) layout.findViewById(android.support.design.R.id.snackbar_text)).setVisibility(View.INVISIBLE);
            ((Button) layout.findViewById(android.support.design.R.id.snackbar_action)).setVisibility(View.INVISIBLE);


            // Inflate our custom view
            final View snackView = this.getLayoutInflater().inflate(R.layout.tththn_snackbar, null);
            TextView tvMessage = (TextView) snackView.findViewById(R.id.tv_snackbar_message);
            Button btnOk = (Button) snackView.findViewById(R.id.btn_snackbar_ok);
            Button btnContent = (Button) snackView.findViewById(R.id.btn_snackbar_content);
            final EditText etContent = (EditText) snackView.findViewById(R.id.et_snackbar_content);


            //set value
            tvMessage.setText(snackQueue.message);
            if (TextUtils.isEmpty(snackQueue.content)) {
                btnContent.setVisibility(View.GONE);
                etContent.setVisibility(View.GONE);
            } else {
                etContent.setText(snackQueue.content);
                btnContent.setVisibility(View.VISIBLE);
                etContent.setVisibility(View.VISIBLE);
            }

            //catch action
            btnContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etContent.getVisibility() == View.GONE)
                        etContent.setVisibility(View.VISIBLE);
                    else
                        etContent.setVisibility(View.GONE);
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (snackQueue.actionOK != null)
                        snackQueue.actionOK.doIfPressOK();
                    else {
                        snackCallback.onDismissed(snackbar, DISMISS_EVENT_SWIPE);
                    }
                }
            });

            // Add the view to the Snackbar's layout and show
            layout.addView(snackView, 0);

            snackbar.show();
        }

    }

    @Deprecated
    protected void doNotHideSnackbar(Snackbar snackbar) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException {
        final Field sHandler = BaseTransientBottomBar.class.getDeclaredField("sHandler");
        sHandler.setAccessible(true);
        final Method handleMessage = Handler.class.getMethod("handleMessage", Message.class);
        final Handler originalHandler = (Handler) sHandler.get(snackbar);
        Handler decoratedHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        try {
                            handleMessage.invoke(originalHandler, Message.obtain(originalHandler, 0));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });
        sHandler.set(snackbar, decoratedHandler);

    }

    @Deprecated
    protected void showSnackBar(String message, @Nullable final ISnackbarIteractions actionOK) {
        try {

            //check
            if (coordinatorLayout == null)
                throw new RuntimeException("Be must set view CoordinatorLayout!");


            snackbar = Snackbar
                    .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });


            // Hide the text and set width full screen
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            ((TextView) layout.findViewById(android.support.design.R.id.snackbar_text)).setVisibility(View.INVISIBLE);
            ((Button) layout.findViewById(android.support.design.R.id.snackbar_action)).setVisibility(View.INVISIBLE);


            // Inflate our custom view
            View snackView = this.getLayoutInflater().inflate(R.layout.tththn_snackbar, null);
            TextView tvMessage = (TextView) snackView.findViewById(R.id.tv_snackbar_message);
            Button btnOk = (Button) snackView.findViewById(R.id.btn_snackbar_ok);
            Button btnContent = (Button) snackView.findViewById(R.id.btn_snackbar_content);
            btnContent.setVisibility(View.GONE);
            final EditText etContent = (EditText) snackView.findViewById(R.id.et_snackbar_content);
            etContent.setVisibility(View.GONE);


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (actionOK != null)
                        actionOK.doIfPressOK();
                    else
                        snackbar.dismiss();
                }
            });


            // Add the view to the Snackbar's layout and show
            layout.addView(snackView, 0);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    private interface ISnackbarIteractions {
        void doIfPressOK();
    }


}
