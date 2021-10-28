package com.example.pexelimages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class PexelDetails extends AppCompatActivity {

    PexelImage pexelImage;
    ImageView bigImage;
    TextView imageDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pexel_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //create back button
        Intent intent = getIntent();

        bigImage = findViewById(R.id.bigImage);
        imageDetails = findViewById(R.id.imageDetails);
        pexelImage = (PexelImage) intent.getParcelableExtra("pexelImage"); //get the clicked image details

        //details of the image stored in variables
        String url = pexelImage.getOriginalUrl();
        int id = pexelImage.getId();
        int photographerId = pexelImage.getPhotographerId();
        String photographer = pexelImage.getPhotographer();
        setTitle(photographer);

        if(isNetworkAvailable()){ //if network is available
            Glide.with(getApplicationContext()).load(url).into(bigImage); //load image from url and show in ImageView
            String details = "Image ID: " + "<b>" + id + "</b>" +
                    "<br><br>" + "Photographer ID: " + "<b>" + photographerId + "</b>" +
                    "<br><br>" + "Photographer: " + "<b>" + photographer + "</b>";
            imageDetails.setText(Html.fromHtml(details)); //display details
        } else {
            Toast.makeText(this, "No Internet. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * when back button is clicked, go back to previous activity
     * @param item button is clicked
     * @return boolean of whether the back button is clicked (true) or not(false)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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