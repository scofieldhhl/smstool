package com.systemteam.smstool.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.systemteam.smstool.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class SMS15Activity extends Activity
{
	String srt_ordr = SNT;
	boolean srt_typ = DEC;
	public class lst_str implements Comparable<lst_str>
	{
		String name_str, sent_str, rcvd_str;

		lst_str(String name_str, String sent_str, String rcvd_str)
		{
			//Log.d("sms8e", "lst_str_en");
			this.name_str = name_str;
			this.sent_str = sent_str;
			this.rcvd_str = rcvd_str;
			//Log.d("sms8e", "lst_str_ex");
		}

		public int compareTo(lst_str cmpr)
		{
			Integer rtrn = 1;
			//Log.d("sms8e", "lst_str_cmpr_enx");
			switch (Integer.valueOf(srt_ordr))
			{
				case 0:
					rtrn = cmpr.name_str.compareToIgnoreCase(this.name_str);
						break;
				case 1:
					rtrn = Integer.parseInt(cmpr.rcvd_str)
						- Integer.parseInt(this.rcvd_str);
						break;
				case 2:
				
			
			rtrn = Integer.parseInt(cmpr.sent_str)
					- Integer.parseInt(this.sent_str);
					break;
	default :
	rtrn = 1;
	}
	rtrn = srt_typ?rtrn:(rtrn*-1);
	return rtrn;
		}
	}

	/** Called when the activity is first created. */
	String nmbr, val, cnt, namestr;
	String oldnmbr = null;
	Cursor cur;
	int i = -1;
	boolean clk_flg;
	boolean flg;
	public static final String REC = "1";
	public static final String SNT = "2";
	public static final String NMM = "0";
	public static final boolean DEC = true;
	public static final boolean ASC = false;
	
	TextView dtvwf, dtvwt;
	static final int FRM_ID = 999;
	static final int TO_ID = 998;
	int year, yearf,yeart,month,monthf,montht, day,dayf,dayt;
	int theme = 0;
	int vid = 0;
	long ms_frm, ms_to;
	Date dt;
	String url = "content://sms";
	Uri uri = Uri.parse(url);
	String columns[] = new String[] { "address", "type", "COUNT('body')" };
	AlertDialog.Builder alrtdlg_hlp;
	AlertDialog.Builder alrtdlg_abt;
	Toast tst;
	ArrayList<lst_str> lst = new ArrayList<lst_str>();
	ArrayList<String> nmstr = new ArrayList<String>();
	HashMap<Pair<String, String>, String> hsmp = new HashMap<Pair<String,String>, String>();
	Pair<String, String>  k;
	InputStream is;
	BufferedReader br;
	String readLine = null;
	Integer sntt,rcvt;
	ImageView i1;
	SharedPreferences preferences;
	boolean srt_flg = false;
	boolean srt_flgn = false;
	boolean srt_flgs = true;
	boolean srt_flgr = false;
	public DatePickerDialog.OnDateSetListener frm_lstnr = new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int yeard, int monthd, int dayd)
		{
			//Log.d("sms8e", "dateset_en");
			if (clk_flg == true)
			{
				//Log.d("sms8e", "dateset_tr_en");
				switch (vid)
				{
					case 0:
						yearf = yeard;
						monthf = monthd;
						dayf = dayd;
						//Log.d("sms8e", "dateset_tr_f_en");
						dtvwf.setText(new StringBuilder().append(dayf)
								.append("-").append(monthf + 1).append("-")
								.append(yearf).append(" "));
						ms_frm = new Date(yearf - 1900, monthf, dayf, 00, 00)
								.getTime();
						go();
						//Log.d("sms8e", "dateset_tr_f_ex");
						break;
					case 1:
						//Log.d("sms8e", "dateset_tr_t_en");
						yeart = yeard;
						montht = monthd;
						dayt = dayd;
						dtvwt.setText(new StringBuilder().append(dayt)
								.append("-").append(montht + 1).append("-")
								.append(yeart).append(" "));
						ms_to = new Date(yeart - 1900, montht, dayt, 23, 59)
								.getTime();
						go();
						//Log.d("sms8e", "dateset_tr_t_ex");
						break;
					default:
						//Log.d("sms8e", "dateset_tr_d_enx");
						break;
				}
				clk_flg = false;
			}
			//Log.d("sms8e", "dateset_ex");
		}
		
	};


	public void addlistener()
	{
		//Log.d("sms8e", "addlstnr_en");
		dtvwf = (TextView) findViewById(R.id.frmDate);
		dtvwt = (TextView) findViewById(R.id.toDate);
		//Log.d("sms8e", "addlstnr_ex");
	}
		public void ButtonOnClick(View v){
			
			switch(v.getId()){
				case R.id.frmDate:
					vid = 0;
				clk_flg = true;
				//Log.d("sms8e", "onclick_f_en");
				showDialog(FRM_ID);
				//Log.d("sms8e", "onclick_f_ex");
				break;
				case R.id.toDate:
					vid = 1;
				clk_flg = true;
				//Log.d("sms8e", "onclick_t_en");
				showDialog(TO_ID);
				//Log.d("sms8e", "onclick_t_ex");
				break;
				case R.id.contactt:
					srt_ordr = NMM;
					srt_flgs = srt_flgr = false;
					
					if(srt_flgn)
						srt_typ = ASC;
					else
						srt_typ = DEC;
					srt_flgn = !srt_flgn;
					srt_flg = true;
					go();
					srt_flg = false;
					break;
				case R.id.recievedt:
					srt_ordr = REC;
					srt_flgn = srt_flgs = false;
					
					if(srt_flgr)
						srt_typ = ASC;
					else
						srt_typ = DEC;
					srt_flgr = !srt_flgr;
					srt_flg = true;
					go();
					srt_flg = false;

					break;
				case R.id.sentt:
					srt_ordr = SNT;
					srt_flgn = srt_flgr = false;
					
					if(srt_flgs)
						srt_typ = ASC;
					else
						srt_typ = DEC;
					srt_flgs = !srt_flgs;
					srt_flg = true;
					go();
					srt_flg = false;

					break;
				default:
				break;
			}
		}


	public void dt_clear()
	{
		//Log.d("sms8e", "clear_en");
		i = -1;
		oldnmbr = "";
		lst.clear();
		nmstr.clear();
		hsmp.clear();
		//Log.d("sms8e", "clear_lst_dn");
		//Log.d("sms8e", "clear_ex");
	}
	
	void tbl_clr(){
		TableLayout table = (TableLayout) findViewById(R.id.val_tbl);
		table.removeAllViews();
		//Log.d("sms8e", "clear_tbl_dn");

		
	}

	void dlg_init()
	{
		tst = Toast.makeText(SMS15Activity.this,"Future Year",Toast.LENGTH_SHORT);
		//Log.d("sms8e", "alrt_bldr_en");
		alrtdlg_hlp = new AlertDialog.Builder(SMS15Activity.this);
		alrtdlg_hlp.setTitle(R.string.pop_hlp_ttl);
		alrtdlg_hlp.setMessage(read_txt(R.raw.help));
		//Log.d("sms8e", "alrt_bldr_hlp_dn");
		alrtdlg_abt = new AlertDialog.Builder(SMS15Activity.this);
		alrtdlg_abt.setTitle(R.string.pop_abt_ttl);
		alrtdlg_abt.setMessage(read_txt(R.raw.about));
		//Log.d("sms8e", "alrt_bldr_abt_dn");
		//Log.d("sms8e", "alrt_bldr_ex");
	}

	public void dt_init()
	{
		//Log.d("sms8e", "dt_init_en");
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		yearf = yeart = year;
		monthf = montht = month;
		dayf = dayt = day;
		//Log.d("sms8e", "dt_init_dn");
	addlistener();
		//Log.d("sms8e", "add_lsnt_dn");
		//Log.d("sms8e", "dt_init_ex");
	}

	
	/**
	 * 获取数据
	 */
	public void generate()
	{
		dt_clear();
		//Log.d("sms8e", "gen_en");
		cur = managedQuery(uri, columns, // Which columns to return
				"date BETWEEN " + ms_frm + " AND " + ms_to
						+ ") GROUP BY (address),(type", // WHERE clause; which
														// rows to return(all
														// rows)
				null, // WHERE clause selection arguments (none)
				"address"); // Order-by clause (ascending by name)
		//Log.d("sms8e", "qr_dn");
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			nmbr = cur.getString(0);
			namestr = getnme(nmbr);
			//Log.d("sms8e", "getnme_dn" + nmbr);
			if (!nmstr.contains(namestr))
			{
				nmstr.add(namestr);
			}
						
			val = cur.getString(1);
			cnt = cur.getString(2);
			      k = new Pair<String, String>(namestr, val);
		            if (hsmp.containsKey(k)) {
		                  hsmp.put(k,String.valueOf(Integer.decode(hsmp.get(k))+Integer.decode(cnt)));
		                  } else {
		                        hsmp.put(k, cnt);
		                  }
		    i++;              
			cur.moveToNext();
		}
		//Log.d("sms8e", "hsmp_dn");
		rcvt = sntt = 0;
	       for (int i = 0; i < nmstr.size(); i++){
	    	   String rcv = "",snt = "";
	        	rcv = hsmp.get(new Pair<String,String>(nmstr.get(i),REC));
	        	rcv = (rcv==null)?"0":rcv;
	        	rcvt = rcvt + Integer.parseInt(rcv);
	        	snt = hsmp.get(new Pair<String,String>(nmstr.get(i),SNT));
	        	snt = (snt==null)?"0":snt;
	        	sntt = sntt + Integer.parseInt(snt);
	        	lst.add(i,new lst_str(nmstr.get(i),snt,rcv));
	       }
	       //Log.d("sms8e", "lst_dn");
		
		//Log.d("sms8e", "gen_ex");
	}
	public void sort(){
		Collections.sort(lst);
		//Log.d("sms8e", "sort_dn");
	}

	public String getnme(String num)
	{
		//Log.d("sms8e", "getnme_enx");
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(num));
		String[] projection = new String[] { PhoneLookup.DISPLAY_NAME };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor_nm = managedQuery(uri, projection, selection,
				selectionArgs, sortOrder);
		//Log.d("sms8e", "qr_dn");
		cursor_nm.moveToFirst();
		if (cursor_nm.getCount() != 0) return cursor_nm.getString(cursor_nm
				.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		else return num;
	}
	public String getnmbr(String nme) {
		//Log.d("sms8e", "getnmbr_enx");
		String nmbr = null;
		Uri uri = CommonDataKinds.Phone.CONTENT_URI;
		String selection = CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + nme +"%'";

		String[] projection = new String[] { CommonDataKinds.Phone.NUMBER};
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor c =managedQuery(uri,projection, selection, selectionArgs, sortOrder);

		if (c.moveToFirst()) {
			nmbr = c.getString(0);
		}
		if(nmbr==null)
			nmbr = nme;
		return nmbr;
	}
	public void go()
	{
		//Log.d("sms8e", "go_en");
		tbl_clr();
		//Log.d("sms8e", "clr_dn");
		if((ms_frm-ms_to)>0)
		{
			frm_to_err();
			return;
		}
		else
			findViewById(R.id.frm_to_err).setVisibility(View.GONE);
		if(!srt_flg){
		generate();
		//Log.d("sms8e", "gen_dn");
		}
		sort();
		show();
		//Log.d("sms8e", "shw_dn");
		//Log.d("sms8e", "go_ex");
		//Log.d("sms8e","total" + rcvt.toString() + ";" + sntt.toString() );
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		//Log.d("sms8e", "oncrt_en");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//Log.d("sms8e", "r.lyt.mn_dn");
		dt_init();
		dlg_init();
		//Log.d("sms8e", "dt_init_dn");
		clk_flg = true;
		vid = 0;
		frm_lstnr.onDateSet(new DatePicker(this), year, month, day);
		vid = 1;
		clk_flg = true;
		frm_lstnr.onDateSet(new DatePicker(this), year, month, day);
		clk_flg = false;
		//Log.d("sms8e", "init_go_dn");
		//Log.d("sms8e", "oncrt_ex");
	}

	@Override
	public Dialog onCreateDialog(int id)
	{
		//Log.d("sms8e", "oncrtdlg_enx");
		switch (id)
		{
			case FRM_ID:
				//Log.d("sms8e", "oncrtdlg_frm");
				return new DatePickerDialog(this, theme, frm_lstnr, yearf,
						monthf, dayf){
					public void onDateChanged(DatePicker view, int yearf, int monthf, int dayf){
						if (yearf>year)
						{
							tst.setText(R.string.Future_Year);
							tst.show();
							view.updateDate(year, monthf, dayf);
						}
						else if(yearf == year)
						{
							if(monthf>month)
							{
								tst.setText(R.string.Future_Month);
								tst.show();
								view.updateDate(yearf, month, dayf);
							}
							else if(monthf == month)
							{
								if(dayf>day)
								{
									tst.setText(R.string.Future_Day);
									tst.show();
									view.updateDate(yearf, monthf, day);
								}
							}
						}
					}
				};
			case TO_ID:
				//Log.d("sms8e", "oncrtdlg_to");
				return new DatePickerDialog(this, theme, frm_lstnr, yeart,
						montht, dayt){
					public void onDateChanged(DatePicker view, int yeart, int montht, int dayt){
//						Toast.makeText(SMS15Activity.this,"to",Toast.LENGTH_SHORT).show();
					}
				};
				
				
			
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.Help:
				alrtdlg_hlp.show();
				return true;
			case R.id.About:
				alrtdlg_abt.show();
				return true;
			case R.id.Settings:
				startActivity(new Intent(SMS15Activity.this, settings.class));
				return true;
			default:
				return false;
		}
	}

	public StringBuilder read_txt(int rsrc)
	{
		//Log.d("sms8e", "read_txt_en");
		is = this.getResources().openRawResource(rsrc);
		br = new BufferedReader(new InputStreamReader(is));
		readLine = null;
		StringBuilder T = new StringBuilder();
		try
		{
			while ((readLine = br.readLine()) != null)
			{
				readLine = readLine.replace("%app_name%",
						getString(R.string.app_name));
				readLine = readLine.replace("%dev_name%",
						getString(R.string.dev_name));
				readLine = readLine.replace("%mail_id%",
						getString(R.string.mail_id));
				T.append(readLine);
				T.append('\n');
			}
			is.close();
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//Log.d("sms8e", "read_txt_ex");
		return T;
	}
	public void frm_to_err(){
		  if(i1!=null) 
			i1.setImageResource(android.R.color.transparent);
			TextView t1 = (TextView)findViewById(R.id.contactt);
			   TextView t2 = (TextView)findViewById(R.id.sentt);
				  TextView t3 = (TextView)findViewById(R.id.recievedt);
					t1.setText("Contact");
					t2.setText("Sent (0)");
					t3.setText("Recieved (0)");
					findViewById(R.id.frm_to_err).setVisibility(View.VISIBLE);	
	}

	public void show()
	{
		//Log.d("sms8e", "shw_en");
		TextView t1 = (TextView)findViewById(R.id.contactt);
		   TextView t2 = (TextView)findViewById(R.id.sentt);
			  TextView t3 = (TextView)findViewById(R.id.recievedt);
			  if(i1!=null) 
			i1.setImageResource(android.R.color.transparent);
		switch (Integer.valueOf(srt_ordr))
		{
			case 0:
				i1 = (ImageView)findViewById(R.id.arrowc);
				break;
			case 1:
				i1 = (ImageView)findViewById(R.id.arrowr);
				break;
			case 2:
				i1 = (ImageView)findViewById(R.id.arrows);
				break;
			default :
				i1 = (ImageView)findViewById(R.id.arrowc);
		}
		if(srt_typ==DEC)
			i1.setImageResource (android.R.drawable.arrow_down_float);
		else
			i1.setImageResource (android.R.drawable.arrow_up_float);
				t1.setText("Contact");
				t2.setText("Sent (" + sntt + ")");
				t3.setText("Recieved (" + rcvt + ")");
	int j = 0, sz = lst.size();
	TableLayout tl = (TableLayout) findViewById(R.id.val_tbl);
	LayoutInflater inflater = getLayoutInflater();
  TableRow tr = (TableRow)inflater.inflate(R.layout.tbl_rw, tl, false);
  
	   t1 = (TextView)tr.findViewById(R.id.contact);
	   t1.getLayoutParams().height=0;
t2 = (TextView)tr.findViewById(R.id.sent);
t2.getLayoutParams().height=0;
t3 = (TextView)tr.findViewById(R.id.recieved);
t3.getLayoutParams().height=0;
	    t1.setGravity(Gravity.CENTER);
		t1.setText("Contact");
		t2.setText("Sent (" + sntt + ")");
		t3.setText("Recieved (" + rcvt + ")");
		tl.addView(tr);
		while (j < sz)
		{
			tr = (TableRow)inflater.inflate(R.layout.tbl_rw, tl, false);
		    t1 = (TextView)tr.findViewById(R.id.contact);
		    t2 = (TextView)tr.findViewById(R.id.sent);
		    t3 = (TextView)tr.findViewById(R.id.recieved);
			t1.setText(lst.get(j).name_str);
			registerForContextMenu(t1);
			t1.setTag(j);
			//Log.d("sms8e", lst.get(j).name_str + "_dn");
			t2.setText(lst.get(j).sent_str);
			t3.setText(lst.get(j).rcvd_str);
			tl.addView(tr);
			j++;
		}
		//Log.d("sms8e", "shw_ex");
	}
	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		 super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu");
		 menu.add(0,Integer.valueOf(v.getTag().toString()), 0, "Action 1");
	 }

	 @Override
public boolean onContextItemSelected(MenuItem item) {
		 
		if(item.getTitle().equals("Action 1")){
		String nmmbbrr =  getnmbr(lst.get(item.getItemId()).name_str);
		sendsms(nmmbbrr,item.getItemId());
		Toast.makeText(this,"function 1 called" +nmmbbrr ,Toast.LENGTH_SHORT).show();
		}
		 else if(item.getTitle().equals("Action 2")){
			Toast.makeText(this,"function 2 called" + item.getItemId(),Toast.LENGTH_SHORT).show();
		}
		 else {return false;}
	 return true;
}
	public void sendsms(String tonmbr,int id) {
		 Intent smsIntent = new Intent(Intent.ACTION_VIEW);
String msgtxt = preferences.getString("msgtxt","");
		msgtxt = msgtxt.replaceAll("<sc>",lst.get(id).sent_str);
		msgtxt = msgtxt.replaceAll("<rc>",lst.get(id).rcvd_str);
		smsIntent.putExtra("sms_body",msgtxt);
		 smsIntent.putExtra("address", tonmbr);
		 smsIntent.setType("vnd.android-dir/mms-sms");

		 startActivity(smsIntent);
	 }
}
