package com.example.vmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

// for npermission of songs from externan file----->
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                       
                       // Here make a array for reading our songs and store it in------>
                       ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                       String [] items = new String[mySongs.size()];
                      // for showing or songs name ----->
                      for (int i=0; i<mySongs.size(); i++){
                          items[i] = mySongs.get(i).getName().replace(".mp3","");
                      }
                     // for display this by using arrayAdapter---->
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                      listView.setAdapter(adapter);
                      // here we click on any songs then go to other playing display by help of intent------->
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Intent intent = new Intent(MainActivity.this,PlaySong.class);


                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList",mySongs);
                                intent.putExtra("currentSong",currentSong);
                               intent.putExtra("position",position);
                                startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                })
                .check();
    }
    // here finding the songs in my external memory and show here app------>
    public ArrayList<File> fetchSongs(File file){
       ArrayList arrayList = new ArrayList();
       File [] songs = file.listFiles();
       if(songs !=null){
           for(File myFile: songs){
               if(!myFile.isHidden() && myFile.isDirectory()){
                   arrayList.addAll(fetchSongs(myFile));
               }
               else{
                   if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                       arrayList.add(myFile);
                   }
               }
           }
       }
       return arrayList;

    }
}