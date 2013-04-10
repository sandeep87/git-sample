/*
 * bp_layout.js
 * Copyright 2007, Oracle. All rights reserved.
 */
 
function init () {
  stripeLi();
  stripeTr();
}

function stripeLi () {
  if (document.getElementsByTagName) {
    var divs = document.getElementsByTagName("div");
    for (var i = 0; i < divs.length; i++) {
      if (divs.item(i).getAttribute("class") == "portlet" ||
        (navigator.appName.indexOf("Microsoft") != -1 &&
        divs.item(i).getAttributeNode("class").value == "portlet")) {
        var uls = divs.item(i).getElementsByTagName("ul");
        if (uls.length > 0) {
          var lis = uls.item(uls.length - 1).getElementsByTagName("li");
          for (var j = 0; j < lis.length; j++) {
            if (j % 2 == 0) {
              lis.item(j).style.background = "white";
            }
          }
        }
      }
    }
  }
}

function stripeTr () {
  if (document.getElementsByTagName) {
    var tbodys = document.getElementsByTagName("tbody");
    for (var i = 0; i < tbodys.length; i++) {
      var trs = tbodys.item(i).getElementsByTagName("tr");
      for (var j = 0; j < trs.length; j++) {
        if (j % 2 == 0) {
          trs.item(j).style.background = "white";
        }
      }
    }
  }
}

function advancedSearch (mode) {
  if (document.getElementById) {
    if (mode == 'show') {
      document.getElementById("BASIC").style.display = "none";
      document.getElementById("ADVANCED").style.display = "block";
    } else {
      document.getElementById("BASIC").style.display = "block";
      document.getElementById("ADVANCED").style.display = "none";
    }
  }
}
