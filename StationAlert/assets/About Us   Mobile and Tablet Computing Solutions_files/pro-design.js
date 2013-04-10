Cufon.replace('h1');
Cufon.replace('h2');
Cufon.replace('h3');
Cufon.replace('h4');
Cufon.replace('#nav', {hover: true});
Cufon.replace('.button', {hover: true}); 
Cufon.replace('#slider-nav span', {hover: true});
Cufon.replace('#tabs li span', {hover: true});


current=1;
timeout=setTimeout("changeSlide(1)", 100);
function changeSlide(cur){
	clearInterval(timeout);
	current=cur;
	$("#slideIt").animate({marginLeft:'-'+((cur-1)*948)+'px'}, 0, function(){$("#slideIt ul:nth-child("+cur+") .featured").fadeIn();});
	$("#slideIt .featured").hide();
	$("#tabs li").removeClass('active');
	$("#tabs li:nth-child("+cur+")").addClass('active');
	if(current>=$("#tabs li").length)
		return false;
}
$(document).ready(function(){
	$("#tabs li").click(function(){changeSlide($(this).attr('rel'))});
});
