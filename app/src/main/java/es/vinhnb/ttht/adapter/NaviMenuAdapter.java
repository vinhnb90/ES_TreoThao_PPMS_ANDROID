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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;

import es.vinhnb.ttht.view.TthtHnMainActivity;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
            naviMenuHolder.rlView = (RelativeLayout) convertView.findViewById(R.id.rl_nav);
            naviMenuHolder.vLine = (View) convertView.findViewById(R.id.v_view_line);

            final int positionFinal = position;
            naviMenuHolder.ibtnIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iNaviMenuAdapter.doClickNaviMenu(positionFinal, naviMenuData.tagMenuNaviLeft);
                }
            });


            convertView.setTag(naviMenuHolder);
        } else
            naviMenuHolder = (NaviMenuHolder) convertView.getTag();


        //if menuTag is line
        if (naviMenuData.tagMenuNaviLeft.typeViewMenu == TthtHnMainActivity.TypeViewMenu.EMPTY)
            naviMenuHolder.rlView.setVisibility(View.GONE);
        if (naviMenuData.tagMenuNaviLeft.typeViewMenu == TthtHnMainActivity.TypeViewMenu.LINE) {
            naviMenuHolder.vLine.setVisibility(View.VISIBLE);
            naviMenuHolder.ibtnIcon.setVisibility(View.GONE);
            naviMenuHolder.tvText.setVisibility(View.GONE);
        }
        if (naviMenuData.tagMenuNaviLeft.typeViewMenu == TthtHnMainActivity.TypeViewMenu.VIEW) {
            naviMenuHolder.rlView.setVisibility(View.VISIBLE);
            naviMenuHolder.vLine.setVisibility(View.GONE);
            naviMenuHolder.ibtnIcon.setVisibility(View.VISIBLE);
            naviMenuHolder.tvText.setVisibility(View.VISIBLE);
        }


        //set background
        naviMenuHolder.tvText.setText(naviMenuData.tagMenuNaviLeft.title);
        naviMenuHolder.ibtnIcon.setImageResource(naviMenuData.tagMenuNaviLeft.drawableIconID);
        if(naviMenuData.tagMenuNaviLeft.typeViewMenu == TthtHnMainActivity.TypeViewMenu.VIEW) {
            if (posOldClick != position) {
                naviMenuHolder.tvText.setTextColor(getContext().getResources().getColor(R.color.tththn_navi_menu_icon_default));
                naviMenuHolder.ibtnIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.xml_tththn_cricle));
            } else {
                naviMenuHolder.tvText.setTextColor(getContext().getResources().getColor(R.color.tththn_navi_menu_icon));
                naviMenuHolder.ibtnIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.xml_tththn_cricle_type2));
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }


    public void refresh(ArrayList<NaviMenu> menuArrayList, int posNew) {
        if (posNew != 0)
            this.posOldClick = posNew;

        this.menuArrayList.clear();
        this.menuArrayList.addAll(menuArrayList);
        notifyDataSetChanged();
    }

    public void refresh(TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft) {
        //find NaviMenu
        for (int i = 0; i< menuArrayList.size(); i++)
        {
            if(menuArrayList.get(i).tagMenuNaviLeft == tagMenuNaviLeft)
            {
                this.posOldClick = i;
                break;
            }
        }

        notifyDataSetChanged();
    }

    public static class NaviMenuHolder {
        public ImageButton ibtnIcon;
        public TextView tvText;
        public RelativeLayout rlView;
        public View vLine;
    }

    public static class NaviMenu {
        public TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft;


        public NaviMenu(TthtHnMainActivity.TagMenuNaviLeft tagMenuNaviLeft) {
            this.tagMenuNaviLeft = tagMenuNaviLeft;
        }
    }

    public interface INaviMenuAdapter {
        void doClickNaviMenu(int pos, TthtHnMainActivity.TagMenuNaviLeft tagNew);
    }
}
