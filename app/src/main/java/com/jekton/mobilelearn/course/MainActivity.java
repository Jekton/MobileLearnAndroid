/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jekton.mobilelearn.course;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;
import com.jekton.mobilelearn.common.network.CredentialStorage;
import com.jekton.mobilelearn.login.LoginActivity;

import java.util.List;

public class MainActivity extends DialogEnabledActivity<MainActivityOps, MainActivityDocumentOps>
        implements AdapterView.OnItemClickListener, MainActivityOps {

    private static final int DRAWER_ITEM_MY_COURSES = 0;
    private static final int DRAWER_ITEM_ALL_COURSES = 1;
    private static final int DRAWER_ITEM_LOGOUT = 2;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int mPreviousSelection;
    private ActionBarDrawerToggle mDrawerToggle;

    private CourseListOps mListOps;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOpTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDocument(this, MainActivityDocument.class);

        initDrawer();
        initSelectedItem(savedInstanceState);
    }

    private void initDrawer() {
        mTitle = mDrawerTitle = getTitle();
        mOpTitles = getResources().getStringArray(R.array.main_activity_drawers);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.closeDrawers();
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                                                  R.layout.drawer_list_item, mOpTitles));
        mDrawerList.setOnItemClickListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(mTitle);
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(mDrawerTitle);
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initSelectedItem(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            int selection = DRAWER_ITEM_ALL_COURSES;
            if (CredentialStorage.isLogin()) {
                selection = DRAWER_ITEM_MY_COURSES;
            }
            selectItem(selection);
            mPreviousSelection = selection;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        Fragment fragment;
        switch (position) {
            case DRAWER_ITEM_MY_COURSES:
                if (!CredentialStorage.isLogin()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    mDrawerList.setItemChecked(mPreviousSelection, true);
                    return;
                }
                fragment = new MyCoursesFragment();
                break;
            case DRAWER_ITEM_ALL_COURSES:
                fragment = new AllCoursesFragment();
                break;
            case DRAWER_ITEM_LOGOUT:
                showDialog();
                getDocument().onLogout();
                return;
            default:
                throw new IllegalStateException("Unknown position " + position);
        }

        mListOps = (CourseListOps) fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mPreviousSelection = position;
        setTitle(mOpTitles[position]);
        mDrawerLayout.closeDrawers();
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void doOnCoursesChange(final List<Course> courses) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListOps != null) {
                    mListOps.onCoursesChange(courses);
                }
                closeDialog();
            }
        });
    }

    @Override
    public void onAllCoursesChange(List<Course> courses) {
        doOnCoursesChange(courses);
    }

    @Override
    public void onMyCoursesChange(List<Course> courses) {
        doOnCoursesChange(courses);
    }

    @Override
    public void onLogoutSuccess() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        CredentialStorage.setLogin(false);
                        selectItem(DRAWER_ITEM_ALL_COURSES);
                        // since we are immediately selection the "all courses" page, which will
                        // trigger the networking, there is no need in here to close the dialog
                        // closeDialog();
                    }
                }
        );
    }

    @Override
    public void onGetCoursesFail() {
        showToastAndDismissDialog(R.string.msg_fail_get_course_list);
    }

    @Override
    public void onNetworkError() {
        showToastAndDismissDialog(R.string.err_network_error);
    }


    public interface CourseListOps {

        void onCoursesChange(List<Course> courses);

    }


    public static class AllCoursesFragment extends CourseListFragment {

        @Override
        protected void initCourseList() {
            getMainActivity().getDocument().onGettingAllCourses();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = CourseDetailsActivity.makeIntent(getActivity(), getCourses().get(position));
            startActivity(intent);
        }
    }


    public static class MyCoursesFragment extends CourseListFragment {

        @Override
        protected void initCourseList() {
            getMainActivity().getDocument().onGettingMyCourses();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO: 2/22/2016
            Intent intent = CourseDetailsActivity.makeIntent(getActivity(), getCourses().get(position));
            startActivity(intent);
        }
    }
}