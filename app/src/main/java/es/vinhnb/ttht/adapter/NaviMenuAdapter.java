package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;

/**
 * Created by VinhNB on 11/21/2017.
 */

public class NaviMenuAdapter extends ArrayAdapter<NaviMenuAdapter.NaviMenu> {
    private INaviMenuAdapter iNaviMenuAdapter;
    private ArrayList<NaviMenu> menuArrayList = new ArrayList<>();
    private int posOldClick;

    public NaviMenuAdapter(Context context, ArrayList<NaviMenu> menuArrayList) throws Exception {
        super(context, 0, menuArrayList);
        this.menuArrayList.clear();
        this.menuArrayList.addAll(menuArrayList);


        if (context instanceof INaviMenuAdapter)
            iNaviMenuAdapter = (INaviMenuAdapter) context;
        else
            throw new ClassCastException("Must be implement INaviMenuAdapter interface!");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Save view
        // Get the data item for this position
        NaviMenuHolder naviMenuHolder;
        final NaviMenu naviMenuData = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_tththn_navi_menu, parent, false);


            naviMenuHolder = new NaviMenuHolder();
            naviMenuHolder.ibtnIcon = (ImageButton) convertView.findViewById(R.id.ibtn_nav_icon_menu);
            naviMenuHolder.tvText = (TextView) convertView.findViewById(R.id.tv_nav_menu);

            naviMenuHolder.ibtnIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iNaviMenuAdapter.doClickNaviMenu(position);
                }
            });


            convertView.setTag(naviMenuHolder);
        } else
            naviMenuHolder = (NaviMenuHolder) convertView.getTag();


        //set background
        naviMenuHolder.tvText.setText(naviMenuData.text);
        naviMenuHolder.ibtnIcon.setImageResource(naviMenuData.drawableIconID);
        if (posOldClick != position)
        {
            naviMenuHolder.tvText.setTextColor(getContext().getResources().getColor(R.color.tththn_navi_menu_icon_default));
            naviMenuHolder.ibtnIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.xml_tththn_cricle));
        }
        else{
            naviMenuHolder.tvText.setTextColor(getContext().getResources().getColor(R.color.tththn_navi_menu_icon));
            naviMenuHolder.ibtnIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.xml_tththn_cricle_icon));
        }


        // Return the completed view to render on screen
        return convertView;
    }

    public void refresh(ArrayList<NaviMenu> menuArrayList, int posOldClick) {
        this.posOldClick = posOldClick;
        this.menuArrayList.clear();
        this.menuArrayList.addAll(menuArrayList);
        notifyDataSetChanged();
    }

    public static class NaviMenuHolder {
        public ImageButton ibtnIcon;
        public TextView tvText;

    }

    public static class NaviMenu {
        public int drawableIconID;
        public String text;
        public boolean isClicked;

        public NaviMenu(int drawableIconID, String text) {
            this.drawableIconID = drawableIconID;
            this.text = text;
        }

        public NaviMenu setClicked(boolean clicked) {
            isClicked = clicked;
            return this;
        }
    }

    public interface INaviMenuAdapter {
        void doClickNaviMenu(int pos);
    }
}
