
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
    var regIDs = getList('regid_list');
    var message = $('#message').val();
    
    console.log(regIDs);
    console.log(message);
    if(message != ""){
        $('#notification').unbind('submit');
        if(regIDs != null){
            regIDs = regIDs.join("|");
        }
        
        console.log(regIDs);
        console.log(message);
        
        
        
        $.ajax({
            url: "server.php?action=sendNotification",
            type: 'POST',
            data: { regIDs: regIDs, message: message },
            beforeSend: function () {

            },
            success: function (data, textStatus, xhr) {
                $('#message').val("");
                $('#result-message').html(data);
                console.log(data);
            },
            error: function (xhr, textStatus, errorThrown) {

            }
        });  
    }
    
    $('#result-message').html("il faut entrer un message");
    
    return false;
}


function searchByMail() {
    var search = $('#search-user').val();
    
    console.log(search);
    
    if(search != null && search != ""){
        
        $('#search').unbind('submit');


        $.ajax({
            url: "server.php?action=searchUser",
            type: 'POST',
            data: { w: search, page: 1},
            beforeSend: function () {

            },
            success: function (data, textStatus, xhr) {
                console.log(data);
                
                var res = JSON.parse(data);
                var list_group = $('#users');
                
                reset(list_group);
                
                if(res.length>0){
 
                    for(var i=0;i<res.length;i++){
                        var new_item = addSelectable(list_group, res[i].pseudo, res[i].email, res[i].gcm_regid);
             
                        if(existInList(res[i].email, 'user_list')){
                            toggleSelectable(new_item); 
                        }
                    }   
                }else{
                    addRow(list_group, 'aucun résultat');
                }
            },
            error: function (xhr, textStatus, errorThrown) {

            }
        });  
    }
    
    return false;
}


function deleteUser(user) {
    if(user != null && user != ""){
       
        console.log(user);

        $.ajax({
            url: "server.php?action=deleteUser",
            type: 'POST',
            data: { email: user },
            beforeSend: function () {

            },
            success: function (data, textStatus, xhr) {
                if(data == "true"){
                    $('#result-message').html('utilisateur '+user+' supprimé');
                    $('.list-group-item.selectable').each(function(){
                        if($(this).attr('mail') == user){
                            $(this).prev('.list-group-option').remove();
                            $(this).prev('.list-group-options').remove();
                            this.remove(); 
                        }
                    });
                }
                console.log(data);
            },
            error: function (xhr, textStatus, errorThrown) {

            }
        });  
    }
    
    return false;
}
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