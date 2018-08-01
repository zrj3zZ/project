var str = '';
var copyright = '金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAH8BAACAAQAALAAAAE1+7WTzW21A19TuA1cau54N2ZPhBA1dNdxToLlaIEvUFY/EefxeNA0CBVyR8Wn19O8EQyLCEpc/Tbb2NKuPv7dlg5oCgyKBqW8spfz3zyABXwC216socbPgVecmn7GoVIzimlOIJEwJxqMgU0RhJVhefnALuHE3tJgebjSimjOhaQoAckr73fgUK0H+EFvEbiKa5njSQ/qG3WIymZFOheLrdj+mfAm34AnZ/nrT+vDkdt62QDt03ulDBcqjT4IsDPLBpKyV7IBYlsX1rDMGY2yOdnn4jFLZDwEihXJHEcvIimShoKmJN4EElBE76OeN3UqmxbcUf8G/moxHDbW5rE/9EVV1vSZaiOz755K72+dlibI3swmm0trm3FnWQl3OZ1Zq46V6ZFmS46Sl8ANhEPzMrH9/OrjVQKtGv1udsUIPjrxjvGc6qp/kzfeavhb4rYQimeRUgBH8fMOYCCDoeh81DEEEPPkkHAwma8M4gdir8/ySilHw6fn2rqeDJTbpkFr01pC5ZoeFqOrnY4w0OJ0=';

str += '<object id="WebOffice2015" ';

str += ' width="100%"';
str += ' height="100%"';

if ((window.ActiveXObject!=undefined) || (window.ActiveXObject!=null) ||"ActiveXObject" in window)
{
	str += ' CLASSID="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"  codebase="iWebOffice2015.cab#version=12,0,0,370"';
	str += '>';
	str += '<param name="Copyright" value="' + copyright + '">';
}
else
{
	str += ' progid="Kinggrid.iWebOffice"';
	str += ' type="application/iwebplugin"';
	str += ' OnCommand="OnCommand"';
	str += ' OnReady="OnReady"'; 
	str += ' OnOLECommand="OnOLECommand"';
	str += ' OnExecuteScripted="OnExecuteScripted"';
	str += ' OnQuit="OnQuit"';
	str += ' OnSendStart="OnSendStart"'; 
	str += ' OnSending="OnSending"';
	str += ' OnSendEnd="OnSendEnd"';
	str += ' OnRecvStart="OnRecvStart"';
	str += ' OnRecving="OnRecving"';
	str += ' OnRecvEnd="OnRecvEnd"';
	str += ' OnRightClickedWhenAnnotate="OnRightClickedWhenAnnotate"';
	str += ' OnFullSizeBefore="OnFullSizeBefore"';
	str += ' OnFullSizeAfter="OnFullSizeAfter"';	
	str += ' Copyright="' + copyright + '"';
	str += '>';
}
str += '</object>';
document.write(str); 