
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
                $('#message').val("");
                $('#result-message').html(data);
                console.log(data);
            },
            error: function (xhr, textStatus, errorThrown) {

            }
        });  
    }
    
    $('#result-message').html("il faut entrer un message et selectionner au moins un utilisateur");
    
    return false;
}

function searchByMail() {
    var search = $('#search-user').val();
    
    console.log(search);
    
    if(search != null && search != ""){
        
        $('#search').unbind('submit');


        $.ajax({
            url: "server.php?action=searchByMail",
            type: 'POST',
            data: { email: search},
            beforeSend: function () {

            },
            success: function (data, textStatus, xhr) {
                console.log(data);
                
                var res = JSON.parse(data);
                
                $('#select-list').html("");
                
                if(res.length>0){
                   for(var i=0;i<res.length;i++){
                   $('#select-list').append('<a class="label label-default label-circle inset label-hover list-group-option remove" onclick="deleteUser('+res[i].email+');">'
                                +'<i class="glyphicon glyphicon-remove"></i>'
                            +'</a>'
                            +'<a class="list-group-item selectable" mail="'+res[i].email+'" regID="'+res[i].gcm_regid+'" onclick="return false;">'
                               +'<div class="row">'
                                    +'<div class="col-sm-4">'
                                        +'<span class="label label-default inset">'
                                            +'<i class="glyphicon glyphicon-user"></i>'
                                            +'<i class="glyphicon glyphicon-ok invisible"></i>'
                                        +'</span> '+res[i].pseudo
                                    +'</div>'
                                    +'<div class="col-sm-4">'
                                        +res[i].email
                                    +'</div>'
                                    +'<div class="col-sm-4">'
                                        +res[i].gcm_regid
                                    +'</div>'
                                +'</div>'
                            +'</a>');
                    }
                    $('.list-group-item.selectable').click(function(e){selectable_click(this)});
                }else{
                    $('#select-list').append('<a class="list-group-item" onclick="return false;">'
                                       +'<div class="row">'
                                            +'<div class="col-sm-12">'
                                                +'aucun résultats'
                                            +'</div>'
                                        +'</div>'
                                    +'</a>');
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

function selectable_click(obj) {
    toggleClass(obj, 'active');

    var label = $(obj).find('.label');
    toggleClass(label, 'label-default');
    toggleClass(label, 'label-primary');

    var options = $(obj).prev('.list-group-options').find('a');
    toggleClass(options, 'label-default');
    toggleClass(options, 'label-primary');


    var selected = $(obj).find('i:last-child');
    toggleClass(selected, 'invisible');

    var mail = $(obj).attr('mail');
    var registrationId = $(obj).attr('regID');


    if (!existInList(mail, 'user_list')) {
        var callback = "removeword2('regid_list','" + registrationId + "')";
        add_to_list(mail, 'user_list', callback);
        add_to_list(registrationId, 'regid_list');
    } else {
        removeword('user_list', mail, filterMail(mail));
        removeword2('regid_list', registrationId);
    }
}

$(document).ready(function ()	{
  $('.list-group-item.selectable').click(function(e){selectable_click(this)});
      
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