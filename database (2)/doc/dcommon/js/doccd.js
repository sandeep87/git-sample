/*
	Copyright 2006, 2007 Oracle Corporation.
	All rights reserved.
	Author: John Russell
*/

var arrow_width = 18;
var arrow_height = 18;
var arrow_style = 'margin: 0px 0px 0px 0px; padding: 0px; /* position: relative; top: 2px; */';
var arrow_side_name = 'blue_med_dark_side.gif';
var arrow_down_name = 'blue_med_dark_down.gif';

var sidearrow = '<img alt="Expand this chapter" width="' + arrow_width + '" height="' + arrow_height + '" style="' + arrow_style + '" src="../../nav/images/' + arrow_side_name + '" />';
var downarrow = '<img alt="Collapse this chapter" width="' + arrow_width + '" height="' + arrow_height + '" style="' + arrow_style + '" src="../../nav/images/' + arrow_down_name + '" />';

var sidearrow_regexp = new RegExp("<img.*?" + arrow_side_name + ".*?>","i");
var downarrow_regexp = new RegExp("<img.*?" + arrow_down_name + ".*?>","i");

var flipOne = (function (evt)
{
	var id = this.id.replace(/^[a-z]*/,"");

	var subheads = document.getElementById("list"+id);
	if (subheads == null)
	{
		print("Can't find list of subheadings, id list" + id);
		return false;
	}

	var discloseControl = document.getElementById("disclose"+id);
	if (discloseControl == null)
	{
		print("Can't find list of subheadings, id disclose" + id);
		return false;
	}

	if (!evt)
	{
		evt = window.event;
	}

	var shiftPressed;
//		var altPressed;
//		var ctrlPressed;

	if (evt)
	{
		shiftPressed = evt.shiftKey;
//			altPressed = evt.altKey;
//			ctrlPressed = evt.ctrlKey;
	}

	if (subheads.style.display == "none")
	{
		if (shiftPressed)
		{
			expand();
			return false;
		}

		subheads.style.display = "block";
		discloseControl.innerHTML = discloseControl.innerHTML.replace(sidearrow_regexp,downarrow);
	}
	else
	{
		if (shiftPressed)
		{
			collapse();
			return false;
		}

		subheads.style.display = "none";
		discloseControl.innerHTML = discloseControl.innerHTML.replace(downarrow_regexp,sidearrow);
	}

// Don't want the href="#" part of the link to trigger any scrolling.
	return false;
});


function addOnload(theFunc)
{
  var previous = window.onload;
  if (typeof window.onload != 'function')
  {
    window.onload = theFunc;
  }
  else
  {
    window.onload = function()
	 {
      previous();
      theFunc();
    }
  }
}

function find_home_link()
{
	var anchors = document.getElementsByTagName("a");
	for (i=0; i < anchors.length; i++)
	{
		theAnchor = anchors[i];
		if (theAnchor.href)
		{
			if (theAnchor.href.match(/homepage$/))
			{
				return theAnchor;
			}
		}
	}
}

function set_cookie(value)
{
	if (value.length != 16)
	{
		alert("Can't set preferences to non-16 length string '" + value + "'");
		return;
	}

	var now = new Date();
	var future = new Date();
	var expiry = now.getTime();  //Get the milliseconds since Jan 1, 1970
	expiry += 3600*1000*24*30;  //expires in 1 month(milliseconds)
	future.setTime(expiry);
	document.cookie = 'ORA_TAHITI_PREFS=' + value + '; expires=' + future.toGMTString() + '; path=/; domain=.oracle.com;';
	document.tahiti_prefs = value;
}

function get_prefs(which)
{
	if (! (which >= 0))
	{
		return "";
	}

	var prefs;

	if ((prefs = document.tahiti_prefs) == null)
	{
		prefs = document.cookie;
		prefs = prefs.replace(/.*; *(ORA_TAHITI_PREFS=.*?)(;|$).*/, "$1");

		if (prefs.length == 0)
		{
			return "";
		}

		if (!prefs.match(/^ORA_TAHITI_PREFS=/))
		{
			return "";
		}

		prefs = prefs.replace(/^ORA_TAHITI_PREFS=(.*)/, "$1");
		document.tahiti_prefs = prefs;
	}
	else
	{
	}

	return prefs.charAt(which);
}

function cookie_exists()
{
	var prefs;

	if ((prefs = document.tahiti_prefs) == null)
	{
		prefs = document.cookie;
		prefs = prefs.replace(/.*; *(ORA_TAHITI_PREFS=.*?)(;|$).*/, "$1");

		if (prefs.length == 0)
		{
			return false;
		}

		if (!prefs.match(/^ORA_TAHITI_PREFS=/))
		{
			return false;
		}
	}
	else
	{
		return true;
	}
}

