function toggleClass(obj, Class) {
    $(obj).toggleClass(Class);
}

function addClass(obj, Class) {
    $(obj).addClass(Class);
}

function removeClass(obj, Class) {
    $(obj).removeClass(Class);
}

var list;

function sendNotification() {
    var regIDs = list.getValues();
    var message = $('#message').val();

    console.log(regIDs);
    console.log(message);
    if (message != "" && message != " ") {
        $('#notification').unbind('submit');
        if (regIDs != null) {
            regIDs = regIDs.join("|");
        }

        console.log(regIDs);
        console.log(message);

        $.ajax({
            url: "server.php?action=sendNotification",
            type: 'POST',
            data: {
                regIDs: regIDs,
                message: message
            },
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
    else {
        $('#result-message').html("il faut entrer un message");
    } 

    return false;
}


function getUsers() {
    var search = $('#search-user').val();

    console.log(search);

    var url = "server.php?action=searchUser";
    var data = {
        w: search,
        page: 1
    };

    if (search == null || search == "") {
        url = "server.php?action=getAllUsers";
        data = {
            page: 1
        };

    }

    $('#search').unbind('submit');

    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        beforeSend: function () {

        },
        success: function (data, textStatus, xhr) {
            console.log(data);

            var res = JSON.parse(data);
            var list_group = $('#users');

            list.reset();

            if (res.length > 0) {

                for (var i = 0; i < res.length; i++) {
                    var new_item = list.addRow([res[i].pseudo, res[i].gcm_regid]);

                    if (list.rowSelected(res[i].gcm_regid)) {
                        list.toggleSelectable(new_item);
                    }
                }
            } else {
                list.addMessageRow('aucun résultat');
            }
        },
        error: function (xhr, textStatus, errorThrown) {

        }
    });


    return false;
}


function deleteUser(user) {
    if (user != null && user != "") {

        console.log(user);

        $.ajax({
            url: "server.php?action=deleteUser",
            type: 'POST',
            data: {
                regID: user
            },
            beforeSend: function () {

            },
            success: function (data, textStatus, xhr) {
                $('#result-message').html(data + ' : ' + list.delRow(user));
                console.log(data);
            },
            error: function (xhr, textStatus, errorThrown) {

            }
        });
    }

    return false;
}


$(document).ready(function () {

    //$('#user_list').myList();
    //var selection_list = $('#user_list').myList();
    //list = $('#users').myTab({list: selection_list});
    var cols = ['pseudo','registrationID'];

    list = $('#users').myTab({
        list: '#user_list',
        cols: ['pseudo', 'registrationID'],
        primary: 'registrationID',
        visible: 'pseudo'
    });
    getUsers();
    
    $('#notification').hide();
});

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