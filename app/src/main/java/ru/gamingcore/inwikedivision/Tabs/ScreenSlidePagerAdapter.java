package ru.gamingcore.inwikedivision.Tabs;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ru.gamingcore.inwikedivision.Service.MyService;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private DialogFragment[] fragments = new DialogFragment[3];
    private MyService service;
    private int proj_position = 0;
    private int allow_position = 0;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new AboutTab();
        fragments[1] = new AboutTab2();
        fragments[2] = new AboutTab3();
    }

    @Override
    public DialogFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    public void init(MyService service) {
        this.service = service;
        ((AboutTab)fragments[0]).init(service);
        ((AboutTab2)fragments[1]).init(service);
        ((AboutTab3)fragments[2]).init(service);

    }

    public void setPosition(int position,int page) {
        if(page == 1) {
            proj_position = position;
            ((AboutTab2) fragments[1]).setPosition(position);
        }
        if(page == 2) {
            allow_position = position;
            ((AboutTab3) fragments[2]).setPosition(proj_position,position);
        }
    }

    public String getAllowID() {
        return ((AboutTab3) fragments[2]).id_allow;
    }
}