package me.zdnuist.android.demo11;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import me.zdnuist.android.utils.ToastUtil;

/**
 * 操作联系人
 * db path: /data/data/com.android.providers.contacts/databases/contacts2.db
 * table : raw_contacts
 * @author zdnuist
 *
 */
public class Demo11Activity extends BaseActivity implements OnClickListener{

	
	Button generator;
	ListView show;
	
	/** 
     * 定义字母表的排序规则 
     */  
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo11);
		
		generator = (Button) findViewById(R.id.btn_generate);
		
		show = (ListView) findViewById(R.id.lv_contacts);
		
		generator.setOnClickListener(this);
		
		initDatas();
		
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		 Cursor cursor = getContentResolver().query(uri,
				new String[] { "_id","display_name", "sort_key" }, null, null,
				"sort_key");
		final AlphabetIndexer indexer = new AlphabetIndexer(cursor, 1, alphabet);  
		
		CursorAdapter adapter = new CursorAdapter(this,cursor){

			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent) {
				
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				View v = inflater.inflate(R.layout.item_demo11, parent, false);
				return v;
			}

			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				TextView display = (TextView) view.findViewById(R.id.tv_name);
				display.setText(cursor.getString(cursor.getColumnIndex("display_name")));
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				Cursor cursor = this.getCursor();
				int section = indexer.getSectionForPosition(position);  
		        if (position == indexer.getPositionForSection(section)) {  
		        	ToastUtil.show_s(Demo11Activity.this, cursor.getString(cursor.getColumnIndex("sort_key")));
		        }
				
				return v;
			}
			
		};
		
		
		show.setAdapter(adapter);
		show.setFastScrollEnabled(true);
	}

	/**
	 * 批量添加通讯录
	 * 
	 * @throws OperationApplicationException
	 * @throws RemoteException
	 */
	public static void batchAddContact(List<MyContacts> list, Context mContext)
			throws RemoteException, OperationApplicationException {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = 0;
		for (MyContacts contact : list) {
			rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

			ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
					.withValue(RawContacts.ACCOUNT_TYPE, null)
					.withValue(RawContacts.ACCOUNT_NAME, null)
					.withYieldAllowed(true).build());

			// 添加姓名
			ops.add(ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
					.withValue(StructuredName.DISPLAY_NAME, contact.getName())
					.withYieldAllowed(true).build());
			// 添加号码
			ops.add(ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.NUMBER, contact.getNumber())
					.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
					.withValue(Phone.LABEL, "").withYieldAllowed(true).build());
		}
		if (ops != null) {
			// 真正添加
			ContentProviderResult[] results = mContext.getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, ops);
		}
	}

	/**
	 * 往数据库中新增联系人
	 * 
	 * @param name
	 * @param number
	 */
	public static void AddContact(String name, String number,Context mContext) {

		ContentValues values = new ContentValues();
		// 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
		Uri rawContactUri = mContext.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		// 往data表插入姓名数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);// 内容类型
		values.put(StructuredName.GIVEN_NAME, name);
		mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
				values);

		// 往data表插入电话数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, number);
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
				values);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_generate:
			if(!list.isEmpty()){
				try {
					batchAddContact(list, Demo11Activity.this);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OperationApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
	}
	
	
	List<MyContacts> list = new ArrayList<MyContacts>();
	private void initDatas(){
		MyContacts contacts = null;
		for(int i = 0; i< HanziToPinyin.UNIHANS.length;i++){
			contacts = new MyContacts();
			contacts.setName(Character.toString(HanziToPinyin.UNIHANS[i]));
			contacts.setNumber("18900000" + i);
			list.add(contacts);
		}
	}

	
}
