package com.paradigmcreatives.sqliteproduct;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static String DATABASENAME = "androidadvancesqlite";  // our database Name
    public static String PRODUCTTABLE = "products";                    //table name
    private ArrayList<ProductModel> cartList = new ArrayList<ProductModel>();
    Context mContext;
	public DatabaseHelper(Context context) {
		super(context, DATABASENAME, null, 33);
	}

	public SQLiteDatabase getWritableDatabase() {
		return null;
		// TODO Auto-generated method stub
		
	}

	public void addProduct(ProductModel productList) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("Product Name", productList.productname);
		contentValues.put("Product Price", productList.productprice);
		db.update(PRODUCTTABLE, contentValues, "Product idno =" + productList.idno, null);
		db.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists producttable(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "productidno"
                + " TEXT ,"
                + "productname"
                + " TEXT,"
                + "productprice" + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + PRODUCTTABLE);
		onCreate(db);
	}
	
	public void emptyProduct() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from producttable");
		db.close();
	}
	
	public void removeProduct(String productid, String pname, String pprice) {
		String[] args = {productid};
		getWritableDatabase().delete("producttable", "productidno = ?", args);
	}
	
	public ArrayList<ProductModel> getProducts() {
		cartList.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from producttable", null);
		
		if(cursor.getCount() != 0) {
			if(cursor.moveToFirst()) {
				do {
					ProductModel item = new ProductModel();
					item.idno = cursor.getString(cursor.getColumnIndex("product idno"));
					item.productname = cursor.getString(cursor.getColumnIndex("product name"));
					item.productprice = cursor.getString(cursor.getColumnIndex("product price"));
					cartList.add(item);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return cartList;
		
	}

}
