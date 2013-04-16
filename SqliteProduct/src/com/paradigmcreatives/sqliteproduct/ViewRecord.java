package com.paradigmcreatives.sqliteproduct;

import java.util.ArrayList;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewRecord extends Activity {
	private ListView listView;
	private TextView totalRecords;
	DatabaseHelper db;
	public ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewrecord);
		totalRecords = (TextView) findViewById(R.id.totalrecords);
		listView = (ListView) findViewById(R.id.list);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		productList.clear();
		db = new DatabaseHelper(getApplicationContext());
		db.getWritableDatabase();
		ArrayList<ProductModel> product_list = db.getProducts();
		
		for ( int i = 0; i < product_list.size(); i ++) {
			String tidno = product_list.get(i).getIdno();
			String tpname = product_list.get(i).getProductname();
			String tpprice = product_list.get(i).getProductprice();
			
			ProductModel productModel = new ProductModel();
			productModel.setIdno(tidno);
			productModel.setProductname(tpname);
			productModel.setProductprice(tpprice);
			productList.add(productModel);
		}
		totalRecords.setText("Total records size :" +productList.size());
	    listView.setAdapter(new Listadapter(this));
	    db.close();

}
}
