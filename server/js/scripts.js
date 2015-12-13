
function toggleClass (obj, Class) {
  $(obj).toggleClass(Class);
}

function addClass (obj, Class) {
  $(obj).addClass(Class);
}

function removeClass (obj, Class)	{
  $(obj).removeClass(Class);
}

function sendNotification() {
    var regIDs = getList('user_list');
    var message = $('.message').val();
    
    console.log(regIDs);
    console.log(message);
    if(regIDs != null && message != ""){
        regIDs = regIDs.join("|");
        
        console.log(regIDs);
        console.log(message);
        $('#notification').unbind('submit');


        $.ajax({
            url: "server.php?action=sendNotificationPost",
            type: 'POST',
            data: { regIDs: regIDs, message: message },
            beforeSend: function () {

            },
            success: function (data, textStatus, xhr) {
                $('.message').val("");
                $('#result-message').html(data);
                console.log(data);
            },
            error: function (xhr, textStatus, errorThrown) {

            }
        });  
    }
    
    return false;
}

$(document).ready(function ()	{
  $('.list-group-item.selectable').click(function(e){
      toggleClass(this, 'active'); 
      
      var label = $(this).find('.label');
      toggleClass(label, 'label-default'); 
      toggleClass(label, 'label-primary');
      
      var options = $(this).prev('.list-group-options').find('a');
      toggleClass(options, 'label-default'); 
      toggleClass(options, 'label-primary');
      
      
      var selected = $(this).find('i:last-child');
      toggleClass(selected, 'invisible');
      
      var mail = $(this).attr('mail');
      if(!existInList(mail,'user_list')){
          add_to_list(mail,'user_list');
      }else{
          removeword('user_list',mail,filterMail(mail));
      }
      
      var registrationId = $(this).attr('regID');
      
  });
    
    
})
/*

bootbox.dialog({
          message: text,
          title: "<h3>Confirmer le lancement de la recherche</h3>",
          buttons: {
            main: {
              label: "annuler",
              className: "btn-default btn-mini",
              callback: function() {
                  launching=false;
              }
            },
            success: {
              label: "ok",
              className: "btn-primary btn-mini",
              callback: function() {
                $.ajax(
                  {
                    type: "POST",
                    url:"../Controlers/ControleurUser.php?action=validerRecherche",
                 
                  data:{
                    search : s_new_search
                  },
                  error: function() { alert("erreur !"); },

                  success: function(retour){ 
                      //console.log(retour);
                      launching = false;
                      title_accept=false;
                      bootbox.dialog({
                              message: retour,
                              buttons: {
                                success: {
                                  label: "rafraichir la page",
                                  className: "btn-primary btn-mini",
                                  callback: function() {
                                    location.reload();
                                    
                                  }
                                }
                              }
                      });
                  } 
                });
              }
            }
          }
        });   */