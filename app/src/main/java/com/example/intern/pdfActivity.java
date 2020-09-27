package com.example.intern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Canvas;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class pdfActivity extends AppCompatActivity {
    TextView fullname,email,address,number,buisness;
    Button pdf;
    LinearLayout linearLayout;
    String dirpath;
    private Context mContext=pdfActivity.this;
    private static final int REQUEST = 112;
    ImageView share,shareemail;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(mContext, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
            } else {
                //do here
            }
        } else {
            //do here
        }
        fullname=findViewById(R.id.fullname);
        share=findViewById(R.id.share);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        number=findViewById(R.id.number);
        buisness=findViewById(R.id.buisness);
        linearLayout=findViewById(R.id.linear);
        shareemail=findViewById(R.id.shareviaemail);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputFile = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS), "test-2.pdf");
                Uri uri = FileProvider.getUriForFile(pdfActivity.this, BuildConfig.APPLICATION_ID + ".provider",outputFile);

                Intent share = new Intent();
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage("com.whatsapp");

                startActivity(Intent.createChooser(share,
                        "Send Email Using: "));
            }
        });
        shareemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
                File outputFile = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS), directory_path+"test-2.pdf");

                Uri uri = FileProvider.getUriForFile(pdfActivity.this, BuildConfig.APPLICATION_ID + ".provider",outputFile);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abc@gmail.com" });
                shareIntent.putExtra(Intent.EXTRA_TEXT, "test");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent,
                        "Send Email Using: "));
            }
        });
        pdf=findViewById(R.id.pdf);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            final String strfullname = extras.getString("fullname");
            assert strfullname != null;

            final String stremail = extras.getString("email");
            final String straddress = extras.getString("address");
            final String strphone = extras.getString("number");
            Log.i("number",""+strphone);
            final String strbuisness = extras.getString("buisness");
            fullname.setText(strfullname);
            email.setText(stremail);
            address.setText(straddress);
            number.setText(strphone);
            buisness.setText(strbuisness);

            pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPdf(strfullname,stremail,straddress,strphone,strbuisness);
                }
            });
        }

    }
    public String getPDFPath(Uri uri){

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void createPdf(String strfullname1, String stremail1, String straddress1, String sometext, String sometext1){
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("Fullname : ", 50, 50, paint);
        canvas.drawText("Email : ", 50, 80, paint);
        canvas.drawText("Address : ", 50, 110, paint);
        canvas.drawText("Phone : ", 50, 140, paint);
        canvas.drawText("Buisness : ", 50, 170, paint);
        canvas.drawText(strfullname1, 100, 50, paint);
        canvas.drawText(stremail1, 100, 80, paint);
        canvas.drawText(straddress1, 100, 110, paint);
        canvas.drawText(sometext, 100, 140, paint);
        canvas.drawText(sometext1, 100, 170, paint);

        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Please look for pdf inside Internal storage", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
}
