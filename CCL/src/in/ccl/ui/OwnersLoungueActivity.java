package in.ccl.ui;

import in.ccl.adapters.DirectorAdapter;
import in.ccl.helper.Util;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OwnersLoungueActivity extends TopActivity {

	Button button_team, button_board;

	private LinearLayout layoutTeamOwners;

	private LinearLayout layoutBoard;

	private ListView directorList;

	private TextView txtOwnersLounge;

	// kerala
	private TextView mEntireTeamTitleKerala, mOwnerOneNameOneKerala, mOwnerOneDataKerala, mOwnerOneNameTwoKerala;

	private TextView mOwnerTwoNameOneKerala, mOwnerTwoDataKerala, mOwnerTwoNameTwoKerala;

	private TextView mOwnerThreeNameOneKerala, mOwnerThreeDataKerala, mOwnerThreeNameTwoKerala;

	// Bengal tigers
	private TextView mEntireTeamTitleBengal, mOwnerOneNameOneBengal, mOwnerOneDataBengal, mOwnerOneNameTwoBengal;

	private TextView mOwnerTwoNameOneBengal, mOwnerTwoDataBengal, mOwnerTwoNameTwoBengal;

	private TextView mOwnerThreeNameOneBengal, mOwnerThreeDataBengal, mOwnerThreeNameTwoBengal;

	// Mumbai heros
	private TextView mEntireTeamTitleMumbai, mOwnerOneNameOneMumbai, mOwnerOneDataMumbai, mOwnerOneNameTwoMumbai;

	// chennai
	private TextView mEntireTeamTitleChennai, mOwnerOneNameOneChennai, mOwnerOneDataChennai, mOwnerOneNameTwoChennai;

	// telugu warriors
	private TextView mEntireTeamTitleTelugu, mOwnerOneNameOneTelugu, mOwnerOneDataTelugu, mOwnerOneNameTwoTelugu;

	private TextView mOwnerTwoNameOneTelugu, mOwnerTwoDataTelugu, mOwnerTwoNameTwoTelugu;

	// karnataka
	private TextView mEntireTeamTitleKarnataka, mOwnerOneNameOneKarnataka, mOwnerOneDataKarnataka, mOwnerOneNameTwoKarnataka;

	// veer
	private TextView mEntireTeamTitleVeer, mOwnerOneNameOneVeer, mOwnerOneDataVeer, mOwnerOneNameTwoVeer;

	// bhojpuri
	private TextView mEntireTeamTitleBhojpuri, mOwnerOneNameOneBhojpuri, mOwnerOneDataBhojpuri, mOwnerOneNameTwoBhojpuri;

	private int[] directorImages = { R.raw.vishnu_nduri, R.raw.sharath_kumar_director, R.raw.tirumalreddy_director, R.raw.murthy_director, R.raw.ashokkheny_director, R.raw.sohailkhan_director, R.raw.pujari_director };

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.owners_lounge);
		button_team = (Button) findViewById(R.id.btn_team_owners);
		button_board = (Button) findViewById(R.id.btn_board_of_directors);
		layoutTeamOwners = (LinearLayout) findViewById(R.id.layout_team_owners);
		layoutBoard = (LinearLayout) findViewById(R.id.layout_board_of_directors);
		directorList = (ListView) findViewById(R.id.new_owners_list);

		txtOwnersLounge = (TextView) findViewById(R.id.txt_participated_owners_lounge);

		Util.setTextFont(this, button_team);
		Util.setTextFont(this, button_board);
		Util.setTextFont(this, txtOwnersLounge);

		// kerala
		mEntireTeamTitleKerala = (TextView) findViewById(R.id.entire_team_title_kerala);
		mOwnerOneNameOneKerala = (TextView) findViewById(R.id.owner_one_name_one_kerala);
		mOwnerOneDataKerala = (TextView) findViewById(R.id.owner_one_data_kerala);
		mOwnerOneNameTwoKerala = (TextView) findViewById(R.id.owner_one_name_two_kerala);

		mOwnerTwoNameOneKerala = (TextView) findViewById(R.id.owner_two_name_one_kerala);
		mOwnerTwoDataKerala = (TextView) findViewById(R.id.owner_two_data_kerala);
		mOwnerTwoNameTwoKerala = (TextView) findViewById(R.id.owner_two_name_two_kerala);

		mOwnerThreeNameOneKerala = (TextView) findViewById(R.id.owner_three_name_one_kerala);
		mOwnerThreeDataKerala = (TextView) findViewById(R.id.owner_three_data_kerala);
		mOwnerThreeNameTwoKerala = (TextView) findViewById(R.id.owner_three_name_two_kerala);

		Util.setTextFont(this, mEntireTeamTitleKerala);
		Util.setTextFont(this, mOwnerOneNameOneKerala);
		Util.setTextFont(this, mOwnerOneDataKerala);
		Util.setTextFont(this, mOwnerOneNameTwoKerala);

		Util.setTextFont(this, mOwnerTwoNameOneKerala);
		Util.setTextFont(this, mOwnerTwoDataKerala);
		Util.setTextFont(this, mOwnerTwoNameTwoKerala);

		Util.setTextFont(this, mOwnerThreeNameOneKerala);
		Util.setTextFont(this, mOwnerThreeDataKerala);
		Util.setTextFont(this, mOwnerThreeNameTwoKerala);

		// Bengal tigers
		mEntireTeamTitleBengal = (TextView) findViewById(R.id.entire_team_title_bengal);
		mOwnerOneNameOneBengal = (TextView) findViewById(R.id.owner_one_name_one_bengal);
		mOwnerOneDataBengal = (TextView) findViewById(R.id.owner_one_data_bengal);
		mOwnerOneNameTwoBengal = (TextView) findViewById(R.id.owner_one_name_two_bengal);

		mOwnerTwoNameOneBengal = (TextView) findViewById(R.id.owner_two_name_one_bengal);
		mOwnerTwoDataBengal = (TextView) findViewById(R.id.owner_two_data_bengal);
		mOwnerTwoNameTwoBengal = (TextView) findViewById(R.id.owner_two_name_two_bengal);

		mOwnerThreeNameOneBengal = (TextView) findViewById(R.id.owner_three_name_one_bengal);
		mOwnerThreeDataBengal = (TextView) findViewById(R.id.owner_three_data_bengal);
		mOwnerThreeNameTwoBengal = (TextView) findViewById(R.id.owner_three_name_two_bengal);

		/*
		 * mEntireTeamTitleBengal.setText(mEntireTeamTitleBengal.getText().toString()); mOwnerOneNameOneBengal.setText(mOwnerOneNameOneBengal.getText().toString()); mOwnerOneDataBengal.setText(mOwnerOneDataBengal.getText().toString());
		 * mOwnerOneNameTwoBengal.setText(mOwnerOneNameTwoBengal.getText().toString());
		 * 
		 * mOwnerTwoNameOneBengal.setText(mOwnerTwoNameOneBengal.getText().toString()); mOwnerTwoDataBengal.setText(mOwnerTwoDataBengal.getText().toString()); mOwnerTwoNameTwoBengal.setText(mOwnerTwoNameTwoBengal.getText().toString());
		 * 
		 * mOwnerThreeNameOneBengal.setText(mOwnerThreeNameOneBengal.getText().toString()); mOwnerThreeDataBengal.setText(mOwnerThreeDataBengal.getText().toString()); mOwnerThreeNameTwoBengal.setText(mOwnerThreeNameTwoBengal.getText().toString());
		 */
		Util.setTextFont(this, mEntireTeamTitleBengal);
		Util.setTextFont(this, mOwnerOneNameOneBengal);
		Util.setTextFont(this, mOwnerOneDataBengal);
		Util.setTextFont(this, mOwnerOneNameTwoBengal);

		Util.setTextFont(this, mOwnerTwoNameOneBengal);
		Util.setTextFont(this, mOwnerTwoDataBengal);
		Util.setTextFont(this, mOwnerTwoNameTwoBengal);

		Util.setTextFont(this, mOwnerThreeNameOneBengal);
		Util.setTextFont(this, mOwnerThreeDataBengal);
		Util.setTextFont(this, mOwnerThreeNameTwoBengal);

		// mumbai heros
		mEntireTeamTitleMumbai = (TextView) findViewById(R.id.entire_team_title_mumbai);
		mOwnerOneNameOneMumbai = (TextView) findViewById(R.id.owner_one_name_one_mumbai);
		mOwnerOneDataMumbai = (TextView) findViewById(R.id.owner_one_data_mumbai);
		mOwnerOneNameTwoMumbai = (TextView) findViewById(R.id.owner_one_name_two_mumbai);

		/*
		 * mEntireTeamTitleMumbai.setText(mEntireTeamTitleMumbai.getText().toString()); mOwnerOneNameOneMumbai.setText(mOwnerOneNameOneMumbai.getText().toString()); mOwnerOneDataMumbai.setText(mOwnerOneDataMumbai.getText().toString());
		 * mOwnerOneNameTwoMumbai.setText(mOwnerOneNameTwoMumbai.getText().toString());
		 */

		Util.setTextFont(this, mEntireTeamTitleMumbai);
		Util.setTextFont(this, mOwnerOneNameOneMumbai);
		Util.setTextFont(this, mOwnerOneDataMumbai);
		Util.setTextFont(this, mOwnerOneNameTwoMumbai);

		// chennai rhinos
		mEntireTeamTitleChennai = (TextView) findViewById(R.id.entire_team_title_chennai);
		mOwnerOneNameOneChennai = (TextView) findViewById(R.id.owner_one_name_one_chennai);
		mOwnerOneDataChennai = (TextView) findViewById(R.id.owner_one_data_chennai);
		mOwnerOneNameTwoChennai = (TextView) findViewById(R.id.owner_one_name_two_chennai);

		/*
		 * mEntireTeamTitleChennai.setText(mEntireTeamTitleChennai.getText().toString()); mOwnerOneNameOneChennai.setText(mOwnerOneNameOneChennai.getText().toString()); mOwnerOneDataChennai.setText(mOwnerOneDataChennai.getText().toString());
		 * mOwnerOneNameTwoChennai.setText(mOwnerOneNameTwoChennai.getText().toString());
		 */

		Util.setTextFont(this, mEntireTeamTitleChennai);
		Util.setTextFont(this, mOwnerOneNameOneChennai);
		Util.setTextFont(this, mOwnerOneDataChennai);
		Util.setTextFont(this, mOwnerOneNameTwoChennai);

		// Telugu warriors
		mEntireTeamTitleTelugu = (TextView) findViewById(R.id.entire_team_title_telugu);
		mOwnerOneNameOneTelugu = (TextView) findViewById(R.id.owner_one_name_one_telugu);
		mOwnerOneDataTelugu = (TextView) findViewById(R.id.owner_one_data_telugu);
		mOwnerOneNameTwoTelugu = (TextView) findViewById(R.id.owner_one_name_two_telugu);

		mOwnerTwoNameOneTelugu = (TextView) findViewById(R.id.owner_two_name_one_telugu);
		mOwnerTwoDataTelugu = (TextView) findViewById(R.id.owner_two_data_telugu);
		mOwnerTwoNameTwoTelugu = (TextView) findViewById(R.id.owner_two_name_two_telugu);

		/*
		 * mEntireTeamTitleTelugu.setText(mEntireTeamTitleTelugu.getText().toString()); mOwnerOneNameOneTelugu.setText(mOwnerOneNameOneTelugu.getText().toString()); mOwnerOneDataTelugu.setText(mOwnerOneDataTelugu.getText().toString());
		 * mOwnerOneNameTwoTelugu.setText(mOwnerOneNameTwoTelugu.getText().toString());
		 * 
		 * mOwnerTwoNameOneTelugu.setText(mOwnerTwoNameOneTelugu.getText().toString()); mOwnerTwoDataTelugu.setText(mOwnerTwoDataTelugu.getText().toString()); mOwnerTwoNameTwoTelugu.setText(mOwnerTwoNameTwoTelugu.getText().toString());
		 */

		Util.setTextFont(this, mEntireTeamTitleTelugu);
		Util.setTextFont(this, mOwnerOneNameOneTelugu);
		Util.setTextFont(this, mOwnerOneDataTelugu);
		Util.setTextFont(this, mOwnerOneNameTwoTelugu);

		Util.setTextFont(this, mOwnerTwoNameOneTelugu);
		Util.setTextFont(this, mOwnerTwoDataTelugu);
		Util.setTextFont(this, mOwnerTwoNameTwoTelugu);

		// karnataka
		mEntireTeamTitleKarnataka = (TextView) findViewById(R.id.entire_team_title_karnataka);
		mOwnerOneNameOneKarnataka = (TextView) findViewById(R.id.owner_one_name_one_karnataka);
		mOwnerOneDataKarnataka = (TextView) findViewById(R.id.owner_one_data_karnataka);
		mOwnerOneNameTwoKarnataka = (TextView) findViewById(R.id.owner_one_name_two_karnataka);

		mEntireTeamTitleKarnataka.setText(mEntireTeamTitleKarnataka.getText().toString());
		mOwnerOneNameOneKarnataka.setText(mOwnerOneNameOneKarnataka.getText().toString());
		mOwnerOneDataKarnataka.setText(mOwnerOneDataKarnataka.getText().toString());
		mOwnerOneNameTwoKarnataka.setText(mOwnerOneNameTwoKarnataka.getText().toString());

		Util.setTextFont(this, mEntireTeamTitleKarnataka);
		Util.setTextFont(this, mOwnerOneNameOneKarnataka);
		Util.setTextFont(this, mOwnerOneDataKarnataka);
		Util.setTextFont(this, mOwnerOneNameTwoKarnataka);

		// veer
		mEntireTeamTitleVeer = (TextView) findViewById(R.id.entire_team_title_veer);
		mOwnerOneNameOneVeer = (TextView) findViewById(R.id.owner_one_name_one_veer);
		mOwnerOneDataVeer = (TextView) findViewById(R.id.owner_one_data_veer);
		mOwnerOneNameTwoVeer = (TextView) findViewById(R.id.owner_one_name_two_veer);

		/*
		 * mEntireTeamTitleVeer.setText(mEntireTeamTitleVeer.getText().toString()); mOwnerOneNameOneVeer.setText(mOwnerOneNameOneVeer.getText().toString()); mOwnerOneDataVeer.setText(mOwnerOneDataVeer.getText().toString()); mOwnerOneNameTwoVeer.setText(mOwnerOneNameTwoVeer.getText().toString());
		 */

		Util.setTextFont(this, mEntireTeamTitleVeer);
		Util.setTextFont(this, mOwnerOneNameOneVeer);
		Util.setTextFont(this, mOwnerOneDataVeer);
		Util.setTextFont(this, mOwnerOneNameTwoVeer);

		// bhojpuri
		mEntireTeamTitleBhojpuri = (TextView) findViewById(R.id.entire_team_title_bhojpuri);
		mOwnerOneNameOneBhojpuri = (TextView) findViewById(R.id.owner_one_name_one_bhojpuri);
		mOwnerOneDataBhojpuri = (TextView) findViewById(R.id.owner_one_data_bhojpuri);
		mOwnerOneNameTwoBhojpuri = (TextView) findViewById(R.id.owner_one_name_two_bhojpuri);

		/*
		 * mEntireTeamTitleBhojpuri.setText(mEntireTeamTitleBhojpuri.getText().toString()); mOwnerOneNameOneBhojpuri.setText(mOwnerOneNameOneBhojpuri.getText().toString()); mOwnerOneDataBhojpuri.setText(mOwnerOneDataBhojpuri.getText().toString());
		 * mOwnerOneNameTwoBhojpuri.setText(mOwnerOneNameTwoBhojpuri.getText().toString());
		 */

		Util.setTextFont(this, mEntireTeamTitleBhojpuri);
		Util.setTextFont(this, mOwnerOneNameOneBhojpuri);
		Util.setTextFont(this, mOwnerOneDataBhojpuri);
		Util.setTextFont(this, mOwnerOneNameTwoBhojpuri);

		String[] directorNames = this.getResources().getStringArray(R.array.director_names);

		directorList.setAdapter(new DirectorAdapter(OwnersLoungueActivity.this, directorNames, directorImages));

		button_team.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				button_team.setBackgroundResource(R.drawable.inningstab_tapped);
				button_board.setBackgroundResource(R.drawable.innings_tab);
				layoutBoard.setVisibility(View.GONE);
				layoutTeamOwners.setVisibility(View.VISIBLE);
			}
		});
		button_board.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				button_team.setBackgroundResource(R.drawable.innings_tab);
				button_board.setBackgroundResource(R.drawable.inningstab_tapped);
				layoutBoard.setVisibility(View.VISIBLE);
				layoutTeamOwners.setVisibility(View.GONE);

			}
		});

	}

}