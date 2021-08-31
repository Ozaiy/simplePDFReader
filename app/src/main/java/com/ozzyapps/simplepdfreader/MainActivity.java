package com.ozzyapps.simplepdfreader;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private PDFView pdfView;
    private final int STORAGE_PERMISSION = 1;
    private TextView pageCounter;
    private Uri currentPDF_URI = null;
    private int currentPage = 0;
    private int maxPageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestStoragePermission();
        }

        pageCounter = findViewById(R.id.pageCounter);
        pdfView = findViewById(R.id.pdfView);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            createPdfView(currentPDF_URI);

        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            createPdfView(currentPDF_URI);
        }
    }

    public void createPdfView(Uri uri) {
        pdfView.fromUri(uri)
                .enableAntialiasing(true).defaultPage(currentPage).onPageChange(new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {
                pageCounter.setText(page + 1 + "/" + pageCount);
                currentPage = page;
                maxPageCount = pageCount;
            }
        }).enableAntialiasing(true)
                .enableSwipe(true)
                .enableDoubletap(true)
                .load();
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    createPdfView(uri);
                    pageCounter.setVisibility(View.VISIBLE);
                    currentPDF_URI = uri;
                }
            });

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.find_pdf) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    mGetContent.launch("*/*");
                } else {

                    requestStoragePermission();

                }
                return true;
            }
            if (item.getItemId() == R.id.exit_pdf) {
                if(currentPDF_URI == null){
                    Toast.makeText(MainActivity.this, "Open a file first", Toast.LENGTH_SHORT).show();
                }else {
                    createPdfView(null);
                    currentPDF_URI = null;
                    pageCounter.setText("0/0");
                    pageCounter.setVisibility(View.INVISIBLE);
                }

            }
            return false;
        }
    };

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("File Permission Needed")
                    .setMessage("Need permission to access files")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }
}