package edu.csi5230.maged.listviewassignment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends AppCompatActivity {
   Intent m_Intent = null;
   EditText txtProductName = null;
   EditText txtProductPrice = null;
   ImageView imgProductImage = null;
   Button buttonLoadImage = null;
   Button btnCancel = null;
   Button btnSave = null;
   String m_imgDecodableString = null;
   TextView txtOperationType = null;

   private static int RESULT_LOAD_IMAGE = 1;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_product_details);

      m_Intent = getIntent();
      txtProductName = (EditText) findViewById(R.id.txtAddUpdate_ProductName);
      txtProductPrice = (EditText) findViewById(R.id.txtAddUpdate_ProductPrice);
      imgProductImage = (ImageView) findViewById(R.id.imgProductAddUpdate);
      txtOperationType = (TextView) findViewById(R.id.txtOperationType);

      imgProductImage.setBackgroundColor(0);
      txtOperationType.setText(m_Intent.getStringExtra("OperationType"));

      if (m_Intent.getStringExtra("OperationType").equals("UPDATE"))
      {
         txtProductName.setText(m_Intent.getStringExtra("ProductName"));
         txtProductPrice.setText(m_Intent.getStringExtra("ProductPrice"));
         imgProductImage.setImageResource(m_Intent.getIntExtra("ProductImageId", 0));
      }

      buttonLoadImage = (Button) findViewById(R.id.btnSelectImage);
      buttonLoadImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View arg0) {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, RESULT_LOAD_IMAGE);
         }
      });

      btnCancel = (Button) findViewById(R.id.btnCancel);
      btnCancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View arg0)  {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
         }
      });

      btnSave = (Button) findViewById(R.id.btnSave);
      btnSave.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View arg0)  {
            Intent intent = new Intent();
            intent.putExtra("OperationType", txtOperationType.getText().toString());
            intent.putExtra("ProductName", txtProductName.getText().toString());
            intent.putExtra("ProductPrice", txtProductPrice.getText().toString());
            //intent.putExtra("ProductImageId", imgProductImage.getId());
            setResult(RESULT_OK, intent);
            finish();
         }
      });

   }

   @Override
   public void finish()
   {

      super.finish();
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      try {
         // When an Image is picked
         if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                 && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            m_imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            // Set the Image in ImageView after decoding the String
            imgProductImage.setImageBitmap(BitmapFactory.decodeFile(m_imgDecodableString));

            Toast.makeText(this, "Image Selected!",
                    Toast.LENGTH_LONG).show();

         } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
         }
      } catch (Exception e) {
         Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                 .show();
      }
   }
}
