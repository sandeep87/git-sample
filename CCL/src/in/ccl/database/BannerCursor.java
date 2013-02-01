package in.ccl.database;

import in.ccl.model.Items;
import in.ccl.model.TeamMember;
import in.ccl.model.Teams;

import java.util.ArrayList;

import android.database.Cursor;

public class BannerCursor {

	public static ArrayList <Items> getItems (Cursor cursor) {
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor.moveToFirst()) {
			do {
				Items item = new Items();
				item.setId(cursor.getInt(0));
				item.setPhotoOrVideoUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setAlbumId(cursor.getInt(3));
				list.add(item);
			}
			while (cursor.moveToNext());
		}
		return list;

	}
	public static ArrayList <Teams> getTeamLogoItems (Cursor cursor) {
		ArrayList <Teams> teamsLogolist = new ArrayList <Teams>();
		if (cursor.moveToFirst()) {
			do {
				Teams teamsLogoitem = new Teams();
				teamsLogoitem.setId(cursor.getInt(0));
			  teamsLogoitem.setName(cursor.getString(1));
			  teamsLogoitem.setLogo(cursor.getString(2));
			  teamsLogoitem.setBanner(cursor.getString(3));
			  teamsLogolist.add(teamsLogoitem);
			}
			while (cursor.moveToNext());
		}
		return teamsLogolist;

	}

	public static ArrayList <TeamMember> getTeamMemberItems (Cursor cursor) {
		ArrayList <TeamMember> teamMemberList = new ArrayList <TeamMember>();

		if (cursor.moveToFirst()) {
			do {
				TeamMember teamMember = new TeamMember();
				teamMember.setId(cursor.getInt(0));
				teamMember.setPersonName(cursor.getString(1));
				teamMember.setMemberThumbUrl(cursor.getString(2));
				teamMember.setTeamName(cursor.getString(3));
				teamMember.setRole(cursor.getString(4));
				teamMemberList.add(teamMember);

			}
			while (cursor.moveToNext());
		}
		return teamMemberList;
	}

}
