package in.ccl.score;

import in.ccl.ui.ScoreBoardActivity;
import in.ccl.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class ScoreParser {

	public void ParseJsonScoreBoard (Context ctx) {
		boolean isSecondInnings = false;
		StringBuilder builder = new StringBuilder();
		File file = new File(Environment.getExternalStorageDirectory() + "/score_board");
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				if (inputStream != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					if (reader != null) {
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}
		try {
			ArrayList <Innings> firstInnings = new ArrayList <Innings>();
			ArrayList <Innings> secondInnings = new ArrayList <Innings>();
			// System.out.println("" + builder.toString().trim());
			JSONObject mainJsonObject = new JSONObject(builder.toString().trim());
		
			String firstInn = mainJsonObject.getString("innings1");
			// System.out.println("firstInn" + firstInn);

			JSONObject firstInningsObject = new JSONObject(firstInn.trim());

			Innings firstInningsItems = parseFirstInnings(firstInningsObject);

			firstInnings.add(firstInningsItems);
			if (mainJsonObject.has("innings2")) {
				isSecondInnings = true;

				String secondInn = mainJsonObject.getString("innings2");

				JSONObject secondInningsObject = new JSONObject(secondInn.trim());

				Innings secondInningsItems = parseFirstInnings(secondInningsObject);

				secondInnings.add(secondInningsItems);
			}
			else {
				isSecondInnings = false;
			}
			if (isSecondInnings) {
				if (firstInnings != null && firstInnings.size() > 0 && secondInnings != null && secondInnings.size() > 0) {
					Intent scoreBoardIntent = new Intent(ctx, ScoreBoardActivity.class);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.FIRST_INNINGS_KEY, firstInnings);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.SECOND_INNINGS_KEY, secondInnings);
					scoreBoardIntent.putExtra("secondInnsStatus", isSecondInnings);
					ctx.startActivity(scoreBoardIntent);
				}

			}
			else {
				if (firstInnings != null && firstInnings.size() > 0) {
					Intent scoreBoardIntent = new Intent(ctx, ScoreBoardActivity.class);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.FIRST_INNINGS_KEY, firstInnings);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.SECOND_INNINGS_KEY, secondInnings);
					scoreBoardIntent.putExtra(Constants.SECOND_INNINGS_STATUS, isSecondInnings);
					ctx.startActivity(scoreBoardIntent);
				}

			}

		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Innings parseFirstInnings (JSONObject inningsObject) {
		Innings innings = new Innings();
		try {

			innings.setBowling_team(inningsObject.getString("bowling_team"));
			innings.setBatting_team(inningsObject.getString("batting_team"));

			JSONObject totalObject = inningsObject.getJSONObject("total");

			innings.setRuns(totalObject.getInt("runs"));
			innings.setOvers(totalObject.getDouble("overs"));
			innings.setWickets(totalObject.getInt("wickets"));

			JSONObject extraObject = inningsObject.getJSONObject("extras");

			innings.setLegbyes(extraObject.getInt("legbyes"));
			innings.setWides(extraObject.getInt("wides"));
			innings.setByes(extraObject.getInt("byes"));
			innings.setNoballs(extraObject.getInt("noballs"));

			JSONArray battingArrary = inningsObject.getJSONArray("batting");
			ArrayList <Batting> battingList = new ArrayList <Batting>();
			for (int i = 0; i < battingArrary.length(); i++) {
				JSONObject battJsonObject = new JSONObject();
				battJsonObject = battingArrary.getJSONObject(i);
				Batting batting = new Batting();
				batting.setName(battJsonObject.getString("name"));
				batting.setScore(battJsonObject.getInt("score"));
				batting.setBalls(battJsonObject.getInt("balls"));
				batting.setFours(battJsonObject.getInt("fours"));
				batting.setSixes(battJsonObject.getInt("sixes"));
				batting.setCaught(battJsonObject.getString("caught"));
				batting.setBowled(battJsonObject.getString("bowled"));
				batting.setHitwicket(battJsonObject.getString("hitwicket"));
				batting.setRetiredhurt(battJsonObject.getString("retiredhurt"));
				batting.setRunout(battJsonObject.getString("runout"));
				batting.setNotout(battJsonObject.getString("notout"));
				batting.setStumped(battJsonObject.getString("stumped"));
				batting.setCandb(battJsonObject.getString("candb"));
				batting.setLbw(battJsonObject.getString("lbw"));
				batting.setDidnotbat(battJsonObject.getString("handledtheball"));
				batting.setDidnotbat(battJsonObject.getString("didnotbat"));
				battingList.add(batting);
			}
			innings.setBatting_info(battingList);

			JSONArray bowlerArrary = inningsObject.getJSONArray("bowling");
			ArrayList <Bowler> bowerList = new ArrayList <Bowler>();
			for (int i = 0; i < bowlerArrary.length(); i++) {
				JSONObject bowlerJsonObject = new JSONObject();
				bowlerJsonObject = bowlerArrary.getJSONObject(i);
				Bowler bowler = new Bowler();

				bowler.setBowlerName(bowlerJsonObject.getString("name"));
				bowler.setBowlerOvers(bowlerJsonObject.getDouble("overs"));
				bowler.setMadiens(bowlerJsonObject.getInt("madiens"));
				bowler.setBowlerRuns(bowlerJsonObject.getInt("runs"));
				bowler.setBowlerWickets(bowlerJsonObject.getInt("wickets"));
				bowerList.add(bowler);
			}
			innings.setBowler_info(bowerList);
		}
		catch (JSONException e) {
			// e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return innings;
	}

	public LiveScore parseLiveScore () {
		LiveScore liveScore = null;
		StringBuilder builder = new StringBuilder();
		File file = new File(Environment.getExternalStorageDirectory() + "/livescore.txt");
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				if (inputStream != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					if (reader != null) {
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}

		JSONObject object;
		try {
			object = new JSONObject(builder.toString());
			liveScore = new LiveScore();
			if (object.has("teams")) {
				JSONObject teamObject = object.getJSONObject("teams");

				if (teamObject.has("team1")) {
					liveScore.setTeam1(teamObject.getString("team1"));
				}
				if (teamObject.has("team2")) {
					liveScore.setTeam2(teamObject.getString("team2"));
				}
			}

			if (object.has("target")) {
				JSONObject targetObject = object.getJSONObject("target");
				if (targetObject.has("score")) {
					liveScore.setTarget_score(targetObject.getInt("score"));
				}
				if (targetObject.has("wickets")) {
					liveScore.setTarget_wickets(targetObject.getInt("wickets"));
				}
				if (targetObject.has("overs")) {
					liveScore.setTarget_overs(targetObject.getDouble("overs"));
				}
				if (targetObject.has("score")) {
					liveScore.setNeed_score(object.getString("score"));
				}
			}
			if (object.has("current_score")) {
				JSONObject currentScoreObject = object.getJSONObject("current_score");
				if (currentScoreObject.has("score")) {
					liveScore.setCurrent_score_score(currentScoreObject.getInt("score"));
				}
				if (currentScoreObject.has("wickets")) {
					liveScore.setCurrent_score_wickets(currentScoreObject.getInt("wickets"));
				}
				if (currentScoreObject.has("overs")) {
					liveScore.setCurrent_score_overs(currentScoreObject.getDouble("overs"));
				}
			}

			if (object.has("bastman1")) {

				JSONObject strikerObject = object.getJSONObject("bastman1");
				if (strikerObject.has("name")) {
					liveScore.setStriker_name(strikerObject.getString("name"));
				}
				if (strikerObject.has("score")) {

					liveScore.setStriker_score(strikerObject.getInt("score"));
				}
				if (strikerObject.has("balls")) {

					liveScore.setStriker_balls(strikerObject.getInt("balls"));
				}
				if (strikerObject.has("strikerrate")) {

					liveScore.setStriker_strikerate(strikerObject.getDouble("strikerrate"));
				}
			}
			if (object.has("bastman2")) {

				JSONObject nonStrikerObject = object.getJSONObject("bastman2");
				if (nonStrikerObject.has("name")) {
					liveScore.setNonstriker_name(nonStrikerObject.getString("name"));
				}
				if (nonStrikerObject.has("score")) {
					liveScore.setNonstriker_score(nonStrikerObject.getInt("score"));
				}
				if (nonStrikerObject.has("balls")) {

					liveScore.setNonstriker_balls(nonStrikerObject.getInt("balls"));
				}
				if (nonStrikerObject.has("strikerrate")) {

					liveScore.setNonstriker_strikerate(nonStrikerObject.getDouble("strikerrate"));
				}

			}
			if (object.has("bowler1")) {

				JSONObject currentBowlerObject = object.getJSONObject("bowler1");
				if (currentBowlerObject.has("name")) {
					liveScore.setCurrent_bowler_name(currentBowlerObject.getString("name"));
				}
				if (currentBowlerObject.has("overs")) {

					liveScore.setCurrent_bowler_overs(currentBowlerObject.getDouble("overs"));
				}
				if (currentBowlerObject.has("madiens")) {

					liveScore.setCurrent_bowler_madiens(currentBowlerObject.getInt("madiens"));
				}
				if (currentBowlerObject.has("runs")) {

					liveScore.setCurrent_bowler_runs(currentBowlerObject.getInt("runs"));
				}
				if (currentBowlerObject.has("wickets")) {

					liveScore.setCurrent_bowler_wickets(currentBowlerObject.getInt("wickets"));
				}
			}
			if (object.has("bowler2")) {

				JSONObject previousBowlerObject = object.getJSONObject("bowler2");
			
				if (previousBowlerObject.has("name")) {
				
					liveScore.setPrevious_bowler_name(previousBowlerObject.getString("name"));
				}
				if (previousBowlerObject.has("overs")) {

					liveScore.setPrevious_bowler_overs(previousBowlerObject.getDouble("overs"));
				}
				if (previousBowlerObject.has("madiens")) {

					liveScore.setPrevious_bowler_madiens(previousBowlerObject.getInt("madiens"));
				}
				if (previousBowlerObject.has("runs")) {

					liveScore.setPrevious_bowler_runs(previousBowlerObject.getInt("runs"));
				}
				if (previousBowlerObject.has("wickets")) {

					liveScore.setPrevious_bowler_wickets(previousBowlerObject.getInt("wickets"));
				}
			}
		}
		catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		return liveScore;

	}

	public ArrayList <MatchesResponse> liveMatchesRequest () {
		StringBuilder matchresponsebuilder = new StringBuilder();
		File file = new File(Environment.getExternalStorageDirectory() + "/matchrequest");
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				if (inputStream != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					if (reader != null) {
						String line;
						while ((line = reader.readLine()) != null) {
							matchresponsebuilder.append(line);
						}
					}
					// parseLiveScore(builder);
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}

		ArrayList <MatchesResponse> matchesResponseList = null;

		JSONObject responseObject = null;
		try {
			responseObject = new JSONObject(matchresponsebuilder.toString());
			 if(responseObject.has("data")){
			JSONArray matchesList = responseObject.getJSONArray("data");
			matchesResponseList = new ArrayList <MatchesResponse>();
			for (int i = 0; i < matchesList.length(); i++) {
				MatchesResponse matchesResponse = new MatchesResponse();
				JSONObject matchId = matchesList.getJSONObject(i);
				if(matchId.has("id")){
				matchesResponse.setId(matchId.getInt("id"));
				}
				if(matchId.has("name")){
				matchesResponse.setMathesName(matchId.getString("name"));
				}
				matchesResponseList.add(matchesResponse);
			}
		}
		}
		catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matchesResponseList;

	}

}
