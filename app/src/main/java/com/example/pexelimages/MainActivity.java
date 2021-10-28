package com.example.pexelimages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //initialization
    RecyclerView recyclerView;
    NestedScrollView scrollView;
    List<PexelImage> pexelImageList;
    PexelAdapter pexelAdapter;
    GridLayoutManager gridLayoutManager;

    boolean scrolling = false; //check if user is scrolling or not
    int pageNum = 1;
    String url = "https://api.pexels.com/v1/curated/?page="+pageNum+"&per_page=20"; //pexel api url
    int currentItems;
    int totalItems;
    int scrollOutItems;

    //for changing layouts
    private final int GRID_SPAN_COUNT = 2;
    private final int LIST_SPAN_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pexelImageList = new ArrayList<>();
        recyclerView = findViewById(R.id.gridViewItem);
        scrollView = findViewById(R.id.scrollView);
        gridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { //detect user scrolling
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) { //when scrolling
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    scrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) { //during scrolling
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if(scrolling && (currentItems+scrollOutItems==totalItems)){
                    scrolling = false;
                    check();
                }
            }
        });

        pexelAdapter = new PexelAdapter(this, pexelImageList, gridLayoutManager);
        recyclerView.setAdapter(pexelAdapter);

        check();

    }

    /**
     * check to see internet connection is available or not
     */
    private void check(){
        if(isNetworkAvailable())
        {
            fetchPexel();
        } else{
            Toast.makeText(this, "No Internet. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * fetch images from pexel api
     * Each image's id, url of image in original size, url of image in medium size, photographer id and name are fetched
     * Each image details are stored in the list and shown to the user
     */
    private void fetchPexel(){
        StringRequest request = new StringRequest( //request to api
            Request.Method.GET,
            url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray= jsonObject.getJSONArray("photos"); //get image details

                        //loop all the fetched images
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject item = jsonArray.getJSONObject(i);
                            int id = item.getInt("id");
                            int photographerId = item.getInt("photographer_id");
                            String photographer = item.getString("photographer");

                            JSONObject itemImage = item.getJSONObject("src");
                            String orignalUrl = itemImage.getString("original");
                            String mediumUrl = itemImage.getString("medium");

                            PexelImage pexelImage = new PexelImage(
                                    id,
                                    orignalUrl,
                                    mediumUrl,
                                    photographerId,
                                    photographer);
                            pexelImageList.add(pexelImage); //store in list
                        }
                        pexelAdapter.notifyDataSetChanged(); //display the fetched items
                        pageNum++; //increment page number to get images from the next page
                    }catch (JSONException e){
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            })
        {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError { //this is to get access tot he images in the api
                    Map<String,String> params = new HashMap<>();
                    params.put("Authorization","563492ad6f9170000100000182bdfc9764984ca1898a3ca35d6f181b");
                    return params;
                }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    /**
     * inflate the custom menu layout to the action bar
     * @param menu the menu option in action bar
     * @return boolean true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return true;
    }

    /**
     * when the switch layout icon is clicked
     * @param item item clicked
     * @return true if clicked, or false if not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_switch_layout) {
            switchViewLayout();
            switchIcon(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * switch the viewing layout,
     * if the current layout is a list, change to grid layout
     * if current layout is the grid layout, change to list layout
     */
    private void switchViewLayout() {
        if (gridLayoutManager.getSpanCount() == GRID_SPAN_COUNT) {
            gridLayoutManager.setSpanCount(LIST_SPAN_COUNT);
        } else {
            gridLayoutManager.setSpanCount(GRID_SPAN_COUNT);
        }
        pexelAdapter.notifyItemRangeChanged(0, pexelAdapter.getItemCount());
    }

    /**
     * if the current layout is grid, the icon will show the list layout icon and vice versa
     * @param item item clicked
     */
    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == GRID_SPAN_COUNT) {
            item.setIcon(getResources().getDrawable(R.drawable.span_1));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.icon_4));
        }
    }

    /**
     * check whether the device is connected to the network or not
     * @return true if internet is available, false if not
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}