package com.example.poonam.todo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String MODULE = "MainActivity";
    private final int REQUEST_CODE = 10;

    ArrayList<String> items;

    ArrayList<Long> ids;

    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    private DataSourceTodo itemsDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.listView);
        items = new ArrayList<String>();
        ids = new ArrayList<Long>();

        itemsDataSource = new DataSourceTodo(this);
        itemsDataSource.open();
        List<Item> entries = itemsDataSource.getAllItems();

        for (Item entry : entries) {
            items.add(entry.getItem());
            ids.add(entry.getId());
        }

        itemsAdapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
        setupListClickListener();
    }

    public void addTodoItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        if (etNewItem.getText().length() > 0) {
            Item item = itemsDataSource.createItem(etNewItem.getText().toString());
            itemsAdapter.add(item.getItem());
            etNewItem.setText("");
            ids.add(item.getId());
        }
    }


    public void ClearAllItems(View v){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert Dialog");
        alertDialogBuilder.setMessage("Confirm to clear all items");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        final ProgressDialog progresRing = ProgressDialog.show(MainActivity.this, "Clearing all Items", "Wait Loading......", true);
                        //progresRing.setCancelable(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);

                                } catch (Exception e) {

                                }
                                progresRing.dismiss();
                            }
                        }).start();
                        items.clear();
                        ids.clear();
                        itemsAdapter.notifyDataSetChanged();


                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // dialog.this.finish();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



       /* itemsAdapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);*/

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Item item = new Item();
                item.setItem(items.get(position));
                item.setId(ids.get(position));
                itemsDataSource.deleteItem(item);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                ids.remove(position);

                return true;
            }
        });
    }

    private void setupListClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                launchEditView(position);
            }
        });
    }

    public void launchEditView(int position) {
        Intent intent = new Intent(this, ItemEditActivity.class);
        intent.putExtra(ItemEditActivity.INPUT_POSITION, position);
        intent.putExtra(ItemEditActivity.INPUT_TEXT, items.get(position));

        startActivityForResult(intent, REQUEST_CODE); // brings up the edit item activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedText = data.getExtras().getString(ItemEditActivity.EDITED_TEXT);


            if (editedText.length() > 0) {
                int position = data.getIntExtra(ItemEditActivity.EDITED_POSITION, items.size());
                Item item = new Item();
                item.setItem(items.get(position));

                item.setId(ids.get(position));
                itemsDataSource.deleteItem(item);

                items.set(position, editedText);
                itemsAdapter.notifyDataSetChanged();

                item = itemsDataSource.createItem(editedText);
                ids.set(position, item.getId());
            }
        }
    }
}
