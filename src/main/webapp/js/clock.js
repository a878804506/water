/******************************************************************************
	ݒ
******************************************************************************/
var swfUrl = "js/clock.swf";

var swfTitle = "";


LoadBlogParts();

/******************************************************************************
			Ȃ
	o		document.writeɂHTMLo
******************************************************************************/
function LoadBlogParts(){
	var sUrl = swfUrl;
	
	var sHtml = "";
	sHtml += '<object classid="" width="220" height="100"  align="middle">';
	sHtml += '<param name="allowScriptAccess" value="always" />';
	sHtml += '<param name="movie" value="' + sUrl + '" />';
	sHtml += '<param name="quality" value="high" />';
	sHtml += '<param name="bgcolor" value="#ffffff" />';
	sHtml += '<param name="wmode" value="transparent" />';
	sHtml += '<embed wmode="transparent" src="' + sUrl + '" quality="high" bgcolor="#ffffff" width="220" height="100" name="' + swfTitle + '" align="middle" allowScriptAccess="always" type="application/x-shockwave-flash" />';
	sHtml += '</object>';
	
	document.write(sHtml);
}