package com.paradigmcreatives.ccl;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.paradigmcreatives.ccl.CCL.ScrollCanvas;

public class Teams implements CommandListener{

	
	private Display display;
	CCL midlet;
	private static final Command CMD_BACK = new Command("Back", Command.EXIT, 1);
	private Form mumbaiList, chennaiList, teluguList, karnatakaList,
			keralaList, bengalList, veermarList, bhojpuriList;
	private Command exit, next;
	
	private Image mumbai, chennai, telugu, karnataka, kerala, bengal,
			veermarathi, bhojpuri, mumbaiteam, chennaiteam, teluguteam,
			karnatakateam, keralateam, bengalteam, veermarathiteam,
			bhojpuriteam;
	String[] stringElements = { "Mumbai-Heroes", "Chennai-Rhinos",
			"Telugu-warriors", "Karnataka-bulldozers", "Kerala-strikers",
			"Bengal-tigers", "Veer-marathi", "Bhojpuri-Dabanggs" };
	Image[] imageElements = { mumbai, chennai, telugu, karnataka, kerala,
			bengal, veermarathi, bhojpuri };
	 List list ;

	public Teams(CCL midlet){
	
		this.midlet = midlet;
		try {
			mumbai = Image.createImage("/mumbailogo.png");
			chennai = Image.createImage("/chennailogo.png");
			telugu = Image.createImage("/telugulogo.png");
			karnataka = Image.createImage("/karanatakalogo.png");
			kerala = Image.createImage("/keralalogo.png");
			bengal = Image.createImage("/bengallogo.png");
			veermarathi = Image.createImage("/veermarlogo.png");
			bhojpuri = Image.createImage("/bhojpurilogo.png");
			mumbaiteam = Image.createImage("/mumbai.png");
			chennaiteam = Image.createImage("/chennaiteam.png");
			teluguteam = Image.createImage("/telugu.png");
			karnatakateam = Image.createImage("/karnataka.png");
			keralateam = Image.createImage("/kerala.png");
			bengalteam = Image.createImage("/bengal.png");
			veermarathiteam = Image.createImage("/veermarathi.png");
			bhojpuriteam = Image.createImage("/bhojpuri.png");
			System.out.println("Creating images");
			list = new List("TEAMS", List.IMPLICIT, stringElements, imageElements);
			next = new Command("Select", Command.SCREEN, 0);
			exit = new Command("Back", Command.EXIT, 0);
			list.addCommand(next);
			list.addCommand(exit);
			
			list.setCommandListener(this);
			display =display.getDisplay(midlet);
			display.setCurrent(list);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}


	public void commandAction(Command c, Displayable d) {
		System.out.println(c.getLabel());
		int index = list.getSelectedIndex();
		System.out.println(index);
		if (d.equals(list)) {

			if (c == next || c == List.SELECT_COMMAND) {
				if (d.equals(list)) {
					switch (((List) d).getSelectedIndex()) {
					case 0:
						mumbaiList = new Form("TEAM MEMBERS");
						mumbaiList.append(mumbaiteam);
						mumbaiList
								.append("\nSUNIEL SHETTY(CAPTAIN)\n AFTABSHIVDASANI(VC)\n SOHAIL KHAN (BAT)\n BOBBY DEOL (ALL)\n SONU SOOD (ALL)\n ASHISHCHOWDHRY(BAT)\n VATSAL SHETH (ALL)\n SHABIR AHLUWALIA(ALL)\n ANGAD BEDI (BAT)\n APOORVA LAKHIA(ALL)\n VARUN BADOLA (ALL)\n KABIR SADANAND(ALL)\n RAJA BHERWANI(WC/ALL)\n SUNNY SINGH (ALL)\n SACHIN JOSHI (ALL)\n MANAV GOHIL (BAT)\n TUSHARJALOTA(ALL)\n SHARAD KELKAR (ALL)\n VARUN RAO (ALL)\n  INDRANEIL(WC/BAT)");
						mumbaiList.addCommand(CMD_BACK);
						mumbaiList.setCommandListener(this);
						Display.getDisplay(midlet).setCurrent(mumbaiList);

						break;

					case 1:
						chennaiList = new Form("TEAM MEMBERS");
						chennaiList.append(chennaiteam);
						chennaiList
								.append("\nVISHAL(CAPTAIN)\n VIKRAANTH(VICE CAPTAIN)\n BHARATH(BOWLER)\n ARYA(BOWLER)\n JIIVA(BOWLER)\n  ABBAS(BOWLER)\n PRITHIVI(BATSMAN)\n JITHANRAMESH(BOWLER)\n SHIVA(BOWLER)\n SHANTHNU(BOWLER)\n KARTHIKKUMAR(BOWLER)\n VISHNU(BOWLER)\n SRIKANTH(BOWLER)\n RAMANAA(WC/BAT)\n SHAAM(BOWLER)\n HAMSAVIRDHAN(BOWLER)\n RISHIKANTH(ALL)\n SRIBALAJIUDAY KUMAR");
						chennaiList.addCommand(CMD_BACK);
						chennaiList.setCommandListener(this);
						display.setCurrent(chennaiList);

						break;
					case 2:
						teluguList = new Form("TEAM MEMBERS");
						teluguList.append(teluguteam);
						teluguList
								.append("\nVENKATESH(BAT/CAPTAIN)\n RAM CHARAN TEJ(BAT)\n SRIKANTH (BAT)\n TARUN (ALL)\n NITIN (ALL)\n  RAGHU (ALL)\n AJAY (BOWLER)\n RAJIV KANAKALA(ALL)\n NIKHIL (BOWLER)\n KARTEEK (WC/BAT)\n SAMRAT (BOWLER)\n AADARSH (ALL)\n  PRINCE (BOWLER)\n NANDAKISHORE(BOWLER)\n VISHWA (ALL)\n HARSHVARDHAN(BOWLER)\n SUNNY (BOWLER)\n CHARAN TEJA (ALL)\n GIRI (ALL)\n ADETYA (ALL)");
						teluguList.addCommand(CMD_BACK);
						teluguList.setCommandListener(this);
						display.setCurrent(teluguList);

						break;
					case 3:
						karnatakaList = new Form("TEAM MEMBERS");
						karnatakaList.append(karnatakateam);
						karnatakaList
								.append("\nSUDEEP (WC/CAPTAIN)\n DHRUV SHARMA (ALL/VC)\n PRADEEP (ALL)\n THARUN CHANDRA (BAT)\n RAHUL (ALL)\n SAURAV (BAT)\n VISHWAS (BAT/WC)\n RAJEEV (ALL)\n ABHIMANYU (BOWLER)\n DARSHANCHIRUDIGANTH\n MCHETHANDHARMA\n KREETHIRAJTARUN\n SUDHIRJAYRAM\n KARTHIKHIMMATHMAHESHSP\n MANJUNATHAVANISH");
						karnatakaList.addCommand(CMD_BACK);
						karnatakaList.setCommandListener(this);
						display.setCurrent(karnatakaList);

						break;
					case 4:
						keralaList = new Form("TEAM MEMBERS");
						keralaList.append(keralateam);
						keralaList
								.append("\nMOHAN LAL (ALL/CAPTAIN)\n INDRAJITH (WC/BAT/VC)\n RAJEEV PILLAI (ALL)\n NIVIN PAULY (BAT)\n VIVEK GOPAN (ALL)\n MANIKUTTAN (ALL)\n BINEESH KODIYERI (ALL)\n SAIJU KURUP (ALL)\n PRAJOD KALABHAVAN (BAT)\n MANARUN BENNYMADAN\n MOHANARJUN\n SANTHOSH\n SLEEBARAGENDUSURESH\n NAIRRIAZ KHANSHAFIQUE\n REHMANRAHUL\n MADHAVVINOO MOHAN");
						keralaList.addCommand(CMD_BACK);
						keralaList.setCommandListener(this);
						display.setCurrent(keralaList);

						break;
					case 5:
						bengalList = new Form("TEAM MEMBERS");
						bengalList.append(bengalteam);
						bengalList
								.append("\nJEET (BAT/CAPTAIN)\n JISSHU (ALL/VC)\n JOY (BAT) RAJA (ALL)\n TABUN (ALL)\n SAUGATA (ALL)\n AMITABH (WC/BAT)\n INDRAJIT (ALL)\n SUMAN (ALL)\n SANDY (ALL)\n KAUSHIK (BAT)\n HEEROK\n INDRASISH\n RUBAN");
						bengalList.addCommand(CMD_BACK);
						bengalList.setCommandListener(this);
						display.setCurrent(bengalList);

						break;
					case 6:
						veermarList = new Form("TEAM MEMBERS");
						veermarList.append(veermarathiteam);
						veermarList
								.append("\nRITEISHDESHMUKH (ALL/CAPTAIN)\n MAHESH MANJREKAR (VC)\n AJIT PARAB\n ANKUSH CHAUDHARY\n MANOJE BIDDVAI\n NUPUR DHUDWADKAR\n RAHUL GORE\n RAJESH SHRINGARPURE\n SANJAY NARVEKAR\n SIDDHART JADHAV\n UPENDRA LIMAYE\n SIDDHANT MOOLEY\n RAHUL SUGANDH\n RAIEES LASHKARIA\n MADHAV DEOCHAKKE\n VIJAY KENKRE\n MAKRAND DESHPANDE\n SIDDHARTH CHANDEKAR\n ANIKET VISHWASRAO\n SAMIR DHARMADHIKARI");
						veermarList.addCommand(CMD_BACK);
						veermarList.setCommandListener(this);
						display.setCurrent(veermarList);

						break;
					case 7:
						bhojpuriList = new Form("TEAM MEMBERS");
						bhojpuriList.append(bhojpuriteam);
						bhojpuriList
								.append("\nMANOJ TIWARI(CAPTAIN)\n DINESH LAL YADAV (VC)\n RAVI KISHAN\n PRAVESH LAL YADAV\n UDAY TIWARI\n VIKRANT SINGH\n AJHOY SHARMA\n ADITYA OJHA\n PRAKASH JAISH\n AYAZ KHAN\n SUSHIL SINGH\n ABHAY SINHA\n KESARI LAL YADAV\n JAY YADAV\n SURYA DWIVEDI\n VIKASH SINGH\n PAWAN SINGH\n SANTOSH SINGH\n AJAY SRIVASTAV\n ANIL SAMRAT");
						bhojpuriList.addCommand(CMD_BACK);
						bhojpuriList.setCommandListener(this);
						display.setCurrent(bhojpuriList);

						break;

					}
				}
			}
		}else if (c == CMD_BACK) {
			//Display.getDisplay(MIDlet).setCurrent(list);
			display.setCurrent(list);
	}else if (c == exit) {
		
		midlet.Show();
	}
	}
	
}
