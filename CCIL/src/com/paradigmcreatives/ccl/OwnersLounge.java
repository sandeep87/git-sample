package com.paradigmcreatives.ccl;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

public class OwnersLounge implements CommandListener{
	
	private Display display;
	CCL midlet;
	private static final Command CMD_BACK = new Command("Back", Command.BACK, 1);
    List list ;
	private Command exit, next, back;
	private Form maheshlist, tirumalalist, mohList, lissyList, shajiList,
	arjunkapoorList,boneykapoorList, rajshahList, anandList,
	manojList, ashokkhenyList, riteshList, gangaprasadList, sohailList;
	
	private Image mahesh, tirumal, mohanlal, lissy, shaji, arjunkapoor,
			boneykapoor, rajshah, anand, manoj, ashokkheny, ritesh,
			gangaprasad, sohail;
	String[] stringElements = { "MaheshReddy", "TirumalaReddy", "MohanLal",
			"LissyPriyadarshan", "Shaji", "Arjunkapoor", "BoneyKapoor",
			"RajShah", "AnandBihari", "ManojTiwari", "Ashokkheny",
			"RiteishDeshmukh", "Gangaprasad", "SohailKhan" };
	Image[] imageElements = { mahesh, tirumal, mohanlal, lissy, shaji,
			arjunkapoor, boneykapoor, rajshah, anand, manoj, ashokkheny,
			ritesh, gangaprasad, sohail };
	
