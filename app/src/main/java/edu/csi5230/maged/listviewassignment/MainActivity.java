package edu.csi5230.maged.listviewassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
   ListView listView = null;
   ProductAdapter productAdapter = null;
   int MC_ItemIndex = -1;
   String MC_Operation = "";
   Button btnAdd = null;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      listView = (ListView) findViewById(R.id.lvProduct);

      productAdapter = new ProductAdapter();

      productAdapter.addProduct("Galaxy Note 8", "$700", R.drawable.galaxy);
      productAdapter.addProduct("iPhone", "$900", R.drawable.iphone);
      productAdapter.addProduct("Nikon", "$1500.45", R.drawable.nikon);

      listView.setAdapter(productAdapter);

      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             MC_ItemIndex = i;
            MC_Operation = "";
            Toast toast = null;

            long viewId = view.getId();
            if (viewId == R.id.imageDelete_Button) {
               productAdapter.removeProduct(i);
               listView.setAdapter(productAdapter);
               toast = Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_LONG);
               toast.show();
            }

            if (viewId == R.id.btnUpdate) {
                MC_Operation = "UPDATE";

               Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
               intent.putExtra("OperationType", MC_Operation);
               intent.putExtra("ProductName", ((Product) productAdapter.getItem(i)).getName());
               intent.putExtra("ProductPrice", ((Product) productAdapter.getItem(i)).getPrice());
               intent.putExtra("ProductImageId", ((Product) productAdapter.getItem(i)).getImage());
               startActivityForResult(intent, 555);
            }
         }
      });

      btnAdd = (Button) findViewById(R.id.btnAdd);
      btnAdd.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view)  {
            Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
            intent.putExtra("OperationType", "ADD");
            startActivityForResult(intent, 555);
         }
      });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      try {
         // When an Image is picked
         if (requestCode == 555 && resultCode == RESULT_OK && null != data) {
            if (data.getStringExtra("OperationType").equals("ADD"))
            {
               productAdapter.addProduct(
                       data.getStringExtra("ProductName")
                       , data.getStringExtra("ProductPrice")
                       , R.drawable.galaxy);
               listView.setAdapter(productAdapter);
            }

            if (data.getStringExtra("OperationType").equals("UPDATE"))
            {
               ((Product) productAdapter.getItem(MC_ItemIndex)).setName(data.getStringExtra("ProductName"));
               ((Product) productAdapter.getItem(MC_ItemIndex)).setPrice(data.getStringExtra("ProductPrice"));
               //((Product) productAdapter.getItem(m_CurrentItemIndex)).setImage(data.getIntExtra("ProductImageId", 0));
               listView.setAdapter(productAdapter);
            }
         } else {
            Toast.makeText(this, "Operation Canceled!",
                    Toast.LENGTH_LONG).show();
         }
      } catch (Exception e) {
         Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                 .show();
      }
   }

}