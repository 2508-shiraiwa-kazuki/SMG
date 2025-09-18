function CheckDelete(){
	if(confirm('削除しますか?')){
		return true;
	} else{
		return false;
	}
}

function CheckAlter(){
	if(confirm('変更しますか?')){
		return true;
	} else{
		return false;
	}
}

//document.addEventListener("DOMContentLoaded", function() {
//
//    var strlimitDate = document.getElementById("limitDate");
//
//    var today = new Date();
//    var limitDate = new Date(strlimitDate);
//
//    if(limitDate.getTime() < today.getTime()){
//        strlimitDate.style.color = "red";
//    } else {
//        strlimitDate.style.color = "yellow";
//    }
//});


