package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_OK_CODE = 101;
    public static final int REQUEST_DELETE_CODE = 100;
    public static final String RESULT_TITLE = "title";
    public static final String RESULT_DESCRIPTION = "desc";
    public static final String KEY_TASK = "task";
    public static final String ID_TASK = "id";
    private static final int REQUEST_IMAGE_CAPTURE = 808;
    private static final int MY_CAMERA_PERMISSION_CODE = 200;
    private final int CAPTURE_IMAGE_REQUEST = 1;
    String currentPhotoPath;

    @Nullable
    private File photoFile;
    private int taskId;
    ImageView imageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        EditText editTextName = findViewById(R.id.edit_text_name);
        EditText editTextDescription = findViewById(R.id.edit_text_description);

        Button addNewElement = findViewById(R.id.button_add);
        Button deleteElement = findViewById(R.id.delete_button);

        imageView = findViewById(R.id.image_task_iv);
        imageView.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });



        String taskAsString = getIntent().getExtras() != null ?
                getIntent().getExtras().getString(KEY_TASK, null) : null;
        if (taskAsString != null) {
            Task task = new Gson().fromJson(taskAsString, Task.class);
            taskId = task.getId();
            editTextName.setText(task.getName());
            editTextDescription.setText(task.getDescription());
            if(task.getPhotoPath() != null){
                currentPhotoPath = task.getPhotoPath();
                imageView.setImageURI(Uri.parse(currentPhotoPath));
            }
        }

        addNewElement.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();

            Task task = new Task(name, description);

            task.setPhotoPath(currentPhotoPath);

            Intent startActivityIntent = new Intent();
            String taskAsStringAdd = new Gson().toJson(task);
            startActivityIntent.putExtra(KEY_TASK, taskAsStringAdd);

            setResult(REQUEST_OK_CODE, startActivityIntent);
            finish();
        });

        deleteElement.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();
            Task task = new Task(name, description);
            task.setId(taskId);

            Intent startActivityIntent = new Intent();
            String taskAsStringAdd = new Gson().toJson(task);
            startActivityIntent.putExtra(KEY_TASK, taskAsStringAdd);

            setResult(REQUEST_DELETE_CODE, startActivityIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(Uri.parse(currentPhotoPath));
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("TEST_INFO", currentPhotoPath.toString());
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
}