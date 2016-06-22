package com.example.poonam.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by poonam on 6/19/16.
 */
public class ItemEditActivity extends Activity  {
    public final static String MODULE = "ItemEditAcitivity";

    EditText etEditItem;
    TextView tvSetDate;

    public final static String INPUT_TEXT = "inputText";
    public final static String INPUT_POSITION = "inputPosition";

    public final static String EDITED_TEXT = "editedText";
    public final static String EDITED_POSITION = "editedPosition";
    String itemText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_edit_item);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        /*this.getActionBar().setDisplayShowTitleEnabled(true);
        this.getActionBar().setTitle(R.string.title_activity_display_message);
*/

        itemText = getIntent().getStringExtra(INPUT_TEXT);
        etEditItem = (EditText) findViewById(R.id.editText2);
        etEditItem.setText(itemText);
        etEditItem.setSelection(etEditItem.getText().length());



    }

    public void saveTodoItem(View c) {

        String oldValue = itemText;

        etEditItem = (EditText) findViewById(R.id.editText2);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert Dialog");
        alertDialogBuilder.setMessage("Do you want to change "+  oldValue + " in to " + etEditItem.getText().toString() );
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        int position = getIntent().getIntExtra(INPUT_POSITION, 0);
                        Intent ChangeData = new Intent();
                        ChangeData.putExtra(EDITED_TEXT, etEditItem.getText().toString());
                        ChangeData.putExtra(EDITED_POSITION, position);
                        setResult(RESULT_OK, ChangeData);
                        finish(); // pass data to MainActivity

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



    }


}