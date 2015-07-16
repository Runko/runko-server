$(document).ready(function(){

	var toggling = $(".show-next-hidden");
	var toggleSpeed = 200;
	
	toggling.click(function(){
	    var button = $(this);
	    var next = button.nextAll(".hidden").first();
	    
	    if(next.css("display") == "none"){
    		next.show(toggleSpeed);
            button.find("a").text("Yikes, hide this after all!");
		} else {
		    next.hide(toggleSpeed);   
		    button.find("a").text("Uhh, let me specify the key anyway...");
	    }
	});
	
});
