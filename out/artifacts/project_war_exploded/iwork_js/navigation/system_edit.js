	  var api = frameElement.api, W = api.opener, D = document;
	  $(document).ready(function(){
              $('#system_save').validate({meta:"validate"});
           });
      function closeWin(){
      	api.close();
      }