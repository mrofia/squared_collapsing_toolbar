package com.example.testrofi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button gridItems[][];
    JSONObject apiResponse;
    JSONObject talentVersion;
    GridLayout mGridLayout;
    int numberOfGlobalLayoutCalled = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout=findViewById(R.id.mygrid);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("WLB");

        collapsingToolbarLayout.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(
                ContextCompat.getColor(this, R.color.colorPrimary));


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        AppBarLayout abl=findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams ap=(CoordinatorLayout.LayoutParams) abl.getLayoutParams();
        ap.height=width;
        abl.setLayoutParams(ap);


        try {
            apiResponse=new JSONObject(TalentVersion.jsonString);
            talentVersion=apiResponse.getJSONObject("data").getJSONArray("talent_versions").getJSONObject(0);

            gridItems=new Button[talentVersion.getInt("box_height")][talentVersion.getInt("box_width")];
            mGridLayout.setColumnCount(talentVersion.getInt("box_width"));
            mGridLayout.setRowCount(talentVersion.getInt("box_height"));

            for(int y=0;y<gridItems.length;y++){
                for(int x=0;x<gridItems[y].length;x++){
                    Button tView=new Button(this);
                    tView.setText("("+x+","+y+")");
                    gridItems[y][x]=tView;
                    mGridLayout.addView(tView);
                }
            }

            mGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
        
                    @Override
                    public void onGlobalLayout() {
        
                        final int MARGIN = 5;
        
                        int pWidth = mGridLayout.getWidth();
                        int pHeight = mGridLayout.getHeight();
                        int numOfCol = mGridLayout.getColumnCount();
                        int numOfRow = mGridLayout.getRowCount();
                        int w = pWidth/numOfCol;
                        int h = pHeight/numOfRow;
        
                        for(int yPos=0; yPos<numOfRow; yPos++){
                            for(int xPos=0; xPos<numOfCol; xPos++){
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams)gridItems[yPos][xPos].getLayoutParams();
                                params.width = w - 2*MARGIN;
                                params.height = h - 2*MARGIN;
                                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                                gridItems[yPos][xPos].setLayoutParams(params);
                            }
                        }
        
                        Toast.makeText(MainActivity.this,
                                "numberOfGlobalLayoutCalled = " + numberOfGlobalLayoutCalled,
                                Toast.LENGTH_SHORT).show();
                        numberOfGlobalLayoutCalled++;
        
                        //deprecated in API level 16
                        mGridLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //for API Level >= 16
                        //mGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }});


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
