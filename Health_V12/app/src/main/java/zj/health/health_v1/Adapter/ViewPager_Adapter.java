package zj.health.health_v1.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ViewPager_Adapter extends FragmentPagerAdapter{

    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    private FragmentTransaction mCurTransaction;



    public ViewPager_Adapter(FragmentManager fragmentManager,List<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.fragmentList = fragmentList;
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * 根据viewId和项Id生成Fragment名称
     * @param viewId
     * @param id
     * @return Fragment名称
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            // 创建新事务
            mCurTransaction = fragmentManager.beginTransaction();
        }

        // 获取单项的Id
        final long itemId = getItemId(position);

        // 根据View的Id和单项Id生成名称
        String name = makeFragmentName(container.getId(), itemId);
        // 根据生成的名称获取FragmentManager中的Fragment
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            // 如果Fragment已被添加到FragmentManager中,则只需要附着到Activity
            mCurTransaction.attach(fragment);
        } else {
            // 如果Fragment未被添加到FragmentManager中,则先获取,再添加到Activity中
            fragment = getItem(position);
            mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
        }
        // 非当前主要项,需要隐藏相关的菜单及信息
        if (fragment != fragmentList.get(position)) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            // 创建新事务
            mCurTransaction = fragmentManager.beginTransaction();
        }

        // 将Fragment移出UI,但并未从FragmentManager中移除
        mCurTransaction.detach((Fragment) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != fragmentList.get(position)) {
            // 主要项切换,相关菜单及信息进行切换
            if (fragmentList.get(position) != null) {
                fragmentList.get(position).setMenuVisibility(false);
                fragmentList.get(position).setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            fragmentList.set(position,fragment);
        }


    }
}