function set_prefs(which, value)
{
	if (! (which >= 0))
	{
		return;
	}

	if (value != 0 && value != 1)
	{
		return;
	}

	var prefs;

	if ((prefs = document.tahiti_prefs) == null)
	{
		if (!cookie_exists())
		{
			set_cookie("----------------");
		}

		prefs = document.cookie;
		prefs = prefs.replace(/.*; *(ORA_TAHITI_PREFS=.*?)(;|$).*/, "$1");

		if (prefs.length == 0)
		{
			return;
		}

		if (!prefs.match(/^ORA_TAHITI_PREFS=/))
		{
			return;
		}

		prefs = prefs.replace(/^ORA_TAHITI_PREFS=(.*)/, "$1");
		document.tahiti_prefs = prefs;
	}
	else
	{
	}

	var new_prefs = prefs.substring(0,which) + value + prefs.substring(which+1,16);
	set_cookie(new_prefs);
}

addOnload(function()
{
	var converterLevel = (function()
	{
		var allmeta = document.getElementsByTagName("meta");
		var theLevel;

		for (i=0; i < allmeta.length; i++)
		{

			if (allmeta[i].name == "generator")
			{
				theLevel = allmeta[i].content;
				return theLevel;
			}
		}
		return null;
	});

	var findSubheadTag = (function()
	{
		var potential = "ul";
		var subheads = document.getElementsByTagName(potential);
		if (subheads != null && subheads.length > 0)
		{
			return potential;
		}

		potential = "dl";
		subheads = document.getElementsByTagName(potential);
		if (subheads != null && subheads.length > 0)
		{
			return potential;
		}

		return null;
	});

	var eligibleToc = (function()
	{
		var myAddress = window.location.href;

		if (!myAddress.match(/\/[a-z]+\.[0-9]+\//))
		{
			return false;
		}

		if (myAddress.match(/\/mix.[0-9]{3}\//))
		{
			return false;
		}

		var head1s = document.getElementsByTagName("h1");
		if (head1s == null || head1s.length == 0)
		{
			return false;
		}

		var conflict = document.getElementById("expandlink");
		if (conflict)
		{
			return false;
		}
		conflict = document.getElementById("collapselink");
		if (conflict)
		{
			return false;
		}

		var firstHead1 = head1s[0];
		if (! firstHead1.innerHTML.match(/\bContents\b/))
		{
			return false;
		}

		if (converterLevel() == null)
		{
			return false;
		}

		if (findSubheadTag() == null)
		{
			return false;
		}

		return true;
	});

	if (eligibleToc() == false)
	{
		return;
	}

	var head1s = document.getElementsByTagName("h1");
	var firstHead1 = head1s[0];

	var head2s = document.getElementsByTagName("h2");

	var subheadTag = findSubheadTag();
	var subheads = document.getElementsByTagName(subheadTag);

	var listCounter = 0;
	var firstLevel = {};

	var print = (function (what)
	{
		alert(what);
		return;

		var msgArea = document.getElementById("message");
		var msgBackup = document.getElementById("footer");
		if (msgArea != null)
		{
			msgArea.innerHTML += what + "<br />";
		}
		else if (msgBackup != null)
		{
			msgBackup.innerHTML += what + "<br />";
		}
	});

	var analyze = (function()
	{
		var subheadTag = findSubheadTag();

		var subheads = document.getElementsByTagName(subheadTag);
		var head2s = document.getElementsByTagName("h2");
		var counter = 0;
		for (i = 0; i < subheads.length; i++)
		{
			currentList = subheads[i];
			var previousHead = currentList.previousSibling;
			if ((previousHead != null) && (previousHead.nodeName != "H2"))
			{
				previousHead = previousHead.previousSibling;
			}
			else
			{
			}

			if (previousHead == null)
			{
			}
			else if (previousHead.nodeName != "H2")
			{
			}
			else
			{
				var disclosureControl = '<a href="#" id="disclose' + counter + '">' + sidearrow + '</a> ';

				firstLevel[counter] = previousHead;
				previousHead.id = "head" + counter;
				previousHead.innerHTML = disclosureControl + previousHead.innerHTML;

				var justCreated = document.getElementById("disclose" + counter);
				if (justCreated != null)
				{
					justCreated.numericID = counter;
					justCreated.title = "Expand or Collapse";


					if (justCreated.addEventListener)
					{
						justCreated.addEventListener('click',flipOne,false);
					}
					else
					{
						justCreated.onclick = flipOne;
					}
				}
				else
				{
				}
				currentList.id =  "list" + counter;
				counter++;
			}
		}

		for (i = 0; i < head2s.length; i++)
		{
			currentHead = head2s[i];
			if (currentHead.innerHTML.match(/>Part (I|V|X)/))
			{
				currentHead.style.textAlign = "center";
				currentHead.style.borderTop = "medium solid #039";
				currentHead.style.marginTop = "10px";
				currentHead.style.paddingTop = "10px";
			}
			else if (!currentHead.innerHTML.match(/id="disclose/))
			{
				currentHead.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + currentHead.innerHTML;
			}
			else
			{
			}
		}
	});

	var expand = (function()
	{
		var subheads = document.getElementsByTagName(findSubheadTag());
		var head2s = document.getElementsByTagName("h2");

		for (i = 0; i < subheads.length; i++)
		{
			var theList = subheads[i];
			theList.style.display = "block";
			theList.style.marginTop = "1px";
			theList.style.marginBottom = "1px";
		}

		for (i = 0; i < head2s.length; i++)
		{
			var theHead = head2s[i];
			if (!currentHead.innerHTML.match(/>Part (I|V|X)/))
			{
				theHead.style.marginTop = "5px";
			}
			if (currentHead.innerHTML.match(/>Part (I|V|X)/))
			{
				theHead.style.marginBottom = "1px";
			}
			var discloseControl = document.getElementById("disclose"+i);
			if (discloseControl != null)
			{
				discloseControl.innerHTML = discloseControl.innerHTML.replace(sidearrow_regexp,downarrow);
			}
		}
	});

	var collapse = (function()
	{
		var subheads = document.getElementsByTagName(findSubheadTag());
		var head2s = document.getElementsByTagName("h2");

		for (i = 0; i < subheads.length; i++)
		{
			var theList = subheads[i];
			var previousHead = theList.previousSibling;
			if ((previousHead != null) && (previousHead.nodeName != "H2"))
			{
				previousHead = previousHead.previousSibling;
			}
			else
			{
			}

			if ((previousHead != null) && (previousHead.nodeName == "H2"))
			{
				theList.style.display = "none";
				theList.style.paddingLeft = "18px";
			}
			theList.style.marginTop = "1px";
			theList.style.marginBottom = "1px";
		}

		for (i = 0; i < head2s.length; i++)
		{
			var theHead = head2s[i];
			if (!currentHead.innerHTML.match(/>Part (I|V|X)/))
			{
				theHead.style.marginTop = "5px";
			}
			theHead.style.marginBottom = "1px";

			theHead.style.marginLeft = "0px";
			theHead.style.paddingLeft = "0px";

			var discloseControl = document.getElementById("disclose"+i);
			if (discloseControl != null)
			{
				discloseControl.innerHTML = discloseControl.innerHTML.replace(downarrow_regexp,sidearrow);
				discloseControl.style.padding = "0px 6px";
				if (discloseControl.addEventListener == null)
				{
					discloseControl.onclick = flipOne;
				}
			}
		}

	});

	analyze();
	collapse();

	var toolbar = document.createElement('div');
	var toolbarContent = document.createTextNode('');
	var fontsize = "120%";

	var button_style = '';
	var expand_all = '<img id="expand_all_button" style="' + button_style + '" alt="Expand All" src="../../nav/images/expand_hlight_button.gif" />';
	var collapse_all = '<img id="collapse_all_button" style="' + button_style + '" alt="Collapse All" src="../../nav/images/collapse_hlight_butt.gif" />';

	toolbar.appendChild(toolbarContent);
	toolbar.innerHTML = '<a style="font-size: 120%; border: none; padding: 4px 6px 4px 6px; " href="#" id="expandlink">' + expand_all + '</a><span style="font-size: 120%;"> | </span><a style="font-size: 120%; border: none; padding: 4px 6px 4px 6px; " href="#" id="collapselink">' + collapse_all + '</a> (Or click any <span id="expand_collapse_icon">' + sidearrow + '</span> icon to expand that chapter.)';

	firstHead1.parentNode.insertBefore(toolbar, firstHead1.nextSibling);

	var expandLink = document.getElementById('expandlink');
	var collapseLink = document.getElementById('collapselink');

	expandLink.onclick = expand;
	collapseLink.onclick = collapse;

	var always_link = document.getElementById("collapse_preference_expanded");
	var always_expand;
	var always_collapse;

	var bumpDown = (function(id)
	{
		var theItem = document.getElementById(id);
		if (theItem)
		{
			try
			{
				theItem.style.position = "inherit";
				theItem.style.position = "relative";
				theItem.style.top = "4px";
			}
			catch (err) {}
		}

	});

	bumpDown("expand_all_button");
	bumpDown("collapse_all_button");
	bumpDown("expand_collapse_icon");

	always_expand = (function()
	{
		expand();
		var me = document.getElementById("collapse_preference_expanded");
		if (me)
		{
			me.innerHTML = "Always collapse all";
			me.id = "collapse_preference_collapsed";
			if (me.href)
			{
				me.href = me.href.replace(/_expanded/,"_collapsed");
			}
			me.onclick = always_collapse;
		}
		else
		{
		}
		return true;
	});

	always_collapse = (function()
	{
		collapse();
		var me = document.getElementById("collapse_preference_collapsed");
		if (me)
		{
			me.innerHTML = "Always expand all";
			me.id = "collapse_preference_expanded";
			if (me.href)
			{
				me.href = me.href.replace(/_collapsed/,"_expanded");
			}
			me.onclick = always_expand;
		}
		else
		{
		}
		return true;
	});

	if (always_link)
	{
		always_link.onclick = always_expand;
	}
	else
	{
		always_link = document.getElementById("collapse_preference_collapsed");
		if (always_link)
		{
			always_link.onclick = always_collapse;
		}
	}

});

function showPlatform(what)
{
	var platform_name = new RegExp(what,"i");

	var head1s = document.getElementsByTagName("h1");

	for (i = 0; i < head1s.length; i++)
	{
		theHead = head1s[i];
		theText = theHead.innerHTML;

		if (what == null)
		{
			theHead.style.display = "block";
			if (theHead.className.match(/libportletheader/))
			{
				var para = theHead.nextSibling;
				while (para && ((para.nodeName == 'P') || (para.nodeName == '#text')))
				{
					if (para.nodeName == 'P')
					{
						para.style.display = "block";
					}
					para = para.nextSibling;
				}
			}
		}
		else if (theHead.className == null)
		{
			theHead.style.display = "block";
		}
		else if (!theHead.className.match(/libportletheader/))
		{
			theHead.style.display = "block";
		}
		else if (theHead.id.match(platform_name))
		{
			theHead.style.display = "block";
			var para = theHead.nextSibling;
			while (para && ((para.nodeName == 'P') || (para.nodeName == '#text')))
			{
				if (para.nodeName == 'P')
				{
					para.style.display = "block";
				}
				para = para.nextSibling;
			}
		}
		else if (!theText.match(/Installation Guides/i))
		{
			theHead.style.display = "block";
		}
		else
		{
			theHead.style.display = "none";


			var para = theHead.nextSibling;
			while (para && ((para.nodeName == 'P') || (para.nodeName == '#text')))
			{
				if (para.nodeName == 'P')
				{
					para.style.display = "none";
				}
				para = para.nextSibling;
			}
		}
	}

	var tables = document.getElementsByTagName("table");

	for (i = 0; i < tables.length; i++)
	{
		theTable = tables[i];

		if (what == null)
		{
			theTable.style.display = "block";
		}
		else if (theTable.className == null)
		{
			theTable.style.display = "block";
		}
		else if (!theTable.className.match(/libdoclist/))
		{
			theTable.style.display = "block";
		}
		else if (theTable.id.match(platform_name))
		{
			theTable.style.display = "block";
		}
		else if (!theTable.id.match(/install/i))
		{
			theTable.style.display = "block";
		}
		else
		{
			theTable.style.display = "none";
		}
	}

}

addOnload(function()
{
	var needs_absolute_position = (function()
	{
		if (window.XMLHttpRequest)
		{
			return false;
		}
		return true;
	});

	var converterLevel = (function()
	{
		var allmeta = document.getElementsByTagName("meta");
		var theLevel;

		for (i=0; i < allmeta.length; i++)
		{

			if (allmeta[i].name == "generator")
			{
				theLevel = allmeta[i].content;
				return theLevel;
			}
		}
		return null;
	});

	var eligibleBookNav = (function()
	{
		var myAddress = window.location.href;
		try
		{
			var myParams = window.location.search.substring(1);

			if (myParams.match(/type=popup/))
			{
				return false;
			}
		}
		catch (err)
		{
		}

		if (!myAddress.match(/\/[a-z]+\.[0-9]+\//))
		{
			return false;
		}

		if (myAddress.match(/\/mix.[0-9]{3}\//))
		{
			return false;
		}

		if (top != self)
		{
			return false;
		}

		var head1s = document.getElementsByTagName("h1");
		if (head1s == null || head1s.length == 0)
		{
			return false;
		}

		var conflict = document.getElementById("nav");
		if (conflict)
		{
			return false;
		}

		if (converterLevel() == null)
		{
			return false;
		}

		return true;
	});

	if (eligibleBookNav() == false)
	{
		return;
	}

	var rememberLayout = (function()
	{
		if (document.oldlayout == null)
		{
			var bodyTags = document.getElementsByTagName("body");
			var htmlTags = document.getElementsByTagName("html");
			var layout = {};

			if (bodyTags && htmlTags)
			{
				var theBody = bodyTags[0];
				var theHTML = htmlTags[0];

				layout.htmlOverflowY = theHTML.style.overflowY;
				layout.bodyOverflowY = theBody.style.overflowY;

				layout.bodyPaddingLeft = theBody.style.paddingLeft;

				layout.bodyOverflowX = theBody.style.overflowX;
				layout.bodyOverflowY = theBody.style.overflowY;
				layout.bodyHeight = theBody.style.height;

				layout.htmlOverflowY = theHTML.style.overflowY;
				layout.htmlHeight = theHTML.style.height;

				document.oldlayout = layout;
			}
		}
	});

	var restoreLayout = (function()
	{
		if (document.oldlayout)
		{
			var bodyTags = document.getElementsByTagName("body");
			var htmlTags = document.getElementsByTagName("html");
			var layout = {};

			if (bodyTags && htmlTags)
			{
				var theBody = bodyTags[0];
				var theHTML = htmlTags[0];
				var layout = {};

				if (layout = document.oldlayout)
				{

					if (layout.htmlOverflowY != theHTML.style.overflowY)
					{
						theHTML.style.overflowY = layout.htmlOverflowY;
					}

					if (layout.bodyOverflowY != theBody.style.overflowY)
					{
						theBody.style.overflowY = layout.bodyOverflowY;
					}

					if (layout.bodyPaddingLeft != theBody.style.paddingLeft)
					{
						layout.bodyPaddingLeft = theBody.style.paddingLeft;
					}

					if (layout.bodyOverflowX != theBody.style.overflowX)
					{
						layout.bodyOverflowX = theBody.style.overflowX;
					}

					if (layout.bodyOverflowY != theBody.style.overflowY)
					{
						layout.bodyOverflowY = theBody.style.overflowY;
					}

					if (layout.bodyHeight != theBody.style.height)
					{
						layout.bodyHeight = theBody.style.height;
					}

					if (layout.htmlOverflowY != theHTML.style.overflowY)
					{
						layout.htmlOverflowY = theHTML.style.overflowY;
					}

					if (layout.htmlHeight != theHTML.style.height)
					{
						layout.htmlHeight = theHTML.style.height;
					}
				}
			}
		}
	});

	var makeNav = (function()
	{
		var bookTitle = (function()
		{
			var allmeta = document.getElementsByTagName("meta");

			for (i=0; i < allmeta.length; i++)
			{
				if (allmeta[i].name == "doctitle")
				{
					return '\
<h1 id="nav_this_book" style="font-size: 110%; margin-bottom: 0px; padding-bottom: 0px;">This Book</h1>\
<p style="margin-top: 0px; padding-top: 4px; padding-left: 0.5em;"><a href="toc.htm">' + allmeta[i].content + '</a></p>';
				}
			}
			return ''; // '<h1>This Book</h1><p>Title unknown.</p>';
		});

		rememberLayout();


		var head1s = document.getElementsByTagName("div");
		if (head1s)
		{
			var firstHead1 = head1s[0];
			var myAddress = window.location.href;
			var partno = myAddress.replace(/.*\/([a-z][0-9]{5})\/.*/, "$1");
			var navDiv = document.createElement('div');
			var navContent = document.createTextNode('');
			navDiv.appendChild(navContent);
			navDiv.innerHTML = '\
<div id="flipNav_container" style="margin-top: 20px; clear: both;">\
</div>\
\
<h1 style="font-size: 110%; margin-bottom: 0px; padding-bottom: 0px;">Quick Lookup</h1>\
<div class="shortcut_links">\
<a href="../../nav/portal_booklist.htm" target="_top">Master Book List</a>\
</div>\
</div>\
<div>\
</div>\
\
<div style="margin-right: 8px;">' + 
bookTitle() +
'<h1 id="nav_this_page" style="font-size: 110%; margin-bottom: 4px; padding-bottom: 0px;">This Page</h1>\
<div style="margin-right: 8px; margin-left: 0.5em;">\
';

			var divCounter = 0;

			var allHead1s = document.getElementsByTagName("h1");
			if (allHead1s)
			{
				var theHead1 = allHead1s[0];
				if (theHead1.id == null || theHead1.id.length == 0)
				{
					theHead1.id = "insertedID" + divCounter++;
				}
				else
				{
				}

				var innerText = theHead1.innerHTML.replace(/<.*?>/g,"");
				navDiv.innerHTML += '<div style="margin-left: 0.5em; margin-right: 8px;"><a href="#' + theHead1.id + '">' + innerText + '</a></div>';

				if (theHead1.className != null && theHead1.className == "glossary")
				{
					var allParas = document.getElementsByTagName("p");
					if (allParas)
					{
						for (i=0; i < allParas.length; i++)
						{
							var thePara = allParas[i];
							if (thePara.className != null && thePara.className == "glossterm")
							{
								if (thePara.id == null || thePara.id.length == 0)
								{
									thePara.id = "insertedID" + divCounter++;
								}
								var innerText = thePara.innerHTML.replace(/<.*?>/g,"");
								navDiv.innerHTML += '<div style="padding-left: 1.5em; padding-right: 8px;"><a href="#' + thePara.id + '">' + innerText + '</a></div>';
							}
						}
					}
				}
				else if ((theHead1.className != null && theHead1.className == "toc") || theHead1.innerHTML == "Contents")

				{
				}
				else
				{
					var allHead2s = document.getElementsByTagName("h2");
					for (i=0; i < allHead2s.length; i++)
					{
						var theHead2 = allHead2s[i];
						if (theHead2.id == null || theHead2.id.length == 0)
						{
							theHead2.id = "insertedID" + divCounter++;
						}
						var innerText = theHead2.innerHTML.replace(/<.*?>/g,"");
						innerText = innerText.replace(/&nbsp;/g,"");
						navDiv.innerHTML += '<div style="padding-right: 8px; padding-left: 1.5em;"><a href="#' + theHead2.id + '">' + innerText + '</a></div>';
					}
				}
			}

			navDiv.innerHTML += '</div></div>';

			firstHead1.parentNode.insertBefore(navDiv, firstHead1.nextSibling);
			navDiv.style.display = "none";
			navDiv.id = "nav";
			navDiv.style.top = "0";
			navDiv.style.left = "0";
			navDiv.style.width = "19.5em";



			navDiv.style.position = "fixed";

			try
			{
				navDiv.style.marginLeft = "1em;"
			}
			catch(err)
			{
				navDiv.style.position = "absolute";
			}

			navDiv.style.overflowX = "hidden";
			navDiv.style.overflowY = "auto";
			navDiv.style.padding = "0px 0px 0px 0px";
			navDiv.style.margin = "0px 0px 0px 4px";
			navDiv.style.height = "100%";
			navDiv.style.borderRight = "solid 2px #ccc";
			navDiv.style.zOrder = "100";
			navDiv.style.backgroundColor = "white";
		}

		var toolbar = document.getElementById('flipNav_container');

		if (toolbar != null)
		{
			toolbar.innerHTML = '<a id="flipNavInline" style="float: right; clear: both; margin-top: 4px;" href="#"><img src="../../nav/images/show_nav.gif" border="0" alt="Show Navigation" /></a><br />';
		}
		else
		{
	//		alert("flipNav_container does not exist");
		}

	});

	var showNav = (function()
	{
		var navToggle = document.getElementById("flipNav");
		var navToggleInline = document.getElementById("flipNavInline");
		var bodyTags = document.getElementsByTagName("body");
		var htmlTags = document.getElementsByTagName("html");
		var navDiv = document.getElementById("nav");
		if (bodyTags && htmlTags)
		{
			var theBody = bodyTags[0];
			var theHTML = htmlTags[0];
			theBody.id = "left-nav-present";
			theBody.style.paddingLeft = "20em";

			try
			{
				theHTML.style.overflowY = "inherit";
				theHTML.style.overflowY = "auto";
			}
			catch (err)
			{
				{
					theBody.style.overflowX = "hidden";
					theBody.style.overflowY = "auto";
					theBody.style.height = "100%";

					theHTML.style.overflowY = "hidden";
					theHTML.style.height = "100%";
					if (window.onresize)
					{
						window.onresize();
					}
				}
			}

		}
		else
		{
			alert("Couldn't find body element");
		}

		if (navToggle)
		{
			navToggle.onclick = navToggle.hideFunction;
			navToggle.innerHTML = navToggle.innerHTML.replace(/Show Navigation/,"Hide Navigation");
			navToggle.innerHTML = navToggle.innerHTML.replace(/show_nav/,"hide_nav");
			if (navDiv)
			{
				navDiv.style.display = "block";
				set_prefs(1,0);
			}
		}
		else
		{
			alert("Couldn't find flipNav element");
		}

		if (navToggleInline)
		{
			navToggleInline.onclick = navToggleInline.hideFunction;
			navToggleInline.innerHTML = navToggleInline.innerHTML.replace(/Show Navigation/,"Hide Navigation");
			navToggleInline.innerHTML = navToggleInline.innerHTML.replace(/show_nav/,"hide_nav");
		}
	});

	var hideNav = (function()
	{

		restoreLayout();

		var navToggle = document.getElementById("flipNav");
		var navToggleInline = document.getElementById("flipNavInline");
		var bodyTags = document.getElementsByTagName("body");
		var htmlTags = document.getElementsByTagName("html");
		var navDiv = document.getElementById("nav");

		if (bodyTags)
		{
			var theBody = bodyTags[0];
			theBody.id = "";
			theBody.style.paddingLeft = "0px";
		}

		if (navToggle)
		{
			navToggle.onclick = navToggle.showFunction;
			navToggle.innerHTML = navToggle.innerHTML.replace(/Hide Navigation/,"Show Navigation");
			navToggle.innerHTML = navToggle.innerHTML.replace(/hide_nav/,"show_nav");
			if (navDiv)
			{
				navDiv.style.display = "none";
				set_prefs(1,1);
			}
		}
		if (navToggleInline)
		{
			navToggleInline.onclick = navToggleInline.showFunction;
			navToggleInline.innerHTML = navToggleInline.innerHTML.replace(/Hide Navigation/,"Show Navigation");
			navToggleInline.innerHTML = navToggleInline.innerHTML.replace(/hide_nav/,"show_nav");
			if (navDiv)
			{
				navDiv.style.display = "none";
				set_prefs(1,1);
			}
		}
	});

	var head1s = document.getElementsByTagName("h1");
	var firstHead1 = head1s[0];

	var toolbar = document.createElement('div');
	var toolbarContent = document.createTextNode('');
	toolbar.appendChild(toolbarContent);
	toolbar.innerHTML = '<a id="flipNav" style="float: left; clear: left; margin-top: 4px;" href="#"><img src="../../nav/images/show_nav.gif" border="0" alt="Show Navigation" /></a><br />';

	firstHead1.parentNode.insertBefore(toolbar, firstHead1);

	makeNav();

	var navToggle = document.getElementById("flipNav");
	if (navToggle)
	{
		navToggle.onclick = showNav;
		navToggle.showFunction = showNav;
		navToggle.hideFunction = hideNav;
	}
	else
	{
		alert("Can't find flipNav element");
	}

	var navToggleInline = document.getElementById("flipNavInline");
	if (navToggleInline)
	{
		navToggleInline.onclick = showNav;
		navToggleInline.showFunction = showNav;
		navToggleInline.hideFunction = hideNav;
	}
	else
	{
	}


	var should_hide_nav = get_prefs(1);
	if (should_hide_nav != "1")
	{
		showNav();
	}
});

addOnload(function()
{
	var myAddress = window.location.href;
	var anchor;
	var tab;

	if (myAddress.match(/#/))
	{
		anchor = myAddress.replace(/.*#(.*)/,"$1");
	}

	if (myAddress.match(/\bselected=[0-9]+\b/))
	{
		tab = myAddress.replace(/.*\bselected=([0-9]+)\b.*/,"$1")
	}

	if (tab == 99)
	{
		return;
	}

	var anchors = document.getElementsByTagName("a");
	var theAnchor;

	var progress = "";
	var portal_id = new RegExp("\\bselected=" + tab + "\\b");
	var anchor_id = new RegExp("#" + anchor + "$");

	for (i=0; i < anchors.length; i++)
	{
		theAnchor = anchors[i];
		if (theAnchor.className == "linkanchor")
		{
			progress += "+";
			if (theAnchor.href.match(portal_id))
			{
				if
				(
					(
						(anchor == null)
						&&
						(theAnchor.href.match(/#/) == false)
					)
					|| 					theAnchor.href.match(anchor_id)
				)
				{
					var id = theAnchor.id;
					var numeric = id.replace(/^a/,"");
					var theDiv = document.getElementById("div" + numeric);


					if (id != null)
					{
						theDiv = document.getElementById("div" + numeric);
					}
					else
					{
						theDiv = theAnchor;
						alert("Starting at node with ID " + theDiv.id);
						theDiv = theDiv.parentNode;
						alert("Walking up the DOM to node with ID " + theDiv.id);
						theDiv = theDiv.parentNode;
						alert("Walking up the DOM to node with ID " + theDiv.id);
						theDiv.style.backgroundColor = "#e6e6ef";
						alert("Changing color of div with ID " + theDiv.id);
						theDiv = null;
					}

					if (theDiv != null)
					{
						theDiv.style.backgroundColor = "#e6e6ef";
						theDiv = theDiv.parentNode;
						if (theDiv != null)
						{
							theDiv = theDiv.parentNode;
							if (theDiv != null)
							{
								flipEntry(theDiv.id);
							}
							else
							{
							}
						}
						else
						{
						}
					}
					else
					{
					}
				}
			}
		}
		else
		{
			progress += "-" + theAnchor.className;
		}
	}

});

addOnload(function()
{
	var bodyTags = document.getElementsByTagName("body");
	var htmlTags = document.getElementsByTagName("html");
	var examine;

	var eligibleScrollbars = (function()
	{
		var theBody = bodyTags[0];
		var theHTML = htmlTags[0];

		if (theBody == null)
		{
			return false;
		}

		if (theHTML == null)
		{
			return false;
		}

		var needs_absolute_position = (function()
		{
			if (window.XMLHttpRequest)
			{
				return false;
			}
			return true;
		});

		var hcw = theHTML.clientWidth;
		var how = theHTML.offsetWidth;
		var hsw = theHTML.scrollWidth;

		var bcw = theBody.clientWidth;
		var bow = theBody.offsetWidth;
		var bsw = theBody.scrollWidth;

		if ((bcw != bow) && (bsw != bow) && (bcw == bsw))
		{
			return true;
		}

		return false;
	});

	if (eligibleScrollbars())
	{

		try
		{
			bodyTags[0].style.width = (htmlTags[0].offsetWidth - 280) + "px";
		}
		catch(err) {}

		htmlTags[0].style.overflowY = "hidden";


		var resize_handler = (function()
		{
			var bodyTags = document.getElementsByTagName("body");
			var htmlTags = document.getElementsByTagName("html");
			if (bodyTags && htmlTags)
			{
				try
				{
					bodyTags[0].style.width = (htmlTags[0].offsetWidth - 280) + "px";
				}
				catch(err) {}
			}
		});

		window.onresize = resize_handler;
	}

});

addOnload(function()
{

	var eligibleSynch = (function()
	{
		var vendor = navigator.vendor;
		if (vendor == null)
		{
			return false;
		}

		var is_safari = vendor.match(/Apple/);
		if (is_safari)
		{
			return false;
		}

		var myAddress = window.location.href;


		if (myAddress.match(/\.htm#.*/))
		{
			return true;
		}
		return false;
	});

	if (eligibleSynch() == true)
	{
		var anchor =  window.location.href.replace(/.*#/,"");
		var el = document.getElementById(anchor);
		if (el)
		{
			var scroll_position = findPosY(el);
			window.scrollTo(0,scroll_position);
		}
//		window.location.href = window.location.href;
	}
});

addOnload(function()
{
	var navDiv = document.getElementById("nav");
	if (navDiv)
	{
		navDiv.style.visibility = "visible";
	}
});

addOnload(function()
{
	var eligibleErrorFile = (function()
	{
		return false;

		var myAddress = window.location.href;
		if (myAddress.match(/\/mix.[0-9]{3}\//))
		{
			return false;
		}
		if (myAddress.match(/toc.htm$/))
		{
			return false;
		}
		if (myAddress.match(/index.htm$/))
		{
			return false;
		}
		if (myAddress.match(/\/nav\//))
		{
			return false;
		}
		if (myAddress.match(/\/pls\//))
		{
			return false;
		}

		var allmeta = document.getElementsByTagName("meta");

		for (i=0; i < allmeta.length; i++)
		{
			if (allmeta[i].name == "doctitle")
			{
				if (allmeta[i].content.match(/Error Messages/))
				{
					return false;
				}
			}
		}

		return true;
	});

	if (eligibleErrorFile() == false)
	{
		return;
	}

	var myAddress = window.location.href;
	var mylib = myAddress.replace(/.*\/(B19306_01|10\/102|11\/111|B25553_01|cs\/1012|B14099_19|101202fulldoc|B25221_03|101300doc_final|B28196_01|1014im_final)\/.*/, "$1");
	var library = new Array();
	library["10/102"] = {code:"db102", host:"tahiti-stage.us.oracle.com"};
	library["B19306_01"] = {code:"db102", host:"www.oracle.com"};
	library["11/111"] = {code:"db111", host:"tahiti-stage.us.oracle.com"};

	var dad = "db111";
	var host = "tahiti-stage.us.oracle.com";

	if (dad == null || host == null)
	{
		alert("No search URL defined for library " + mylib);
		return;
	}

	var errors = document.body.innerHTML.match(/(ORA|SQL|EXP|IMP|KUP|UDE|UDI|DBV|NID|DGM|LCD|QSM|OCI|RMAN|LRM|LFI|PLS|PLW|AMD|CLSR|CLSS|PROC|PROT|TNS|NNC|NNO|NNL|NPL|NNF|NMP|NCR|NZE|O2F|O2I|O2U|PCB|PCC|AUD|IMG|VID|DRG|LPX|LSX)-\d{3,5}/g);
	var errorstring;

	if (errors != null)
	{
		document.body.innerHTML = document.body.innerHTML.replace( /((ORA|SQL|EXP|IMP|KUP|UDE|UDI|DBV|NID|DGM|LCD|QSM|OCI|RMAN|LRM|LFI|PLS|PLW|AMD|CLSR|CLSS|PROC|PROT|TNS|NNC|NNO|NNL|NPL|NNF|NMP|NCR|NZE|O2F|O2I|O2U|PCB|PCC|AUD|IMG|VID|DRG|LPX|LSX)-\d{3,5})/g ,"<a href=\"http://" + host + "/pls/" + dad + "/lookup?id=$1\">$1</a>");
	}
});

function findPosY(obj)
{
	var curtop = 0;
	if(obj.offsetParent)
	while(1)
	{
	  curtop += obj.offsetTop;
	  if(!obj.offsetParent)
	    break;
	  obj = obj.offsetParent;
	}
	else if(obj.y)
	curtop += obj.y;
	return curtop;
}

