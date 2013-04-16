package com.paradigmcreatives.sqliteproduct;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRecord extends Activity implements OnClickListener {

	private Button btn_addRecord;
	private EditText edit_pid, edit_pname, edit_pprice;
	DatabaseHelper db;
	ProductModel pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrecord);

		edit_pprice = (EditText) findViewById(R.id.txtpprice);
		edit_pname = (EditText) findViewById(R.id.txtpname);
		btn_addRecord = (Button) findViewById(R.id.save_btn);

		edit_pid = (EditText) findViewById(R.id.txtpid);
		btn_addRecord.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_btn:
			if (edit_pname.getText().toString().equals("")
					|| edit_pprice.getText().toString().equals("")) {
				Toast.makeText(AddRecord.this, "Pleae add values",
						Toast.LENGTH_LONG).show();
			} else {
				db = new DatabaseHelper(getApplicationContext());
				db.getWritableDatabase();
				pm = new ProductModel();
				pm.idno = (edit_pid.getText().toString());
				pm.productname = edit_pname.getText().toString();
				pm.productprice = edit_pprice.getText().toString();

				Log.i("idno, productname, product price", "" + pm.idno + ""
						+ pm.productname + "" + pm.productprice);
				db.addProduct(pm);

				Toast.makeText(AddRecord.this, "record added successfully",
						Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

}