	public OwnersLounge(CCL midlet) {
		
		this.midlet = midlet;
		try {
			mahesh = Image.createImage("/maheshreddy.png");
			tirumal = Image.createImage("/tirumalreddy.png");
			mohanlal = Image.createImage("/mohanlal.png");
			lissy = Image.createImage("/lissypriyadarshan.png");
			shaji = Image.createImage("/shaji.png");
			arjunkapoor = Image.createImage("/arjunkapoor.png");
			boneykapoor = Image.createImage("/boneykapoor.png");
			rajshah = Image.createImage("/rajshah.png");
			anand = Image.createImage("/anand.png");
			manoj = Image.createImage("/manojtiwariowner.png");
			ashokkheny = Image.createImage("/ashokkheny.png");
			ritesh = Image.createImage("/riteshdeshmukhow.png");
			gangaprasad = Image.createImage("/gangaprasad.png");
			sohail = Image.createImage("/sohailkhan.png");
			
			// menu = new List("", List.IMPLICIT, stringElements, null);
			list = new List("OWNER'S LOUNGE", List.IMPLICIT, stringElements,
					imageElements);
			next = new Command("Select", Command.SCREEN, 0);
			exit = new Command("Exit", Command.EXIT, 0);
			back = new Command("Back",Command.BACK,1);
			list.addCommand(next);
			list.addCommand(exit);
			list.addCommand(back);
			list.setCommandListener(this);
			display = Display.getDisplay(midlet);
			display.setCurrent(list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void commandAction(Command c, Displayable d) {
		
		int index = list.getSelectedIndex();
		System.out.println("value" + index);
		
		if( c == back & (d.equals(list)))
		{
			display.setCurrent(midlet.mCanvas);
		}
		else if (d.equals(list)) {

			if (c == next || c == List.SELECT_COMMAND) {
				if (d.equals(list)) {
					switch (((List) d).getSelectedIndex()) {
					case 0:
						maheshlist = new Form("TELUGU WARRIORS");
						maheshlist.append(mahesh);
						maheshlist.append("\nAMR Constructions was established in the year 1992 and has diversified into mining, irrigation and construction. AMR has successfully executed various civil projects across India and Nepal.");
						maheshlist.addCommand(CMD_BACK);
						maheshlist.setCommandListener(this);
						display.setCurrent(maheshlist);
						break;

					case 1:
						tirumalalist = new Form("TELUGU WARRIORS");
						tirumalalist.append(tirumal);
						tirumalalist.append("\nMr A. Tirumal Reddy is a media -sales entrepreneur with over 10 years of experience in electronic media. Sprint tele films is one of the leading compaines in Electronic media sales in South India.");
						tirumalalist.addCommand(CMD_BACK);
						tirumalalist.setCommandListener(this);
						display.setCurrent(tirumalalist);

						break;
					case 2:
						mohList = new Form("KERALA STRIKERS");
						mohList.append(mohanlal);
						mohList.append("\nMr. Mohanlal, the four time National Films Award winning actor, the man with the Midas touch, the versatile actor, described by film fraternity as the most gifted; a star who always lived up to the expectations of his fans.");
						mohList.addCommand(CMD_BACK);
						mohList.setCommandListener(this);
						display.setCurrent(mohList);
						break;
					case 3:
						lissyList = new Form("KERALA STRIKERS");
						lissyList.append(lissy);
						lissyList.append("\nMs. Lissy's transition from one of the silver screen's favourite leading ladies to one of the cinemas most successful businesswoman was seamless.");
						lissyList.addCommand(CMD_BACK);
						lissyList.setCommandListener(this);
						display.setCurrent(lissyList);

						break;
					case 4:
						shajiList = new Form("KERALA STRIKERS");
						shajiList.append(shaji);
						shajiList.append("\nMr. P.M. Shaji is the Managing Director of Cold Point Pvt. Ltd., a 33 year old company in the air conditioning industry, having a base of over 50,000 customers. It is the only company in its field with an ISO 9001:2008 Quality Certification.");
						shajiList.addCommand(CMD_BACK);
						shajiList.setCommandListener(this);
						display.setCurrent(shajiList);

						break;
					case 5:
						arjunkapoorList = new Form("BENGAL TIGERS");
						arjunkapoorList.append(arjunkapoor);
						arjunkapoorList.append("\nMr. Arjun Kapoor, the son of Boney Kapoor is an upcoming Bollywood actor. He won a lot of critical acclaim for his acting in his debut movie ishaqzaade.");
						arjunkapoorList.addCommand(CMD_BACK);
						arjunkapoorList.setCommandListener(this);
						display.setCurrent(arjunkapoorList);

						break;
					case 6:
						boneykapoorList = new Form("BENGAL TIGERS");
						boneykapoorList.append(boneykapoor);
						boneykapoorList.append("\nMr. Boney Kapoor is one of the most high profile Bollywood producers who has delivered a number of huge successful hits. Boney Kapoor's journey began in 1980 in the industry and some of his biggest hits have been movies like Mr.India, No entry, Company, Wanted and national award winning movie 'Pukar'.");
						boneykapoorList.addCommand(CMD_BACK);
						boneykapoorList.setCommandListener(this);
						display.setCurrent(boneykapoorList);

						break;
					case 7:
						rajshahList = new Form("BENGAL TIGERS");
						rajshahList.append(rajshah);
						rajshahList.append("\nMr. Raj Shah started his career as a Management Consultant and then diversified into distribution of Indian films in North America, Event management and then on to Line Producing all over North America. Actively involved in the media world, sports was the next field to diversify.");
						rajshahList.addCommand(CMD_BACK);
						rajshahList.setCommandListener(this);
						display.setCurrent(rajshahList);
						break;
					case 8:
						anandList = new Form("BHOJPURI DABANGGS");
						anandList.append(anand);
						anandList.append("\nAVIKA GROUP Ltd. is into diverse businesses like Information Technology, Marine Development, Airlines, Movies, Finance, Infrastructure and Sports. The company with its partners works with small, medium, and large firms, including many Fortune 1000 companies to help them solve business challenges.The Group is headquartered in the Central Business District of Mumbai with Regional offices in New Delhi, Bhopal and Chennai. Avika Group Ltd. has delivery, sales, support offices, consultant and sales partnerships with domain-expertise based consulting companies worldwide including Australia, USA, UK & Hong Kong and development centers in Bhopal, Pune and Nashik.'Raise the Bar Together' with our Customers, Employees and Shareholders is the Avika Group Ltd. mantra. With a strong, reputed and experienced management team, we promote a solution oriented business culture and an excellent working environment through our focus on customer care.");
						anandList.addCommand(CMD_BACK);
						anandList.setCommandListener(this);
						display.setCurrent(anandList);
						break;
					case 9:
						manojList = new Form("BHOJPURI DABANGGS");
						manojList.append(manoj);
						manojList.append("\nAVIKA GROUP Ltd. is into diverse businesses like Information Technology, Marine Development, Airlines, Movies, Finance, Infrastructure and Sports. The company with its partners works with small, medium, and large firms, including many Fortune 1000 companies to help them solve business challenges.The Group is headquartered in the Central Business District of Mumbai with Regional offices in New Delhi, Bhopal and Chennai. Avika Group Ltd. has delivery, sales, support offices, consultant and sales partnerships with domain-expertise based consulting companies worldwide including Australia, USA, UK & Hong Kong and development centers in Bhopal, Pune and Nashik.'Raise the Bar Together' with our Customers, Employees and Shareholders is the Avika Group Ltd. mantra. With a strong, reputed and experienced management team, we promote a solution oriented business culture and an excellent working environment through our focus on customer care.");
						manojList.addCommand(CMD_BACK);
						manojList.setCommandListener(this);
						display.setCurrent(manojList);
						break;
					case 10:
						ashokkhenyList = new Form("KARNATAKA BULLDOZERS");
						ashokkhenyList.append(ashokkheny);
						ashokkhenyList.append("\nAKK ENTERTAINMENT PVT. LTD.\nMr. Kheny is the promoter of Nandi Infrastructure Corridor Enterprises LTD. (NICE) which is developing the Bangalore-Mysore Infrastructure Corridor as the owners of this Build-Own-Operate-Transfer (BOOT) project.");
						ashokkhenyList.addCommand(CMD_BACK);
						ashokkhenyList.setCommandListener(this);
						display.setCurrent(ashokkhenyList);

						break;
					case 11:
						riteshList = new Form("VEER MARATHI");
						riteshList.append(ritesh);
						riteshList.append("\nMr. Riteish Deshmukh is a highly successful and popular Bollywood actor who has acted in several Bollywood movies over the past 10 years. He has been a part of many commercial as well critically acclaimed films. Apart from his acting skills, Riteish is also well known for his cricketing abilities.");
						riteshList.addCommand(CMD_BACK);
						riteshList.setCommandListener(this);
						display.setCurrent(riteshList);
						break;
					case 12:
						gangaprasadList = new Form("CHENNAI RHINOS");
						gangaprasadList.append(gangaprasad);
						gangaprasadList.append("\nCHENNAI RHINOS PVT. LTD.\nMr. K. Ganga Prasad is the Managing Director of GVR Infra Projects Ltd., the flagship company of the group is an ISO 9001:2008 certified integrated project management company offering consolidated services in the infrastructure space.");
						gangaprasadList.addCommand(CMD_BACK);
						gangaprasadList.setCommandListener(this);
						display.setCurrent(gangaprasadList);
						break;
					case 13:
						sohailList = new Form("MUMBAI HEROES");
						sohailList.append(sohail);
						sohailList.append("\nMUMBAI HEROES SPORTS LLP\nA prominent film production and distribution company of full length feature films in Hindi. Produced many large format events and has organised exhibition matches in various sports with Bollywood artists in India and Abroad.");
						sohailList.addCommand(CMD_BACK);
						sohailList.setCommandListener(this);
						display.setCurrent(sohailList);
						break;

					}
				}
			}
		}
		else if (c == CMD_BACK) {
			display.setCurrent(list);
		}
		
		
	}
}
