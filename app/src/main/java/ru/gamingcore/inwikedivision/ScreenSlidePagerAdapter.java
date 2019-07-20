package ru.gamingcore.inwikedivision;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private DialogFragment[] fragments = new DialogFragment[1];
    private MyService service;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new AboutTab();
        //fragments[1] = new AboutTab();
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
    }
}