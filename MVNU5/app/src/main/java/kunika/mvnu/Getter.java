package kunika.mvnu;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Getter extends FragmentPagerAdapter {
    private int numOfTabs;
    public Getter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i)
        {
            case 0:
                return new Events();
            case 1:
                return new Participated();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}