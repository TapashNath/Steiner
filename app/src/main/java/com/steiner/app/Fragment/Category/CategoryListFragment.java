package com.steiner.app.Fragment.Category;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Models.Category.CategoryAdepter;
import com.steiner.app.Models.Category.CategoryModel;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.databinding.FragmentCategoryListBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.SPDataSave;

import java.util.ArrayList;
import java.util.List;

public class CategoryListFragment extends Fragment implements OnClickListeners {

    private FragmentCategoryListBinding fragmentCategoryListBinding;
    private AppCompatActivity activity;
    private OnClickListeners listeners;
    private SPDataSave spDataSave;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentCategoryListBinding = FragmentCategoryListBinding.inflate(inflater, container,false);
        setHasOptionsMenu(true);
        activity = ((AppCompatActivity)getActivity());
        listeners = this;
        spDataSave = new SPDataSave();
        Config.addToolbarWithNAme(fragmentCategoryListBinding.getRoot(),activity,fragmentCategoryListBinding.categoryToolbar.getId(),"All Category");


        List<CategoryModel> categoryModelArrayList = new ArrayList<>();

        categoryModelArrayList = new DBHandler().getCategoryData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        CategoryAdepter adepter = new CategoryAdepter(getContext(), categoryModelArrayList, listeners,activity);
        fragmentCategoryListBinding.CategoryViewList.setLayoutManager(layoutManager);
        fragmentCategoryListBinding.CategoryViewList.setAdapter(adepter);

        return fragmentCategoryListBinding.getRoot();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void OnClick(String CATEGORY, String TYPE, String ID) {
        if (CATEGORY.equals("CATEGORY")) {
            if (TYPE.equals("CLICK")) {
                spDataSave.setStr("IMAGE_CATEGORY","CATEGORY",ID);
                Config.showLongToast(ID);
                Intent intent = new Intent(getContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "IMAGE_GALLERY");
                startActivity(intent);

            }
        }
    }
}